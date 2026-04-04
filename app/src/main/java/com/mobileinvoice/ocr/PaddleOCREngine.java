package com.mobileinvoice.ocr;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes7.dex */
public class PaddleOCREngine {
    private static final int DET_MAX_SIDE = 960;
    private static final String DET_MODEL_PATH = "models/ppocr_det.onnx";
    private static final float DET_THRESHOLD = 0.3f;
    private static final String DICT_PATH = "models/en_dict.txt";
    private static final int REC_IMG_HEIGHT = 48;
    private static final int REC_MAX_WIDTH = 480;
    private static final String REC_MODEL_PATH = "models/ppocr_rec.onnx";
    private static final int SMALL_CROP_THRESHOLD = 500;
    private static final String TAG = "PaddleOCREngine";
    private OrtSession detSession;
    private String[] dictionary;
    private OrtEnvironment env;
    private boolean initialized = false;
    private OrtSession recSession;
    private static final float[] MEAN = {0.5f, 0.5f, 0.5f};
    private static final float[] STD = {0.5f, 0.5f, 0.5f};

    public void init(Context context) {
        try {
            this.env = OrtEnvironment.getEnvironment();
            byte[] detModel = loadAssetBytes(context, DET_MODEL_PATH);
            if (detModel == null) {
                Log.w(TAG, "Detection model not found — will run recognition only");
            } else {
                OrtSession.SessionOptions detOpts = new OrtSession.SessionOptions();
                detOpts.setIntraOpNumThreads(2);
                this.detSession = this.env.createSession(detModel, detOpts);
                Log.d(TAG, "Detection model loaded successfully");
            }
            byte[] recModel = loadAssetBytes(context, REC_MODEL_PATH);
            if (recModel == null) {
                Log.e(TAG, "Recognition model not found — OCR will not work");
                return;
            }
            OrtSession.SessionOptions recOpts = new OrtSession.SessionOptions();
            recOpts.setIntraOpNumThreads(2);
            this.recSession = this.env.createSession(recModel, recOpts);
            Log.d(TAG, "Recognition model loaded successfully");
            this.dictionary = loadDictionary(context);
            Log.d(TAG, "Dictionary loaded: " + this.dictionary.length + " characters");
            this.initialized = true;
        } catch (OrtException e) {
            Log.e(TAG, "Failed to initialize ONNX Runtime", e);
        }
    }

    public String recognizeText(Bitmap bitmap) {
        if (!this.initialized || this.recSession == null) {
            Log.e(TAG, "Engine not initialized");
            return "";
        }
        try {
            if (bitmap.getWidth() >= 500 && this.detSession != null) {
                List<Rect> regions = detectTextRegions(bitmap);
                if (regions.isEmpty()) {
                    return recognizeRegion(bitmap);
                }
                StringBuilder result = new StringBuilder();
                Collections.sort(regions, new Comparator() { // from class: com.mobileinvoice.ocr.PaddleOCREngine$$ExternalSyntheticLambda0
                    @Override // java.util.Comparator
                    public int compare(Object obj, Object obj2) {
                        return PaddleOCREngine.lambda$recognizeText$0((Rect) obj, (Rect) obj2);
                    }
                });
                for (Rect region : regions) {
                    int left = Math.max(0, region.left);
                    int top = Math.max(0, region.top);
                    int right = Math.min(bitmap.getWidth(), region.right);
                    int bottom = Math.min(bitmap.getHeight(), region.bottom);
                    if (right - left > 0 && bottom - top > 0) {
                        Bitmap regionBitmap = Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top);
                        String text = recognizeRegion(regionBitmap);
                        regionBitmap.recycle();
                        if (!text.isEmpty()) {
                            if (result.length() > 0) {
                                result.append(StringUtils.SPACE);
                            }
                            result.append(text);
                        }
                    }
                }
                return result.toString();
            }
            return recognizeRegion(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "OCR processing failed", e);
            return "";
        }
    }

    static /* synthetic */ int lambda$recognizeText$0(Rect a, Rect b) {
        int dy = a.top - b.top;
        return Math.abs(dy) > Math.min(a.height(), b.height()) / 2 ? dy : a.left - b.left;
    }

    public List<TextRegion> detectAndRecognize(Bitmap bitmap) {
        List<Rect> regions;
        List<TextRegion> results = new ArrayList<>();
        if (!this.initialized || this.recSession == null) {
            return results;
        }
        try {
            if (this.detSession != null && bitmap.getWidth() >= 500) {
                regions = detectTextRegions(bitmap);
            } else {
                regions = new ArrayList<>();
                regions.add(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            }
            for (Rect region : regions) {
                int left = Math.max(0, region.left);
                int top = Math.max(0, region.top);
                int right = Math.min(bitmap.getWidth(), region.right);
                int bottom = Math.min(bitmap.getHeight(), region.bottom);
                if (right - left > 0 && bottom - top > 0) {
                    Bitmap regionBitmap = Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top);
                    String text = recognizeRegion(regionBitmap);
                    regionBitmap.recycle();
                    if (!text.isEmpty()) {
                        results.add(new TextRegion(text, new Rect(left, top, right, bottom)));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Detect and recognize failed", e);
        }
        return results;
    }

    public void close() {
        try {
            if (this.detSession != null) {
                this.detSession.close();
                this.detSession = null;
            }
            if (this.recSession != null) {
                this.recSession.close();
                this.recSession = null;
            }
        } catch (OrtException e) {
            Log.e(TAG, "Error closing ONNX sessions", e);
        }
        this.initialized = false;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    private List<Rect> detectTextRegions(Bitmap bitmap) throws OrtException {
        int mapW;
        boolean[][] visited;
        int width;
        int mapH;
        int x;
        int resizedH;
        OnnxTensor inputTensor;
        Map<String, OnnxTensor> inputs;
        Bitmap resized;
        float[] inputData;
        OrtSession.Result output;
        int y;
        int mapW2;
        List<Rect> regions = new ArrayList<>();
        float scale = 1.0f;
        int width2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxSide = Math.max(width2, height);
        if (maxSide > DET_MAX_SIDE) {
            scale = 960.0f / maxSide;
        }
        int resizedW = Math.max(32, ((int) ((width2 * scale) / 32.0f)) * 32);
        int resizedH2 = Math.max(32, ((int) ((height * scale) / 32.0f)) * 32);
        Bitmap resized2 = Bitmap.createScaledBitmap(bitmap, resizedW, resizedH2, true);
        float scaleX = width2 / resizedW;
        float scaleY = height / resizedH2;
        float[] inputData2 = preprocessImage(resized2, resizedW, resizedH2);
        resized2.recycle();
        long[] shape = {1, 3, resizedH2, resizedW};
        OnnxTensor inputTensor2 = OnnxTensor.createTensor(this.env, FloatBuffer.wrap(inputData2), shape);
        Map<String, OnnxTensor> inputs2 = new HashMap<>();
        inputs2.put("x", inputTensor2);
        OrtSession.Result output2 = this.detSession.run(inputs2);
        float[][][][] probMap = (float[][][][]) output2.get(0).getValue();
        inputTensor2.close();
        output2.close();
        int mapH2 = probMap[0][0].length;
        int mapW3 = probMap[0][0][0].length;
        boolean[][] visited2 = (boolean[][]) Array.newInstance(Boolean.TYPE, mapH2, mapW3);
        int y2 = 0;
        while (y2 < mapH2) {
            int resizedW2 = resizedW;
            int py = 0;
            while (py < mapW3) {
                if (probMap[0][0][y2][py] <= 0.3f || visited2[y2][py]) {
                    mapW = mapW3;
                    visited = visited2;
                    width = width2;
                    mapH = mapH2;
                    x = py;
                    resizedH = resizedH2;
                    inputTensor = inputTensor2;
                    inputs = inputs2;
                    resized = resized2;
                    inputData = inputData2;
                    output = output2;
                    y = y2;
                } else {
                    int minX = py;
                    int maxX = py;
                    int minY = y2;
                    int maxY = y2;
                    List<int[]> stack = new ArrayList<>();
                    resizedH = resizedH2;
                    inputTensor = inputTensor2;
                    stack.add(new int[]{y2, py});
                    char c = 1;
                    visited2[y2][py] = true;
                    int pixelCount = 0;
                    inputs = inputs2;
                    int minX2 = minX;
                    resized = resized2;
                    int maxX2 = maxX;
                    inputData = inputData2;
                    int minY2 = minY;
                    output = output2;
                    int maxY2 = maxY;
                    while (!stack.isEmpty()) {
                        int y3 = y2;
                        int y4 = stack.size() - 1;
                        int[] pt = stack.remove(y4);
                        int x2 = py;
                        int py2 = pt[0];
                        List<Rect> regions2 = regions;
                        int px = pt[c];
                        int pixelCount2 = pixelCount + 1;
                        int minX3 = Math.min(minX2, px);
                        int maxX3 = Math.max(maxX2, px);
                        int minY3 = Math.min(minY2, py2);
                        maxY2 = Math.max(maxY2, py2);
                        int pixelCount3 = py2 - 1;
                        int minX4 = py2 + 1;
                        int maxX4 = px - 1;
                        int minY4 = px + 1;
                        int[][] neighbors = {new int[]{pixelCount3, px}, new int[]{minX4, px}, new int[]{py2, maxX4}, new int[]{py2, minY4}};
                        int length = neighbors.length;
                        int i = 0;
                        while (i < length) {
                            int[] n = neighbors[i];
                            int px2 = px;
                            int px3 = n[0];
                            int py3 = py2;
                            int py4 = n[1];
                            if (px3 < 0 || px3 >= mapH2 || py4 < 0 || py4 >= mapW3 || visited2[px3][py4] || probMap[0][0][px3][py4] <= 0.3f) {
                                mapW2 = mapW3;
                            } else {
                                visited2[px3][py4] = true;
                                mapW2 = mapW3;
                                stack.add(new int[]{px3, py4});
                            }
                            i++;
                            px = px2;
                            py2 = py3;
                            mapW3 = mapW2;
                        }
                        c = 1;
                        py = x2;
                        y2 = y3;
                        regions = regions2;
                        pixelCount = pixelCount2;
                        minX2 = minX3;
                        maxX2 = maxX3;
                        minY2 = minY3;
                    }
                    mapW = mapW3;
                    List<Rect> regions3 = regions;
                    x = py;
                    y = y2;
                    if (pixelCount < 10) {
                        visited = visited2;
                        width = width2;
                        mapH = mapH2;
                        regions = regions3;
                    } else {
                        int left = (int) ((minX2 - 2) * scaleX);
                        int top = (int) ((minY2 - 2) * scaleY);
                        int right = (int) ((maxX2 + 2) * scaleX);
                        visited = visited2;
                        int bottom = (int) ((maxY2 + 2) * scaleY);
                        mapH = mapH2;
                        int pixelCount4 = Math.max(0, left);
                        int left2 = Math.max(0, top);
                        width = width2;
                        Rect rect = new Rect(pixelCount4, left2, Math.min(width2, right), Math.min(height, bottom));
                        regions = regions3;
                        regions.add(rect);
                    }
                }
                py = x + 1;
                inputs2 = inputs;
                visited2 = visited;
                resized2 = resized;
                inputData2 = inputData;
                output2 = output;
                resizedH2 = resizedH;
                inputTensor2 = inputTensor;
                y2 = y;
                mapH2 = mapH;
                width2 = width;
                mapW3 = mapW;
            }
            y2++;
            resizedW = resizedW2;
        }
        return regions;
    }

    private String recognizeRegion(Bitmap regionBitmap) {
        int pixel;
        try {
            int origW = regionBitmap.getWidth();
            int origH = regionBitmap.getHeight();
            if (origW > 0 && origH > 0) {
                float ratio = 48.0f / origH;
                char c = 1;
                int max = Math.max(1, (int) (origW * ratio));
                int i = REC_MAX_WIDTH;
                int targetW = Math.min(REC_MAX_WIDTH, max);
                try {
                    Bitmap resized = Bitmap.createScaledBitmap(regionBitmap, targetW, 48, true);
                    float[] inputData = new float[69120];
                    int y = 0;
                    for (int i2 = 48; y < i2; i2 = 48) {
                        int x = 0;
                        while (x < i) {
                            if (x < targetW) {
                                pixel = resized.getPixel(x, y);
                            } else {
                                pixel = -1;
                            }
                            float r = ((Color.red(pixel) / 255.0f) - MEAN[0]) / STD[0];
                            float g = ((Color.green(pixel) / 255.0f) - MEAN[c]) / STD[c];
                            float b = ((Color.blue(pixel) / 255.0f) - MEAN[2]) / STD[2];
                            inputData[(y * REC_MAX_WIDTH) + x] = r;
                            inputData[(y * REC_MAX_WIDTH) + 23040 + x] = g;
                            inputData[(y * REC_MAX_WIDTH) + 46080 + x] = b;
                            x++;

                            c = 1;
                            i = REC_MAX_WIDTH;
                        }
                        y++;
                        c = 1;
                        i = REC_MAX_WIDTH;
                    }
                    resized.recycle();
                    long[] shape = {1, 3, 48, 480};
                    OnnxTensor inputTensor = OnnxTensor.createTensor(this.env, FloatBuffer.wrap(inputData), shape);
                    Map<String, OnnxTensor> inputs = new HashMap<>();
                    inputs.put("x", inputTensor);
                    OrtSession.Result output = this.recSession.run(inputs);
                    float[][][] outputData = (float[][][]) output.get(0).getValue();
                    inputTensor.close();
                    output.close();
                    return ctcDecode(outputData[0]);
                } catch (Exception e) {
                    Log.e(TAG, "Recognition failed for region", e);
                    return "";
                }
            }
            return "";
        } catch (Exception e2) {
            Log.e(TAG, "Recognition failed for region", e2);
            return "";
        }
    }

    private float[] preprocessImage(Bitmap bitmap, int width, int height) {
        float[] data = new float[height * 3 * width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                float r = ((Color.red(pixel) / 255.0f) - MEAN[0]) / STD[0];
                float g = ((Color.green(pixel) / 255.0f) - MEAN[1]) / STD[1];
                float b = ((Color.blue(pixel) / 255.0f) - MEAN[2]) / STD[2];
                data[(0) + (y * width) + x] = r;
                data[(height * width) + (y * width) + x] = g;
                data[(height * 2 * width) + (y * width) + x] = b;
            }
        }
        return data;
    }

    private String ctcDecode(float[][] output) {
        StringBuilder result = new StringBuilder();
        int prevIndex = -1;
        for (float[] step : output) {
            int maxIdx = 0;
            float maxVal = step[0];
            for (int j = 1; j < step.length; j++) {
                if (step[j] > maxVal) {
                    maxVal = step[j];
                    maxIdx = j;
                }
            }
            int charIdx = maxIdx - 1;
            if (maxIdx != 0 && maxIdx != prevIndex && charIdx >= 0 && charIdx < this.dictionary.length) {
                result.append(this.dictionary[charIdx]);
            }
            prevIndex = maxIdx;
        }
        String decoded = result.toString().trim();
        Log.d(TAG, "CTC decoded: \"" + decoded + "\" (seq_len=" + output.length + ", classes=" + (output.length > 0 ? output[0].length : 0) + ")");
        return decoded;
    }

    private byte[] loadAssetBytes(Context context, String assetPath) {
        try {
            InputStream is = context.getAssets().open(assetPath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len != -1) {
                    bos.write(buf, 0, len);
                } else {
                    is.close();
                    byte[] result = bos.toByteArray();
                    Log.d(TAG, "Loaded " + assetPath + ": " + result.length + " bytes");
                    return result;
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "Asset not found: " + assetPath);
            return null;
        }
    }

    private String[] loadDictionary(Context context) {
        List<String> chars = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(DICT_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (!line.isEmpty() || chars.size() > 0) {
                    chars.add(line);
                }
            }
            while (!chars.isEmpty() && chars.get(chars.size() - 1).isEmpty()) {
                chars.remove(chars.size() - 1);
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to load dictionary", e);
            for (int i = 32; i < 127; i++) {
                chars.add(String.valueOf((char) i));
            }
        }
        return chars.toArray(new String[0]);
    }

    public record TextRegion(String text, Rect boundingBox) {
    }
}
