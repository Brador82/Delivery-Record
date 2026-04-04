package com.mobileinvoice.ocr.database;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String fromLong(Long value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    @TypeConverter
    public static Long toLong(String value) {
        if (value == null) {
            return null;
        }
        return Long.valueOf(Long.parseLong(value));
    }
}
