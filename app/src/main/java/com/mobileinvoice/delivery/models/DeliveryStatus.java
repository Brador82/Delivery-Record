package com.mobileinvoice.delivery.models;

/* loaded from: classes9.dex */
public enum DeliveryStatus {
    PENDING("Pending", "#FFA726"),
    IN_TRANSIT("In Transit", "#42A5F5"),
    DELIVERED("Delivered", "#66BB6A"),
    FAILED("Failed", "#EF5350"),
    RESCHEDULED("Rescheduled", "#AB47BC"),
    CANCELLED("Cancelled", "#78909C");

    private final String colorHex;
    private final String displayName;

    DeliveryStatus(String displayName, String colorHex) {
        this.displayName = displayName;
        this.colorHex = colorHex;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getColorHex() {
        return this.colorHex;
    }

    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED;
    }

    public boolean isActive() {
        return this == PENDING || this == IN_TRANSIT;
    }
}
