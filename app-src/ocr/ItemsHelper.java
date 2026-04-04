package com.mobileinvoice.ocr;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.VectorFormat;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes7.dex */
public class ItemsHelper {
    public static String toJson(List<DeliveryItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        try {
            JSONArray array = new JSONArray();
            for (DeliveryItem di : items) {
                JSONObject obj = new JSONObject();
                obj.put("item", di.item != null ? di.item : "");
                obj.put("model", di.model != null ? di.model : "");
                obj.put("serial", di.serial != null ? di.serial : "");
                array.put(obj);
            }
            return array.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<DeliveryItem> fromJson(String json) {
        List<DeliveryItem> items = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            return items;
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("[")) {
            try {
                JSONArray array = new JSONArray(trimmed);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String item = obj.optString("item", "");
                    String model = obj.optString("model", "");
                    String serial = obj.optString("serial", "");
                    if (!item.isEmpty()) {
                        items.add(new DeliveryItem(item, model, serial));
                    }
                }
                return items;
            } catch (Exception e) {
            }
        }
        String[] parts = trimmed.split(",");
        for (String part : parts) {
            String name = part.trim();
            if (!name.isEmpty()) {
                items.add(new DeliveryItem(name));
            }
        }
        return items;
    }

    public static String toDisplayString(List<DeliveryItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(VectorFormat.DEFAULT_SEPARATOR);
            }
            sb.append(items.get(i).getFullDetail());
        }
        return sb.toString();
    }

    public static String toNamesString(List<DeliveryItem> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(items.get(i).item);
        }
        return sb.toString();
    }

    public static List<String> toNameList(List<DeliveryItem> items) {
        List<String> names = new ArrayList<>();
        if (items == null) {
            return names;
        }
        for (DeliveryItem di : items) {
            if (di.item != null && !di.item.isEmpty()) {
                names.add(di.item);
            }
        }
        return names;
    }
}
