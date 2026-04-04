package com.mobileinvoice.delivery.data.dao;

import android.database.Cursor;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mobileinvoice.delivery.data.converters.DateConverter;
import com.mobileinvoice.delivery.data.converters.EnumConverters;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.logging.LogFactory;

/* loaded from: classes4.dex */
public final class DeliveryDao_Impl implements DeliveryDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<Delivery> __deletionAdapterOfDelivery;
    private final EntityInsertionAdapter<Delivery> __insertionAdapterOfDelivery;
    private final SharedSQLiteStatement __preparedStmtOfCompleteDelivery;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAll;
    private final SharedSQLiteStatement __preparedStmtOfDeleteById;
    private final SharedSQLiteStatement __preparedStmtOfDeleteByStatus;
    private final SharedSQLiteStatement __preparedStmtOfMarkDeliveryFailed;
    private final SharedSQLiteStatement __preparedStmtOfUpdateRouteOrder;
    private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;
    private final EntityDeletionOrUpdateAdapter<Delivery> __updateAdapterOfDelivery;

    public DeliveryDao_Impl(final RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfDelivery = new EntityInsertionAdapter<Delivery>(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `deliveries` (`id`,`tracking_number`,`customer_name`,`customer_phone`,`customer_email`,`street_address`,`city`,`state`,`zip_code`,`latitude`,`longitude`,`package_description`,`package_count`,`package_weight`,`special_instructions`,`status`,`priority`,`created_at`,`scheduled_date`,`time_window_start`,`time_window_end`,`completed_at`,`route_order`,`estimated_arrival`,`actual_arrival`,`signature_path`,`pod_photo_paths`,`delivery_notes`,`recipient_name`,`failure_reason`,`retry_count`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertionAdapter
            public void bind(final SupportSQLiteStatement statement, final Delivery entity) {
                statement.bindLong(1, entity.getId());
                if (entity.getTrackingNumber() == null) {
                    statement.bindNull(2);
                } else {
                    statement.bindString(2, entity.getTrackingNumber());
                }
                if (entity.getCustomerName() == null) {
                    statement.bindNull(3);
                } else {
                    statement.bindString(3, entity.getCustomerName());
                }
                if (entity.getCustomerPhone() == null) {
                    statement.bindNull(4);
                } else {
                    statement.bindString(4, entity.getCustomerPhone());
                }
                if (entity.getCustomerEmail() == null) {
                    statement.bindNull(5);
                } else {
                    statement.bindString(5, entity.getCustomerEmail());
                }
                if (entity.getStreetAddress() == null) {
                    statement.bindNull(6);
                } else {
                    statement.bindString(6, entity.getStreetAddress());
                }
                if (entity.getCity() == null) {
                    statement.bindNull(7);
                } else {
                    statement.bindString(7, entity.getCity());
                }
                if (entity.getState() == null) {
                    statement.bindNull(8);
                } else {
                    statement.bindString(8, entity.getState());
                }
                if (entity.getZipCode() == null) {
                    statement.bindNull(9);
                } else {
                    statement.bindString(9, entity.getZipCode());
                }
                if (entity.getLatitude() == null) {
                    statement.bindNull(10);
                } else {
                    statement.bindDouble(10, entity.getLatitude().doubleValue());
                }
                if (entity.getLongitude() == null) {
                    statement.bindNull(11);
                } else {
                    statement.bindDouble(11, entity.getLongitude().doubleValue());
                }
                if (entity.getPackageDescription() == null) {
                    statement.bindNull(12);
                } else {
                    statement.bindString(12, entity.getPackageDescription());
                }
                statement.bindLong(13, entity.getPackageCount());
                if (entity.getPackageWeight() == null) {
                    statement.bindNull(14);
                } else {
                    statement.bindDouble(14, entity.getPackageWeight().doubleValue());
                }
                if (entity.getSpecialInstructions() == null) {
                    statement.bindNull(15);
                } else {
                    statement.bindString(15, entity.getSpecialInstructions());
                }
                String _tmp = EnumConverters.fromDeliveryStatus(entity.getStatus());
                if (_tmp == null) {
                    statement.bindNull(16);
                } else {
                    statement.bindString(16, _tmp);
                }
                String _tmp_1 = EnumConverters.fromPriority(entity.getPriority());
                if (_tmp_1 == null) {
                    statement.bindNull(17);
                } else {
                    statement.bindString(17, _tmp_1);
                }
                Long _tmp_2 = DateConverter.dateToTimestamp(entity.getCreatedAt());
                if (_tmp_2 != null) {
                    statement.bindLong(18, _tmp_2.longValue());
                } else {
                    statement.bindNull(18);
                }
                Long _tmp_3 = DateConverter.dateToTimestamp(entity.getScheduledDate());
                if (_tmp_3 != null) {
                    statement.bindLong(19, _tmp_3.longValue());
                } else {
                    statement.bindNull(19);
                }
                if (entity.getTimeWindowStart() == null) {
                    statement.bindNull(20);
                } else {
                    statement.bindString(20, entity.getTimeWindowStart());
                }
                if (entity.getTimeWindowEnd() == null) {
                    statement.bindNull(21);
                } else {
                    statement.bindString(21, entity.getTimeWindowEnd());
                }
                Long _tmp_4 = DateConverter.dateToTimestamp(entity.getCompletedAt());
                if (_tmp_4 != null) {
                    statement.bindLong(22, _tmp_4.longValue());
                } else {
                    statement.bindNull(22);
                }
                statement.bindLong(23, entity.getRouteOrder());
                Long _tmp_5 = DateConverter.dateToTimestamp(entity.getEstimatedArrival());
                if (_tmp_5 != null) {
                    statement.bindLong(24, _tmp_5.longValue());
                } else {
                    statement.bindNull(24);
                }
                Long _tmp_6 = DateConverter.dateToTimestamp(entity.getActualArrival());
                if (_tmp_6 != null) {
                    statement.bindLong(25, _tmp_6.longValue());
                } else {
                    statement.bindNull(25);
                }
                if (entity.getSignaturePath() == null) {
                    statement.bindNull(26);
                } else {
                    statement.bindString(26, entity.getSignaturePath());
                }
                if (entity.getPodPhotoPaths() == null) {
                    statement.bindNull(27);
                } else {
                    statement.bindString(27, entity.getPodPhotoPaths());
                }
                if (entity.getDeliveryNotes() == null) {
                    statement.bindNull(28);
                } else {
                    statement.bindString(28, entity.getDeliveryNotes());
                }
                if (entity.getRecipientName() == null) {
                    statement.bindNull(29);
                } else {
                    statement.bindString(29, entity.getRecipientName());
                }
                if (entity.getFailureReason() == null) {
                    statement.bindNull(30);
                } else {
                    statement.bindString(30, entity.getFailureReason());
                }
                statement.bindLong(31, entity.getRetryCount());
            }
        };
        this.__deletionAdapterOfDelivery = new EntityDeletionOrUpdateAdapter<Delivery>(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.2
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            protected String createQuery() {
                return "DELETE FROM `deliveries` WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(final SupportSQLiteStatement statement, final Delivery entity) {
                statement.bindLong(1, entity.getId());
            }
        };
        this.__updateAdapterOfDelivery = new EntityDeletionOrUpdateAdapter<Delivery>(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.3
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            protected String createQuery() {
                return "UPDATE OR ABORT `deliveries` SET `id` = ?,`tracking_number` = ?,`customer_name` = ?,`customer_phone` = ?,`customer_email` = ?,`street_address` = ?,`city` = ?,`state` = ?,`zip_code` = ?,`latitude` = ?,`longitude` = ?,`package_description` = ?,`package_count` = ?,`package_weight` = ?,`special_instructions` = ?,`status` = ?,`priority` = ?,`created_at` = ?,`scheduled_date` = ?,`time_window_start` = ?,`time_window_end` = ?,`completed_at` = ?,`route_order` = ?,`estimated_arrival` = ?,`actual_arrival` = ?,`signature_path` = ?,`pod_photo_paths` = ?,`delivery_notes` = ?,`recipient_name` = ?,`failure_reason` = ?,`retry_count` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(final SupportSQLiteStatement statement, final Delivery entity) {
                statement.bindLong(1, entity.getId());
                if (entity.getTrackingNumber() == null) {
                    statement.bindNull(2);
                } else {
                    statement.bindString(2, entity.getTrackingNumber());
                }
                if (entity.getCustomerName() == null) {
                    statement.bindNull(3);
                } else {
                    statement.bindString(3, entity.getCustomerName());
                }
                if (entity.getCustomerPhone() == null) {
                    statement.bindNull(4);
                } else {
                    statement.bindString(4, entity.getCustomerPhone());
                }
                if (entity.getCustomerEmail() == null) {
                    statement.bindNull(5);
                } else {
                    statement.bindString(5, entity.getCustomerEmail());
                }
                if (entity.getStreetAddress() == null) {
                    statement.bindNull(6);
                } else {
                    statement.bindString(6, entity.getStreetAddress());
                }
                if (entity.getCity() == null) {
                    statement.bindNull(7);
                } else {
                    statement.bindString(7, entity.getCity());
                }
                if (entity.getState() == null) {
                    statement.bindNull(8);
                } else {
                    statement.bindString(8, entity.getState());
                }
                if (entity.getZipCode() == null) {
                    statement.bindNull(9);
                } else {
                    statement.bindString(9, entity.getZipCode());
                }
                if (entity.getLatitude() == null) {
                    statement.bindNull(10);
                } else {
                    statement.bindDouble(10, entity.getLatitude().doubleValue());
                }
                if (entity.getLongitude() == null) {
                    statement.bindNull(11);
                } else {
                    statement.bindDouble(11, entity.getLongitude().doubleValue());
                }
                if (entity.getPackageDescription() == null) {
                    statement.bindNull(12);
                } else {
                    statement.bindString(12, entity.getPackageDescription());
                }
                statement.bindLong(13, entity.getPackageCount());
                if (entity.getPackageWeight() == null) {
                    statement.bindNull(14);
                } else {
                    statement.bindDouble(14, entity.getPackageWeight().doubleValue());
                }
                if (entity.getSpecialInstructions() == null) {
                    statement.bindNull(15);
                } else {
                    statement.bindString(15, entity.getSpecialInstructions());
                }
                String _tmp = EnumConverters.fromDeliveryStatus(entity.getStatus());
                if (_tmp == null) {
                    statement.bindNull(16);
                } else {
                    statement.bindString(16, _tmp);
                }
                String _tmp_1 = EnumConverters.fromPriority(entity.getPriority());
                if (_tmp_1 == null) {
                    statement.bindNull(17);
                } else {
                    statement.bindString(17, _tmp_1);
                }
                Long _tmp_2 = DateConverter.dateToTimestamp(entity.getCreatedAt());
                if (_tmp_2 != null) {
                    statement.bindLong(18, _tmp_2.longValue());
                } else {
                    statement.bindNull(18);
                }
                Long _tmp_3 = DateConverter.dateToTimestamp(entity.getScheduledDate());
                if (_tmp_3 != null) {
                    statement.bindLong(19, _tmp_3.longValue());
                } else {
                    statement.bindNull(19);
                }
                if (entity.getTimeWindowStart() == null) {
                    statement.bindNull(20);
                } else {
                    statement.bindString(20, entity.getTimeWindowStart());
                }
                if (entity.getTimeWindowEnd() == null) {
                    statement.bindNull(21);
                } else {
                    statement.bindString(21, entity.getTimeWindowEnd());
                }
                Long _tmp_4 = DateConverter.dateToTimestamp(entity.getCompletedAt());
                if (_tmp_4 != null) {
                    statement.bindLong(22, _tmp_4.longValue());
                } else {
                    statement.bindNull(22);
                }
                statement.bindLong(23, entity.getRouteOrder());
                Long _tmp_5 = DateConverter.dateToTimestamp(entity.getEstimatedArrival());
                if (_tmp_5 != null) {
                    statement.bindLong(24, _tmp_5.longValue());
                } else {
                    statement.bindNull(24);
                }
                Long _tmp_6 = DateConverter.dateToTimestamp(entity.getActualArrival());
                if (_tmp_6 != null) {
                    statement.bindLong(25, _tmp_6.longValue());
                } else {
                    statement.bindNull(25);
                }
                if (entity.getSignaturePath() == null) {
                    statement.bindNull(26);
                } else {
                    statement.bindString(26, entity.getSignaturePath());
                }
                if (entity.getPodPhotoPaths() == null) {
                    statement.bindNull(27);
                } else {
                    statement.bindString(27, entity.getPodPhotoPaths());
                }
                if (entity.getDeliveryNotes() == null) {
                    statement.bindNull(28);
                } else {
                    statement.bindString(28, entity.getDeliveryNotes());
                }
                if (entity.getRecipientName() == null) {
                    statement.bindNull(29);
                } else {
                    statement.bindString(29, entity.getRecipientName());
                }
                if (entity.getFailureReason() == null) {
                    statement.bindNull(30);
                } else {
                    statement.bindString(30, entity.getFailureReason());
                }
                statement.bindLong(31, entity.getRetryCount());
                statement.bindLong(32, entity.getId());
            }
        };
        this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE deliveries SET status = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateRouteOrder = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE deliveries SET route_order = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfCompleteDelivery = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE deliveries SET status = ?, completed_at = ?, actual_arrival = ?, signature_path = ?, recipient_name = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfMarkDeliveryFailed = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.7
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE deliveries SET status = ?, failure_reason = ?, retry_count = retry_count + 1 WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.8
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM deliveries WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteByStatus = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.9
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM deliveries WHERE status = ?";
            }
        };
        this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.10
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM deliveries";
            }
        };
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public long insert(final Delivery delivery) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long _result = this.__insertionAdapterOfDelivery.insertAndReturnId(delivery);
            this.__db.setTransactionSuccessful();
            return _result;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public List<Long> insertAll(final List<Delivery> deliveries) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            List<Long> _result = this.__insertionAdapterOfDelivery.insertAndReturnIdsList(deliveries);
            this.__db.setTransactionSuccessful();
            return _result;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int delete(final Delivery delivery) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            int _total = 0 + this.__deletionAdapterOfDelivery.handle(delivery);
            this.__db.setTransactionSuccessful();
            return _total;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int update(final Delivery delivery) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            int _total = 0 + this.__updateAdapterOfDelivery.handle(delivery);
            this.__db.setTransactionSuccessful();
            return _total;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int updateStatus(final long id, final DeliveryStatus status) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfUpdateStatus.acquire();
        String _tmp = EnumConverters.fromDeliveryStatus(status);
        if (_tmp == null) {
            _stmt.bindNull(1);
        } else {
            _stmt.bindString(1, _tmp);
        }
        _stmt.bindLong(2, id);
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfUpdateStatus.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int updateRouteOrder(final long id, final int order) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfUpdateRouteOrder.acquire();
        _stmt.bindLong(1, order);
        _stmt.bindLong(2, id);
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfUpdateRouteOrder.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int completeDelivery(final long id, final DeliveryStatus status, final Date completedAt, final Date actualArrival, final String signaturePath, final String recipientName) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfCompleteDelivery.acquire();
        String _tmp = EnumConverters.fromDeliveryStatus(status);
        if (_tmp == null) {
            _stmt.bindNull(1);
        } else {
            _stmt.bindString(1, _tmp);
        }
        Long _tmp_1 = DateConverter.dateToTimestamp(completedAt);
        if (_tmp_1 != null) {
            _stmt.bindLong(2, _tmp_1.longValue());
        } else {
            _stmt.bindNull(2);
        }
        Long _tmp_2 = DateConverter.dateToTimestamp(actualArrival);
        if (_tmp_2 != null) {
            _stmt.bindLong(3, _tmp_2.longValue());
        } else {
            _stmt.bindNull(3);
        }
        if (signaturePath == null) {
            _stmt.bindNull(4);
        } else {
            _stmt.bindString(4, signaturePath);
        }
        if (recipientName == null) {
            _stmt.bindNull(5);
        } else {
            _stmt.bindString(5, recipientName);
        }
        _stmt.bindLong(6, id);
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfCompleteDelivery.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int markDeliveryFailed(final long id, final DeliveryStatus status, final String reason) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfMarkDeliveryFailed.acquire();
        String _tmp = EnumConverters.fromDeliveryStatus(status);
        if (_tmp == null) {
            _stmt.bindNull(1);
        } else {
            _stmt.bindString(1, _tmp);
        }
        if (reason == null) {
            _stmt.bindNull(2);
        } else {
            _stmt.bindString(2, reason);
        }
        _stmt.bindLong(3, id);
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfMarkDeliveryFailed.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int deleteById(final long id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfDeleteById.acquire();
        _stmt.bindLong(1, id);
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfDeleteById.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int deleteByStatus(final DeliveryStatus status) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfDeleteByStatus.acquire();
        String _tmp = EnumConverters.fromDeliveryStatus(status);
        if (_tmp == null) {
            _stmt.bindNull(1);
        } else {
            _stmt.bindString(1, _tmp);
        }
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfDeleteByStatus.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public int deleteAll() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfDeleteAll.acquire();
        try {
            this.__db.beginTransaction();
            try {
                int _result = _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
                return _result;
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfDeleteAll.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getAllDeliveries() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries ORDER BY route_order ASC, scheduled_date ASC", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.11
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<Delivery> getDeliveryById(final long id) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE id = ? LIMIT 1", 1);
        _statement.bindLong(1, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<Delivery>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Delivery call() throws Exception {
                Delivery _result;
                String _tmpTrackingNumber;
                String _tmpTrackingNumber2;
                String _tmpCustomerName;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                Double _tmpPackageWeight;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                String _tmpTimeWindowStart;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                String _tmpSignaturePath;
                String _tmpPodPhotoPaths;
                String _tmpDeliveryNotes;
                String _tmpRecipientName;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfTimeWindowStart = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfSignaturePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfPodPhotoPaths = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfDeliveryNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfRecipientName = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfFailureReason = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    if (_cursor.moveToFirst()) {
                        Delivery _result2 = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        _result = _result2;
                        _result.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _result.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerName2 = _cursor.getString(_cursorIndexOfCustomerName);
                            _tmpTrackingNumber2 = _tmpCustomerName2;
                        }
                        _result.setCustomerName(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpCustomerName = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpCustomerName = _tmpCustomerPhone2;
                        }
                        _result.setCustomerPhone(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _result.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _result.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _result.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _result.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _result.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _result.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _result.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _result.setPackageDescription(_tmpPackageDescription);
                        int _tmpPackageCount = _cursor.getInt(_cursorIndexOfPackageCount);
                        _result.setPackageCount(_tmpPackageCount);
                        if (_cursor.isNull(_cursorIndexOfPackageWeight)) {
                            _tmpPackageWeight = null;
                        } else {
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_cursorIndexOfPackageWeight));
                        }
                        _result.setPackageWeight(_tmpPackageWeight);
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions)) {
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions);
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _result.setSpecialInstructions(_tmpSpecialInstructions);
                        if (_cursor.isNull(_cursorIndexOfStatus)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _result.setStatus(_tmpStatus);
                        if (_cursor.isNull(_cursorIndexOfPriority)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _result.setPriority(_tmpPriority);
                        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _result.setCreatedAt(_tmpCreatedAt);
                        if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _result.setScheduledDate(_tmpScheduledDate);
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart)) {
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart);
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _result.setTimeWindowStart(_tmpTimeWindowStart);
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd)) {
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd);
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _result.setTimeWindowEnd(_tmpTimeWindowEnd);
                        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _result.setCompletedAt(_tmpCompletedAt);
                        int _tmpRouteOrder = _cursor.getInt(_cursorIndexOfRouteOrder);
                        _result.setRouteOrder(_tmpRouteOrder);
                        if (_cursor.isNull(_cursorIndexOfEstimatedArrival)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfEstimatedArrival));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _result.setEstimatedArrival(_tmpEstimatedArrival);
                        if (_cursor.isNull(_cursorIndexOfActualArrival)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _result.setActualArrival(_tmpActualArrival);
                        if (_cursor.isNull(_cursorIndexOfSignaturePath)) {
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath);
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _result.setSignaturePath(_tmpSignaturePath);
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths)) {
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths);
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _result.setPodPhotoPaths(_tmpPodPhotoPaths);
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes)) {
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes);
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _result.setDeliveryNotes(_tmpDeliveryNotes);
                        if (_cursor.isNull(_cursorIndexOfRecipientName)) {
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName);
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _result.setRecipientName(_tmpRecipientName);
                        if (_cursor.isNull(_cursorIndexOfFailureReason)) {
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason);
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _result.setFailureReason(_tmpFailureReason);
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
                        _result.setRetryCount(_tmpRetryCount);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<Delivery> getDeliveryByTrackingNumber(final String trackingNumber) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE tracking_number = ? LIMIT 1", 1);
        if (trackingNumber == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, trackingNumber);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<Delivery>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Delivery call() throws Exception {
                Delivery _result;
                String _tmpTrackingNumber;
                String _tmpTrackingNumber2;
                String _tmpCustomerName;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                Double _tmpPackageWeight;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                String _tmpTimeWindowStart;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                String _tmpSignaturePath;
                String _tmpPodPhotoPaths;
                String _tmpDeliveryNotes;
                String _tmpRecipientName;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfTimeWindowStart = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfSignaturePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfPodPhotoPaths = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfDeliveryNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfRecipientName = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfFailureReason = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    if (_cursor.moveToFirst()) {
                        Delivery _result2 = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        _result = _result2;
                        _result.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _result.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerName2 = _cursor.getString(_cursorIndexOfCustomerName);
                            _tmpTrackingNumber2 = _tmpCustomerName2;
                        }
                        _result.setCustomerName(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpCustomerName = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpCustomerName = _tmpCustomerPhone2;
                        }
                        _result.setCustomerPhone(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _result.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _result.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _result.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _result.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _result.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _result.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _result.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _result.setPackageDescription(_tmpPackageDescription);
                        int _tmpPackageCount = _cursor.getInt(_cursorIndexOfPackageCount);
                        _result.setPackageCount(_tmpPackageCount);
                        if (_cursor.isNull(_cursorIndexOfPackageWeight)) {
                            _tmpPackageWeight = null;
                        } else {
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_cursorIndexOfPackageWeight));
                        }
                        _result.setPackageWeight(_tmpPackageWeight);
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions)) {
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions);
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _result.setSpecialInstructions(_tmpSpecialInstructions);
                        if (_cursor.isNull(_cursorIndexOfStatus)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _result.setStatus(_tmpStatus);
                        if (_cursor.isNull(_cursorIndexOfPriority)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _result.setPriority(_tmpPriority);
                        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _result.setCreatedAt(_tmpCreatedAt);
                        if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _result.setScheduledDate(_tmpScheduledDate);
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart)) {
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart);
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _result.setTimeWindowStart(_tmpTimeWindowStart);
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd)) {
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd);
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _result.setTimeWindowEnd(_tmpTimeWindowEnd);
                        if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _result.setCompletedAt(_tmpCompletedAt);
                        int _tmpRouteOrder = _cursor.getInt(_cursorIndexOfRouteOrder);
                        _result.setRouteOrder(_tmpRouteOrder);
                        if (_cursor.isNull(_cursorIndexOfEstimatedArrival)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfEstimatedArrival));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _result.setEstimatedArrival(_tmpEstimatedArrival);
                        if (_cursor.isNull(_cursorIndexOfActualArrival)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _result.setActualArrival(_tmpActualArrival);
                        if (_cursor.isNull(_cursorIndexOfSignaturePath)) {
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath);
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _result.setSignaturePath(_tmpSignaturePath);
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths)) {
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths);
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _result.setPodPhotoPaths(_tmpPodPhotoPaths);
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes)) {
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes);
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _result.setDeliveryNotes(_tmpDeliveryNotes);
                        if (_cursor.isNull(_cursorIndexOfRecipientName)) {
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName);
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _result.setRecipientName(_tmpRecipientName);
                        if (_cursor.isNull(_cursorIndexOfFailureReason)) {
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason);
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _result.setFailureReason(_tmpFailureReason);
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
                        _result.setRetryCount(_tmpRetryCount);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getDeliveriesByStatus(final DeliveryStatus status) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE status = ? ORDER BY route_order ASC", 1);
        String _tmp = EnumConverters.fromDeliveryStatus(status);
        if (_tmp == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, _tmp);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.14
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp_1;
                String _tmp_2;
                Long _tmp_3;
                Long _tmp_4;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_5;
                Long _tmp_6;
                Long _tmp_7;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp_1);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_2);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_3);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_4);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_5);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_7 = null;
                        } else {
                            _tmp_7 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_7);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getDeliveriesByStatuses(final List<DeliveryStatus> statuses) {
        StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("SELECT * FROM deliveries WHERE status IN (");
        int _inputSize = statuses == null ? 1 : statuses.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(") ORDER BY route_order ASC");
        String _sql = _stringBuilder.toString();
        int _argCount = _inputSize + 0;
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
        int _argIndex = 1;
        if (statuses == null) {
            _statement.bindNull(1);
        } else {
            for (DeliveryStatus _item : statuses) {
                String _tmp = EnumConverters.fromDeliveryStatus(_item);
                if (_tmp == null) {
                    _statement.bindNull(_argIndex);
                } else {
                    _statement.bindString(_argIndex, _tmp);
                }
                _argIndex++;
            }
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.15
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp_1;
                String _tmp_2;
                Long _tmp_3;
                Long _tmp_4;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_5;
                Long _tmp_6;
                Long _tmp_7;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item_1 = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item_1.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item_1.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item_1.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item_1.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item_1.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item_1.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item_1.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item_1.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item_1.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item_1.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item_1.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item_1.setPackageDescription(_tmpPackageDescription);
                        _item_1.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item_1.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item_1.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp_1);
                        _item_1.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_2);
                        _item_1.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_3);
                        _item_1.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_4);
                        _item_1.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item_1.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item_1.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_5);
                        _item_1.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item_1.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item_1.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_7 = null;
                        } else {
                            _tmp_7 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_7);
                        _item_1.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item_1.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item_1.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item_1.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item_1.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item_1.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item_1.setRetryCount(_tmpRetryCount);
                        _result2.add(_item_1);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getDeliveriesByPriority(final Priority priority) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE priority = ? ORDER BY scheduled_date ASC", 1);
        String _tmp = EnumConverters.fromPriority(priority);
        if (_tmp == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, _tmp);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.16
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp_1;
                String _tmp_2;
                Long _tmp_3;
                Long _tmp_4;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_5;
                Long _tmp_6;
                Long _tmp_7;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp_1);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_2);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_3);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_4);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_5);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_7 = null;
                        } else {
                            _tmp_7 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_7);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getDeliveriesByDate(final long date) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE DATE(scheduled_date / 1000, 'unixepoch') = DATE(? / 1000, 'unixepoch') ORDER BY route_order ASC", 1);
        _statement.bindLong(1, date);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.17
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getDeliveriesInDateRange(final Date startDate, final Date endDate) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE scheduled_date >= ? AND scheduled_date <= ? ORDER BY scheduled_date ASC", 2);
        Long _tmp = DateConverter.dateToTimestamp(startDate);
        if (_tmp != null) {
            _statement.bindLong(1, _tmp.longValue());
        } else {
            _statement.bindNull(1);
        }
        Long _tmp_1 = DateConverter.dateToTimestamp(endDate);
        if (_tmp_1 != null) {
            _statement.bindLong(2, _tmp_1.longValue());
        } else {
            _statement.bindNull(2);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.18
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp_2;
                String _tmp_3;
                Long _tmp_4;
                Long _tmp_5;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_6;
                Long _tmp_7;
                Long _tmp_8;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp_2);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_3);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_5);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_6);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_7 = null;
                        } else {
                            _tmp_7 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_7);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_8 = null;
                        } else {
                            _tmp_8 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_8);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getActiveDeliveries() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE status IN ('PENDING', 'IN_TRANSIT') ORDER BY priority DESC, route_order ASC", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.19
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getTodaysDeliveries() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE DATE(scheduled_date / 1000, 'unixepoch') = DATE('now') ORDER BY route_order ASC", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.20
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> getOverdueDeliveries(final long currentTime) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE scheduled_date < ? AND status NOT IN ('DELIVERED', 'CANCELLED') ORDER BY scheduled_date ASC", 1);
        _statement.bindLong(1, currentTime);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.21
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<List<Delivery>> searchDeliveries(final String query) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE customer_name LIKE '%' || ? || '%' OR tracking_number LIKE '%' || ? || '%' OR street_address LIKE '%' || ? || '%' OR city LIKE '%' || ? || '%' ORDER BY scheduled_date DESC", 4);
        if (query == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, query);
        }
        if (query == null) {
            _statement.bindNull(2);
        } else {
            _statement.bindString(2, query);
        }
        if (query == null) {
            _statement.bindNull(3);
        } else {
            _statement.bindString(3, query);
        }
        if (query == null) {
            _statement.bindNull(4);
        } else {
            _statement.bindString(4, query);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<List<Delivery>>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.22
            @Override // java.util.concurrent.Callable
            public List<Delivery> call() throws Exception {
                String _tmpTrackingNumber;
                String _tmpCustomerName;
                String _tmpTrackingNumber2;
                String _tmpCustomerPhone;
                String _tmpCustomerEmail;
                String _tmpStreetAddress;
                String _tmpCity;
                String _tmpState;
                Double _tmpLatitude;
                Double _tmpLatitude2;
                String _tmpPackageDescription;
                int _cursorIndexOfPackageWeight;
                Double _tmpPackageWeight;
                int _cursorIndexOfSpecialInstructions;
                String _tmpSpecialInstructions;
                String _tmp;
                String _tmp_1;
                Long _tmp_2;
                Long _tmp_3;
                int _cursorIndexOfTimeWindowStart;
                String _tmpTimeWindowStart;
                int _cursorIndexOfTimeWindowEnd;
                String _tmpTimeWindowEnd;
                Long _tmp_4;
                Long _tmp_5;
                Long _tmp_6;
                int _cursorIndexOfSignaturePath;
                String _tmpSignaturePath;
                int _cursorIndexOfPodPhotoPaths;
                String _tmpPodPhotoPaths;
                int _cursorIndexOfDeliveryNotes;
                String _tmpDeliveryNotes;
                int _cursorIndexOfRecipientName;
                String _tmpRecipientName;
                int _cursorIndexOfFailureReason;
                String _tmpFailureReason;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
                    int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
                    int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
                    int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
                    int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
                    int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
                    int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
                    int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
                    int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight2 = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                    int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                    int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
                    int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                    int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
                    int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                    int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
                    int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                    int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
                    int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                    int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
                    int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                    int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
                    int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                    int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
                    int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                    int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
                    int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                    int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
                    int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                    int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
                    int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                    int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
                    int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                    int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
                    int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                    int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
                    int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                    int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
                    int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                    int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
                    int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                    int _cursorIndexOfPackageWeight4 = _cursor.getCount();
                    List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
                    while (_cursor.moveToNext()) {
                        Delivery _item = new Delivery();
                        long _tmpId = _cursor.getLong(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        List<Delivery> _result2 = _result;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                            _tmpTrackingNumber = null;
                        } else {
                            String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                            _tmpTrackingNumber = _tmpTrackingNumber3;
                        }
                        _item.setTrackingNumber(_tmpTrackingNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpCustomerName = null;
                        } else {
                            _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                        }
                        _item.setCustomerName(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                            _tmpTrackingNumber2 = null;
                        } else {
                            String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                            _tmpTrackingNumber2 = _tmpCustomerPhone2;
                        }
                        _item.setCustomerPhone(_tmpTrackingNumber2);
                        if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                            _tmpCustomerPhone = null;
                        } else {
                            String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                            _tmpCustomerPhone = _tmpCustomerEmail2;
                        }
                        _item.setCustomerEmail(_tmpCustomerPhone);
                        if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                            _tmpCustomerEmail = null;
                        } else {
                            String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                            _tmpCustomerEmail = _tmpStreetAddress2;
                        }
                        _item.setStreetAddress(_tmpCustomerEmail);
                        if (_cursor.isNull(_cursorIndexOfCity)) {
                            _tmpStreetAddress = null;
                        } else {
                            String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                            _tmpStreetAddress = _tmpCity2;
                        }
                        _item.setCity(_tmpStreetAddress);
                        if (_cursor.isNull(_cursorIndexOfState)) {
                            _tmpCity = null;
                        } else {
                            String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                            _tmpCity = _tmpState2;
                        }
                        _item.setState(_tmpCity);
                        if (_cursor.isNull(_cursorIndexOfZipCode)) {
                            _tmpState = null;
                        } else {
                            String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                            _tmpState = _tmpZipCode;
                        }
                        _item.setZipCode(_tmpState);
                        if (_cursor.isNull(_cursorIndexOfLatitude)) {
                            _tmpLatitude = null;
                        } else {
                            _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                        }
                        _item.setLatitude(_tmpLatitude);
                        if (_cursor.isNull(_cursorIndexOfLongitude)) {
                            _tmpLatitude2 = null;
                        } else {
                            _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                        }
                        _item.setLongitude(_tmpLatitude2);
                        if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                            _tmpPackageDescription = null;
                        } else {
                            String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                            _tmpPackageDescription = _tmpPackageDescription2;
                        }
                        _item.setPackageDescription(_tmpPackageDescription);
                        _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount));
                        int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                        if (_cursor.isNull(_tmpPackageCount)) {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = null;
                        } else {
                            _cursorIndexOfPackageWeight = _tmpPackageCount;
                            _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                        }
                        _item.setPackageWeight(_tmpPackageWeight);
                        int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                        if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = null;
                        } else {
                            String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                            _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                            _tmpSpecialInstructions = _tmpSpecialInstructions2;
                        }
                        _item.setSpecialInstructions(_tmpSpecialInstructions);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmp = null;
                        } else {
                            _tmp = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                        if (_cursor.isNull(_cursorIndexOfPriority3)) {
                            _tmp_1 = null;
                        } else {
                            _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                        }
                        Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                        _item.setPriority(_tmpPriority);
                        int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                        if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                            _tmp_2 = null;
                        } else {
                            _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                        }
                        Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                        _item.setCreatedAt(_tmpCreatedAt);
                        int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                        if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                            _tmp_3 = null;
                        } else {
                            _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                        }
                        Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                        _item.setScheduledDate(_tmpScheduledDate);
                        int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = null;
                        } else {
                            String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                            _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                            _tmpTimeWindowStart = _tmpTimeWindowStart2;
                        }
                        _item.setTimeWindowStart(_tmpTimeWindowStart);
                        int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                        if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = null;
                        } else {
                            String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                            _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                            _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                        }
                        _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                        int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                        if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                            _tmp_4 = null;
                        } else {
                            _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                        }
                        Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                        _item.setCompletedAt(_tmpCompletedAt);
                        int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                        _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                        _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                        int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                        if (_cursor.isNull(_tmpRouteOrder)) {
                            _tmp_5 = null;
                        } else {
                            _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                        }
                        Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                        _item.setEstimatedArrival(_tmpEstimatedArrival);
                        int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                        if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                            _tmp_6 = null;
                        } else {
                            _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                        }
                        Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                        _item.setActualArrival(_tmpActualArrival);
                        int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                        if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = null;
                        } else {
                            String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                            _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                            _tmpSignaturePath = _tmpSignaturePath2;
                        }
                        _item.setSignaturePath(_tmpSignaturePath);
                        int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                        if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = null;
                        } else {
                            String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                            _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                            _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                        }
                        _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                        int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                        if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = null;
                        } else {
                            String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                            _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                            _tmpDeliveryNotes = _tmpDeliveryNotes2;
                        }
                        _item.setDeliveryNotes(_tmpDeliveryNotes);
                        int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                        if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = null;
                        } else {
                            String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                            _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                            _tmpRecipientName = _tmpRecipientName2;
                        }
                        _item.setRecipientName(_tmpRecipientName);
                        int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                        if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = null;
                        } else {
                            String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                            _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                            _tmpFailureReason = _tmpFailureReason2;
                        }
                        _item.setFailureReason(_tmpFailureReason);
                        int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                        int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount3);
                        _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                        _item.setRetryCount(_tmpRetryCount);
                        _result2.add(_item);
                        _result = _result2;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
                        _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                        _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                        _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                        _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                        _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                        _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                        _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                        _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                        _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                        _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                        _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                        _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                        _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<Integer> getTotalCount() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM deliveries", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<Integer>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                Integer _result;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    if (_cursor.moveToFirst()) {
                        if (_cursor.isNull(0)) {
                            _result = null;
                        } else {
                            _result = Integer.valueOf(_cursor.getInt(0));
                        }
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<Integer> getCountByStatus(final DeliveryStatus status) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM deliveries WHERE status = ?", 1);
        String _tmp = EnumConverters.fromDeliveryStatus(status);
        if (_tmp == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, _tmp);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<Integer>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                Integer _result;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    if (_cursor.moveToFirst()) {
                        if (_cursor.isNull(0)) {
                            _result = null;
                        } else {
                            _result = Integer.valueOf(_cursor.getInt(0));
                        }
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<Integer> getTodaysCount() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM deliveries WHERE DATE(scheduled_date / 1000, 'unixepoch') = DATE('now')", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<Integer>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.25
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                Integer _result;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    if (_cursor.moveToFirst()) {
                        if (_cursor.isNull(0)) {
                            _result = null;
                        } else {
                            _result = Integer.valueOf(_cursor.getInt(0));
                        }
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public LiveData<Integer> getOverdueCount(final long currentTime) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM deliveries WHERE scheduled_date < ? AND status NOT IN ('DELIVERED', 'CANCELLED')", 1);
        _statement.bindLong(1, currentTime);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"deliveries"}, false, new Callable<Integer>() { // from class: com.mobileinvoice.delivery.data.dao.DeliveryDao_Impl.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                Integer _result;
                Cursor _cursor = DBUtil.query(DeliveryDao_Impl.this.__db, _statement, false, null);
                try {
                    if (_cursor.moveToFirst()) {
                        if (_cursor.isNull(0)) {
                            _result = null;
                        } else {
                            _result = Integer.valueOf(_cursor.getInt(0));
                        }
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _cursor.close();
                }
            }

            protected void finalize() {
                _statement.release();
            }
        });
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public Delivery getDeliveryByIdSync(final long id) {
        RoomSQLiteQuery _statement;
        Delivery _result;
        String _tmpTrackingNumber;
        String _tmpCustomerName;
        String _tmpTrackingNumber2;
        String _tmpCustomerPhone;
        String _tmpCustomerEmail;
        String _tmpStreetAddress;
        String _tmpCity;
        String _tmpState;
        Double _tmpLatitude;
        Double _tmpLatitude2;
        String _tmpPackageDescription;
        Double _tmpPackageWeight;
        String _tmpSpecialInstructions;
        String _tmp;
        String _tmp_1;
        Long _tmp_2;
        Long _tmp_3;
        String _tmpTimeWindowStart;
        String _tmpTimeWindowEnd;
        Long _tmp_4;
        Long _tmp_5;
        Long _tmp_6;
        String _tmpSignaturePath;
        String _tmpPodPhotoPaths;
        String _tmpDeliveryNotes;
        String _tmpRecipientName;
        String _tmpFailureReason;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE id = ? LIMIT 1", 1);
        _statement2.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement2, false, null);
        try {
            int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            int _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
            int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
            int _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
            int _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
            int _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
            int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
            int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
            int _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
            int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            try {
                int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
                try {
                    int _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
                    int _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
                    int _cursorIndexOfPackageWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                    _statement = _statement2;
                    try {
                        int _cursorIndexOfSpecialInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
                        int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                        int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
                        int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
                        int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
                        int _cursorIndexOfTimeWindowStart = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
                        int _cursorIndexOfTimeWindowEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
                        int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
                        int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
                        int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
                        int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
                        int _cursorIndexOfSignaturePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
                        int _cursorIndexOfPodPhotoPaths = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
                        int _cursorIndexOfDeliveryNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
                        int _cursorIndexOfRecipientName = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
                        int _cursorIndexOfFailureReason = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
                        int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
                        if (_cursor.moveToFirst()) {
                            Delivery _result2 = new Delivery();
                            long _tmpId = _cursor.getLong(_cursorIndexOfId);
                            _result = _result2;
                            _result.setId(_tmpId);
                            if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                                _tmpTrackingNumber = null;
                            } else {
                                String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                                _tmpTrackingNumber = _tmpTrackingNumber3;
                            }
                            _result.setTrackingNumber(_tmpTrackingNumber);
                            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                                _tmpCustomerName = null;
                            } else {
                                _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                            }
                            _result.setCustomerName(_tmpCustomerName);
                            if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                                _tmpTrackingNumber2 = null;
                            } else {
                                String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                                _tmpTrackingNumber2 = _tmpCustomerPhone2;
                            }
                            _result.setCustomerPhone(_tmpTrackingNumber2);
                            if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                                _tmpCustomerPhone = null;
                            } else {
                                String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                                _tmpCustomerPhone = _tmpCustomerEmail2;
                            }
                            _result.setCustomerEmail(_tmpCustomerPhone);
                            if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                                _tmpCustomerEmail = null;
                            } else {
                                String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                                _tmpCustomerEmail = _tmpStreetAddress2;
                            }
                            _result.setStreetAddress(_tmpCustomerEmail);
                            if (_cursor.isNull(_cursorIndexOfCity)) {
                                _tmpStreetAddress = null;
                            } else {
                                String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                                _tmpStreetAddress = _tmpCity2;
                            }
                            _result.setCity(_tmpStreetAddress);
                            if (_cursor.isNull(_cursorIndexOfState)) {
                                _tmpCity = null;
                            } else {
                                String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                                _tmpCity = _tmpState2;
                            }
                            _result.setState(_tmpCity);
                            if (_cursor.isNull(_cursorIndexOfZipCode)) {
                                _tmpState = null;
                            } else {
                                String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                                _tmpState = _tmpZipCode;
                            }
                            _result.setZipCode(_tmpState);
                            if (_cursor.isNull(_cursorIndexOfLatitude)) {
                                _tmpLatitude = null;
                            } else {
                                _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                            }
                            _result.setLatitude(_tmpLatitude);
                            if (_cursor.isNull(_cursorIndexOfLongitude)) {
                                _tmpLatitude2 = null;
                            } else {
                                _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                            }
                            _result.setLongitude(_tmpLatitude2);
                            if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                                _tmpPackageDescription = null;
                            } else {
                                String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                                _tmpPackageDescription = _tmpPackageDescription2;
                            }
                            _result.setPackageDescription(_tmpPackageDescription);
                            int _tmpPackageCount = _cursor.getInt(_cursorIndexOfPackageCount);
                            _result.setPackageCount(_tmpPackageCount);
                            if (_cursor.isNull(_cursorIndexOfPackageWeight)) {
                                _tmpPackageWeight = null;
                            } else {
                                _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_cursorIndexOfPackageWeight));
                            }
                            _result.setPackageWeight(_tmpPackageWeight);
                            if (_cursor.isNull(_cursorIndexOfSpecialInstructions)) {
                                _tmpSpecialInstructions = null;
                            } else {
                                String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions);
                                _tmpSpecialInstructions = _tmpSpecialInstructions2;
                            }
                            _result.setSpecialInstructions(_tmpSpecialInstructions);
                            if (_cursor.isNull(_cursorIndexOfStatus)) {
                                _tmp = null;
                            } else {
                                _tmp = _cursor.getString(_cursorIndexOfStatus);
                            }
                            DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                            _result.setStatus(_tmpStatus);
                            if (_cursor.isNull(_cursorIndexOfPriority)) {
                                _tmp_1 = null;
                            } else {
                                _tmp_1 = _cursor.getString(_cursorIndexOfPriority);
                            }
                            Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                            _result.setPriority(_tmpPriority);
                            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
                                _tmp_2 = null;
                            } else {
                                _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt));
                            }
                            Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                            _result.setCreatedAt(_tmpCreatedAt);
                            if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
                                _tmp_3 = null;
                            } else {
                                _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate));
                            }
                            Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                            _result.setScheduledDate(_tmpScheduledDate);
                            if (_cursor.isNull(_cursorIndexOfTimeWindowStart)) {
                                _tmpTimeWindowStart = null;
                            } else {
                                String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart);
                                _tmpTimeWindowStart = _tmpTimeWindowStart2;
                            }
                            _result.setTimeWindowStart(_tmpTimeWindowStart);
                            if (_cursor.isNull(_cursorIndexOfTimeWindowEnd)) {
                                _tmpTimeWindowEnd = null;
                            } else {
                                String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd);
                                _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                            }
                            _result.setTimeWindowEnd(_tmpTimeWindowEnd);
                            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
                                _tmp_4 = null;
                            } else {
                                _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt));
                            }
                            Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                            _result.setCompletedAt(_tmpCompletedAt);
                            int _tmpRouteOrder = _cursor.getInt(_cursorIndexOfRouteOrder);
                            _result.setRouteOrder(_tmpRouteOrder);
                            if (_cursor.isNull(_cursorIndexOfEstimatedArrival)) {
                                _tmp_5 = null;
                            } else {
                                _tmp_5 = Long.valueOf(_cursor.getLong(_cursorIndexOfEstimatedArrival));
                            }
                            Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                            _result.setEstimatedArrival(_tmpEstimatedArrival);
                            if (_cursor.isNull(_cursorIndexOfActualArrival)) {
                                _tmp_6 = null;
                            } else {
                                _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival));
                            }
                            Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                            _result.setActualArrival(_tmpActualArrival);
                            if (_cursor.isNull(_cursorIndexOfSignaturePath)) {
                                _tmpSignaturePath = null;
                            } else {
                                String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath);
                                _tmpSignaturePath = _tmpSignaturePath2;
                            }
                            _result.setSignaturePath(_tmpSignaturePath);
                            if (_cursor.isNull(_cursorIndexOfPodPhotoPaths)) {
                                _tmpPodPhotoPaths = null;
                            } else {
                                String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths);
                                _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                            }
                            _result.setPodPhotoPaths(_tmpPodPhotoPaths);
                            if (_cursor.isNull(_cursorIndexOfDeliveryNotes)) {
                                _tmpDeliveryNotes = null;
                            } else {
                                String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes);
                                _tmpDeliveryNotes = _tmpDeliveryNotes2;
                            }
                            _result.setDeliveryNotes(_tmpDeliveryNotes);
                            if (_cursor.isNull(_cursorIndexOfRecipientName)) {
                                _tmpRecipientName = null;
                            } else {
                                String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName);
                                _tmpRecipientName = _tmpRecipientName2;
                            }
                            _result.setRecipientName(_tmpRecipientName);
                            if (_cursor.isNull(_cursorIndexOfFailureReason)) {
                                _tmpFailureReason = null;
                            } else {
                                String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason);
                                _tmpFailureReason = _tmpFailureReason2;
                            }
                            _result.setFailureReason(_tmpFailureReason);
                            int _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
                            _result.setRetryCount(_tmpRetryCount);
                        } else {
                            _result = null;
                        }
                        _cursor.close();
                        _statement.release();
                        return _result;
                    } catch (Throwable th) {
                        th = th;
                        _cursor.close();
                        _statement.release();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    _statement = _statement2;
                }
            } catch (Throwable th3) {
                th = th3;
                _statement = _statement2;
            }
        } catch (Throwable th4) {
            th = th4;
            _statement = _statement2;
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public List<Delivery> getAllDeliveriesSync() {
        RoomSQLiteQuery _statement;
        int _tmpRetryCount;
        int _cursorIndexOfTrackingNumber;
        int _cursorIndexOfCustomerName;
        int _cursorIndexOfCustomerPhone;
        int _cursorIndexOfCustomerEmail;
        int _cursorIndexOfStreetAddress;
        int _cursorIndexOfCity;
        int _cursorIndexOfState;
        int _cursorIndexOfZipCode;
        int _cursorIndexOfLatitude;
        int _cursorIndexOfLongitude;
        int _cursorIndexOfPackageDescription;
        int _cursorIndexOfPackageCount;
        int _cursorIndexOfPackageWeight;
        String _tmpTrackingNumber;
        String _tmpCustomerName;
        String _tmpTrackingNumber2;
        String _tmpCustomerPhone;
        String _tmpCustomerEmail;
        String _tmpStreetAddress;
        String _tmpCity;
        String _tmpState;
        Double _tmpLatitude;
        Double _tmpLatitude2;
        String _tmpPackageDescription;
        int _cursorIndexOfPackageWeight2;
        Double _tmpPackageWeight;
        int _cursorIndexOfSpecialInstructions;
        String _tmpSpecialInstructions;
        String _tmp;
        String _tmp_1;
        Long _tmp_2;
        Long _tmp_3;
        int _cursorIndexOfTimeWindowStart;
        String _tmpTimeWindowStart;
        int _cursorIndexOfTimeWindowEnd;
        String _tmpTimeWindowEnd;
        Long _tmp_4;
        Long _tmp_5;
        Long _tmp_6;
        int _cursorIndexOfSignaturePath;
        String _tmpSignaturePath;
        int _cursorIndexOfPodPhotoPaths;
        String _tmpPodPhotoPaths;
        int _cursorIndexOfDeliveryNotes;
        String _tmpDeliveryNotes;
        int _cursorIndexOfRecipientName;
        String _tmpRecipientName;
        int _cursorIndexOfFailureReason;
        String _tmpFailureReason;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM deliveries ORDER BY route_order ASC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement2, false, null);
        try {
            _tmpRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
            _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
            _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
            _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
            _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
            _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
            _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
            _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
            _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
            _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
            try {
                _cursorIndexOfPackageWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                _statement = _statement2;
            } catch (Throwable th) {
                th = th;
                _statement = _statement2;
            }
        } catch (Throwable th2) {
            th = th2;
            _statement = _statement2;
        }
        try {
            int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
            int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
            int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
            int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
            int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
            int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
            int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
            int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
            int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
            int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
            int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
            int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
            int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
            int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
            int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
            int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
            int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
            int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
            int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
            int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
            int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
            int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
            int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
            int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
            int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
            int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
            int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
            int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
            int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
            int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
            int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
            int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
            int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
            int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
            int _cursorIndexOfPackageWeight4 = _cursor.getCount();
            List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
            while (_cursor.moveToNext()) {
                Delivery _item = new Delivery();
                long _tmpId = _cursor.getLong(_tmpRetryCount);
                int _cursorIndexOfId = _tmpRetryCount;
                int _cursorIndexOfPackageCount2 = _cursorIndexOfPackageCount;
                _item.setId(_tmpId);
                if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                    _tmpTrackingNumber = null;
                } else {
                    String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                    _tmpTrackingNumber = _tmpTrackingNumber3;
                }
                _item.setTrackingNumber(_tmpTrackingNumber);
                if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                    _tmpCustomerName = null;
                } else {
                    _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                }
                _item.setCustomerName(_tmpCustomerName);
                if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                    _tmpTrackingNumber2 = null;
                } else {
                    String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                    _tmpTrackingNumber2 = _tmpCustomerPhone2;
                }
                _item.setCustomerPhone(_tmpTrackingNumber2);
                if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                    _tmpCustomerPhone = null;
                } else {
                    String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                    _tmpCustomerPhone = _tmpCustomerEmail2;
                }
                _item.setCustomerEmail(_tmpCustomerPhone);
                if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                    _tmpCustomerEmail = null;
                } else {
                    String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                    _tmpCustomerEmail = _tmpStreetAddress2;
                }
                _item.setStreetAddress(_tmpCustomerEmail);
                if (_cursor.isNull(_cursorIndexOfCity)) {
                    _tmpStreetAddress = null;
                } else {
                    String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                    _tmpStreetAddress = _tmpCity2;
                }
                _item.setCity(_tmpStreetAddress);
                if (_cursor.isNull(_cursorIndexOfState)) {
                    _tmpCity = null;
                } else {
                    String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                    _tmpCity = _tmpState2;
                }
                _item.setState(_tmpCity);
                if (_cursor.isNull(_cursorIndexOfZipCode)) {
                    _tmpState = null;
                } else {
                    String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                    _tmpState = _tmpZipCode;
                }
                _item.setZipCode(_tmpState);
                if (_cursor.isNull(_cursorIndexOfLatitude)) {
                    _tmpLatitude = null;
                } else {
                    _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                }
                _item.setLatitude(_tmpLatitude);
                if (_cursor.isNull(_cursorIndexOfLongitude)) {
                    _tmpLatitude2 = null;
                } else {
                    _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                }
                _item.setLongitude(_tmpLatitude2);
                if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                    _tmpPackageDescription = null;
                } else {
                    String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                    _tmpPackageDescription = _tmpPackageDescription2;
                }
                _item.setPackageDescription(_tmpPackageDescription);
                _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount2));
                int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                if (_cursor.isNull(_tmpPackageCount)) {
                    _cursorIndexOfPackageWeight2 = _tmpPackageCount;
                    _tmpPackageWeight = null;
                } else {
                    _cursorIndexOfPackageWeight2 = _tmpPackageCount;
                    _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                }
                _item.setPackageWeight(_tmpPackageWeight);
                int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                    _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                    _tmpSpecialInstructions = null;
                } else {
                    String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                    _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                    _tmpSpecialInstructions = _tmpSpecialInstructions2;
                }
                _item.setSpecialInstructions(_tmpSpecialInstructions);
                int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                if (_cursor.isNull(_cursorIndexOfStatus3)) {
                    _tmp = null;
                } else {
                    _tmp = _cursor.getString(_cursorIndexOfStatus3);
                }
                DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                _item.setStatus(_tmpStatus);
                int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                if (_cursor.isNull(_cursorIndexOfPriority3)) {
                    _tmp_1 = null;
                } else {
                    _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                }
                Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                _item.setPriority(_tmpPriority);
                int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                    _tmp_2 = null;
                } else {
                    _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                }
                Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                _item.setCreatedAt(_tmpCreatedAt);
                int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                    _tmp_3 = null;
                } else {
                    _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                }
                Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                _item.setScheduledDate(_tmpScheduledDate);
                int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                    _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                    _tmpTimeWindowStart = null;
                } else {
                    String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                    _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                    _tmpTimeWindowStart = _tmpTimeWindowStart2;
                }
                _item.setTimeWindowStart(_tmpTimeWindowStart);
                int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                    _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                    _tmpTimeWindowEnd = null;
                } else {
                    String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                    _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                    _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                }
                _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                    _tmp_4 = null;
                } else {
                    _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                }
                Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                _item.setCompletedAt(_tmpCompletedAt);
                int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                if (_cursor.isNull(_tmpRouteOrder)) {
                    _tmp_5 = null;
                } else {
                    _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                }
                Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                _item.setEstimatedArrival(_tmpEstimatedArrival);
                int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                    _tmp_6 = null;
                } else {
                    _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                }
                Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                _item.setActualArrival(_tmpActualArrival);
                int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                    _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                    _tmpSignaturePath = null;
                } else {
                    String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                    _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                    _tmpSignaturePath = _tmpSignaturePath2;
                }
                _item.setSignaturePath(_tmpSignaturePath);
                int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                    _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                    _tmpPodPhotoPaths = null;
                } else {
                    String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                    _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                    _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                }
                _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                    _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                    _tmpDeliveryNotes = null;
                } else {
                    String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                    _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                    _tmpDeliveryNotes = _tmpDeliveryNotes2;
                }
                _item.setDeliveryNotes(_tmpDeliveryNotes);
                int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                    _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                    _tmpRecipientName = null;
                } else {
                    String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                    _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                    _tmpRecipientName = _tmpRecipientName2;
                }
                _item.setRecipientName(_tmpRecipientName);
                int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                    _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                    _tmpFailureReason = null;
                } else {
                    String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                    _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                    _tmpFailureReason = _tmpFailureReason2;
                }
                _item.setFailureReason(_tmpFailureReason);
                int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                int _tmpRetryCount2 = _cursor.getInt(_cursorIndexOfRetryCount3);
                _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                _item.setRetryCount(_tmpRetryCount2);
                _result.add(_item);
                _tmpRetryCount = _cursorIndexOfId;
                _cursorIndexOfPackageCount = _cursorIndexOfPackageCount2;
                _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
            }
            _cursor.close();
            _statement.release();
            return _result;
        } catch (Throwable th3) {
            th = th3;
            _cursor.close();
            _statement.release();
            throw th;
        }
    }

    @Override // com.mobileinvoice.delivery.data.dao.DeliveryDao
    public List<Delivery> getActiveDeliveriesSync() {
        RoomSQLiteQuery _statement;
        int _tmpRetryCount;
        int _cursorIndexOfTrackingNumber;
        int _cursorIndexOfCustomerName;
        int _cursorIndexOfCustomerPhone;
        int _cursorIndexOfCustomerEmail;
        int _cursorIndexOfStreetAddress;
        int _cursorIndexOfCity;
        int _cursorIndexOfState;
        int _cursorIndexOfZipCode;
        int _cursorIndexOfLatitude;
        int _cursorIndexOfLongitude;
        int _cursorIndexOfPackageDescription;
        int _cursorIndexOfPackageCount;
        int _cursorIndexOfPackageWeight;
        String _tmpTrackingNumber;
        String _tmpCustomerName;
        String _tmpTrackingNumber2;
        String _tmpCustomerPhone;
        String _tmpCustomerEmail;
        String _tmpStreetAddress;
        String _tmpCity;
        String _tmpState;
        Double _tmpLatitude;
        Double _tmpLatitude2;
        String _tmpPackageDescription;
        int _cursorIndexOfPackageWeight2;
        Double _tmpPackageWeight;
        int _cursorIndexOfSpecialInstructions;
        String _tmpSpecialInstructions;
        String _tmp;
        String _tmp_1;
        Long _tmp_2;
        Long _tmp_3;
        int _cursorIndexOfTimeWindowStart;
        String _tmpTimeWindowStart;
        int _cursorIndexOfTimeWindowEnd;
        String _tmpTimeWindowEnd;
        Long _tmp_4;
        Long _tmp_5;
        Long _tmp_6;
        int _cursorIndexOfSignaturePath;
        String _tmpSignaturePath;
        int _cursorIndexOfPodPhotoPaths;
        String _tmpPodPhotoPaths;
        int _cursorIndexOfDeliveryNotes;
        String _tmpDeliveryNotes;
        int _cursorIndexOfRecipientName;
        String _tmpRecipientName;
        int _cursorIndexOfFailureReason;
        String _tmpFailureReason;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM deliveries WHERE status IN ('PENDING', 'IN_TRANSIT') ORDER BY route_order ASC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement2, false, null);
        try {
            _tmpRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            _cursorIndexOfTrackingNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tracking_number");
            _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_name");
            _cursorIndexOfCustomerPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_phone");
            _cursorIndexOfCustomerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "customer_email");
            _cursorIndexOfStreetAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "street_address");
            _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
            _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
            _cursorIndexOfZipCode = CursorUtil.getColumnIndexOrThrow(_cursor, "zip_code");
            _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            _cursorIndexOfPackageDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "package_description");
            _cursorIndexOfPackageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "package_count");
            try {
                _cursorIndexOfPackageWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "package_weight");
                _statement = _statement2;
            } catch (Throwable th) {
                th = th;
                _statement = _statement2;
            }
        } catch (Throwable th2) {
            th = th2;
            _statement = _statement2;
        }
        try {
            int _cursorIndexOfSpecialInstructions2 = CursorUtil.getColumnIndexOrThrow(_cursor, "special_instructions");
            int _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions2;
            int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
            int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
            int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, LogFactory.PRIORITY_KEY);
            int _cursorIndexOfPriority2 = _cursorIndexOfPriority;
            int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            int _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt;
            int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduled_date");
            int _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate;
            int _cursorIndexOfTimeWindowStart2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_start");
            int _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart2;
            int _cursorIndexOfTimeWindowEnd2 = CursorUtil.getColumnIndexOrThrow(_cursor, "time_window_end");
            int _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd2;
            int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
            int _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt;
            int _cursorIndexOfRouteOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "route_order");
            int _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder;
            int _cursorIndexOfEstimatedArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_arrival");
            int _cursorIndexOfEstimatedArrival2 = _cursorIndexOfEstimatedArrival;
            int _cursorIndexOfActualArrival = CursorUtil.getColumnIndexOrThrow(_cursor, "actual_arrival");
            int _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival;
            int _cursorIndexOfSignaturePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "signature_path");
            int _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath2;
            int _cursorIndexOfPodPhotoPaths2 = CursorUtil.getColumnIndexOrThrow(_cursor, "pod_photo_paths");
            int _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths2;
            int _cursorIndexOfDeliveryNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "delivery_notes");
            int _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes2;
            int _cursorIndexOfRecipientName2 = CursorUtil.getColumnIndexOrThrow(_cursor, "recipient_name");
            int _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName2;
            int _cursorIndexOfFailureReason2 = CursorUtil.getColumnIndexOrThrow(_cursor, "failure_reason");
            int _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason2;
            int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retry_count");
            int _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount;
            int _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight;
            int _cursorIndexOfPackageWeight4 = _cursor.getCount();
            List<Delivery> _result = new ArrayList<>(_cursorIndexOfPackageWeight4);
            while (_cursor.moveToNext()) {
                Delivery _item = new Delivery();
                long _tmpId = _cursor.getLong(_tmpRetryCount);
                int _cursorIndexOfId = _tmpRetryCount;
                int _cursorIndexOfPackageCount2 = _cursorIndexOfPackageCount;
                _item.setId(_tmpId);
                if (_cursor.isNull(_cursorIndexOfTrackingNumber)) {
                    _tmpTrackingNumber = null;
                } else {
                    String _tmpTrackingNumber3 = _cursor.getString(_cursorIndexOfTrackingNumber);
                    _tmpTrackingNumber = _tmpTrackingNumber3;
                }
                _item.setTrackingNumber(_tmpTrackingNumber);
                if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                    _tmpCustomerName = null;
                } else {
                    _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
                }
                _item.setCustomerName(_tmpCustomerName);
                if (_cursor.isNull(_cursorIndexOfCustomerPhone)) {
                    _tmpTrackingNumber2 = null;
                } else {
                    String _tmpCustomerPhone2 = _cursor.getString(_cursorIndexOfCustomerPhone);
                    _tmpTrackingNumber2 = _tmpCustomerPhone2;
                }
                _item.setCustomerPhone(_tmpTrackingNumber2);
                if (_cursor.isNull(_cursorIndexOfCustomerEmail)) {
                    _tmpCustomerPhone = null;
                } else {
                    String _tmpCustomerEmail2 = _cursor.getString(_cursorIndexOfCustomerEmail);
                    _tmpCustomerPhone = _tmpCustomerEmail2;
                }
                _item.setCustomerEmail(_tmpCustomerPhone);
                if (_cursor.isNull(_cursorIndexOfStreetAddress)) {
                    _tmpCustomerEmail = null;
                } else {
                    String _tmpStreetAddress2 = _cursor.getString(_cursorIndexOfStreetAddress);
                    _tmpCustomerEmail = _tmpStreetAddress2;
                }
                _item.setStreetAddress(_tmpCustomerEmail);
                if (_cursor.isNull(_cursorIndexOfCity)) {
                    _tmpStreetAddress = null;
                } else {
                    String _tmpCity2 = _cursor.getString(_cursorIndexOfCity);
                    _tmpStreetAddress = _tmpCity2;
                }
                _item.setCity(_tmpStreetAddress);
                if (_cursor.isNull(_cursorIndexOfState)) {
                    _tmpCity = null;
                } else {
                    String _tmpState2 = _cursor.getString(_cursorIndexOfState);
                    _tmpCity = _tmpState2;
                }
                _item.setState(_tmpCity);
                if (_cursor.isNull(_cursorIndexOfZipCode)) {
                    _tmpState = null;
                } else {
                    String _tmpZipCode = _cursor.getString(_cursorIndexOfZipCode);
                    _tmpState = _tmpZipCode;
                }
                _item.setZipCode(_tmpState);
                if (_cursor.isNull(_cursorIndexOfLatitude)) {
                    _tmpLatitude = null;
                } else {
                    _tmpLatitude = Double.valueOf(_cursor.getDouble(_cursorIndexOfLatitude));
                }
                _item.setLatitude(_tmpLatitude);
                if (_cursor.isNull(_cursorIndexOfLongitude)) {
                    _tmpLatitude2 = null;
                } else {
                    _tmpLatitude2 = Double.valueOf(_cursor.getDouble(_cursorIndexOfLongitude));
                }
                _item.setLongitude(_tmpLatitude2);
                if (_cursor.isNull(_cursorIndexOfPackageDescription)) {
                    _tmpPackageDescription = null;
                } else {
                    String _tmpPackageDescription2 = _cursor.getString(_cursorIndexOfPackageDescription);
                    _tmpPackageDescription = _tmpPackageDescription2;
                }
                _item.setPackageDescription(_tmpPackageDescription);
                _item.setPackageCount(_cursor.getInt(_cursorIndexOfPackageCount2));
                int _tmpPackageCount = _cursorIndexOfPackageWeight3;
                if (_cursor.isNull(_tmpPackageCount)) {
                    _cursorIndexOfPackageWeight2 = _tmpPackageCount;
                    _tmpPackageWeight = null;
                } else {
                    _cursorIndexOfPackageWeight2 = _tmpPackageCount;
                    _tmpPackageWeight = Double.valueOf(_cursor.getDouble(_tmpPackageCount));
                }
                _item.setPackageWeight(_tmpPackageWeight);
                int _cursorIndexOfSpecialInstructions4 = _cursorIndexOfSpecialInstructions3;
                if (_cursor.isNull(_cursorIndexOfSpecialInstructions4)) {
                    _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                    _tmpSpecialInstructions = null;
                } else {
                    String _tmpSpecialInstructions2 = _cursor.getString(_cursorIndexOfSpecialInstructions4);
                    _cursorIndexOfSpecialInstructions = _cursorIndexOfSpecialInstructions4;
                    _tmpSpecialInstructions = _tmpSpecialInstructions2;
                }
                _item.setSpecialInstructions(_tmpSpecialInstructions);
                int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                if (_cursor.isNull(_cursorIndexOfStatus3)) {
                    _tmp = null;
                } else {
                    _tmp = _cursor.getString(_cursorIndexOfStatus3);
                }
                DeliveryStatus _tmpStatus = EnumConverters.toDeliveryStatus(_tmp);
                _item.setStatus(_tmpStatus);
                int _cursorIndexOfPriority3 = _cursorIndexOfPriority2;
                if (_cursor.isNull(_cursorIndexOfPriority3)) {
                    _tmp_1 = null;
                } else {
                    _tmp_1 = _cursor.getString(_cursorIndexOfPriority3);
                }
                Priority _tmpPriority = EnumConverters.toPriority(_tmp_1);
                _item.setPriority(_tmpPriority);
                int _cursorIndexOfCreatedAt3 = _cursorIndexOfCreatedAt2;
                if (_cursor.isNull(_cursorIndexOfCreatedAt3)) {
                    _tmp_2 = null;
                } else {
                    _tmp_2 = Long.valueOf(_cursor.getLong(_cursorIndexOfCreatedAt3));
                }
                Date _tmpCreatedAt = DateConverter.fromTimestamp(_tmp_2);
                _item.setCreatedAt(_tmpCreatedAt);
                int _cursorIndexOfScheduledDate3 = _cursorIndexOfScheduledDate2;
                if (_cursor.isNull(_cursorIndexOfScheduledDate3)) {
                    _tmp_3 = null;
                } else {
                    _tmp_3 = Long.valueOf(_cursor.getLong(_cursorIndexOfScheduledDate3));
                }
                Date _tmpScheduledDate = DateConverter.fromTimestamp(_tmp_3);
                _item.setScheduledDate(_tmpScheduledDate);
                int _cursorIndexOfTimeWindowStart4 = _cursorIndexOfTimeWindowStart3;
                if (_cursor.isNull(_cursorIndexOfTimeWindowStart4)) {
                    _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                    _tmpTimeWindowStart = null;
                } else {
                    String _tmpTimeWindowStart2 = _cursor.getString(_cursorIndexOfTimeWindowStart4);
                    _cursorIndexOfTimeWindowStart = _cursorIndexOfTimeWindowStart4;
                    _tmpTimeWindowStart = _tmpTimeWindowStart2;
                }
                _item.setTimeWindowStart(_tmpTimeWindowStart);
                int _cursorIndexOfTimeWindowEnd4 = _cursorIndexOfTimeWindowEnd3;
                if (_cursor.isNull(_cursorIndexOfTimeWindowEnd4)) {
                    _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                    _tmpTimeWindowEnd = null;
                } else {
                    String _tmpTimeWindowEnd2 = _cursor.getString(_cursorIndexOfTimeWindowEnd4);
                    _cursorIndexOfTimeWindowEnd = _cursorIndexOfTimeWindowEnd4;
                    _tmpTimeWindowEnd = _tmpTimeWindowEnd2;
                }
                _item.setTimeWindowEnd(_tmpTimeWindowEnd);
                int _cursorIndexOfCompletedAt3 = _cursorIndexOfCompletedAt2;
                if (_cursor.isNull(_cursorIndexOfCompletedAt3)) {
                    _tmp_4 = null;
                } else {
                    _tmp_4 = Long.valueOf(_cursor.getLong(_cursorIndexOfCompletedAt3));
                }
                Date _tmpCompletedAt = DateConverter.fromTimestamp(_tmp_4);
                _item.setCompletedAt(_tmpCompletedAt);
                int _cursorIndexOfRouteOrder3 = _cursorIndexOfRouteOrder2;
                _cursorIndexOfRouteOrder2 = _cursorIndexOfRouteOrder3;
                _item.setRouteOrder(_cursor.getInt(_cursorIndexOfRouteOrder3));
                int _tmpRouteOrder = _cursorIndexOfEstimatedArrival2;
                if (_cursor.isNull(_tmpRouteOrder)) {
                    _tmp_5 = null;
                } else {
                    _tmp_5 = Long.valueOf(_cursor.getLong(_tmpRouteOrder));
                }
                Date _tmpEstimatedArrival = DateConverter.fromTimestamp(_tmp_5);
                _item.setEstimatedArrival(_tmpEstimatedArrival);
                int _cursorIndexOfActualArrival3 = _cursorIndexOfActualArrival2;
                if (_cursor.isNull(_cursorIndexOfActualArrival3)) {
                    _tmp_6 = null;
                } else {
                    _tmp_6 = Long.valueOf(_cursor.getLong(_cursorIndexOfActualArrival3));
                }
                Date _tmpActualArrival = DateConverter.fromTimestamp(_tmp_6);
                _item.setActualArrival(_tmpActualArrival);
                int _cursorIndexOfSignaturePath4 = _cursorIndexOfSignaturePath3;
                if (_cursor.isNull(_cursorIndexOfSignaturePath4)) {
                    _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                    _tmpSignaturePath = null;
                } else {
                    String _tmpSignaturePath2 = _cursor.getString(_cursorIndexOfSignaturePath4);
                    _cursorIndexOfSignaturePath = _cursorIndexOfSignaturePath4;
                    _tmpSignaturePath = _tmpSignaturePath2;
                }
                _item.setSignaturePath(_tmpSignaturePath);
                int _cursorIndexOfPodPhotoPaths4 = _cursorIndexOfPodPhotoPaths3;
                if (_cursor.isNull(_cursorIndexOfPodPhotoPaths4)) {
                    _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                    _tmpPodPhotoPaths = null;
                } else {
                    String _tmpPodPhotoPaths2 = _cursor.getString(_cursorIndexOfPodPhotoPaths4);
                    _cursorIndexOfPodPhotoPaths = _cursorIndexOfPodPhotoPaths4;
                    _tmpPodPhotoPaths = _tmpPodPhotoPaths2;
                }
                _item.setPodPhotoPaths(_tmpPodPhotoPaths);
                int _cursorIndexOfDeliveryNotes4 = _cursorIndexOfDeliveryNotes3;
                if (_cursor.isNull(_cursorIndexOfDeliveryNotes4)) {
                    _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                    _tmpDeliveryNotes = null;
                } else {
                    String _tmpDeliveryNotes2 = _cursor.getString(_cursorIndexOfDeliveryNotes4);
                    _cursorIndexOfDeliveryNotes = _cursorIndexOfDeliveryNotes4;
                    _tmpDeliveryNotes = _tmpDeliveryNotes2;
                }
                _item.setDeliveryNotes(_tmpDeliveryNotes);
                int _cursorIndexOfRecipientName4 = _cursorIndexOfRecipientName3;
                if (_cursor.isNull(_cursorIndexOfRecipientName4)) {
                    _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                    _tmpRecipientName = null;
                } else {
                    String _tmpRecipientName2 = _cursor.getString(_cursorIndexOfRecipientName4);
                    _cursorIndexOfRecipientName = _cursorIndexOfRecipientName4;
                    _tmpRecipientName = _tmpRecipientName2;
                }
                _item.setRecipientName(_tmpRecipientName);
                int _cursorIndexOfFailureReason4 = _cursorIndexOfFailureReason3;
                if (_cursor.isNull(_cursorIndexOfFailureReason4)) {
                    _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                    _tmpFailureReason = null;
                } else {
                    String _tmpFailureReason2 = _cursor.getString(_cursorIndexOfFailureReason4);
                    _cursorIndexOfFailureReason = _cursorIndexOfFailureReason4;
                    _tmpFailureReason = _tmpFailureReason2;
                }
                _item.setFailureReason(_tmpFailureReason);
                int _cursorIndexOfRetryCount3 = _cursorIndexOfRetryCount2;
                int _tmpRetryCount2 = _cursor.getInt(_cursorIndexOfRetryCount3);
                _cursorIndexOfRetryCount2 = _cursorIndexOfRetryCount3;
                _item.setRetryCount(_tmpRetryCount2);
                _result.add(_item);
                _tmpRetryCount = _cursorIndexOfId;
                _cursorIndexOfPackageCount = _cursorIndexOfPackageCount2;
                _cursorIndexOfPackageWeight3 = _cursorIndexOfPackageWeight2;
                _cursorIndexOfSpecialInstructions3 = _cursorIndexOfSpecialInstructions;
                _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                _cursorIndexOfPriority2 = _cursorIndexOfPriority3;
                _cursorIndexOfCreatedAt2 = _cursorIndexOfCreatedAt3;
                _cursorIndexOfScheduledDate2 = _cursorIndexOfScheduledDate3;
                _cursorIndexOfTimeWindowStart3 = _cursorIndexOfTimeWindowStart;
                _cursorIndexOfTimeWindowEnd3 = _cursorIndexOfTimeWindowEnd;
                _cursorIndexOfCompletedAt2 = _cursorIndexOfCompletedAt3;
                _cursorIndexOfEstimatedArrival2 = _tmpRouteOrder;
                _cursorIndexOfActualArrival2 = _cursorIndexOfActualArrival3;
                _cursorIndexOfSignaturePath3 = _cursorIndexOfSignaturePath;
                _cursorIndexOfPodPhotoPaths3 = _cursorIndexOfPodPhotoPaths;
                _cursorIndexOfDeliveryNotes3 = _cursorIndexOfDeliveryNotes;
                _cursorIndexOfRecipientName3 = _cursorIndexOfRecipientName;
                _cursorIndexOfFailureReason3 = _cursorIndexOfFailureReason;
            }
            _cursor.close();
            _statement.release();
            return _result;
        } catch (Throwable th3) {
            th = th3;
            _cursor.close();
            _statement.release();
            throw th;
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
