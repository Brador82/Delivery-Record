package com.mobileinvoice.delivery.models;

/* loaded from: classes9.dex */
public enum Priority {
    LOW(1, "Low", "#4CAF50"),
    NORMAL(2, "Normal", "#2196F3"),
    HIGH(3, "High", "#FF9800"),
    URGENT(4, "Urgent", "#F44336");

    private final String colorHex;
    private final String displayName;
    private final int value;

    Priority(int value, String displayName, String colorHex) {
        this.value = value;
        this.displayName = displayName;
        this.colorHex = colorHex;
    }

    public int getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getColorHex() {
        return this.colorHex;
    }
}
