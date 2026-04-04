package com.mobileinvoice.delivery.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

@Entity(tableName = "deliveries")
public class Delivery {
    @ColumnInfo(name = "actualArrival")
    private Date actualArrival;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "completedAt")
    private Date completedAt;
    @ColumnInfo(name = "customerEmail")
    private String customerEmail;
    @ColumnInfo(name = "customerName")
    private String customerName;
    @ColumnInfo(name = "customerPhone")
    private String customerPhone;
    @ColumnInfo(name = "deliveryNotes")
    private String deliveryNotes;
    @ColumnInfo(name = "estimatedArrival")
    private Date estimatedArrival;
    @ColumnInfo(name = "failureReason")
    private String failureReason;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Double latitude;
    private Double longitude;
    private String packageDescription;
    private Double packageWeight;
    private String podPhotoPaths;
    private String recipientName;
    private int routeOrder;
    private Date scheduledDate;
    private String signaturePath;
    private String specialInstructions;
    private String state;
    private String streetAddress;
    private String timeWindowEnd;
    private String timeWindowStart;
    private String trackingNumber;
    private String zipCode;
    private Date createdAt = new Date();
    private DeliveryStatus status = DeliveryStatus.PENDING;
    private Priority priority = Priority.NORMAL;
    private int packageCount = 1;
    private int retryCount = 0;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return this.customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPackageDescription() {
        return this.packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public int getPackageCount() {
        return this.packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public Double getPackageWeight() {
        return this.packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public DeliveryStatus getStatus() {
        return this.status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getScheduledDate() {
        return this.scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getTimeWindowStart() {
        return this.timeWindowStart;
    }

    public void setTimeWindowStart(String timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    public String getTimeWindowEnd() {
        return this.timeWindowEnd;
    }

    public void setTimeWindowEnd(String timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public Date getCompletedAt() {
        return this.completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getRouteOrder() {
        return this.routeOrder;
    }

    public void setRouteOrder(int routeOrder) {
        this.routeOrder = routeOrder;
    }

    public Date getEstimatedArrival() {
        return this.estimatedArrival;
    }

    public void setEstimatedArrival(Date estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public Date getActualArrival() {
        return this.actualArrival;
    }

    public void setActualArrival(Date actualArrival) {
        this.actualArrival = actualArrival;
    }

    public String getSignaturePath() {
        return this.signaturePath;
    }

    public void setSignaturePath(String signaturePath) {
        this.signaturePath = signaturePath;
    }

    public String getPodPhotoPaths() {
        return this.podPhotoPaths;
    }

    public void setPodPhotoPaths(String podPhotoPaths) {
        this.podPhotoPaths = podPhotoPaths;
    }

    public String getDeliveryNotes() {
        return this.deliveryNotes;
    }

    public void setDeliveryNotes(String deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public String getRecipientName() {
        return this.recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (this.streetAddress != null) {
            address.append(this.streetAddress);
        }
        if (this.city != null) {
            address.append(", ").append(this.city);
        }
        if (this.state != null) {
            address.append(", ").append(this.state);
        }
        if (this.zipCode != null) {
            address.append(StringUtils.SPACE).append(this.zipCode);
        }
        return address.toString();
    }

    public boolean hasCoordinates() {
        return this.latitude != null && this.longitude != null;
    }

    public boolean isOverdue() {
        if (this.scheduledDate == null || this.status.isCompleted()) {
            return false;
        }
        return new Date().after(this.scheduledDate);
    }

    public String getTimeWindow() {
        if (this.timeWindowStart != null && this.timeWindowEnd != null) {
            return this.timeWindowStart + " - " + this.timeWindowEnd;
        }
        return "Anytime";
    }
}
