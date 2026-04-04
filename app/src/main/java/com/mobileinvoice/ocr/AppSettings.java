package com.mobileinvoice.ocr;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes7.dex */
public class AppSettings {
    public static final String AFTER_DELIVERY_ASK = "ask";
    public static final String AFTER_DELIVERY_AUTO_ARCHIVE = "auto_archive";
    public static final String AFTER_DELIVERY_NAVIGATE = "navigate_next";
    public static final String KEY_AFTER_DELIVERY_ACTION = "after_delivery_action";
    public static final String KEY_APP_THEME = "app_theme";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_DEFAULT_SERVICE_TYPE = "default_service_type";
    public static final String KEY_DRIVE_ACCOUNT_EMAIL = "drive_account_email";
    public static final String KEY_DRIVE_SYNC_ENABLED = "drive_sync_enabled";
    public static final String KEY_ETA_WINDOW_MINUTES = "eta_window_minutes";
    public static final String KEY_EXPORT_FOLDER_NAME = "export_folder_name";
    public static final String KEY_WAREHOUSE_ADDRESS = "warehouse_address";
    public static final String KEY_WAREHOUSE_LAT = "warehouse_lat";
    public static final String KEY_WAREHOUSE_LNG = "warehouse_lng";
    public static final String KEY_FOLLOWUP_ENABLED = "followup_enabled";
    public static final String KEY_BROADCAST_MESSAGE = "broadcast_message";
    public static final String KEY_CUSTOM_MESSAGE_1 = "custom_message_1";
    public static final String KEY_CUSTOM_MESSAGE_2 = "custom_message_2";
    public static final String KEY_GOOGLE_REVIEW_URL = "google_review_url";
    public static final String KEY_SHORT_REVIEW_URL = "short_review_url";
    public static final String KEY_DELIVERY_TEAM_NAME = "delivery_team_name";
    public static final String KEY_SUGGESTED_REVIEW = "suggested_review";
    private static final String PREFS_NAME = "app_settings";
    public static final String THEME_BLACK_GOLD = "black_gold";
    public static final String THEME_BLENDED = "blended";
    public static final String THEME_DARK_MARBLE = "dark_marble";
    public static final String THEME_IMPERIAL_MARBLE = "imperial_marble";
    public static final String THEME_LIGHT_MARBLE = "light_marble";
    public static final String THEME_MARBLE = "marble";
    private static AppSettings instance;
    private final SharedPreferences prefs;

    private AppSettings(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
    }

    public static synchronized AppSettings getInstance(Context context) {
        AppSettings appSettings;
        synchronized (AppSettings.class) {
            if (instance == null) {
                instance = new AppSettings(context);
            }
            appSettings = instance;
        }
        return appSettings;
    }

    public String getCompanyName() {
        return this.prefs.getString(KEY_COMPANY_NAME, "Appliances 4 Less");
    }

    public void setCompanyName(String value) {
        this.prefs.edit().putString(KEY_COMPANY_NAME, value).apply();
    }

    public String getWarehouseAddress() {
        return this.prefs.getString(KEY_WAREHOUSE_ADDRESS, "1517 West Battlefield Rd Springfield MO 65807");
    }

    public void setWarehouseAddress(String value) {
        this.prefs.edit().putString(KEY_WAREHOUSE_ADDRESS, value).apply();
    }

    public double getWarehouseLat() {
        long bits = this.prefs.getLong(KEY_WAREHOUSE_LAT, Double.doubleToLongBits(37.1819d));
        return Double.longBitsToDouble(bits);
    }

    public void setWarehouseLat(double value) {
        this.prefs.edit().putLong(KEY_WAREHOUSE_LAT, Double.doubleToLongBits(value)).apply();
    }

    public double getWarehouseLng() {
        long bits = this.prefs.getLong(KEY_WAREHOUSE_LNG, Double.doubleToLongBits(-93.3147d));
        return Double.longBitsToDouble(bits);
    }

    public void setWarehouseLng(double value) {
        this.prefs.edit().putLong(KEY_WAREHOUSE_LNG, Double.doubleToLongBits(value)).apply();
    }

    public int getEtaWindowMinutes() {
        return this.prefs.getInt(KEY_ETA_WINDOW_MINUTES, 45);
    }

    public void setEtaWindowMinutes(int value) {
        this.prefs.edit().putInt(KEY_ETA_WINDOW_MINUTES, value).apply();
    }

    public String getDefaultServiceType() {
        return this.prefs.getString(KEY_DEFAULT_SERVICE_TYPE, "Delivery");
    }

    public void setDefaultServiceType(String value) {
        this.prefs.edit().putString(KEY_DEFAULT_SERVICE_TYPE, value).apply();
    }

    public String getAfterDeliveryAction() {
        return this.prefs.getString(KEY_AFTER_DELIVERY_ACTION, AFTER_DELIVERY_ASK);
    }

    public void setAfterDeliveryAction(String value) {
        this.prefs.edit().putString(KEY_AFTER_DELIVERY_ACTION, value).apply();
    }

    public String getAppTheme() {
        return this.prefs.getString(KEY_APP_THEME, THEME_DARK_MARBLE);
    }

    public void setAppTheme(String value) {
        this.prefs.edit().putString(KEY_APP_THEME, value).apply();
    }

    public String getExportFolderName() {
        return this.prefs.getString(KEY_EXPORT_FOLDER_NAME, "MobileInvoiceOCR");
    }

    public void setExportFolderName(String value) {
        this.prefs.edit().putString(KEY_EXPORT_FOLDER_NAME, value).apply();
    }

    public boolean isDriveSyncEnabled() {
        return this.prefs.getBoolean(KEY_DRIVE_SYNC_ENABLED, false);
    }

    public void setDriveSyncEnabled(boolean value) {
        this.prefs.edit().putBoolean(KEY_DRIVE_SYNC_ENABLED, value).apply();
    }

    public String getDriveAccountEmail() {
        return this.prefs.getString(KEY_DRIVE_ACCOUNT_EMAIL, "");
    }

    public void setDriveAccountEmail(String value) {
        this.prefs.edit().putString(KEY_DRIVE_ACCOUNT_EMAIL, value).apply();
    }

    public boolean isFollowUpEnabled() {
        return this.prefs.getBoolean(KEY_FOLLOWUP_ENABLED, false);
    }

    public void setFollowUpEnabled(boolean value) {
        this.prefs.edit().putBoolean(KEY_FOLLOWUP_ENABLED, value).apply();
    }

    private static final String DEFAULT_BROADCAST =
            "Hi {name}, thank you for choosing {company}! We hope you're enjoying your new {items}. "
            + "If you have a moment, we'd love a 5-star review!\n\n"
            + "You can copy & paste this:\n\"{review_text}\"\n\n"
            + "Leave your review here: {review_url}";

    public String getBroadcastMessage() {
        return this.prefs.getString(KEY_BROADCAST_MESSAGE, DEFAULT_BROADCAST);
    }

    public void setBroadcastMessage(String value) {
        this.prefs.edit().putString(KEY_BROADCAST_MESSAGE, value).apply();
    }

    public String getCustomMessage1() {
        return this.prefs.getString(KEY_CUSTOM_MESSAGE_1,
                "Your delivery from {company} is complete. Thank you for your business!");
    }

    public void setCustomMessage1(String value) {
        this.prefs.edit().putString(KEY_CUSTOM_MESSAGE_1, value).apply();
    }

    public String getCustomMessage2() {
        return this.prefs.getString(KEY_CUSTOM_MESSAGE_2,
                "Hi {name}, just checking in! How is everything working? Leave us a review: {review_url}");
    }

    public void setCustomMessage2(String value) {
        this.prefs.edit().putString(KEY_CUSTOM_MESSAGE_2, value).apply();
    }

    public String getGoogleReviewUrl() {
        return this.prefs.getString(KEY_GOOGLE_REVIEW_URL, "");
    }

    public void setGoogleReviewUrl(String value) {
        this.prefs.edit().putString(KEY_GOOGLE_REVIEW_URL, value).apply();
    }

    public String getShortReviewUrl() {
        return this.prefs.getString(KEY_SHORT_REVIEW_URL, "");
    }

    public void setShortReviewUrl(String value) {
        this.prefs.edit().putString(KEY_SHORT_REVIEW_URL, value).apply();
    }

    public String getDeliveryTeamName() {
        return this.prefs.getString(KEY_DELIVERY_TEAM_NAME, "");
    }

    public void setDeliveryTeamName(String value) {
        this.prefs.edit().putString(KEY_DELIVERY_TEAM_NAME, value).apply();
    }

    private static final String DEFAULT_SUGGESTED_REVIEW =
            "The {team} did an outstanding job with our {items}! "
            + "Professional, on time, and very careful. Highly recommend {company}!";

    public String getSuggestedReview() {
        return this.prefs.getString(KEY_SUGGESTED_REVIEW, DEFAULT_SUGGESTED_REVIEW);
    }

    public void setSuggestedReview(String value) {
        this.prefs.edit().putString(KEY_SUGGESTED_REVIEW, value).apply();
    }

    /** Expand placeholders in a message template with invoice data. */
    public String expandMessage(String template, String customerName, String items) {
        String msg = template;
        msg = msg.replace("{name}", customerName != null ? customerName : "");
        msg = msg.replace("{company}", getCompanyName());
        msg = msg.replace("{items}", items != null ? items : "your purchase");
        String teamName = getDeliveryTeamName();
        if (teamName.isEmpty()) teamName = getCompanyName() + " delivery team";
        msg = msg.replace("{team}", teamName);
        String reviewUrl = getShortReviewUrl();
        if (reviewUrl.isEmpty()) reviewUrl = getGoogleReviewUrl();
        msg = msg.replace("{review_url}", reviewUrl);
        // Expand suggested review text (itself gets placeholder expansion)
        if (msg.contains("{review_text}")) {
            String reviewBody = getSuggestedReview();
            reviewBody = reviewBody.replace("{team}", teamName);
            reviewBody = reviewBody.replace("{company}", getCompanyName());
            reviewBody = reviewBody.replace("{items}", items != null ? items : "your purchase");
            reviewBody = reviewBody.replace("{name}", customerName != null ? customerName : "");
            msg = msg.replace("{review_text}", reviewBody);
        }
        return msg;
    }
}
