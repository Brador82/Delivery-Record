package com.mobileinvoice.delivery.data.converters;

import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;

/* loaded from: classes4.dex */
public class EnumConverters {
    public static String fromDeliveryStatus(DeliveryStatus status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }

    public static DeliveryStatus toDeliveryStatus(String status) {
        if (status == null) {
            return null;
        }
        return DeliveryStatus.valueOf(status);
    }

    public static String fromPriority(Priority priority) {
        if (priority == null) {
            return null;
        }
        return priority.name();
    }

    public static Priority toPriority(String priority) {
        if (priority == null) {
            return null;
        }
        return Priority.valueOf(priority);
    }
}
