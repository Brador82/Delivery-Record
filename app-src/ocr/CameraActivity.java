package com.mobileinvoice.ocr;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Size;
import android.view.View;
import android.widget.Toast;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.mobileinvoice.ocr.DocumentDetector;
import com.mobileinvoice.ocr.databinding.ActivityCameraBinding;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.poi.openxml4j.opc.ContentTypes;

/* loaded from: classes7.dex */
public class CameraActivity extends BaseActivity {
    private static final long AUTO_CAPTURE_DELAY_MS = 500;
    public static final String EXTRA_CAMERA_MODE = "camera_mode";
    public static final String MODE_INVOICE = "invoice";
    public static final String MODE_POD = "pod";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA"};
    private ExecutorService analysisExecutor;
    private Handler autoCaptureHandler;
    private ActivityCameraBinding binding;
    private DocumentDetector documentDetector;
    private ImageAnalysis imageAnalysis;
    private ImageCapture imageCapture;
    private boolean autoCaptureEnabled = true;
    private boolean autoCapturePending = false;
    private String cameraMode = MODE_INVOICE;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        String mode = getIntent().getStringExtra(EXTRA_CAMERA_MODE);
        if (mode != null) {
            this.cameraMode = mode;
        }
        this.documentDetector = new DocumentDetector();
        this.analysisExecutor = Executors.newSingleThreadExecutor();
        this.autoCaptureHandler = new Handler(Looper.getMainLooper());
        if (MODE_POD.equals(this.cameraMode)) {
            this.autoCaptureEnabled = false;
            this.binding.viewfinderOverlay.setVisibility(8);
            this.binding.btnToggleAutoCapture.setVisibility(8);
        } else {
            this.autoCaptureEnabled = true;
            this.binding.viewfinderOverlay.setVisibility(0);
            this.binding.viewfinderOverlay.setResizable(true);
            this.binding.btnToggleAutoCapture.setVisibility(0);
        }
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 10);
        }
        this.binding.btnCapture.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CameraActivity.this.lambda$onCreate$0(view);
            }
        });
        this.binding.btnClose.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CameraActivity.this.lambda$onCreate$1(view);
            }
        });
        this.binding.btnToggleAutoCapture.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CameraActivity.this.lambda$onCreate$2(view);
            }
        });
        updateAutoCaptureButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View v) {
        takePhoto();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View v) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View v) {
        this.autoCaptureEnabled = !this.autoCaptureEnabled;
        updateAutoCaptureButton();
        this.documentDetector.reset();
        this.autoCapturePending = false;
        Toast.makeText(this, this.autoCaptureEnabled ? "Auto-capture ON" : "Auto-capture OFF", 0).show();
    }

    private void updateAutoCaptureButton() {
        this.binding.btnToggleAutoCapture.setAlpha(this.autoCaptureEnabled ? 1.0f : 0.4f);
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CameraActivity.this.lambda$startCamera$3(cameraProviderFuture);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$startCamera$3(ListenableFuture cameraProviderFuture) {
        try {
            ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
            Preview preview = new Preview.Builder().build();
            preview.setSurfaceProvider(this.binding.cameraPreview.getSurfaceProvider());
            this.imageCapture = new ImageCapture.Builder().build();
            CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            if (MODE_INVOICE.equals(this.cameraMode)) {
                this.imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(640, 480)).setBackpressureStrategy(0).build();
                this.imageAnalysis.setAnalyzer(this.analysisExecutor, new ImageAnalysis.Analyzer() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda1
                    @Override // androidx.camera.core.ImageAnalysis.Analyzer
                    public final void analyze(ImageProxy imageProxy) {
                        CameraActivity.this.analyzeFrame(imageProxy);
                    }
                });
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, this.imageCapture, this.imageAnalysis);
            } else {
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, this.imageCapture);
            }
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(this, "Error starting camera: " + e.getMessage(), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void analyzeFrame(ImageProxy image) {
        if (!this.autoCaptureEnabled) {
            return;
        }
        try {
            ImageProxy.PlaneProxy yPlane = image.getPlanes()[0];
            ByteBuffer yBuffer = yPlane.getBuffer();
            byte[] yData = new byte[yBuffer.remaining()];
            yBuffer.get(yData);
            int width = image.getWidth();
            int height = image.getHeight();
            int rowStride = yPlane.getRowStride();
            Rect guideInFrame = mapGuideToFrame(width, height, image.getImageInfo().getRotationDegrees());
            final DocumentDetector.AlignmentResult result = this.documentDetector.analyze(yData, width, height, rowStride, guideInFrame);
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CameraActivity.this.lambda$analyzeFrame$4(result);
                }
            });
        } finally {
            image.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$analyzeFrame$4(DocumentDetector.AlignmentResult result) {
        this.binding.viewfinderOverlay.updateAlignment(result.score, result.anchorScores, result.readyToCapture);
        if (result.readyToCapture && !this.autoCapturePending) {
            triggerAutoCapture();
        }
    }

    private Rect mapGuideToFrame(int frameWidth, int frameHeight, int rotationDegrees) {
        Rect overlayGuide = this.binding.viewfinderOverlay.getGuideRect();
        int overlayW = this.binding.viewfinderOverlay.getWidth();
        int overlayH = this.binding.viewfinderOverlay.getHeight();
        if (overlayW <= 0 || overlayH <= 0) {
            int margin = (int) (frameWidth * 0.1d);
            return new Rect(margin, margin, frameWidth - margin, frameHeight - margin);
        }
        if (rotationDegrees == 90 || rotationDegrees == 270) {
            float scaleX = frameWidth / overlayH;
            float scaleY = frameHeight / overlayW;
            int left = (int) (overlayGuide.top * scaleX);
            int top = (int) (overlayGuide.left * scaleY);
            int right = (int) (overlayGuide.bottom * scaleX);
            int bottom = (int) (overlayGuide.right * scaleY);
            return new Rect(left, top, right, bottom);
        }
        float scaleX2 = frameWidth / overlayW;
        float scaleY2 = frameHeight / overlayH;
        int left2 = (int) (overlayGuide.left * scaleX2);
        int top2 = (int) (overlayGuide.top * scaleY2);
        int right2 = (int) (overlayGuide.right * scaleX2);
        int bottom2 = (int) (overlayGuide.bottom * scaleY2);
        return new Rect(left2, top2, right2, bottom2);
    }

    private void triggerAutoCapture() {
        this.autoCapturePending = true;
        this.autoCaptureHandler.postDelayed(new Runnable() { // from class: com.mobileinvoice.ocr.CameraActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CameraActivity.this.lambda$triggerAutoCapture$5();
            }
        }, AUTO_CAPTURE_DELAY_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerAutoCapture$5() {
        if (this.documentDetector.getAlignmentScore() >= 0.6f) {
            this.binding.viewfinderOverlay.triggerCaptureFlash();
            takePhoto();
        }
        this.autoCapturePending = false;
    }

    private void takePhoto() {
        if (this.imageCapture == null) {
            return;
        }
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Long.valueOf(System.currentTimeMillis()));
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", name);
        contentValues.put("mime_type", ContentTypes.IMAGE_JPEG);
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();
        this.imageCapture.m139lambda$takePicture$2$androidxcameracoreImageCapture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() { // from class: com.mobileinvoice.ocr.CameraActivity.1
            @Override // androidx.camera.core.ImageCapture.OnImageSavedCallback
            public void onImageSaved(ImageCapture.OutputFileResults output) {
                Toast.makeText(CameraActivity.this, "Photo captured successfully!", 0).show();
                Intent resultIntent = new Intent();
                resultIntent.setData(output.getSavedUri());
                CameraActivity.this.setResult(-1, resultIntent);
                CameraActivity.this.finish();
            }

            @Override // androidx.camera.core.ImageCapture.OnImageSavedCallback
            public void onError(ImageCaptureException exception) {
                Toast.makeText(CameraActivity.this, "Failed to save photo: " + exception.getMessage(), 0).show();
                CameraActivity.this.autoCapturePending = false;
            }
        });
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission not granted", 0).show();
                finish();
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.analysisExecutor != null) {
            this.analysisExecutor.shutdown();
        }
        this.autoCaptureHandler.removeCallbacksAndMessages(null);
    }
}
