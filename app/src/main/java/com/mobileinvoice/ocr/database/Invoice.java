package com.mobileinvoice.ocr.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoices")
public class Invoice {
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "customerName")
    private String customerName;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String invoiceNumber;
    private String items;
    private String notes;
    private String originalImagePath;
    private String phone;
    @ColumnInfo(name = "altPhone")
    private String altPhone;
    private String podImagePath1;
    private String podImagePath2;
    private String podImagePath3;
    private String podImagePath4;
    private String podImagePath5;
    private String podImagePath6;
    private String preprocessedImagePath;
    private String rawOcrText;
    private String signatureImagePath;
    private int stopTimeMinutes;
    private int deliverySequence = 0;
    private String serviceType = "Delivery";
    private long timestamp = System.currentTimeMillis();
    private String status = "PENDING";

    public Invoice() {
        this.stopTimeMinutes = 30;
        this.stopTimeMinutes = 30;
    }

    public int getId() {
        return this.id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAltPhone() {
        return this.altPhone != null ? this.altPhone : "";
    }

    public String getItems() {
        return this.items;
    }

    public String getPodImagePath1() {
        return this.podImagePath1;
    }

    public String getPodImagePath2() {
        return this.podImagePath2;
    }

    public String getPodImagePath3() {
        return this.podImagePath3;
    }

    public String getPodImagePath4() {
        return this.podImagePath4;
    }

    public String getPodImagePath5() {
        return this.podImagePath5;
    }

    public String getPodImagePath6() {
        return this.podImagePath6;
    }

    public String getSignatureImagePath() {
        return this.signatureImagePath;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getOriginalImagePath() {
        return this.originalImagePath;
    }

    public String getRawOcrText() {
        return this.rawOcrText;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getStatus() {
        return this.status;
    }

    public int getStopTimeMinutes() {
        return this.stopTimeMinutes;
    }

    public int getDeliverySequence() {
        return this.deliverySequence;
    }

    public String getPreprocessedImagePath() {
        return this.preprocessedImagePath;
    }

    public String getServiceType() {
        return this.serviceType != null ? this.serviceType : "Delivery";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setPodImagePath1(String podImagePath1) {
        this.podImagePath1 = podImagePath1;
    }

    public void setPodImagePath2(String podImagePath2) {
        this.podImagePath2 = podImagePath2;
    }

    public void setPodImagePath3(String podImagePath3) {
        this.podImagePath3 = podImagePath3;
    }

    public void setPodImagePath4(String podImagePath4) {
        this.podImagePath4 = podImagePath4;
    }

    public void setPodImagePath5(String podImagePath5) {
        this.podImagePath5 = podImagePath5;
    }

    public void setPodImagePath6(String podImagePath6) {
        this.podImagePath6 = podImagePath6;
    }

    public void setSignatureImagePath(String signatureImagePath) {
        this.signatureImagePath = signatureImagePath;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setOriginalImagePath(String originalImagePath) {
        this.originalImagePath = originalImagePath;
    }

    public void setRawOcrText(String rawOcrText) {
        this.rawOcrText = rawOcrText;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStopTimeMinutes(int stopTimeMinutes) {
        this.stopTimeMinutes = stopTimeMinutes;
    }

    public void setDeliverySequence(int deliverySequence) {
        this.deliverySequence = deliverySequence;
    }

    public void setPreprocessedImagePath(String preprocessedImagePath) {
        this.preprocessedImagePath = preprocessedImagePath;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isCompleted() {
        return "DELIVERED".equals(this.status) || "CANCELLED".equals(this.status);
    }

    public boolean isActive() {
        return "PENDING".equals(this.status) || "IN_TRANSIT".equals(this.status);
    }

    public void setCompleted(boolean completed) {
        this.status = completed ? "DELIVERED" : "PENDING";
    }
}
