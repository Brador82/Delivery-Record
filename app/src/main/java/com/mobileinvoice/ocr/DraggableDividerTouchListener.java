package com.mobileinvoice.ocr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/* loaded from: classes7.dex */
public class DraggableDividerTouchListener implements View.OnTouchListener {
    private static final int DRAG_THRESHOLD_PX = 15;
    private static final float SNAP_BOTTOM_THRESHOLD = 0.85f;
    private static final int SNAP_DURATION_MS = 250;
    private static final float SNAP_TOP_THRESHOLD = 0.15f;
    private float initialListWeight;
    private float initialMapWeight;
    private float initialRawY;
    private final View listContainer;
    private final OnDividerDragListener listener;
    private final View mapContainer;
    private final View parentLayout;
    private final View summaryBar;
    private boolean isDragging = false;
    private boolean isAnimating = false;
    private SnapPosition currentPosition = SnapPosition.MIDDLE;

    public interface OnDividerDragListener {
        void onDragStart();

        void onDragUpdate(float mapWeight, float listWeight);

        void onSnapComplete(SnapPosition position);
    }

    public enum SnapPosition {
        TOP,
        MIDDLE,
        BOTTOM
    }

    public DraggableDividerTouchListener(View parentLayout, View mapContainer, View summaryBar, View listContainer, OnDividerDragListener listener) {
        this.parentLayout = parentLayout;
        this.mapContainer = mapContainer;
        this.summaryBar = summaryBar;
        this.listContainer = listContainer;
        this.listener = listener;
    }

    public SnapPosition getCurrentPosition() {
        return this.currentPosition;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        if (this.isAnimating) {
            return true;
        }
        switch (event.getActionMasked()) {
            case 0:
                this.initialRawY = event.getRawY();
                captureCurrentWeights();
                this.isDragging = false;
                return true;
            case 1:
            case 3:
                if (this.isDragging) {
                    snapToNearestPosition();
                } else {
                    v.performClick();
                }
                this.isDragging = false;
                return true;
            case 2:
                float deltaY = event.getRawY() - this.initialRawY;
                if (!this.isDragging && Math.abs(deltaY) > 15.0f) {
                    this.isDragging = true;
                    this.mapContainer.setVisibility(0);
                    this.listContainer.setVisibility(0);
                    if (this.listener != null) {
                        this.listener.onDragStart();
                    }
                }
                if (this.isDragging) {
                    updateWeights(deltaY);
                }
                return true;
            default:
                return false;
        }
    }

    private void captureCurrentWeights() {
        LinearLayout.LayoutParams mapParams = (LinearLayout.LayoutParams) this.mapContainer.getLayoutParams();
        LinearLayout.LayoutParams listParams = (LinearLayout.LayoutParams) this.listContainer.getLayoutParams();
        this.initialMapWeight = mapParams.weight;
        this.initialListWeight = listParams.weight;
    }

    private void updateWeights(float deltaY) {
        float availableHeight = this.parentLayout.getHeight() - this.summaryBar.getHeight();
        if (availableHeight <= 0.0f) {
            return;
        }
        float totalWeight = this.initialMapWeight + this.initialListWeight;
        float weightDelta = (deltaY / availableHeight) * totalWeight;
        float newMapWeight = this.initialMapWeight + weightDelta;
        float f = totalWeight - newMapWeight;
        float newMapWeight2 = Math.max(0.01f, Math.min(totalWeight - 0.01f, newMapWeight));
        float newListWeight = totalWeight - newMapWeight2;
        applyWeights(newMapWeight2, newListWeight);
        if (this.listener != null) {
            this.listener.onDragUpdate(newMapWeight2, newListWeight);
        }
    }

    private void snapToNearestPosition() {
        SnapPosition target;
        float currentMapRatio = getCurrentMapRatio();
        if (currentMapRatio < SNAP_TOP_THRESHOLD) {
            target = SnapPosition.TOP;
        } else if (currentMapRatio > SNAP_BOTTOM_THRESHOLD) {
            target = SnapPosition.BOTTOM;
        } else {
            target = SnapPosition.MIDDLE;
        }
        animateToPosition(target);
    }

    public void animateToPosition(final SnapPosition position) {
        final float targetMapWeight;
        final float targetListWeight;
        switch (position) {
            case TOP:
                targetMapWeight = 0.01f;
                targetListWeight = 1.0f;
                break;
            case MIDDLE:
            default:
                targetMapWeight = 1.0f;
                targetListWeight = 1.0f;
                break;
            case BOTTOM:
                targetMapWeight = 1.0f;
                targetListWeight = 0.01f;
                break;
        }
        this.mapContainer.setVisibility(0);
        this.listContainer.setVisibility(0);
        LinearLayout.LayoutParams mapParams = (LinearLayout.LayoutParams) this.mapContainer.getLayoutParams();
        final float startMapWeight = mapParams.weight;
        LinearLayout.LayoutParams listParams = (LinearLayout.LayoutParams) this.listContainer.getLayoutParams();
        final float startListWeight = listParams.weight;
        this.isAnimating = true;
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(250L);
        animator.setInterpolator(new DecelerateInterpolator());
        final float f = targetMapWeight;
        final float f2 = targetListWeight;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.mobileinvoice.ocr.DraggableDividerTouchListener$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DraggableDividerTouchListener.this.lambda$animateToPosition$0(startMapWeight, f, startListWeight, f2, valueAnimator);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.mobileinvoice.ocr.DraggableDividerTouchListener.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                DraggableDividerTouchListener.this.isAnimating = false;
                DraggableDividerTouchListener.this.currentPosition = position;
                DraggableDividerTouchListener.this.applyWeights(targetMapWeight, targetListWeight);
                if (position == SnapPosition.TOP) {
                    DraggableDividerTouchListener.this.mapContainer.setVisibility(8);
                } else if (position == SnapPosition.BOTTOM) {
                    DraggableDividerTouchListener.this.listContainer.setVisibility(8);
                }
                if (DraggableDividerTouchListener.this.listener != null) {
                    DraggableDividerTouchListener.this.listener.onSnapComplete(position);
                }
            }
        });
        animator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateToPosition$0(float startMapWeight, float targetMapWeight, float startListWeight, float targetListWeight, ValueAnimator anim) {
        float fraction = ((Float) anim.getAnimatedValue()).floatValue();
        float mapW = ((targetMapWeight - startMapWeight) * fraction) + startMapWeight;
        float listW = ((targetListWeight - startListWeight) * fraction) + startListWeight;
        applyWeights(mapW, listW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyWeights(float mapWeight, float listWeight) {
        LinearLayout.LayoutParams mapParams = (LinearLayout.LayoutParams) this.mapContainer.getLayoutParams();
        LinearLayout.LayoutParams listParams = (LinearLayout.LayoutParams) this.listContainer.getLayoutParams();
        mapParams.weight = mapWeight;
        listParams.weight = listWeight;
        this.mapContainer.setLayoutParams(mapParams);
        this.listContainer.setLayoutParams(listParams);
    }

    private float getCurrentMapRatio() {
        LinearLayout.LayoutParams mapParams = (LinearLayout.LayoutParams) this.mapContainer.getLayoutParams();
        LinearLayout.LayoutParams listParams = (LinearLayout.LayoutParams) this.listContainer.getLayoutParams();
        float total = mapParams.weight + listParams.weight;
        if (total > 0.0f) {
            return mapParams.weight / total;
        }
        return 0.5f;
    }
}
