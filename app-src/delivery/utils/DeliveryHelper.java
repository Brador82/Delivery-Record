package com.mobileinvoice.delivery.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import com.mobileinvoice.delivery.data.entities.Delivery;
import org.apache.commons.lang3.time.DateUtils;

/* loaded from: classes8.dex */
public class DeliveryHelper {
    public static void callCustomer(Context context, Delivery delivery) {
        if (delivery.getCustomerPhone() == null || delivery.getCustomerPhone().isEmpty()) {
            return;
        }
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + delivery.getCustomerPhone()));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void navigateToDelivery(Context context, Delivery delivery) {
        if (!delivery.hasCoordinates()) {
            if (delivery.getFullAddress() != null && !delivery.getFullAddress().isEmpty()) {
                navigateToAddress(context, delivery.getFullAddress());
                return;
            }
            return;
        }
        String uri = String.format("google.navigation:q=%f,%f", delivery.getLatitude(), delivery.getLongitude());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return;
        }
        String browserUri = String.format("https://www.google.com/maps/dir/?api=1&destination=%f,%f", delivery.getLatitude(), delivery.getLongitude());
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(browserUri));
        context.startActivity(browserIntent);
    }

    public static void navigateToAddress(Context context, String address) {
        String uri = "google.navigation:q=" + Uri.encode(address);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return;
        }
        String browserUri = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(address);
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(browserUri));
        context.startActivity(browserIntent);
    }

    public static void sendSMS(Context context, Delivery delivery, String message) {
        if (delivery.getCustomerPhone() == null || delivery.getCustomerPhone().isEmpty()) {
            return;
        }
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse("smsto:" + delivery.getCustomerPhone()));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.length() == 10) {
            return String.format("(%s) %s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6, 10));
        }
        return phone;
    }

    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public static String getETAString(Delivery delivery) {
        if (delivery.getEstimatedArrival() == null) {
            return "No ETA";
        }
        long now = System.currentTimeMillis();
        long eta = delivery.getEstimatedArrival().getTime();
        long diff = eta - now;
        if (diff < 0) {
            return "Overdue";
        }
        long minutes = diff / DateUtils.MILLIS_PER_MINUTE;
        if (minutes < 60) {
            return minutes + " min";
        }
        long hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m";
    }
}
