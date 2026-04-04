package com.mobileinvoice.ocr.database;

/* loaded from: classes11.dex */
public class Converters {
    public static String fromLong(Long value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    public static Long toLong(String value) {
        if (value == null) {
            return null;
        }
        return Long.valueOf(Long.parseLong(value));
    }
}
