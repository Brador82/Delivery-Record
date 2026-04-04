package com.mobileinvoice.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/* loaded from: classes7.dex */
public class SelectionOverlayView extends View {
    private static final int HANDLE_SIZE = 50;
    private Paint completedFillPaint;
    private Paint completedPaint;
    private final List<CompletedSelection> completedSelections;
    private Rect dragOriginalRect;
    private PointF dragStartPoint;
    private CompletedSelection draggedSelection;
    private Bitmap imageBitmap;
    private final Matrix imageMatrix;
    private boolean cropMode;
    private float currentDragX;
    private float currentDragY;
    private boolean isDragging;
    private boolean isLongPressSelecting;
    private boolean isResizing;
    private boolean isScaling;
    private OnSelectionCompleteListener listener;
    private float maxScale;
    private float minScale;
    private GestureDetector panDetector;
    private String resizeCorner;
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor;
    private CompletedSelection selectedBox;
    private Paint selectionFillPaint;
    private boolean selectionMode;
    private Paint selectionPaint;
    private RectF selectionRect;
    private PointF selectionStart;
    private boolean tapToSelectMode;
    private OnTextSelectedListener textListener;
    private List<PaddleOCREngine.TextRegion> textRegions;
    private List<PaddleOCREngine.TextRegion> charRegions;

    public interface OnSelectionCompleteListener {
        void onSelectionComplete(Rect bitmapRect);
    }

    public interface OnTextSelectedListener {
        void onTextSelected(String text, Rect bitmapRect);
    }

    public static class CompletedSelection {
        public Rect bitmapRect;
        public final int color;
        public final String fieldName;

        public CompletedSelection(String fieldName, Rect bitmapRect, int color) {
            this.fieldName = fieldName;
            this.bitmapRect = bitmapRect;
            this.color = color;
        }
    }

    public SelectionOverlayView(Context context) {
        super(context);
        this.imageMatrix = new Matrix();
        this.scaleFactor = 1.0f;
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.selectionMode = false;
        this.tapToSelectMode = false;
        this.completedSelections = new ArrayList();
        this.textRegions = new ArrayList();
        this.charRegions = new ArrayList();
        this.isScaling = false;
        this.draggedSelection = null;
        this.dragStartPoint = null;
        this.dragOriginalRect = null;
        this.isDragging = false;
        this.selectedBox = null;
        this.resizeCorner = null;
        this.isResizing = false;
        init(context);
    }

    public SelectionOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.imageMatrix = new Matrix();
        this.scaleFactor = 1.0f;
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.selectionMode = false;
        this.tapToSelectMode = false;
        this.completedSelections = new ArrayList();
        this.textRegions = new ArrayList();
        this.charRegions = new ArrayList();
        this.isScaling = false;
        this.draggedSelection = null;
        this.dragStartPoint = null;
        this.dragOriginalRect = null;
        this.isDragging = false;
        this.selectedBox = null;
        this.resizeCorner = null;
        this.isResizing = false;
        init(context);
    }

    public SelectionOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.imageMatrix = new Matrix();
        this.scaleFactor = 1.0f;
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.selectionMode = false;
        this.tapToSelectMode = false;
        this.completedSelections = new ArrayList();
        this.textRegions = new ArrayList();
        this.charRegions = new ArrayList();
        this.isScaling = false;
        this.draggedSelection = null;
        this.dragStartPoint = null;
        this.dragOriginalRect = null;
        this.isDragging = false;
        this.selectedBox = null;
        this.resizeCorner = null;
        this.isResizing = false;
        init(context);
    }

    private void init(Context context) {
        this.selectionPaint = new Paint(1);
        this.selectionPaint.setColor(0xFFFFD700);
        this.selectionPaint.setStyle(Paint.Style.STROKE);
        this.selectionPaint.setStrokeWidth(3.0f);
        this.selectionPaint.setPathEffect(new DashPathEffect(new float[] { 15.0f, 10.0f }, 0.0f));
        this.selectionFillPaint = new Paint(1);
        this.selectionFillPaint.setColor(0x40FFD700);
        this.selectionFillPaint.setStyle(Paint.Style.FILL);
        this.completedPaint = new Paint(1);
        this.completedPaint.setColor(-6381922);
        this.completedPaint.setStyle(Paint.Style.STROKE);
        this.completedPaint.setStrokeWidth(2.0f);
        this.completedFillPaint = new Paint(1);
        this.completedFillPaint.setColor(866033310);
        this.completedFillPaint.setStyle(Paint.Style.FILL);
        this.scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from
                                                                                                                         // class:
                                                                                                                         // com.mobileinvoice.ocr.SelectionOverlayView.1
            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener,
                      // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                SelectionOverlayView.this.isScaling = true;
                return true;
            }

            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener,
                      // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector detector) {
                float factor = detector.getScaleFactor();
                float newScale = Math.max(SelectionOverlayView.this.minScale,
                        Math.min(SelectionOverlayView.this.maxScale, SelectionOverlayView.this.scaleFactor * factor));
                float adjustedFactor = newScale / SelectionOverlayView.this.scaleFactor;
                SelectionOverlayView.this.scaleFactor = newScale;
                SelectionOverlayView.this.imageMatrix.postScale(adjustedFactor, adjustedFactor, detector.getFocusX(),
                        detector.getFocusY());
                SelectionOverlayView.this.constrainTranslation();
                SelectionOverlayView.this.invalidate();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener,
                      // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector detector) {
                SelectionOverlayView.this.isScaling = false;
            }
        });
        this.panDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class:
                                                                                                        // com.mobileinvoice.ocr.SelectionOverlayView.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener,
                      // android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!SelectionOverlayView.this.isDragging) {
                    SelectionOverlayView.this.imageMatrix.postTranslate(-distanceX, -distanceY);
                    SelectionOverlayView.this.constrainTranslation();
                    SelectionOverlayView.this.invalidate();
                    return true;
                }
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener,
                      // android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                PointF touchPoint = new PointF(e.getX(), e.getY());
                CompletedSelection hit = SelectionOverlayView.this.findCompletedSelectionAt(touchPoint);
                if (hit != null && SelectionOverlayView.this.selectedBox == null) {
                    SelectionOverlayView.this.draggedSelection = hit;
                    SelectionOverlayView.this.dragStartPoint = touchPoint;
                    SelectionOverlayView.this.dragOriginalRect = new Rect(hit.bitmapRect);
                    SelectionOverlayView.this.isDragging = true;
                    SelectionOverlayView.this.invalidate();
                } else if (hit == null) {
                    SelectionOverlayView.this.isLongPressSelecting = true;
                    SelectionOverlayView.this.selectionStart = touchPoint;
                    SelectionOverlayView.this.selectionRect = new RectF(touchPoint.x, touchPoint.y, touchPoint.x,
                            touchPoint.y);
                    SelectionOverlayView.this.performHapticFeedback(0);
                    SelectionOverlayView.this.invalidate();
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener,
                      // android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent e) {
                PaddleOCREngine.TextRegion tappedRegion;
                PointF touchPoint = new PointF(e.getX(), e.getY());
                if (SelectionOverlayView.this.textListener != null && !SelectionOverlayView.this.textRegions.isEmpty()
                        && (tappedRegion = SelectionOverlayView.this.findTextRegionAt(touchPoint)) != null) {
                    SelectionOverlayView.this.textListener.onTextSelected(tappedRegion.text(), tappedRegion.boundingBox());
                    return true;
                }
                CompletedSelection hit = SelectionOverlayView.this.findCompletedSelectionAt(touchPoint);
                if (hit != null) {
                    if (SelectionOverlayView.this.selectedBox == hit) {
                        SelectionOverlayView.this.selectedBox = null;
                    } else {
                        SelectionOverlayView.this.selectedBox = hit;
                    }
                    SelectionOverlayView.this.invalidate();
                    return true;
                }
                if (SelectionOverlayView.this.selectedBox != null) {
                    SelectionOverlayView.this.selectedBox = null;
                    SelectionOverlayView.this.invalidate();
                    return true;
                }
                return false;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener,
                      // android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent e) {
                PointF touchPoint = new PointF(e.getX(), e.getY());
                CompletedSelection hit = SelectionOverlayView.this.findCompletedSelectionAt(touchPoint);
                if (hit != null) {
                    SelectionOverlayView.this.completedSelections.remove(hit);
                    if (SelectionOverlayView.this.selectedBox == hit) {
                        SelectionOverlayView.this.selectedBox = null;
                    }
                    SelectionOverlayView.this.invalidate();
                    return true;
                }
                SelectionOverlayView.this.resetZoom();
                return true;
            }
        });
    }

    public void setImage(Bitmap bitmap) {
        this.imageBitmap = bitmap;
        if (getWidth() > 0 && getHeight() > 0) {
            fitImageToView();
        }
        invalidate();
    }

    public Bitmap getImageBitmap() {
        return this.imageBitmap;
    }

    public void setSelectionMode(boolean enabled) {
        this.selectionMode = enabled;
        this.selectionRect = null;
        this.selectionStart = null;
        invalidate();
    }

    public void setOnSelectionCompleteListener(OnSelectionCompleteListener listener) {
        this.listener = listener;
    }

    public void setOnTextSelectedListener(OnTextSelectedListener listener) {
        this.textListener = listener;
    }

    public void setTextRegions(List<PaddleOCREngine.TextRegion> regions) {
        this.textRegions = regions != null ? new ArrayList<>(regions) : new ArrayList<>();
        invalidate();
    }

    public void setCharRegions(List<PaddleOCREngine.TextRegion> regions) {
        this.charRegions = regions != null ? new ArrayList<>(regions) : new ArrayList<>();
    }

    public void setTapToSelectMode(boolean enabled) {
        this.tapToSelectMode = enabled;
        invalidate();
    }

    public void addCompletedSelection(String fieldName, Rect bitmapRect, int color) {
        this.completedSelections.add(new CompletedSelection(fieldName, bitmapRect, color));
        invalidate();
    }

    public void clearSelections() {
        this.completedSelections.clear();
        this.selectionRect = null;
        this.selectionStart = null;
        invalidate();
    }

    public void setCropMode(boolean enabled) {
        this.cropMode = enabled;
        this.selectionRect = null;
        this.selectionStart = null;
        invalidate();
    }

    public boolean isCropMode() {
        return this.cropMode;
    }

    public Rect getCropBitmapRect() {
        if (this.selectionRect == null || this.imageBitmap == null) {
            return null;
        }
        RectF normalized = new RectF(
                Math.min(this.selectionRect.left, this.selectionRect.right),
                Math.min(this.selectionRect.top, this.selectionRect.bottom),
                Math.max(this.selectionRect.left, this.selectionRect.right),
                Math.max(this.selectionRect.top, this.selectionRect.bottom));
        return screenRectToBitmapRect(normalized);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.imageBitmap != null) {
            fitImageToView();
        }
    }

    private void fitImageToView() {
        if (this.imageBitmap == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.imageMatrix.reset();
        float scaleX = getWidth() * 1.0f / this.imageBitmap.getWidth();
        float scaleY = getHeight() * 1.0f / this.imageBitmap.getHeight();
        this.minScale = Math.min(scaleX, scaleY);
        this.scaleFactor = this.minScale;
        this.maxScale = this.minScale * 5.0f;
        float dx = (getWidth() - (this.imageBitmap.getWidth() * this.scaleFactor)) / 2.0f;
        float dy = (getHeight() - (this.imageBitmap.getHeight() * this.scaleFactor)) / 2.0f;
        this.imageMatrix.setScale(this.scaleFactor, this.scaleFactor);
        this.imageMatrix.postTranslate(dx, dy);
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetZoom() {
        fitImageToView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void constrainTranslation() {
        if (this.imageBitmap == null) {
            return;
        }
        float[] values = new float[9];
        this.imageMatrix.getValues(values);
        float transX = values[2];
        float transY = values[5];
        float currentScale = values[0];
        float imageW = this.imageBitmap.getWidth() * currentScale;
        float imageH = this.imageBitmap.getHeight() * currentScale;
        float dx = 0.0f;
        float dy = 0.0f;
        if (imageW <= getWidth()) {
            dx = ((getWidth() - imageW) / 2.0f) - transX;
        } else if (transX > 0.0f) {
            dx = -transX;
        } else if (transX + imageW < getWidth()) {
            dx = (getWidth() - transX) - imageW;
        }
        if (imageH <= getHeight()) {
            dy = ((getHeight() - imageH) / 2.0f) - transY;
        } else if (transY > 0.0f) {
            dy = -transY;
        } else if (transY + imageH < getHeight()) {
            dy = (getHeight() - transY) - imageH;
        }
        if (dx != 0.0f || dy != 0.0f) {
            this.imageMatrix.postTranslate(dx, dy);
        }
    }

    /*
     * JADX WARN: Can't fix incorrect switch cases order, some code will duplicate
     */
    /*
     * JADX WARN: Code restructure failed: missing block: B:39:0x007b, code lost:
     * 
     * if (r11.equals("tl") != false) goto L34;
     */
    @Override // android.view.View
    /*
     * Code decompiled incorrectly, please refer to instructions dump.
     */
    public boolean onTouchEvent(MotionEvent event) {
        char c = 0;
        if (this.imageBitmap == null) {
            return false;
        }
        if (event.getPointerCount() > 1) {
            this.scaleDetector.onTouchEvent(event);
            this.panDetector.onTouchEvent(event);
            return true;
        }
        if (this.cropMode) {
            switch (event.getActionMasked()) {
                case 0:
                    this.selectionStart = new PointF(event.getX(), event.getY());
                    this.selectionRect = new RectF(this.selectionStart.x, this.selectionStart.y, this.selectionStart.x,
                            this.selectionStart.y);
                    invalidate();
                    return true;
                case 2:
                    if (this.selectionRect != null) {
                        this.selectionRect.right = event.getX();
                        this.selectionRect.bottom = event.getY();
                        invalidate();
                    }
                    return true;
                case 1:
                case 3:
                    if (this.selectionRect != null) {
                        this.selectionRect = new RectF(
                                Math.min(this.selectionRect.left, this.selectionRect.right),
                                Math.min(this.selectionRect.top, this.selectionRect.bottom),
                                Math.max(this.selectionRect.left, this.selectionRect.right),
                                Math.max(this.selectionRect.top, this.selectionRect.bottom));
                        invalidate();
                    }
                    return true;
            }
            return true;
        }
        if (this.selectedBox != null) {
            switch (event.getActionMasked()) {
                case 0:
                    RectF screenRect = bitmapRectToScreenRect(this.selectedBox.bitmapRect);
                    this.resizeCorner = getCornerAtPoint(screenRect, event.getX(), event.getY());
                    if (this.resizeCorner != null) {
                        this.isResizing = true;
                        this.dragStartPoint = new PointF(event.getX(), event.getY());
                        this.dragOriginalRect = new Rect(this.selectedBox.bitmapRect);
                        return true;
                    }
                    break;
                case 1:
                case 3:
                    if (this.isResizing) {
                        this.isResizing = false;
                        this.resizeCorner = null;
                        this.dragStartPoint = null;
                        this.dragOriginalRect = null;
                        invalidate();
                        return true;
                    }
                    break;
                case 2:
                    if (this.isResizing && this.resizeCorner != null) {
                        float screenDx = event.getX() - this.dragStartPoint.x;
                        float screenDy = event.getY() - this.dragStartPoint.y;
                        Matrix inverse = new Matrix();
                        this.imageMatrix.invert(inverse);
                        float[] offset = { screenDx, screenDy };
                        inverse.mapVectors(offset);
                        float bitmapDx = offset[0];
                        float bitmapDy = offset[1];
                        Rect newRect = new Rect(this.dragOriginalRect);
                        String str = this.resizeCorner;
                        switch (str.hashCode()) {
                            case 3146:
                                if (str.equals("bl")) {
                                    c = 2;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3152:
                                if (str.equals(CompressorStreamFactory.BROTLI)) {
                                    c = 3;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 3704:
                                break;
                            case 3710:
                                if (str.equals("tr")) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                newRect.left = (int) (this.dragOriginalRect.left + bitmapDx);
                                newRect.top = (int) (this.dragOriginalRect.top + bitmapDy);
                                break;
                            case 1:
                                newRect.right = (int) (this.dragOriginalRect.right + bitmapDx);
                                newRect.top = (int) (this.dragOriginalRect.top + bitmapDy);
                                break;
                            case 2:
                                newRect.left = (int) (this.dragOriginalRect.left + bitmapDx);
                                newRect.bottom = (int) (this.dragOriginalRect.bottom + bitmapDy);
                                break;
                            case 3:
                                newRect.right = (int) (this.dragOriginalRect.right + bitmapDx);
                                newRect.bottom = (int) (this.dragOriginalRect.bottom + bitmapDy);
                                break;
                        }
                        if (newRect.left < newRect.right && newRect.top < newRect.bottom && newRect.width() >= 20
                                && newRect.height() >= 20) {
                            this.selectedBox.bitmapRect.set(newRect);
                            constrainRectToBitmap(this.selectedBox.bitmapRect);
                        }
                        invalidate();
                        return true;
                    }
                    break;
            }
        }
        if (this.isDragging && this.draggedSelection != null) {
            switch (event.getActionMasked()) {
                case 1:
                case 3:
                    this.isDragging = false;
                    this.draggedSelection = null;
                    this.dragStartPoint = null;
                    this.dragOriginalRect = null;
                    invalidate();
                    return true;
                case 2:
                    float screenDx2 = event.getX() - this.dragStartPoint.x;
                    float screenDy2 = event.getY() - this.dragStartPoint.y;
                    Matrix inverse2 = new Matrix();
                    this.imageMatrix.invert(inverse2);
                    float[] offset2 = { screenDx2, screenDy2 };
                    inverse2.mapVectors(offset2);
                    float bitmapDx2 = offset2[0];
                    float bitmapDy2 = offset2[1];
                    this.draggedSelection.bitmapRect.set((int) (this.dragOriginalRect.left + bitmapDx2),
                            (int) (this.dragOriginalRect.top + bitmapDy2),
                            (int) (this.dragOriginalRect.right + bitmapDx2),
                            (int) (this.dragOriginalRect.bottom + bitmapDy2));
                    constrainRectToBitmap(this.draggedSelection.bitmapRect);
                    invalidate();
                    return true;
                default:
                    return true;
            }
        }
        if (this.isLongPressSelecting) {
            switch (event.getActionMasked()) {
                case 2:
                    if (this.selectionRect != null) {
                        this.currentDragX = event.getX();
                        this.currentDragY = event.getY();
                        this.selectionRect.right = event.getX();
                        this.selectionRect.bottom = event.getY();
                        invalidate();
                    }
                    return true;
                case 1:
                case 3:
                    this.isLongPressSelecting = false;
                    if (this.selectionRect != null) {
                        RectF normalized = new RectF(
                                Math.min(this.selectionRect.left, this.selectionRect.right),
                                Math.min(this.selectionRect.top, this.selectionRect.bottom),
                                Math.max(this.selectionRect.left, this.selectionRect.right),
                                Math.max(this.selectionRect.top, this.selectionRect.bottom));
                        String gathered = gatherTextFromScreenRect(normalized);
                        Rect bitmapRect = screenRectToBitmapRect(normalized);
                        this.selectionRect = null;
                        invalidate();
                        // Always fire the listener — even with empty text so the
                        // activity can run an OCR fallback on the selected region.
                        if (this.textListener != null) {
                            this.textListener.onTextSelected(
                                    gathered != null ? gathered : "", bitmapRect);
                        }
                    }
                    return true;
            }
            return true;
        }
        if (!this.selectionMode) {
            this.scaleDetector.onTouchEvent(event);
            if (!this.isScaling) {
                this.panDetector.onTouchEvent(event);
            }
            return true;
        }
        switch (event.getActionMasked()) {
            case 0:
                this.selectionStart = new PointF(event.getX(), event.getY());
                this.selectionRect = new RectF(this.selectionStart.x, this.selectionStart.y, this.selectionStart.x,
                        this.selectionStart.y);
                invalidate();
                return true;
            case 1:
                if (this.selectionRect != null) {
                    RectF normalized = new RectF(Math.min(this.selectionRect.left, this.selectionRect.right),
                            Math.min(this.selectionRect.top, this.selectionRect.bottom),
                            Math.max(this.selectionRect.left, this.selectionRect.right),
                            Math.max(this.selectionRect.top, this.selectionRect.bottom));
                    Rect bitmapRect = screenRectToBitmapRect(normalized);
                    if (bitmapRect.width() < 20 || bitmapRect.height() < 20) {
                        if (this.textListener != null && !this.textRegions.isEmpty()) {
                            PointF tapPoint = new PointF((normalized.left + normalized.right) / 2.0f,
                                    (normalized.top + normalized.bottom) / 2.0f);
                            PaddleOCREngine.TextRegion hit = findTextRegionAt(tapPoint);
                            if (hit != null) {
                                this.selectionRect = null;
                                invalidate();
                                this.textListener.onTextSelected(hit.text(), hit.boundingBox());
                                return true;
                            }
                        }
                        bitmapRect = expandPointToRegion(bitmapRect, 100);
                    }
                    this.selectionRect = null;
                    invalidate();
                    if (this.listener != null && bitmapRect.width() > 0 && bitmapRect.height() > 0) {
                        this.listener.onSelectionComplete(bitmapRect);
                    }
                }
                return true;
            case 2:
                if (this.selectionRect != null) {
                    this.selectionRect.right = event.getX();
                    this.selectionRect.bottom = event.getY();
                    invalidate();
                }
                return true;
            default:
                return true;
        }
    }

    private Rect screenRectToBitmapRect(RectF screenRect) {
        if (this.imageBitmap == null || this.imageBitmap.isRecycled()) {
            return new Rect(0, 0, 0, 0);
        }
        Matrix inverse = new Matrix();
        this.imageMatrix.invert(inverse);
        float[] pts = { screenRect.left, screenRect.top, screenRect.right, screenRect.bottom };
        inverse.mapPoints(pts);
        return new Rect(Math.max(0, (int) pts[0]), Math.max(0, (int) pts[1]),
                Math.min(this.imageBitmap.getWidth(), (int) pts[2]),
                Math.min(this.imageBitmap.getHeight(), (int) pts[3]));
    }

    private RectF bitmapRectToScreenRect(Rect bitmapRect) {
        float[] pts = { bitmapRect.left, bitmapRect.top, bitmapRect.right, bitmapRect.bottom };
        this.imageMatrix.mapPoints(pts);
        return new RectF(pts[0], pts[1], pts[2], pts[3]);
    }

    private Rect expandPointToRegion(Rect point, int radius) {
        int cx = point.centerX();
        int cy = point.centerY();
        return new Rect(Math.max(0, cx - radius), Math.max(0, cy - radius),
                Math.min(this.imageBitmap.getWidth(), cx + radius),
                Math.min(this.imageBitmap.getHeight(), cy + radius));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CompletedSelection findCompletedSelectionAt(PointF screenPoint) {
        for (int i = this.completedSelections.size() - 1; i >= 0; i--) {
            CompletedSelection cs = this.completedSelections.get(i);
            RectF screenRect = bitmapRectToScreenRect(cs.bitmapRect);
            if (screenRect.contains(screenPoint.x, screenPoint.y)) {
                return cs;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PaddleOCREngine.TextRegion findTextRegionAt(PointF screenPoint) {
        Matrix inverse = new Matrix();
        this.imageMatrix.invert(inverse);
        float[] pt = { screenPoint.x, screenPoint.y };
        inverse.mapPoints(pt);
        for (PaddleOCREngine.TextRegion region : this.textRegions) {
            if (region.boundingBox() != null && region.boundingBox().contains((int) pt[0], (int) pt[1])) {
                return region;
            }
        }
        return null;
    }

    private String gatherTextFromScreenRect(RectF screenRect) {
        // Prefer character-level regions for precise selection; fall back to line
        // regions. Take a local snapshot to avoid ConcurrentModificationException
        // when the ML Kit callback replaces the list on another thread.
        List<PaddleOCREngine.TextRegion> charSnap = new ArrayList<>(this.charRegions);
        List<PaddleOCREngine.TextRegion> lineSnap = new ArrayList<>(this.textRegions);
        List<PaddleOCREngine.TextRegion> source = (!charSnap.isEmpty()) ? charSnap : lineSnap;
        if (source.isEmpty() || this.imageBitmap == null || this.imageBitmap.isRecycled()) {
            return null;
        }
        final Rect bitmapRect = screenRectToBitmapRect(screenRect);

        // Collect characters whose center point falls inside the drawn rectangle
        List<PaddleOCREngine.TextRegion> matched = new ArrayList<>();
        for (PaddleOCREngine.TextRegion region : source) {
            if (region.boundingBox() != null) {
                int cx = region.boundingBox().centerX();
                int cy = region.boundingBox().centerY();
                if (bitmapRect.contains(cx, cy)) {
                    matched.add(region);
                }
            }
        }
        if (matched.isEmpty()) {
            return null;
        }

        // Sort in reading order: top-to-bottom rows, then left-to-right within each row
        Collections.sort(matched, new Comparator<PaddleOCREngine.TextRegion>() {
            @Override
            public int compare(PaddleOCREngine.TextRegion a, PaddleOCREngine.TextRegion b) {
                int avgH = (a.boundingBox().height() + b.boundingBox().height()) / 2;
                int rowDiff = a.boundingBox().centerY() - b.boundingBox().centerY();
                if (Math.abs(rowDiff) > avgH / 2) {
                    return rowDiff; // different rows
                }
                return a.boundingBox().centerX() - b.boundingBox().centerX(); // same row
            }
        });

        // Join with spaces between words (large gap) and newlines between rows
        StringBuilder sb = new StringBuilder();
        PaddleOCREngine.TextRegion prev = null;
        for (PaddleOCREngine.TextRegion region : matched) {
            if (prev != null) {
                int avgH = (region.boundingBox().height() + prev.boundingBox().height()) / 2;
                int rowDiff = Math.abs(region.boundingBox().centerY() - prev.boundingBox().centerY());
                if (rowDiff > avgH / 2) {
                    sb.append("\n");
                } else {
                    // Insert space when the gap between characters is wider than one character
                    int gap = region.boundingBox().left - prev.boundingBox().right;
                    int charW = (region.boundingBox().width() + prev.boundingBox().width()) / 2;
                    if (gap > charW / 2) {
                        sb.append(" ");
                    }
                }
            }
            sb.append(region.text());
            prev = region;
        }
        return sb.toString();
    }

    private void constrainRectToBitmap(Rect rect) {
        if (this.imageBitmap == null) {
            return;
        }
        int width = rect.width();
        int height = rect.height();
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = width;
        }
        if (rect.right > this.imageBitmap.getWidth()) {
            rect.right = this.imageBitmap.getWidth();
            rect.left = this.imageBitmap.getWidth() - width;
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = height;
        }
        if (rect.bottom > this.imageBitmap.getHeight()) {
            rect.bottom = this.imageBitmap.getHeight();
            rect.top = this.imageBitmap.getHeight() - height;
        }
    }

    private void drawCornerHandles(Canvas canvas, RectF screenRect, int color) {
        Paint handlePaint = new Paint(1);
        handlePaint.setColor(color);
        handlePaint.setStyle(Paint.Style.FILL);
        Paint handleStroke = new Paint(1);
        handleStroke.setColor(-1);
        handleStroke.setStyle(Paint.Style.STROKE);
        handleStroke.setStrokeWidth(2.0f);
        canvas.drawCircle(screenRect.left, screenRect.top, 12.0f, handlePaint);
        canvas.drawCircle(screenRect.left, screenRect.top, 12.0f, handleStroke);
        canvas.drawCircle(screenRect.right, screenRect.top, 12.0f, handlePaint);
        canvas.drawCircle(screenRect.right, screenRect.top, 12.0f, handleStroke);
        canvas.drawCircle(screenRect.left, screenRect.bottom, 12.0f, handlePaint);
        canvas.drawCircle(screenRect.left, screenRect.bottom, 12.0f, handleStroke);
        canvas.drawCircle(screenRect.right, screenRect.bottom, 12.0f, handlePaint);
        canvas.drawCircle(screenRect.right, screenRect.bottom, 12.0f, handleStroke);
    }

    private String getCornerAtPoint(RectF screenRect, float x, float y) {
        if (Math.abs(x - screenRect.left) < 25.0f && Math.abs(y - screenRect.top) < 25.0f) {
            return "tl";
        }
        if (Math.abs(x - screenRect.right) < 25.0f && Math.abs(y - screenRect.top) < 25.0f) {
            return "tr";
        }
        if (Math.abs(x - screenRect.left) < 25.0f && Math.abs(y - screenRect.bottom) < 25.0f) {
            return "bl";
        }
        if (Math.abs(x - screenRect.right) < 25.0f && Math.abs(y - screenRect.bottom) < 25.0f) {
            return CompressorStreamFactory.BROTLI;
        }
        return null;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.imageBitmap != null) {
            canvas.drawBitmap(this.imageBitmap, this.imageMatrix, null);
        }
        if (!this.textRegions.isEmpty()) {
            Paint textBlockFill = new Paint(1);
            textBlockFill.setColor(0x1A2196F3);
            textBlockFill.setStyle(Paint.Style.FILL);
            Paint textBlockStroke = new Paint(1);
            textBlockStroke.setColor(0x662196F3);
            textBlockStroke.setStyle(Paint.Style.STROKE);
            textBlockStroke.setStrokeWidth(1.5f);
            for (PaddleOCREngine.TextRegion region : this.textRegions) {
                if (region.boundingBox() != null) {
                    RectF screenRect = bitmapRectToScreenRect(region.boundingBox());
                    canvas.drawRect(screenRect, textBlockFill);
                    canvas.drawRect(screenRect, textBlockStroke);
                }
            }
        }
        for (CompletedSelection cs : this.completedSelections) {
            RectF screenRect2 = bitmapRectToScreenRect(cs.bitmapRect);
            Paint fillPaint = new Paint(1);
            fillPaint.setColor((cs.color & 872415231) | 855638016);
            fillPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(screenRect2, fillPaint);
            Paint strokePaint = new Paint(1);
            strokePaint.setColor(cs.color);
            strokePaint.setStyle(Paint.Style.STROKE);
            if (cs == this.draggedSelection) {
                strokePaint.setStrokeWidth(4.0f);
                strokePaint.setColor(-10496);
            } else if (cs == this.selectedBox) {
                strokePaint.setStrokeWidth(4.0f);
                drawCornerHandles(canvas, screenRect2, cs.color);
            } else {
                strokePaint.setStrokeWidth(2.0f);
            }
            canvas.drawRect(screenRect2, strokePaint);
        }
        if (this.selectionRect != null) {
            RectF normalized = new RectF(Math.min(this.selectionRect.left, this.selectionRect.right),
                    Math.min(this.selectionRect.top, this.selectionRect.bottom),
                    Math.max(this.selectionRect.left, this.selectionRect.right),
                    Math.max(this.selectionRect.top, this.selectionRect.bottom));
            if (this.cropMode) {
                Paint dimPaint = new Paint(1);
                dimPaint.setColor(0xAA000000);
                dimPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(0, 0, getWidth(), normalized.top, dimPaint);
                canvas.drawRect(0, normalized.bottom, getWidth(), getHeight(), dimPaint);
                canvas.drawRect(0, normalized.top, normalized.left, normalized.bottom, dimPaint);
                canvas.drawRect(normalized.right, normalized.top, getWidth(), normalized.bottom, dimPaint);
                Paint cropBorderPaint = new Paint(1);
                cropBorderPaint.setColor(0xFFFFD700);
                cropBorderPaint.setStyle(Paint.Style.STROKE);
                cropBorderPaint.setStrokeWidth(3.0f);
                canvas.drawRect(normalized, cropBorderPaint);
                drawCornerHandles(canvas, normalized, 0xFFFFD700);
            } else {
                canvas.drawRect(normalized, this.selectionFillPaint);
                canvas.drawRect(normalized, this.selectionPaint);
                // Draw offset crosshair cursor above the drag thumb
                if (this.isLongPressSelecting && this.currentDragX > 0) {
                    drawDragCursor(canvas, this.currentDragX, this.currentDragY);
                }
            }
        }
    }

    private void drawDragCursor(Canvas canvas, float touchX, float touchY) {
        float density = getResources().getDisplayMetrics().density;
        float offsetY = 110f * density;
        float radius = 18f * density;
        float cx = touchX;
        float cy = touchY - offsetY;
        // Keep cursor on screen
        if (cy - radius < 0)
            cy = radius + 4;

        // Drop shadow
        Paint shadowP = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowP.setColor(0x55000000);
        canvas.drawCircle(cx + density * 2, cy + density * 2, radius, shadowP);

        // Gold fill
        Paint fillP = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillP.setColor(0xFFFFD700);
        fillP.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, radius, fillP);

        // Dark border
        Paint borderP = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderP.setColor(0xCC333333);
        borderP.setStyle(Paint.Style.STROKE);
        borderP.setStrokeWidth(density * 1.5f);
        canvas.drawCircle(cx, cy, radius, borderP);

        // Crosshair lines
        Paint crossP = new Paint(Paint.ANTI_ALIAS_FLAG);
        crossP.setColor(0xFF1A1A1A);
        crossP.setStrokeWidth(density * 2f);
        crossP.setStrokeCap(Paint.Cap.ROUND);
        float arm = radius * 0.55f;
        canvas.drawLine(cx - arm, cy, cx + arm, cy, crossP);
        canvas.drawLine(cx, cy - arm, cx, cy + arm, crossP);

        // Stem from cursor down to actual touch point
        Paint stemP = new Paint(Paint.ANTI_ALIAS_FLAG);
        stemP.setColor(0xBBFFD700);
        stemP.setStrokeWidth(density * 1.5f);
        stemP.setStyle(Paint.Style.STROKE);
        stemP.setPathEffect(new DashPathEffect(new float[] { density * 5f, density * 3f }, 0f));
        canvas.drawLine(cx, cy + radius, touchX, touchY, stemP);
    }
}
