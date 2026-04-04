package com.mobileinvoice.ocr;

import android.graphics.Rect;

/* loaded from: classes7.dex */
public class DocumentDetector {
    private static final float EDGE_CONTRAST_THRESHOLD = 15.0f;
    private static final int NUM_ANCHORS = 8;
    private static final float READY_SCORE_THRESHOLD = 0.65f;
    private static final int REQUIRED_CONSECUTIVE = 5;
    private static final int SAMPLE_SIZE = 20;
    private float overallScore = 0.0f;
    private final float[] anchorScores = new float[8];
    private int consecutiveReadyFrames = 0;
    private boolean readyToCapture = false;

    public AlignmentResult analyze(byte[] yPlane, int width, int height, int rowStride, Rect guideRect) {
        if (guideRect == null || guideRect.isEmpty()) {
            return new AlignmentResult(0.0f, this.anchorScores.clone(), false);
        }
        int[][] anchorPositions = calculateAnchorPositions(guideRect);
        float totalScore = 0.0f;
        for (int i = 0; i < 8; i++) {
            int ax = anchorPositions[i][0];
            int ay = anchorPositions[i][1];
            boolean isHorizontal = anchorPositions[i][2] == 1;
            float score = measureEdgeContrast(yPlane, width, height, rowStride, ax, ay, isHorizontal, guideRect);
            this.anchorScores[i] = score;
            totalScore += score;
        }
        this.overallScore = totalScore / 8.0f;
        if (this.overallScore >= READY_SCORE_THRESHOLD) {
            this.consecutiveReadyFrames++;
        } else {
            this.consecutiveReadyFrames = 0;
        }
        this.readyToCapture = this.consecutiveReadyFrames >= 5;
        return new AlignmentResult(this.overallScore, this.anchorScores.clone(), this.readyToCapture);
    }

    public void reset() {
        this.overallScore = 0.0f;
        this.consecutiveReadyFrames = 0;
        this.readyToCapture = false;
        for (int i = 0; i < 8; i++) {
            this.anchorScores[i] = 0.0f;
        }
    }

    public float getAlignmentScore() {
        return this.overallScore;
    }

    private int[][] calculateAnchorPositions(Rect guide) {
        int cx = guide.centerX();
        int cy = guide.centerY();
        return new int[][]{new int[]{guide.left, guide.top, 1}, new int[]{guide.right, guide.top, 1}, new int[]{guide.left, guide.bottom, 1}, new int[]{guide.right, guide.bottom, 1}, new int[]{cx, guide.top, 1}, new int[]{cx, guide.bottom, 1}, new int[]{guide.left, cy, 0}, new int[]{guide.right, cy, 0}};
    }

    private float measureEdgeContrast(byte[] yPlane, int width, int height, int rowStride, int anchorX, int anchorY, boolean isHorizontal, Rect guide) {
        int insideX;
        int outsideX;
        int insideY;
        int outsideY;
        float insideAvg = 0.0f;
        float outsideAvg = 0.0f;
        int insideCount = 0;
        int outsideCount = 0;
        for (int d = 1; d <= 20; d++) {
            if (isHorizontal) {
                if (anchorY <= guide.centerY()) {
                    insideY = anchorY + d;
                    outsideY = anchorY - d;
                } else {
                    insideY = anchorY - d;
                    outsideY = anchorY + d;
                }
                for (int dx = -10; dx <= 10; dx++) {
                    int sx = anchorX + dx;
                    if (sx >= 0 && sx < width) {
                        if (insideY >= 0 && insideY < height) {
                            insideAvg += yPlane[(insideY * rowStride) + sx] & 255;
                            insideCount++;
                        }
                        if (outsideY >= 0 && outsideY < height) {
                            outsideAvg += yPlane[(outsideY * rowStride) + sx] & 255;
                            outsideCount++;
                        }
                    }
                }
            } else {
                if (anchorX <= guide.centerX()) {
                    insideX = anchorX + d;
                    outsideX = anchorX - d;
                } else {
                    insideX = anchorX - d;
                    outsideX = anchorX + d;
                }
                for (int dy = -10; dy <= 10; dy++) {
                    int sy = anchorY + dy;
                    if (sy >= 0 && sy < height) {
                        if (insideX >= 0 && insideX < width) {
                            insideAvg += yPlane[(sy * rowStride) + insideX] & 255;
                            insideCount++;
                        }
                        if (outsideX >= 0 && outsideX < width) {
                            outsideAvg += yPlane[(sy * rowStride) + outsideX] & 255;
                            outsideCount++;
                        }
                    }
                }
            }
        }
        if (insideCount == 0 || outsideCount == 0) {
            return 0.0f;
        }
        float contrast = Math.abs((insideAvg / insideCount) - (outsideAvg / outsideCount));
        return Math.min(1.0f, contrast / 45.0f);
    }

    public record AlignmentResult(float score, float[] anchorScores, boolean readyToCapture) {
    }
}
