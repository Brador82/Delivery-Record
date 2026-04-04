package com.mobileinvoice.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes7.dex */
public class SignatureView extends View {
    private Bitmap backgroundImage;
    private Bitmap bitmap;
    private Canvas canvas;
    private boolean hasDrawn;
    private Paint paint;
    private Path path;

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.hasDrawn = false;
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth(5.0f);
        this.path = new Path();
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
        if (this.backgroundImage != null) {
            drawBackgroundImage();
        } else {
            this.canvas.drawColor(-1);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, null);
        canvas.drawPath(this.path, this.paint);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                this.path.moveTo(x, y);
                return true;
            case 1:
                this.canvas.drawPath(this.path, this.paint);
                this.path.reset();
                this.hasDrawn = true;
                break;
            case 2:
                this.path.lineTo(x, y);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void clear() {
        this.path.reset();
        this.hasDrawn = false;
        if (this.bitmap != null) {
            if (this.backgroundImage != null) {
                drawBackgroundImage();
            } else {
                this.canvas.drawColor(-1);
            }
        }
        invalidate();
    }

    public boolean hasSignature() {
        return this.hasDrawn;
    }

    public void setBackgroundImage(String imagePath) {
        Log.d("SignatureView", "setBackgroundImage called with: " + imagePath);
        if (imagePath != null) {
            try {
                if (imagePath.startsWith("content://")) {
                    Log.d("SignatureView", "Loading from content URI");
                    Uri uri = Uri.parse(imagePath);
                    Bitmap loadedBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    if (loadedBitmap != null) {
                        Log.d("SignatureView", "Bitmap loaded from content URI successfully");
                        this.backgroundImage = rotateBitmapFromContentUri(loadedBitmap, uri);
                        if (this.backgroundImage != loadedBitmap) {
                            loadedBitmap.recycle();
                        }
                    }
                } else {
                    String filePath = imagePath.replace("file://", "");
                    Log.d("SignatureView", "Loading from file path: " + filePath);
                    File imageFile = new File(filePath);
                    Log.d("SignatureView", "Image file exists: " + imageFile.exists());
                    if (imageFile.exists()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(filePath, options);
                        Log.d("SignatureView", "Image dimensions: " + options.outWidth + "x" + options.outHeight);
                        options.inSampleSize = calculateInSampleSize(options, 1920, 1080);
                        options.inJustDecodeBounds = false;
                        Bitmap loadedBitmap2 = BitmapFactory.decodeFile(filePath, options);
                        if (loadedBitmap2 != null) {
                            Log.d("SignatureView", "Bitmap loaded from file successfully");
                            this.backgroundImage = rotateBitmapIfNeeded(loadedBitmap2, filePath);
                            if (this.backgroundImage != loadedBitmap2) {
                                loadedBitmap2.recycle();
                            }
                        }
                    } else {
                        Log.e("SignatureView", "Image file does not exist: " + imageFile.getAbsolutePath());
                    }
                }
                if (this.canvas != null && this.backgroundImage != null) {
                    drawBackgroundImage();
                    invalidate();
                    return;
                } else {
                    Log.d("SignatureView", "Canvas not ready yet or no bitmap loaded");
                    return;
                }
            } catch (IOException e) {
                Log.e("SignatureView", "Error loading image: " + e.getMessage());
                return;
            }
        }
        Log.d("SignatureView", "imagePath is null");
    }

    private Bitmap rotateBitmapFromContentUri(Bitmap bitmap, Uri uri) {
        int rotation;
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                ExifInterface exif = new ExifInterface(inputStream);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                inputStream.close();
                switch (orientation) {
                    case 3:
                        rotation = 180;
                        break;
                    case 6:
                        rotation = 90;
                        break;
                    case 8:
                        rotation = 270;
                        break;
                    default:
                        return bitmap;
                }
                if (rotation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotation);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            Log.e("SignatureView", "Error reading EXIF from URI: " + e.getMessage());
        }
        return bitmap;
    }

    private Bitmap rotateBitmapIfNeeded(Bitmap bitmap, String imagePath) {
        int rotation;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (orientation) {
                case 3:
                    rotation = 180;
                    break;
                case 6:
                    rotation = 90;
                    break;
                case 8:
                    rotation = 270;
                    break;
                default:
                    return bitmap;
            }
            if (rotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (IOException e) {
        }
        return bitmap;
    }

    private void drawBackgroundImage() {
        if (this.backgroundImage != null && this.canvas != null) {
            this.canvas.drawColor(-1);
            int canvasWidth = this.canvas.getWidth();
            int canvasHeight = this.canvas.getHeight();
            int imgWidth = this.backgroundImage.getWidth();
            int imgHeight = this.backgroundImage.getHeight();
            float scale = Math.min((float) canvasWidth / imgWidth, (float) canvasHeight / imgHeight);
            int scaledWidth = Math.max(1, (int) (imgWidth * scale));
            int scaledHeight = Math.max(1, (int) (imgHeight * scale));
            int left = (canvasWidth - scaledWidth) / 2;
            int top = (canvasHeight - scaledHeight) / 2;
            Rect destRect = new Rect(left, top, left + scaledWidth, top + scaledHeight);
            Paint bgPaint = new Paint();
            bgPaint.setAlpha(180);
            this.canvas.drawBitmap(this.backgroundImage, null, destRect, bgPaint);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap getSignatureBitmap() {
        return this.bitmap;
    }
}
