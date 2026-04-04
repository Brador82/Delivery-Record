package com.mobileinvoice.delivery.data.database;

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
import com.mobileinvoice.delivery.data.dao.DeliveryDao;
import com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.LogFactory;

/* loaded from: classes12.dex */
public final class DeliveryDatabase_Impl extends DeliveryDatabase {
    private volatile DeliveryDao _deliveryDao;

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration config) {
        SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) { // from class: com.mobileinvoice.delivery.data.database.DeliveryDatabase_Impl.1
            @Override // androidx.room.RoomOpenHelper.Delegate
            public void createAllTables(final SupportSQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `deliveries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tracking_number` TEXT, `customer_name` TEXT, `customer_phone` TEXT, `customer_email` TEXT, `street_address` TEXT, `city` TEXT, `state` TEXT, `zip_code` TEXT, `latitude` REAL, `longitude` REAL, `package_description` TEXT, `package_count` INTEGER NOT NULL, `package_weight` REAL, `special_instructions` TEXT, `status` TEXT, `priority` TEXT, `created_at` INTEGER, `scheduled_date` INTEGER, `time_window_start` TEXT, `time_window_end` TEXT, `completed_at` INTEGER, `route_order` INTEGER NOT NULL, `estimated_arrival` INTEGER, `actual_arrival` INTEGER, `signature_path` TEXT, `pod_photo_paths` TEXT, `delivery_notes` TEXT, `recipient_name` TEXT, `failure_reason` TEXT, `retry_count` INTEGER NOT NULL)");
                db.execSQL(RoomMasterTable.CREATE_QUERY);
                db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '44a026b8472467cfdcfb30de7ec3bbfe')");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void dropAllTables(final SupportSQLiteDatabase db) {
                db.execSQL("DROP TABLE IF EXISTS `deliveries`");
                List<? extends RoomDatabase.Callback> _callbacks = DeliveryDatabase_Impl.this.mCallbacks;
                if (_callbacks != null) {
                    for (RoomDatabase.Callback _callback : _callbacks) {
                        _callback.onDestructiveMigration(db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onCreate(final SupportSQLiteDatabase db) {
                List<? extends RoomDatabase.Callback> _callbacks = DeliveryDatabase_Impl.this.mCallbacks;
                if (_callbacks != null) {
                    for (RoomDatabase.Callback _callback : _callbacks) {
                        _callback.onCreate(db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onOpen(final SupportSQLiteDatabase db) {
                DeliveryDatabase_Impl.this.mDatabase = db;
                DeliveryDatabase_Impl.this.internalInitInvalidationTracker(db);
                List<? extends RoomDatabase.Callback> _callbacks = DeliveryDatabase_Impl.this.mCallbacks;
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
                HashMap<String, TableInfo.Column> _columnsDeliveries = new HashMap<>(31);
                _columnsDeliveries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                _columnsDeliveries.put("tracking_number", new TableInfo.Column("tracking_number", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("customer_name", new TableInfo.Column("customer_name", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("customer_phone", new TableInfo.Column("customer_phone", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("customer_email", new TableInfo.Column("customer_email", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("street_address", new TableInfo.Column("street_address", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("city", new TableInfo.Column("city", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("state", new TableInfo.Column("state", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("zip_code", new TableInfo.Column("zip_code", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, 1));
                _columnsDeliveries.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, 1));
                _columnsDeliveries.put("package_description", new TableInfo.Column("package_description", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("package_count", new TableInfo.Column("package_count", "INTEGER", true, 0, null, 1));
                _columnsDeliveries.put("package_weight", new TableInfo.Column("package_weight", "REAL", false, 0, null, 1));
                _columnsDeliveries.put("special_instructions", new TableInfo.Column("special_instructions", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "TEXT", false, 0, null, 1));
                _columnsDeliveries.put(LogFactory.PRIORITY_KEY, new TableInfo.Column(LogFactory.PRIORITY_KEY, "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("created_at", new TableInfo.Column("created_at", "INTEGER", false, 0, null, 1));
                _columnsDeliveries.put("scheduled_date", new TableInfo.Column("scheduled_date", "INTEGER", false, 0, null, 1));
                _columnsDeliveries.put("time_window_start", new TableInfo.Column("time_window_start", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("time_window_end", new TableInfo.Column("time_window_end", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("completed_at", new TableInfo.Column("completed_at", "INTEGER", false, 0, null, 1));
                _columnsDeliveries.put("route_order", new TableInfo.Column("route_order", "INTEGER", true, 0, null, 1));
                _columnsDeliveries.put("estimated_arrival", new TableInfo.Column("estimated_arrival", "INTEGER", false, 0, null, 1));
                _columnsDeliveries.put("actual_arrival", new TableInfo.Column("actual_arrival", "INTEGER", false, 0, null, 1));
                _columnsDeliveries.put("signature_path", new TableInfo.Column("signature_path", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("pod_photo_paths", new TableInfo.Column("pod_photo_paths", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("delivery_notes", new TableInfo.Column("delivery_notes", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("recipient_name", new TableInfo.Column("recipient_name", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("failure_reason", new TableInfo.Column("failure_reason", "TEXT", false, 0, null, 1));
                _columnsDeliveries.put("retry_count", new TableInfo.Column("retry_count", "INTEGER", true, 0, null, 1));
                HashSet<TableInfo.ForeignKey> _foreignKeysDeliveries = new HashSet<>(0);
                HashSet<TableInfo.Index> _indicesDeliveries = new HashSet<>(0);
                TableInfo _infoDeliveries = new TableInfo("deliveries", _columnsDeliveries, _foreignKeysDeliveries, _indicesDeliveries);
                TableInfo _existingDeliveries = TableInfo.read(db, "deliveries");
                if (!_infoDeliveries.equals(_existingDeliveries)) {
                    return new RoomOpenHelper.ValidationResult(false, "deliveries(com.mobileinvoice.delivery.data.entities.Delivery).\n Expected:\n" + _infoDeliveries + "\n Found:\n" + _existingDeliveries);
                }
                return new RoomOpenHelper.ValidationResult(true, null);
            }
        }, "44a026b8472467cfdcfb30de7ec3bbfe", "1d647932d04a96d7e411293ee25b7aa3");
        SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
        SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
        return _helper;
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        HashMap<String, String> _shadowTablesMap = new HashMap<>(0);
        HashMap<String, Set<String>> _viewTables = new HashMap<>(0);
        return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "deliveries");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            _db.execSQL("DELETE FROM `deliveries`");
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
        _typeConvertersMap.put(DeliveryDao.class, DeliveryDao_Impl.getRequiredConverters());
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

    @Override // com.mobileinvoice.delivery.data.database.DeliveryDatabase
    public DeliveryDao deliveryDao() {
        DeliveryDao deliveryDao;
        if (this._deliveryDao != null) {
            return this._deliveryDao;
        }
        synchronized (this) {
            if (this._deliveryDao == null) {
                this._deliveryDao = new DeliveryDao_Impl(this);
            }
            deliveryDao = this._deliveryDao;
        }
        return deliveryDao;
    }
}
