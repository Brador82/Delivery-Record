package com.mobileinvoice.ocr;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.mobileinvoice.ocr.databinding.ActivityCameraBinding;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import org.apache.poi.openxml4j.opc.ContentTypes;

public class CameraActivity extends BaseActivity {
    public static final String EXTRA_CAMERA_MODE = "camera_mode";
    public static final String MODE_INVOICE = "invoice";
    public static final String MODE_POD = "pod";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA"};
    private ActivityCameraBinding binding;
    private ImageCapture imageCapture;
    private String cameraMode = MODE_INVOICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        String mode = getIntent().getStringExtra(EXTRA_CAMERA_MODE);
        if (mode != null) {
            this.cameraMode = mode;
        }
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        this.binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        this.binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider());
                    imageCapture = new ImageCapture.Builder().build();
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(CameraActivity.this,
                            CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture);
                } catch (InterruptedException | ExecutionException e) {
                    Toast.makeText(CameraActivity.this, "Error starting camera: " + e.getMessage(), 0).show();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (this.imageCapture == null) {
            return;
        }
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", name);
        contentValues.put("mime_type", ContentTypes.IMAGE_JPEG);
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();
        this.imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults output) {
                        Toast.makeText(CameraActivity.this, "Photo captured!", 0).show();
                        Intent resultIntent = new Intent();
                        resultIntent.setData(output.getSavedUri());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }

                    @Override
                    public void onError(ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this,
                                "Failed to save photo: " + exception.getMessage(), 0).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission not granted", 0).show();
                finish();
            }
        }
    }
}
