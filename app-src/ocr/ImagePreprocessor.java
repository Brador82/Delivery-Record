package com.mobileinvoice.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes7.dex */
public class ImagePreprocessor {
    public static Bitmap pipeline(Bitmap input) {
        Bitmap result = rotateIfLandscape(input);
        return enhanceContrast(toGrayscale(result), 2.0f);
    }

    public static Bitmap rotateIfLandscape(Bitmap src) {
        if (src.getWidth() > src.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90.0f);
            Bitmap rotated = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            return rotated;
        }
        return src;
    }

    public static Bitmap toGrayscale(Bitmap src) {
        Bitmap grayscale = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        return grayscale;
    }

    public static Bitmap enhanceContrast(Bitmap src, float factor) {
        float translate = (((-0.5f) * factor) + 0.5f) * 255.0f;
        ColorMatrix cm = new ColorMatrix(new float[]{factor, 0.0f, 0.0f, 0.0f, translate, 0.0f, factor, 0.0f, 0.0f, translate, 0.0f, 0.0f, factor, 0.0f, translate, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        return result;
    }

    public static String savePreprocessed(Context context, Bitmap bitmap) {
        File dir = new File(context.getFilesDir(), "preprocessed");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = "preprocessed_" + new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(new Date()) + ".jpg";
        File file = new File(dir, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 92, fos);
                fos.flush();
                String absolutePath = file.getAbsolutePath();
                fos.close();
                return absolutePath;
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
