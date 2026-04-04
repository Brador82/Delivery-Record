package com.mobileinvoice.ocr.database;

import androidx.core.app.NotificationCompat;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes11.dex */
public final class InvoiceDatabase_Impl extends InvoiceDatabase {
    private volatile InvoiceDao _invoiceDao;

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration config) {
        SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(9) { // from class: com.mobileinvoice.ocr.database.InvoiceDatabase_Impl.1
            @Override // androidx.room.RoomOpenHelper.Delegate
            public void createAllTables(final SupportSQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `invoices` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `invoiceNumber` TEXT, `customerName` TEXT, `address` TEXT, `phone` TEXT, `items` TEXT, `podImagePath1` TEXT, `podImagePath2` TEXT, `podImagePath3` TEXT, `podImagePath4` TEXT, `podImagePath5` TEXT, `podImagePath6` TEXT, `signatureImagePath` TEXT, `notes` TEXT, `originalImagePath` TEXT, `rawOcrText` TEXT, `timestamp` INTEGER NOT NULL, `status` TEXT, `stopTimeMinutes` INTEGER NOT NULL, `deliverySequence` INTEGER NOT NULL, `preprocessedImagePath` TEXT, `serviceType` TEXT)");
                db.execSQL(RoomMasterTable.CREATE_QUERY);
                db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '36dd48744eca2de0d87c3b87ff2a7a9c')");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void dropAllTables(final SupportSQLiteDatabase db) {
                db.execSQL("DROP TABLE IF EXISTS `invoices`");
                List<? extends RoomDatabase.Callback> _callbacks = InvoiceDatabase_Impl.this.mCallbacks;
                if (_callbacks != null) {
                    for (RoomDatabase.Callback _callback : _callbacks) {
                        _callback.onDestructiveMigration(db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onCreate(final SupportSQLiteDatabase db) {
                List<? extends RoomDatabase.Callback> _callbacks = InvoiceDatabase_Impl.this.mCallbacks;
                if (_callbacks != null) {
                    for (RoomDatabase.Callback _callback : _callbacks) {
                        _callback.onCreate(db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onOpen(final SupportSQLiteDatabase db) {
                InvoiceDatabase_Impl.this.mDatabase = db;
                InvoiceDatabase_Impl.this.internalInitInvalidationTracker(db);
                List<? extends RoomDatabase.Callback> _callbacks = InvoiceDatabase_Impl.this.mCallbacks;
                if (_callbacks != null) {
                    for (RoomDatabase.Callback _callback : _callbacks) {
                        _callback.onOpen(db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onPreMigrate(final SupportSQLiteDatabase db) {
                DBUtil.dropFtsSyncTriggers(db);
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onPostMigrate(final SupportSQLiteDatabase db) {
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public RoomOpenHelper.ValidationResult onValidateSchema(final SupportSQLiteDatabase db) {
                HashMap<String, TableInfo.Column> _columnsInvoices = new HashMap<>(22);
                _columnsInvoices.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                _columnsInvoices.put("invoiceNumber", new TableInfo.Column("invoiceNumber", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("customerName", new TableInfo.Column("customerName", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("items", new TableInfo.Column("items", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("podImagePath1", new TableInfo.Column("podImagePath1", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("podImagePath2", new TableInfo.Column("podImagePath2", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("podImagePath3", new TableInfo.Column("podImagePath3", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("podImagePath4", new TableInfo.Column("podImagePath4", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("podImagePath5", new TableInfo.Column("podImagePath5", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("podImagePath6", new TableInfo.Column("podImagePath6", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("signatureImagePath", new TableInfo.Column("signatureImagePath", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("originalImagePath", new TableInfo.Column("originalImagePath", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("rawOcrText", new TableInfo.Column("rawOcrText", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, 1));
                _columnsInvoices.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "TEXT", false, 0, null, 1));
                _columnsInvoices.put("stopTimeMinutes", new TableInfo.Column("stopTimeMinutes", "INTEGER", true, 0, null, 1));
                _columnsInvoices.put("deliverySequence", new TableInfo.Column("deliverySequence", "INTEGER", true, 0, null, 1));
                _columnsInvoices.put("preprocessedImagePath", new TableInfo.Column("preprocessedImagePath", "TEXT", false, 0, null, 1));
                _columnsInvoices.put("serviceType", new TableInfo.Column("serviceType", "TEXT", false, 0, null, 1));
                HashSet<TableInfo.ForeignKey> _foreignKeysInvoices = new HashSet<>(0);
                HashSet<TableInfo.Index> _indicesInvoices = new HashSet<>(0);
                TableInfo _infoInvoices = new TableInfo("invoices", _columnsInvoices, _foreignKeysInvoices, _indicesInvoices);
                TableInfo _existingInvoices = TableInfo.read(db, "invoices");
                if (!_infoInvoices.equals(_existingInvoices)) {
                    return new RoomOpenHelper.ValidationResult(false, "invoices(com.mobileinvoice.ocr.database.Invoice).\n Expected:\n" + _infoInvoices + "\n Found:\n" + _existingInvoices);
                }
                return new RoomOpenHelper.ValidationResult(true, null);
            }
        }, "36dd48744eca2de0d87c3b87ff2a7a9c", "4e1fc7faa2ec73bc42ba89c60016783d");
        SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
        SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
        return _helper;
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        HashMap<String, String> _shadowTablesMap = new HashMap<>(0);
        HashMap<String, Set<String>> _viewTables = new HashMap<>(0);
        return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "invoices");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            _db.execSQL("DELETE FROM `invoices`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            _db.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!_db.inTransaction()) {
                _db.execSQL("VACUUM");
            }
        }
    }

    @Override // androidx.room.RoomDatabase
    protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
        HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<>();
        _typeConvertersMap.put(InvoiceDao.class, InvoiceDao_Impl.getRequiredConverters());
        return _typeConvertersMap;
    }

    @Override // androidx.room.RoomDatabase
    public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
        HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<>();
        return _autoMigrationSpecsSet;
    }

    @Override // androidx.room.RoomDatabase
    public List<Migration> getAutoMigrations(final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
        List<Migration> _autoMigrations = new ArrayList<>();
        return _autoMigrations;
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDatabase
    public InvoiceDao invoiceDao() {
        InvoiceDao invoiceDao;
        if (this._invoiceDao != null) {
            return this._invoiceDao;
        }
        synchronized (this) {
            if (this._invoiceDao == null) {
                this._invoiceDao = new InvoiceDao_Impl(this);
            }
            invoiceDao = this._invoiceDao;
        }
        return invoiceDao;
    }
}
