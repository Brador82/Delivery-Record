package com.mobileinvoice.ocr;

import android.graphics.Rect;
import java.util.List;

/* loaded from: classes7.dex */
public class InvoiceTemplate {
    public List<Field> fields;
    public Grid grid;
    public int reference_height;
    public int reference_width;

    public static class Coords {
        public int x1;
        public int x2;
        public int y1;
        public int y2;
    }

    public static class Grid {
        public int cols;
        public int rows;
    }

    public static class Field {
        public Coords coords;
        public String name;
        public String outputColumn;
        public String type;

        public Rect toPixelRect(int imageWidth, int imageHeight, Grid grid) {
            int left = (int) ((this.coords.x1 / grid.cols) * imageWidth);
            int top = (int) ((this.coords.y1 / grid.rows) * imageHeight);
            int right = (int) ((this.coords.x2 / grid.cols) * imageWidth);
            int bottom = (int) ((this.coords.y2 / grid.rows) * imageHeight);
            return new Rect(left, top, right, bottom);
        }
    }
}
