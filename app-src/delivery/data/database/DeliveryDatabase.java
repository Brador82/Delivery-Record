package com.mobileinvoice.delivery.data.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.mobileinvoice.delivery.data.dao.DeliveryDao;

/* loaded from: classes12.dex */
public abstract class DeliveryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "delivery_database";
    private static volatile DeliveryDatabase INSTANCE;

    public abstract DeliveryDao deliveryDao();

    public static DeliveryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DeliveryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = (DeliveryDatabase) Room.databaseBuilder(context.getApplicationContext(), DeliveryDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }
}
