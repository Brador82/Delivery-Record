package com.mobileinvoice.ocr;

/* loaded from: classes7.dex */
public class DeliveryItem {
    public String item;
    public String model;
    public String serial;

    public DeliveryItem() {
        this.item = "";
        this.model = "";
        this.serial = "";
    }

    public DeliveryItem(String item) {
        this.item = item != null ? item : "";
        this.model = "";
        this.serial = "";
    }

    public DeliveryItem(String item, String model, String serial) {
        this.item = item != null ? item : "";
        this.model = model != null ? model : "";
        this.serial = serial != null ? serial : "";
    }

    public String getDisplayName() {
        if (this.model != null && !this.model.isEmpty()) {
            return this.item + " (" + this.model + ")";
        }
        return this.item;
    }

    public String getFullDetail() {
        StringBuilder sb = new StringBuilder(this.item);
        boolean hasModel = (this.model == null || this.model.isEmpty()) ? false : true;
        boolean hasSerial = (this.serial == null || this.serial.isEmpty()) ? false : true;
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
        return sb.toString();
    }
}
