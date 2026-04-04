package com.mobileinvoice.ocr;

/* loaded from: classes7.dex */
public class DeliveryItem {
    public String item;
    public String model;
    public String serial;
    /** Brand / manufacturer: LG, GE, Samsung, Whirlpool … */
    public String make;
    /**
     * Comma-delimited service flags: "DELIVERY", "INSTALL", "HAUL AWAY", "SERVICE"
     */
    public String services;

    public DeliveryItem() {
        this.item = "";
        this.model = "";
        this.serial = "";
        this.make = "";
        this.services = "";
    }

    public DeliveryItem(String item) {
        this.item = item != null ? item : "";
        this.model = "";
        this.serial = "";
        this.make = "";
        this.services = "";
    }

    public DeliveryItem(String item, String model, String serial) {
        this.item = item != null ? item : "";
        this.model = model != null ? model : "";
        this.serial = serial != null ? serial : "";
        this.make = "";
        this.services = "";
    }

    public String getDisplayName() {
        String prefix = (this.make != null && !this.make.isEmpty()) ? this.make + " " : "";
        if (this.model != null && !this.model.isEmpty()) {
            return prefix + this.item + " (" + this.model + ")";
        }
        return prefix + this.item;
    }

    public String getFullDetail() {
        String prefix = (this.make != null && !this.make.isEmpty()) ? this.make + " " : "";
        StringBuilder sb = new StringBuilder(prefix + this.item);
        boolean hasModel = (this.model != null && !this.model.isEmpty());
        boolean hasSerial = (this.serial != null && !this.serial.isEmpty());
        if (hasModel || hasSerial) {
            sb.append(" – ");
            if (hasModel) {
                sb.append("Model: ").append(this.model);
            }
            if (hasModel && hasSerial) {
                sb.append(", ");
            }
            if (hasSerial) {
                sb.append("S/N: ").append(this.serial);
            }
        }
        if (this.services != null && !this.services.isEmpty()) {
            sb.append(" [").append(this.services).append("]");
        }
        return sb.toString();
    }
}
