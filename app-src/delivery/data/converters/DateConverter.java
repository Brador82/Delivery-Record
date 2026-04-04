package com.mobileinvoice.delivery.data.converters;

import java.util.Date;

/* loaded from: classes4.dex */
public class DateConverter {
    public static Date fromTimestamp(Long value) {
        if (value == null) {
            return null;
        }
        return new Date(value.longValue());
    }

    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime());
    }
}
