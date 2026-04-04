package com.mobileinvoice.ocr;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.ViewCompat;

/* loaded from: classes7.dex */
public class ViewfinderOverlayView extends View {
    private static final int COLOR_DIM = 1711276032;
    private static final int COLOR_GOLD = -2838729;
    private static final int COLOR_GREEN = -11751600;
    private static final int COLOR_RED = -769226;
    private static final int COLOR_WHITE = -1;
    private static final int EDGE_BOTTOM = 8;
    private static final int EDGE_LEFT = 1;
    private static final int EDGE_NONE = 0;
    private static final int EDGE_RIGHT = 4;
    private static final int EDGE_TOP = 2;
    private static final float GUIDE_ASPECT_RATIO = 1.2941177f;
    private static final float GUIDE_COVERAGE = 0.78f;
    private static final float MIN_GUIDE_SIZE = 200.0f;
    private static final String PREFS_NAME = "viewfinder_prefs";
    private static final float TOUCH_SLOP = 48.0f;
    private Paint anchorPaint;
    private final float[] anchorScores;
    private boolean captureReady;
    private Paint dimPaint;
    private int dragEdge;
    private final RectF dragStartRect;
    private float dragStartX;
    private float dragStartY;
    private float flashAlpha;
    private ValueAnimator flashAnimator;
    private Paint flashPaint;
    private GestureDetector gestureDetector;
    private Paint guideAlignedPaint;
    private Paint guideDefaultPaint;
    private Paint guideReadyPaint;
    private final RectF guideRect;
    private final Rect guideRectInt;
    private Paint handlePaint;
    private boolean isFullScreen;
    private float overallScore;
    private boolean resizable;
    private Paint scoreBarPaint;
    private Paint scorePaint;
    private Paint textPaint;

    public ViewfinderOverlayView(Context context) {
        super(context);
        this.guideRect = new RectF();
        this.guideRectInt = new Rect();
        this.overallScore = 0.0f;
        this.anchorScores = new float[8];
        this.captureReady = false;
        this.flashAlpha = 0.0f;
        this.resizable = false;
        this.isFullScreen = false;
        this.dragEdge = 0;
        this.dragStartRect = new RectF();
        init();
    }

    public ViewfinderOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.guideRect = new RectF();
        this.guideRectInt = new Rect();
        this.overallScore = 0.0f;
        this.anchorScores = new float[8];
        this.captureReady = false;
        this.flashAlpha = 0.0f;
        this.resizable = false;
        this.isFullScreen = false;
        this.dragEdge = 0;
        this.dragStartRect = new RectF();
        init();
    }

    public ViewfinderOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.guideRect = new RectF();
        this.guideRectInt = new Rect();
        this.overallScore = 0.0f;
        this.anchorScores = new float[8];
        this.captureReady = false;
        this.flashAlpha = 0.0f;
        this.resizable = false;
        this.isFullScreen = false;
        this.dragEdge = 0;
        this.dragStartRect = new RectF();
        init();
    }

    private void init() {
        this.dimPaint = new Paint();
        this.dimPaint.setColor(COLOR_DIM);
        this.dimPaint.setStyle(Paint.Style.FILL);
        this.guideDefaultPaint = new Paint(1);
        this.guideDefaultPaint.setColor(-1);
        this.guideDefaultPaint.setStyle(Paint.Style.STROKE);
        this.guideDefaultPaint.setStrokeWidth(2.0f);
        this.guideDefaultPaint.setPathEffect(new DashPathEffect(new float[]{15.0f, 10.0f}, 0.0f));
        this.guideAlignedPaint = new Paint(1);
        this.guideAlignedPaint.setColor(COLOR_GOLD);
        this.guideAlignedPaint.setStyle(Paint.Style.STROKE);
        this.guideAlignedPaint.setStrokeWidth(3.0f);
        this.guideReadyPaint = new Paint(1);
        this.guideReadyPaint.setColor(COLOR_GREEN);
        this.guideReadyPaint.setStyle(Paint.Style.STROKE);
        this.guideReadyPaint.setStrokeWidth(4.0f);
        this.anchorPaint = new Paint(1);
        this.anchorPaint.setStyle(Paint.Style.FILL);
        this.flashPaint = new Paint();
        this.flashPaint.setStyle(Paint.Style.FILL);
        this.textPaint = new Paint(1);
        this.textPaint.setColor(-1);
        this.textPaint.setTextSize(28.0f);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.setShadowLayer(4.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
        this.scorePaint = new Paint(1);
        this.scorePaint.setColor(1157627903);
        this.scorePaint.setStyle(Paint.Style.FILL);
        this.scoreBarPaint = new Paint(1);
        this.scoreBarPaint.setStyle(Paint.Style.FILL);
        this.handlePaint = new Paint(1);
        this.handlePaint.setColor(COLOR_GOLD);
        this.handlePaint.setStyle(Paint.Style.FILL);
        this.handlePaint.setAlpha(180);
        this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.mobileinvoice.ocr.ViewfinderOverlayView.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent e) {
                if (ViewfinderOverlayView.this.resizable) {
                    ViewfinderOverlayView.this.toggleFullScreen();
                    return true;
                }
                return false;
            }
        });
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        invalidate();
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!restoreSavedRect(w, h)) {
            calculateGuideRect(w, h);
        }
    }

    private void calculateGuideRect(int viewWidth, int viewHeight) {
        float guideW;
        float guideH;
        if (viewWidth <= 0 || viewHeight <= 0) {
            return;
        }
        float availableW = viewWidth * GUIDE_COVERAGE;
        float availableH = viewHeight * GUIDE_COVERAGE;
        if (availableW / availableH > GUIDE_ASPECT_RATIO) {
            guideH = availableH;
            guideW = GUIDE_ASPECT_RATIO * guideH;
        } else {
            guideW = availableW;
            guideH = availableW / GUIDE_ASPECT_RATIO;
        }
        float cx = viewWidth / 2.0f;
        float cy = viewHeight / 2.0f;
        this.guideRect.set(cx - (guideW / 2.0f), cy - (guideH / 2.0f), (guideW / 2.0f) + cx, (guideH / 2.0f) + cy);
        updateIntRect();
    }

    private void setFullScreenRect() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        this.guideRect.set(16.0f, 16.0f, w - 16.0f, h - 16.0f);
        updateIntRect();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleFullScreen() {
        this.isFullScreen = !this.isFullScreen;
        if (this.isFullScreen) {
            setFullScreenRect();
        } else {
            calculateGuideRect(getWidth(), getHeight());
            invalidate();
        }
        saveRect();
    }

    private void updateIntRect() {
        this.guideRectInt.set((int) this.guideRect.left, (int) this.guideRect.top, (int) this.guideRect.right, (int) this.guideRect.bottom);
    }

    public Rect getGuideRect() {
        return this.guideRectInt;
    }

    public void updateAlignment(float score, float[] anchors, boolean ready) {
        this.overallScore = score;
        if (anchors != null && anchors.length == 8) {
            System.arraycopy(anchors, 0, this.anchorScores, 0, 8);
        }
        this.captureReady = ready;
        invalidate();
    }

    public void triggerCaptureFlash() {
        if (this.flashAnimator != null && this.flashAnimator.isRunning()) {
            this.flashAnimator.cancel();
        }
        this.flashAnimator = ValueAnimator.ofFloat(0.4f, 0.0f);
        this.flashAnimator.setDuration(400L);
        this.flashAnimator.setInterpolator(new DecelerateInterpolator());
        this.flashAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.mobileinvoice.ocr.ViewfinderOverlayView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewfinderOverlayView.this.lambda$triggerCaptureFlash$0(valueAnimator);
            }
        });
        this.flashAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerCaptureFlash$0(ValueAnimator anim) {
        this.flashAlpha = ((Float) anim.getAnimatedValue()).floatValue();
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.resizable) {
            this.gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case 0:
                    this.dragEdge = hitTestEdge(event.getX(), event.getY());
                    if (this.dragEdge != 0) {
                        this.dragStartX = event.getX();
                        this.dragStartY = event.getY();
                        this.dragStartRect.set(this.guideRect);
                        return true;
                    }
                    break;
                case 1:
                case 3:
                    if (this.dragEdge != 0) {
                        this.dragEdge = 0;
                        this.isFullScreen = false;
                        updateIntRect();
                        saveRect();
                        return true;
                    }
                    break;
                case 2:
                    if (this.dragEdge != 0) {
                        float dx = event.getX() - this.dragStartX;
                        float dy = event.getY() - this.dragStartY;
                        applyDrag(dx, dy);
                        invalidate();
                        return true;
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private int hitTestEdge(float x, float y) {
        boolean nearLeft = Math.abs(x - this.guideRect.left) < TOUCH_SLOP;
        boolean nearRight = Math.abs(x - this.guideRect.right) < TOUCH_SLOP;
        boolean nearTop = Math.abs(y - this.guideRect.top) < TOUCH_SLOP;
        boolean nearBottom = Math.abs(y - this.guideRect.bottom) < TOUCH_SLOP;
        boolean withinX = x >= this.guideRect.left - TOUCH_SLOP && x <= this.guideRect.right + TOUCH_SLOP;
        boolean withinY = y >= this.guideRect.top - TOUCH_SLOP && y <= this.guideRect.bottom + TOUCH_SLOP;
        if (nearLeft && nearTop) {
            return 3;
        }
        if (nearRight && nearTop) {
            return 6;
        }
        if (nearLeft && nearBottom) {
            return 9;
        }
        if (nearRight && nearBottom) {
            return 12;
        }
        if (nearLeft && withinY) {
            return 1;
        }
        if (nearRight && withinY) {
            return 4;
        }
        if (nearTop && withinX) {
            return 2;
        }
        return (nearBottom && withinX) ? 8 : 0;
    }

    private void applyDrag(float dx, float dy) {
        float newLeft = this.dragStartRect.left;
        float newTop = this.dragStartRect.top;
        float newRight = this.dragStartRect.right;
        float newBottom = this.dragStartRect.bottom;
        int w = getWidth();
        int h = getHeight();
        if ((this.dragEdge & 1) != 0) {
            newLeft = Math.max(0.0f, Math.min(this.dragStartRect.left + dx, newRight - 200.0f));
        }
        if ((this.dragEdge & 4) != 0) {
            newRight = Math.min(w, Math.max(this.dragStartRect.right + dx, newLeft + 200.0f));
        }
        if ((this.dragEdge & 2) != 0) {
            newTop = Math.max(0.0f, Math.min(this.dragStartRect.top + dy, newBottom - 200.0f));
        }
        if ((this.dragEdge & 8) != 0) {
            newBottom = Math.min(h, Math.max(this.dragStartRect.bottom + dy, 200.0f + newTop));
        }
        this.guideRect.set(newLeft, newTop, newRight, newBottom);
        updateIntRect();
    }

    private void saveRect() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().putFloat("left_pct", this.guideRect.left / w).putFloat("top_pct", this.guideRect.top / h).putFloat("right_pct", this.guideRect.right / w).putFloat("bottom_pct", this.guideRect.bottom / h).putBoolean("is_fullscreen", this.isFullScreen).apply();
    }

    private boolean restoreSavedRect(int w, int h) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, 0);
        if (!prefs.contains("left_pct")) {
            return false;
        }
        this.isFullScreen = prefs.getBoolean("is_fullscreen", false);
        float left = prefs.getFloat("left_pct", 0.0f) * w;
        float top = prefs.getFloat("top_pct", 0.0f) * h;
        float right = prefs.getFloat("right_pct", 1.0f) * w;
        float bottom = prefs.getFloat("bottom_pct", 1.0f) * h;
        if (right - left < 200.0f || bottom - top < 200.0f) {
            return false;
        }
        this.guideRect.set(left, top, right, bottom);
        updateIntRect();
        return true;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        drawDimOverlay(canvas, w, h);
        drawGuideFrame(canvas);
        if (this.resizable) {
            drawDragHandles(canvas);
        }
        drawAnchors(canvas);
        drawScoreBar(canvas, w, h);
        drawStatusText(canvas, w, h);
        if (this.flashAlpha > 0.0f) {
            this.flashPaint.setColor(Color.argb((int) (this.flashAlpha * 255.0f), 76, 175, 80));
            canvas.drawRect(0.0f, 0.0f, w, h, this.flashPaint);
        }
    }

    private void drawDimOverlay(Canvas canvas, int w, int h) {
        canvas.drawRect(0.0f, 0.0f, w, this.guideRect.top, this.dimPaint);
        canvas.drawRect(0.0f, this.guideRect.bottom, w, h, this.dimPaint);
        canvas.drawRect(0.0f, this.guideRect.top, this.guideRect.left, this.guideRect.bottom, this.dimPaint);
        canvas.drawRect(this.guideRect.right, this.guideRect.top, w, this.guideRect.bottom, this.dimPaint);
    }

    private void drawGuideFrame(Canvas canvas) {
        Paint framePaint;
        if (this.captureReady) {
            framePaint = this.guideReadyPaint;
        } else if (this.overallScore > 0.3f) {
            framePaint = this.guideAlignedPaint;
        } else {
            framePaint = this.guideDefaultPaint;
        }
        float cornerLen = Math.min(this.guideRect.width(), this.guideRect.height()) * 0.12f;
        Paint paint = framePaint;
        canvas.drawLine(this.guideRect.left, this.guideRect.top, this.guideRect.left + cornerLen, this.guideRect.top, paint);
        canvas.drawLine(this.guideRect.left, this.guideRect.top, this.guideRect.left, this.guideRect.top + cornerLen, paint);
        canvas.drawLine(this.guideRect.right - cornerLen, this.guideRect.top, this.guideRect.right, this.guideRect.top, paint);
        canvas.drawLine(this.guideRect.right, this.guideRect.top, this.guideRect.right, this.guideRect.top + cornerLen, paint);
        canvas.drawLine(this.guideRect.left, this.guideRect.bottom, this.guideRect.left + cornerLen, this.guideRect.bottom, paint);
        canvas.drawLine(this.guideRect.left, this.guideRect.bottom - cornerLen, this.guideRect.left, this.guideRect.bottom, paint);
        canvas.drawLine(this.guideRect.right - cornerLen, this.guideRect.bottom, this.guideRect.right, this.guideRect.bottom, paint);
        canvas.drawLine(this.guideRect.right, this.guideRect.bottom - cornerLen, this.guideRect.right, this.guideRect.bottom, paint);
        Paint thinPaint = new Paint(framePaint);
        thinPaint.setStrokeWidth(1.0f);
        thinPaint.setAlpha(80);
        canvas.drawLine(this.guideRect.left + cornerLen, this.guideRect.top, this.guideRect.right - cornerLen, this.guideRect.top, thinPaint);
        canvas.drawLine(this.guideRect.left + cornerLen, this.guideRect.bottom, this.guideRect.right - cornerLen, this.guideRect.bottom, thinPaint);
        canvas.drawLine(this.guideRect.left, this.guideRect.top + cornerLen, this.guideRect.left, this.guideRect.bottom - cornerLen, thinPaint);
        canvas.drawLine(this.guideRect.right, this.guideRect.top + cornerLen, this.guideRect.right, this.guideRect.bottom - cornerLen, thinPaint);
    }

    private void drawDragHandles(Canvas canvas) {
        float midX = this.guideRect.centerX();
        float midY = this.guideRect.centerY();
        canvas.drawCircle(this.guideRect.left, this.guideRect.top, 8.0f, this.handlePaint);
        canvas.drawCircle(this.guideRect.right, this.guideRect.top, 8.0f, this.handlePaint);
        canvas.drawCircle(this.guideRect.left, this.guideRect.bottom, 8.0f, this.handlePaint);
        canvas.drawCircle(this.guideRect.right, this.guideRect.bottom, 8.0f, this.handlePaint);
        canvas.drawCircle(midX, this.guideRect.top, 5.0f, this.handlePaint);
        canvas.drawCircle(midX, this.guideRect.bottom, 5.0f, this.handlePaint);
        canvas.drawCircle(this.guideRect.left, midY, 5.0f, this.handlePaint);
        canvas.drawCircle(this.guideRect.right, midY, 5.0f, this.handlePaint);
    }

    private void drawAnchors(Canvas canvas) {
        int color;
        float[][] positions = {new float[]{this.guideRect.left, this.guideRect.top}, new float[]{this.guideRect.right, this.guideRect.top}, new float[]{this.guideRect.left, this.guideRect.bottom}, new float[]{this.guideRect.right, this.guideRect.bottom}, new float[]{this.guideRect.centerX(), this.guideRect.top}, new float[]{this.guideRect.centerX(), this.guideRect.bottom}, new float[]{this.guideRect.left, this.guideRect.centerY()}, new float[]{this.guideRect.right, this.guideRect.centerY()}};
        for (int i = 0; i < 8; i++) {
            float score = this.anchorScores[i];
            if (score < 0.5f) {
                float t = 2.0f * score;
                color = interpolateColor(COLOR_RED, COLOR_GOLD, t);
            } else {
                float t2 = (score - 0.5f) * 2.0f;
                color = interpolateColor(COLOR_GOLD, COLOR_GREEN, t2);
            }
            this.anchorPaint.setColor(color);
            canvas.drawCircle(positions[i][0], positions[i][1], 6.0f, this.anchorPaint);
            this.anchorPaint.setStyle(Paint.Style.STROKE);
            this.anchorPaint.setStrokeWidth(1.5f);
            this.anchorPaint.setAlpha(120);
            canvas.drawCircle(positions[i][0], positions[i][1], 3.0f + 6.0f, this.anchorPaint);
            this.anchorPaint.setStyle(Paint.Style.FILL);
            this.anchorPaint.setAlpha(255);
        }
    }

    private void drawScoreBar(Canvas canvas, int w, int h) {
        int barColor;
        float barY = this.guideRect.bottom + 16.0f;
        float barLeft = this.guideRect.left;
        float barRight = this.guideRect.right;
        canvas.drawRoundRect(barLeft, barY, barRight, barY + 6.0f, 3.0f, 3.0f, this.scorePaint);
        if (this.overallScore > 0.0f) {
            if (this.captureReady) {
                barColor = COLOR_GREEN;
            } else if (this.overallScore > 0.3f) {
                barColor = COLOR_GOLD;
            } else {
                barColor = -1;
            }
            this.scoreBarPaint.setColor(barColor);
            float fillWidth = (barRight - barLeft) * this.overallScore;
            canvas.drawRoundRect(barLeft, barY, barLeft + fillWidth, barY + 6.0f, 3.0f, 3.0f, this.scoreBarPaint);
        }
    }

    private void drawStatusText(Canvas canvas, int w, int h) {
        String text;
        if (this.captureReady) {
            text = "Capturing...";
            this.textPaint.setColor(COLOR_GREEN);
        } else if (this.overallScore > 0.3f) {
            text = "Aligning...";
            this.textPaint.setColor(COLOR_GOLD);
        } else {
            text = "Position invoice in frame";
            this.textPaint.setColor(-1);
        }
        float textY = this.guideRect.bottom + 46.0f;
        canvas.drawText(text, w / 2.0f, textY, this.textPaint);
    }

    private int interpolateColor(int from, int to, float fraction) {
        float fraction2 = Math.max(0.0f, Math.min(1.0f, fraction));
        int r = (int) (Color.red(from) + ((Color.red(to) - Color.red(from)) * fraction2));
        int g = (int) (Color.green(from) + ((Color.green(to) - Color.green(from)) * fraction2));
        int b = (int) (Color.blue(from) + ((Color.blue(to) - Color.blue(from)) * fraction2));
        return Color.rgb(r, g, b);
    }
}
