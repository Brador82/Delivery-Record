package com.mobileinvoice.delivery.data.converters;

import androidx.room.TypeConverter;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;

public class EnumConverters {
    @TypeConverter
    public static String fromDeliveryStatus(DeliveryStatus status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }

    @TypeConverter
    public static DeliveryStatus toDeliveryStatus(String status) {
        if (status == null) {
            return null;
        }
        return DeliveryStatus.valueOf(status);
    }

    @TypeConverter
    public static String fromPriority(Priority priority) {
        if (priority == null) {
            return null;
        }
        return priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String priority) {
        if (priority == null) {
            return null;
        }
        return Priority.valueOf(priority);
    }
}
