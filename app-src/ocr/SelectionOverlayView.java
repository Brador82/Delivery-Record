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
import com.mobileinvoice.ocr.PaddleOCREngine;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/* loaded from: classes7.dex */
public class SelectionOverlayView extends View {
    private static final int HANDLE_SIZE = 50;
    private Paint completedFillPaint;
    private Paint completedPaint;
    private List<CompletedSelection> completedSelections;
    private Rect dragOriginalRect;
    private PointF dragStartPoint;
    private CompletedSelection draggedSelection;
    private Bitmap imageBitmap;
    private Matrix imageMatrix;
    private boolean isDragging;
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
        this.selectionPaint.setColor(-14575885);
        this.selectionPaint.setStyle(Paint.Style.STROKE);
        this.selectionPaint.setStrokeWidth(3.0f);
        this.selectionPaint.setPathEffect(new DashPathEffect(new float[]{15.0f, 10.0f}, 0.0f));
        this.selectionFillPaint = new Paint(1);
        this.selectionFillPaint.setColor(857839347);
        this.selectionFillPaint.setStyle(Paint.Style.FILL);
        this.completedPaint = new Paint(1);
        this.completedPaint.setColor(-6381922);
        this.completedPaint.setStyle(Paint.Style.STROKE);
        this.completedPaint.setStrokeWidth(2.0f);
        this.completedFillPaint = new Paint(1);
        this.completedFillPaint.setColor(866033310);
        this.completedFillPaint.setStyle(Paint.Style.FILL);
        this.scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.mobileinvoice.ocr.SelectionOverlayView.1
            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                SelectionOverlayView.this.isScaling = true;
                return true;
            }

            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector detector) {
                float factor = detector.getScaleFactor();
                float newScale = Math.max(SelectionOverlayView.this.minScale, Math.min(SelectionOverlayView.this.maxScale, SelectionOverlayView.this.scaleFactor * factor));
                float adjustedFactor = newScale / SelectionOverlayView.this.scaleFactor;
                SelectionOverlayView.this.scaleFactor = newScale;
                SelectionOverlayView.this.imageMatrix.postScale(adjustedFactor, adjustedFactor, detector.getFocusX(), detector.getFocusY());
                SelectionOverlayView.this.constrainTranslation();
                SelectionOverlayView.this.invalidate();
                return true;
            }

            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector detector) {
                SelectionOverlayView.this.isScaling = false;
            }
        });
        this.panDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: com.mobileinvoice.ocr.SelectionOverlayView.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!SelectionOverlayView.this.isDragging) {
                    SelectionOverlayView.this.imageMatrix.postTranslate(-distanceX, -distanceY);
                    SelectionOverlayView.this.constrainTranslation();
                    SelectionOverlayView.this.invalidate();
                    return true;
                }
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                PointF touchPoint = new PointF(e.getX(), e.getY());
                CompletedSelection hit = SelectionOverlayView.this.findCompletedSelectionAt(touchPoint);
                if (hit != null && SelectionOverlayView.this.selectedBox == null) {
                    SelectionOverlayView.this.draggedSelection = hit;
                    SelectionOverlayView.this.dragStartPoint = touchPoint;
                    SelectionOverlayView.this.dragOriginalRect = new Rect(hit.bitmapRect);
                    SelectionOverlayView.this.isDragging = true;
                    SelectionOverlayView.this.invalidate();
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent e) {
                PaddleOCREngine.TextRegion tappedRegion;
                PointF touchPoint = new PointF(e.getX(), e.getY());
                if (SelectionOverlayView.this.textListener != null && !SelectionOverlayView.this.textRegions.isEmpty() && (tappedRegion = SelectionOverlayView.this.findTextRegionAt(touchPoint)) != null) {
                    SelectionOverlayView.this.textListener.onTextSelected(tappedRegion.text, tappedRegion.boundingBox);
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

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
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
        this.textRegions = regions != null ? regions : new ArrayList<>();
        invalidate();
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
        float scaleX = getWidth() / this.imageBitmap.getWidth();
        float scaleY = getHeight() / this.imageBitmap.getHeight();
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x007b, code lost:
    
        if (r11.equals("tl") != false) goto L34;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
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
                        float[] offset = {screenDx, screenDy};
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
                        if (newRect.left < newRect.right && newRect.top < newRect.bottom && newRect.width() >= 20 && newRect.height() >= 20) {
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
                    float[] offset2 = {screenDx2, screenDy2};
                    inverse2.mapVectors(offset2);
                    float bitmapDx2 = offset2[0];
                    float bitmapDy2 = offset2[1];
                    this.draggedSelection.bitmapRect.set((int) (this.dragOriginalRect.left + bitmapDx2), (int) (this.dragOriginalRect.top + bitmapDy2), (int) (this.dragOriginalRect.right + bitmapDx2), (int) (this.dragOriginalRect.bottom + bitmapDy2));
                    constrainRectToBitmap(this.draggedSelection.bitmapRect);
                    invalidate();
                    return true;
                default:
                    return true;
            }
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
                this.selectionRect = new RectF(this.selectionStart.x, this.selectionStart.y, this.selectionStart.x, this.selectionStart.y);
                invalidate();
                return true;
            case 1:
                if (this.selectionRect != null) {
                    RectF normalized = new RectF(Math.min(this.selectionRect.left, this.selectionRect.right), Math.min(this.selectionRect.top, this.selectionRect.bottom), Math.max(this.selectionRect.left, this.selectionRect.right), Math.max(this.selectionRect.top, this.selectionRect.bottom));
                    Rect bitmapRect = screenRectToBitmapRect(normalized);
                    if (bitmapRect.width() < 20 || bitmapRect.height() < 20) {
                        if (this.textListener != null && !this.textRegions.isEmpty()) {
                            PointF tapPoint = new PointF((normalized.left + normalized.right) / 2.0f, (normalized.top + normalized.bottom) / 2.0f);
                            PaddleOCREngine.TextRegion hit = findTextRegionAt(tapPoint);
                            if (hit != null) {
                                this.selectionRect = null;
                                invalidate();
                                this.textListener.onTextSelected(hit.text, hit.boundingBox);
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
        Matrix inverse = new Matrix();
        this.imageMatrix.invert(inverse);
        float[] pts = {screenRect.left, screenRect.top, screenRect.right, screenRect.bottom};
        inverse.mapPoints(pts);
        return new Rect(Math.max(0, (int) pts[0]), Math.max(0, (int) pts[1]), Math.min(this.imageBitmap.getWidth(), (int) pts[2]), Math.min(this.imageBitmap.getHeight(), (int) pts[3]));
    }

    private RectF bitmapRectToScreenRect(Rect bitmapRect) {
        float[] pts = {bitmapRect.left, bitmapRect.top, bitmapRect.right, bitmapRect.bottom};
        this.imageMatrix.mapPoints(pts);
        return new RectF(pts[0], pts[1], pts[2], pts[3]);
    }

    private Rect expandPointToRegion(Rect point, int radius) {
        int cx = point.centerX();
        int cy = point.centerY();
        return new Rect(Math.max(0, cx - radius), Math.max(0, cy - radius), Math.min(this.imageBitmap.getWidth(), cx + radius), Math.min(this.imageBitmap.getHeight(), cy + radius));
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
        float[] pt = {screenPoint.x, screenPoint.y};
        inverse.mapPoints(pt);
        for (PaddleOCREngine.TextRegion region : this.textRegions) {
            if (region.boundingBox != null && region.boundingBox.contains((int) pt[0], (int) pt[1])) {
                return region;
            }
        }
        return null;
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
            textBlockFill.setColor(438408947);
            textBlockFill.setStyle(Paint.Style.FILL);
            Paint textBlockStroke = new Paint(1);
            textBlockStroke.setColor(1713477363);
            textBlockStroke.setStyle(Paint.Style.STROKE);
            textBlockStroke.setStrokeWidth(1.5f);
            for (PaddleOCREngine.TextRegion region : this.textRegions) {
                if (region.boundingBox != null) {
                    RectF screenRect = bitmapRectToScreenRect(region.boundingBox);
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
            RectF normalized = new RectF(Math.min(this.selectionRect.left, this.selectionRect.right), Math.min(this.selectionRect.top, this.selectionRect.bottom), Math.max(this.selectionRect.left, this.selectionRect.right), Math.max(this.selectionRect.top, this.selectionRect.bottom));
            canvas.drawRect(normalized, this.selectionFillPaint);
            canvas.drawRect(normalized, this.selectionPaint);
        }
    }
}
