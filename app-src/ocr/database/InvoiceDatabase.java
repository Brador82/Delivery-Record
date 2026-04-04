package com.mobileinvoice.ocr.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/* loaded from: classes11.dex */
public abstract class InvoiceDatabase extends RoomDatabase {
    private static final Migration MIGRATION_2_3;
    private static final Migration MIGRATION_3_4;
    private static final Migration MIGRATION_4_5;
    private static final Migration MIGRATION_5_6;
    private static final Migration MIGRATION_6_7;
    private static final Migration MIGRATION_7_8;
    private static final Migration MIGRATION_8_9;
    private static InvoiceDatabase instance;

    public abstract InvoiceDao invoiceDao();

    static {
        int i = 3;
        MIGRATION_2_3 = new Migration(2, i) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.1
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE invoices ADD COLUMN status TEXT DEFAULT 'PENDING'");
            }
        };
        int i2 = 4;
        MIGRATION_3_4 = new Migration(i, i2) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.2
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE invoices_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, invoiceNumber TEXT, customerName TEXT, address TEXT, phone TEXT, items TEXT, podImagePath1 TEXT, podImagePath2 TEXT, podImagePath3 TEXT, signatureImagePath TEXT, notes TEXT, originalImagePath TEXT, rawOcrText TEXT, timestamp INTEGER NOT NULL, status TEXT)");
                database.execSQL("INSERT INTO invoices_new (id, invoiceNumber, customerName, address, phone, items, podImagePath1, podImagePath2, podImagePath3, signatureImagePath, notes, originalImagePath, rawOcrText, timestamp, status) SELECT id, invoiceNumber, customerName, address, phone, items, podImagePath1, podImagePath2, podImagePath3, signatureImagePath, notes, originalImagePath, rawOcrText, timestamp, status FROM invoices");
                database.execSQL("DROP TABLE invoices");
                database.execSQL("ALTER TABLE invoices_new RENAME TO invoices");
            }
        };
        int i3 = 5;
        MIGRATION_4_5 = new Migration(i2, i3) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.3
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE invoices ADD COLUMN stopTimeMinutes INTEGER NOT NULL DEFAULT 30");
            }
        };
        int i4 = 6;
        MIGRATION_5_6 = new Migration(i3, i4) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.4
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE invoices ADD COLUMN podImagePath4 TEXT");
                database.execSQL("ALTER TABLE invoices ADD COLUMN podImagePath5 TEXT");
                database.execSQL("ALTER TABLE invoices ADD COLUMN podImagePath6 TEXT");
            }
        };
        int i5 = 7;
        MIGRATION_6_7 = new Migration(i4, i5) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.5
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE invoices ADD COLUMN deliverySequence INTEGER NOT NULL DEFAULT 0");
            }
        };
        int i6 = 8;
        MIGRATION_7_8 = new Migration(i5, i6) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.6
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE invoices ADD COLUMN preprocessedImagePath TEXT");
            }
        };
        MIGRATION_8_9 = new Migration(i6, 9) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase.7
            @Override // androidx.room.migration.Migration
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE invoices ADD COLUMN serviceType TEXT DEFAULT 'Delivery'");
            }
        };
    }

    public static synchronized InvoiceDatabase getInstance(Context context) {
        InvoiceDatabase invoiceDatabase;
        synchronized (InvoiceDatabase.class) {
            if (instance == null) {
                instance = (InvoiceDatabase) Room.databaseBuilder(context.getApplicationContext(), InvoiceDatabase.class, "invoice_database").addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9).fallbackToDestructiveMigration().build();
            }
            invoiceDatabase = instance;
        }
        return invoiceDatabase;
    }
}
