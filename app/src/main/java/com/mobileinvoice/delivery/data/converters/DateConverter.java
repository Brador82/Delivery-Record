package com.mobileinvoice.delivery.data.converters;

import androidx.room.TypeConverter;
import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        if (value == null) {
            return null;
        }
        return new Date(value.longValue());
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime());
    }
}
