package com.mobileinvoice.ocr.database;

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
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes11.dex */
public final class InvoiceDao_Impl implements InvoiceDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<Invoice> __deletionAdapterOfInvoice;
    private final EntityInsertionAdapter<Invoice> __insertionAdapterOfInvoice;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAll;
    private final EntityDeletionOrUpdateAdapter<Invoice> __updateAdapterOfInvoice;

    public InvoiceDao_Impl(final RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfInvoice = new EntityInsertionAdapter<Invoice>(__db) { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            protected String createQuery() {
                return "INSERT OR ABORT INTO `invoices` (`id`,`invoiceNumber`,`customerName`,`address`,`phone`,`items`,`podImagePath1`,`podImagePath2`,`podImagePath3`,`podImagePath4`,`podImagePath5`,`podImagePath6`,`signatureImagePath`,`notes`,`originalImagePath`,`rawOcrText`,`timestamp`,`status`,`stopTimeMinutes`,`deliverySequence`,`preprocessedImagePath`,`serviceType`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertionAdapter
            public void bind(final SupportSQLiteStatement statement, final Invoice entity) {
                statement.bindLong(1, entity.getId());
                if (entity.getInvoiceNumber() == null) {
                    statement.bindNull(2);
                } else {
                    statement.bindString(2, entity.getInvoiceNumber());
                }
                if (entity.getCustomerName() == null) {
                    statement.bindNull(3);
                } else {
                    statement.bindString(3, entity.getCustomerName());
                }
                if (entity.getAddress() == null) {
                    statement.bindNull(4);
                } else {
                    statement.bindString(4, entity.getAddress());
                }
                if (entity.getPhone() == null) {
                    statement.bindNull(5);
                } else {
                    statement.bindString(5, entity.getPhone());
                }
                if (entity.getItems() == null) {
                    statement.bindNull(6);
                } else {
                    statement.bindString(6, entity.getItems());
                }
                if (entity.getPodImagePath1() == null) {
                    statement.bindNull(7);
                } else {
                    statement.bindString(7, entity.getPodImagePath1());
                }
                if (entity.getPodImagePath2() == null) {
                    statement.bindNull(8);
                } else {
                    statement.bindString(8, entity.getPodImagePath2());
                }
                if (entity.getPodImagePath3() == null) {
                    statement.bindNull(9);
                } else {
                    statement.bindString(9, entity.getPodImagePath3());
                }
                if (entity.getPodImagePath4() == null) {
                    statement.bindNull(10);
                } else {
                    statement.bindString(10, entity.getPodImagePath4());
                }
                if (entity.getPodImagePath5() == null) {
                    statement.bindNull(11);
                } else {
                    statement.bindString(11, entity.getPodImagePath5());
                }
                if (entity.getPodImagePath6() == null) {
                    statement.bindNull(12);
                } else {
                    statement.bindString(12, entity.getPodImagePath6());
                }
                if (entity.getSignatureImagePath() == null) {
                    statement.bindNull(13);
                } else {
                    statement.bindString(13, entity.getSignatureImagePath());
                }
                if (entity.getNotes() == null) {
                    statement.bindNull(14);
                } else {
                    statement.bindString(14, entity.getNotes());
                }
                if (entity.getOriginalImagePath() == null) {
                    statement.bindNull(15);
                } else {
                    statement.bindString(15, entity.getOriginalImagePath());
                }
                if (entity.getRawOcrText() == null) {
                    statement.bindNull(16);
                } else {
                    statement.bindString(16, entity.getRawOcrText());
                }
                statement.bindLong(17, entity.getTimestamp());
                if (entity.getStatus() == null) {
                    statement.bindNull(18);
                } else {
                    statement.bindString(18, entity.getStatus());
                }
                statement.bindLong(19, entity.getStopTimeMinutes());
                statement.bindLong(20, entity.getDeliverySequence());
                if (entity.getPreprocessedImagePath() == null) {
                    statement.bindNull(21);
                } else {
                    statement.bindString(21, entity.getPreprocessedImagePath());
                }
                if (entity.getServiceType() == null) {
                    statement.bindNull(22);
                } else {
                    statement.bindString(22, entity.getServiceType());
                }
            }
        };
        this.__deletionAdapterOfInvoice = new EntityDeletionOrUpdateAdapter<Invoice>(__db) { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.2
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            protected String createQuery() {
                return "DELETE FROM `invoices` WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(final SupportSQLiteStatement statement, final Invoice entity) {
                statement.bindLong(1, entity.getId());
            }
        };
        this.__updateAdapterOfInvoice = new EntityDeletionOrUpdateAdapter<Invoice>(__db) { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.3
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            protected String createQuery() {
                return "UPDATE OR ABORT `invoices` SET `id` = ?,`invoiceNumber` = ?,`customerName` = ?,`address` = ?,`phone` = ?,`items` = ?,`podImagePath1` = ?,`podImagePath2` = ?,`podImagePath3` = ?,`podImagePath4` = ?,`podImagePath5` = ?,`podImagePath6` = ?,`signatureImagePath` = ?,`notes` = ?,`originalImagePath` = ?,`rawOcrText` = ?,`timestamp` = ?,`status` = ?,`stopTimeMinutes` = ?,`deliverySequence` = ?,`preprocessedImagePath` = ?,`serviceType` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(final SupportSQLiteStatement statement, final Invoice entity) {
                statement.bindLong(1, entity.getId());
                if (entity.getInvoiceNumber() == null) {
                    statement.bindNull(2);
                } else {
                    statement.bindString(2, entity.getInvoiceNumber());
                }
                if (entity.getCustomerName() == null) {
                    statement.bindNull(3);
                } else {
                    statement.bindString(3, entity.getCustomerName());
                }
                if (entity.getAddress() == null) {
                    statement.bindNull(4);
                } else {
                    statement.bindString(4, entity.getAddress());
                }
                if (entity.getPhone() == null) {
                    statement.bindNull(5);
                } else {
                    statement.bindString(5, entity.getPhone());
                }
                if (entity.getItems() == null) {
                    statement.bindNull(6);
                } else {
                    statement.bindString(6, entity.getItems());
                }
                if (entity.getPodImagePath1() == null) {
                    statement.bindNull(7);
                } else {
                    statement.bindString(7, entity.getPodImagePath1());
                }
                if (entity.getPodImagePath2() == null) {
                    statement.bindNull(8);
                } else {
                    statement.bindString(8, entity.getPodImagePath2());
                }
                if (entity.getPodImagePath3() == null) {
                    statement.bindNull(9);
                } else {
                    statement.bindString(9, entity.getPodImagePath3());
                }
                if (entity.getPodImagePath4() == null) {
                    statement.bindNull(10);
                } else {
                    statement.bindString(10, entity.getPodImagePath4());
                }
                if (entity.getPodImagePath5() == null) {
                    statement.bindNull(11);
                } else {
                    statement.bindString(11, entity.getPodImagePath5());
                }
                if (entity.getPodImagePath6() == null) {
                    statement.bindNull(12);
                } else {
                    statement.bindString(12, entity.getPodImagePath6());
                }
                if (entity.getSignatureImagePath() == null) {
                    statement.bindNull(13);
                } else {
                    statement.bindString(13, entity.getSignatureImagePath());
                }
                if (entity.getNotes() == null) {
                    statement.bindNull(14);
                } else {
                    statement.bindString(14, entity.getNotes());
                }
                if (entity.getOriginalImagePath() == null) {
                    statement.bindNull(15);
                } else {
                    statement.bindString(15, entity.getOriginalImagePath());
                }
                if (entity.getRawOcrText() == null) {
                    statement.bindNull(16);
                } else {
                    statement.bindString(16, entity.getRawOcrText());
                }
                statement.bindLong(17, entity.getTimestamp());
                if (entity.getStatus() == null) {
                    statement.bindNull(18);
                } else {
                    statement.bindString(18, entity.getStatus());
                }
                statement.bindLong(19, entity.getStopTimeMinutes());
                statement.bindLong(20, entity.getDeliverySequence());
                if (entity.getPreprocessedImagePath() == null) {
                    statement.bindNull(21);
                } else {
                    statement.bindString(21, entity.getPreprocessedImagePath());
                }
                if (entity.getServiceType() == null) {
                    statement.bindNull(22);
                } else {
                    statement.bindString(22, entity.getServiceType());
                }
                statement.bindLong(23, entity.getId());
            }
        };
        this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM invoices";
            }
        };
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public long insert(final Invoice invoice) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long _result = this.__insertionAdapterOfInvoice.insertAndReturnId(invoice);
            this.__db.setTransactionSuccessful();
            return _result;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public void delete(final Invoice invoice) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfInvoice.handle(invoice);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public void update(final Invoice invoice) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfInvoice.handle(invoice);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public void deleteAll() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement _stmt = this.__preparedStmtOfDeleteAll.acquire();
        try {
            this.__db.beginTransaction();
            try {
                _stmt.executeUpdateDelete();
                this.__db.setTransactionSuccessful();
            } finally {
                this.__db.endTransaction();
            }
        } finally {
            this.__preparedStmtOfDeleteAll.release(_stmt);
        }
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public LiveData<List<Invoice>> getAllInvoices() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM invoices ORDER BY timestamp DESC", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"invoices"}, false, new Callable<List<Invoice>>() { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.5
            @Override // java.util.concurrent.Callable
            public List<Invoice> call() throws Exception {
                String _tmpInvoiceNumber;
                String _tmpInvoiceNumber2;
                String _tmpCustomerName;
                String _tmpAddress;
                String _tmpPhone;
                String _tmpItems;
                String _tmpPodImagePath1;
                String _tmpPodImagePath2;
                String _tmpPodImagePath3;
                String _tmpPodImagePath4;
                String _tmpPodImagePath5;
                String _tmpPodImagePath6;
                int _cursorIndexOfNotes;
                String _tmpNotes;
                int _cursorIndexOfOriginalImagePath;
                String _tmpOriginalImagePath;
                int _cursorIndexOfRawOcrText;
                String _tmpRawOcrText;
                int _cursorIndexOfStatus;
                String _tmpStatus;
                int _cursorIndexOfPreprocessedImagePath;
                String _tmpPreprocessedImagePath;
                int _cursorIndexOfServiceType;
                String _tmpServiceType;
                Cursor _cursor = DBUtil.query(InvoiceDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
                    int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
                    int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
                    int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
                    int _cursorIndexOfPodImagePath1 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath1");
                    int _cursorIndexOfPodImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath2");
                    int _cursorIndexOfPodImagePath3 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath3");
                    int _cursorIndexOfPodImagePath4 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath4");
                    int _cursorIndexOfPodImagePath5 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath5");
                    int _cursorIndexOfPodImagePath6 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath6");
                    int _cursorIndexOfSignatureImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signatureImagePath");
                    int _cursorIndexOfNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
                    int _cursorIndexOfOriginalImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "originalImagePath");
                    int _cursorIndexOfOriginalImagePath3 = _cursorIndexOfOriginalImagePath2;
                    int _cursorIndexOfRawOcrText2 = CursorUtil.getColumnIndexOrThrow(_cursor, "rawOcrText");
                    int _cursorIndexOfRawOcrText3 = _cursorIndexOfRawOcrText2;
                    int _cursorIndexOfInvoiceNumber2 = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
                    int _cursorIndexOfStatus2 = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                    int _cursorIndexOfStopTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "stopTimeMinutes");
                    int _cursorIndexOfStopTimeMinutes2 = _cursorIndexOfStopTimeMinutes;
                    int _cursorIndexOfDeliverySequence = CursorUtil.getColumnIndexOrThrow(_cursor, "deliverySequence");
                    int _cursorIndexOfDeliverySequence2 = _cursorIndexOfDeliverySequence;
                    int _cursorIndexOfPreprocessedImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "preprocessedImagePath");
                    int _cursorIndexOfPreprocessedImagePath3 = _cursorIndexOfPreprocessedImagePath2;
                    int _cursorIndexOfServiceType2 = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceType");
                    int _cursorIndexOfServiceType3 = _cursorIndexOfServiceType2;
                    int _cursorIndexOfNotes3 = _cursorIndexOfNotes2;
                    int _cursorIndexOfNotes4 = _cursor.getCount();
                    List<Invoice> _result = new ArrayList<>(_cursorIndexOfNotes4);
                    while (_cursor.moveToNext()) {
                        Invoice _item = new Invoice();
                        int _tmpId = _cursor.getInt(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfInvoiceNumber)) {
                            _tmpInvoiceNumber = null;
                        } else {
                            String _tmpInvoiceNumber3 = _cursor.getString(_cursorIndexOfInvoiceNumber);
                            _tmpInvoiceNumber = _tmpInvoiceNumber3;
                        }
                        _item.setInvoiceNumber(_tmpInvoiceNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpInvoiceNumber2 = null;
                        } else {
                            String _tmpCustomerName2 = _cursor.getString(_cursorIndexOfCustomerName);
                            _tmpInvoiceNumber2 = _tmpCustomerName2;
                        }
                        _item.setCustomerName(_tmpInvoiceNumber2);
                        if (_cursor.isNull(_cursorIndexOfAddress)) {
                            _tmpCustomerName = null;
                        } else {
                            String _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress);
                            _tmpCustomerName = _tmpAddress2;
                        }
                        _item.setAddress(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfPhone)) {
                            _tmpAddress = null;
                        } else {
                            String _tmpPhone2 = _cursor.getString(_cursorIndexOfPhone);
                            _tmpAddress = _tmpPhone2;
                        }
                        _item.setPhone(_tmpAddress);
                        if (_cursor.isNull(_cursorIndexOfItems)) {
                            _tmpPhone = null;
                        } else {
                            String _tmpItems2 = _cursor.getString(_cursorIndexOfItems);
                            _tmpPhone = _tmpItems2;
                        }
                        _item.setItems(_tmpPhone);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath1)) {
                            _tmpItems = null;
                        } else {
                            String _tmpPodImagePath12 = _cursor.getString(_cursorIndexOfPodImagePath1);
                            _tmpItems = _tmpPodImagePath12;
                        }
                        _item.setPodImagePath1(_tmpItems);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath2)) {
                            _tmpPodImagePath1 = null;
                        } else {
                            String _tmpPodImagePath22 = _cursor.getString(_cursorIndexOfPodImagePath2);
                            _tmpPodImagePath1 = _tmpPodImagePath22;
                        }
                        _item.setPodImagePath2(_tmpPodImagePath1);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath3)) {
                            _tmpPodImagePath2 = null;
                        } else {
                            String _tmpPodImagePath32 = _cursor.getString(_cursorIndexOfPodImagePath3);
                            _tmpPodImagePath2 = _tmpPodImagePath32;
                        }
                        _item.setPodImagePath3(_tmpPodImagePath2);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath4)) {
                            _tmpPodImagePath3 = null;
                        } else {
                            String _tmpPodImagePath42 = _cursor.getString(_cursorIndexOfPodImagePath4);
                            _tmpPodImagePath3 = _tmpPodImagePath42;
                        }
                        _item.setPodImagePath4(_tmpPodImagePath3);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath5)) {
                            _tmpPodImagePath4 = null;
                        } else {
                            String _tmpPodImagePath52 = _cursor.getString(_cursorIndexOfPodImagePath5);
                            _tmpPodImagePath4 = _tmpPodImagePath52;
                        }
                        _item.setPodImagePath5(_tmpPodImagePath4);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath6)) {
                            _tmpPodImagePath5 = null;
                        } else {
                            String _tmpPodImagePath62 = _cursor.getString(_cursorIndexOfPodImagePath6);
                            _tmpPodImagePath5 = _tmpPodImagePath62;
                        }
                        _item.setPodImagePath6(_tmpPodImagePath5);
                        if (_cursor.isNull(_cursorIndexOfSignatureImagePath)) {
                            _tmpPodImagePath6 = null;
                        } else {
                            String _tmpSignatureImagePath = _cursor.getString(_cursorIndexOfSignatureImagePath);
                            _tmpPodImagePath6 = _tmpSignatureImagePath;
                        }
                        _item.setSignatureImagePath(_tmpPodImagePath6);
                        int _cursorIndexOfNotes5 = _cursorIndexOfNotes3;
                        if (_cursor.isNull(_cursorIndexOfNotes5)) {
                            _cursorIndexOfNotes = _cursorIndexOfNotes5;
                            _tmpNotes = null;
                        } else {
                            String _tmpNotes2 = _cursor.getString(_cursorIndexOfNotes5);
                            _cursorIndexOfNotes = _cursorIndexOfNotes5;
                            _tmpNotes = _tmpNotes2;
                        }
                        _item.setNotes(_tmpNotes);
                        int _cursorIndexOfOriginalImagePath4 = _cursorIndexOfOriginalImagePath3;
                        if (_cursor.isNull(_cursorIndexOfOriginalImagePath4)) {
                            _cursorIndexOfOriginalImagePath = _cursorIndexOfOriginalImagePath4;
                            _tmpOriginalImagePath = null;
                        } else {
                            String _tmpOriginalImagePath2 = _cursor.getString(_cursorIndexOfOriginalImagePath4);
                            _cursorIndexOfOriginalImagePath = _cursorIndexOfOriginalImagePath4;
                            _tmpOriginalImagePath = _tmpOriginalImagePath2;
                        }
                        _item.setOriginalImagePath(_tmpOriginalImagePath);
                        int _cursorIndexOfRawOcrText4 = _cursorIndexOfRawOcrText3;
                        if (_cursor.isNull(_cursorIndexOfRawOcrText4)) {
                            _cursorIndexOfRawOcrText = _cursorIndexOfRawOcrText4;
                            _tmpRawOcrText = null;
                        } else {
                            String _tmpRawOcrText2 = _cursor.getString(_cursorIndexOfRawOcrText4);
                            _cursorIndexOfRawOcrText = _cursorIndexOfRawOcrText4;
                            _tmpRawOcrText = _tmpRawOcrText2;
                        }
                        _item.setRawOcrText(_tmpRawOcrText);
                        int _cursorIndexOfTimestamp = _cursorIndexOfInvoiceNumber2;
                        long _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
                        int _cursorIndexOfInvoiceNumber3 = _cursorIndexOfInvoiceNumber;
                        int _cursorIndexOfCustomerName2 = _cursorIndexOfCustomerName;
                        _item.setTimestamp(_tmpTimestamp);
                        int _cursorIndexOfTimestamp2 = _cursorIndexOfStatus3;
                        if (_cursor.isNull(_cursorIndexOfTimestamp2)) {
                            _cursorIndexOfStatus = _cursorIndexOfTimestamp2;
                            _tmpStatus = null;
                        } else {
                            String _tmpStatus2 = _cursor.getString(_cursorIndexOfTimestamp2);
                            _cursorIndexOfStatus = _cursorIndexOfTimestamp2;
                            _tmpStatus = _tmpStatus2;
                        }
                        _item.setStatus(_tmpStatus);
                        int _cursorIndexOfStopTimeMinutes3 = _cursorIndexOfStopTimeMinutes2;
                        _cursorIndexOfStopTimeMinutes2 = _cursorIndexOfStopTimeMinutes3;
                        _item.setStopTimeMinutes(_cursor.getInt(_cursorIndexOfStopTimeMinutes3));
                        int _tmpStopTimeMinutes = _cursorIndexOfDeliverySequence2;
                        _cursorIndexOfDeliverySequence2 = _tmpStopTimeMinutes;
                        _item.setDeliverySequence(_cursor.getInt(_tmpStopTimeMinutes));
                        int _tmpDeliverySequence = _cursorIndexOfPreprocessedImagePath3;
                        if (_cursor.isNull(_tmpDeliverySequence)) {
                            _cursorIndexOfPreprocessedImagePath = _tmpDeliverySequence;
                            _tmpPreprocessedImagePath = null;
                        } else {
                            String _tmpPreprocessedImagePath2 = _cursor.getString(_tmpDeliverySequence);
                            _cursorIndexOfPreprocessedImagePath = _tmpDeliverySequence;
                            _tmpPreprocessedImagePath = _tmpPreprocessedImagePath2;
                        }
                        _item.setPreprocessedImagePath(_tmpPreprocessedImagePath);
                        int _cursorIndexOfServiceType4 = _cursorIndexOfServiceType3;
                        if (_cursor.isNull(_cursorIndexOfServiceType4)) {
                            _cursorIndexOfServiceType = _cursorIndexOfServiceType4;
                            _tmpServiceType = null;
                        } else {
                            String _tmpServiceType2 = _cursor.getString(_cursorIndexOfServiceType4);
                            _cursorIndexOfServiceType = _cursorIndexOfServiceType4;
                            _tmpServiceType = _tmpServiceType2;
                        }
                        _item.setServiceType(_tmpServiceType);
                        _result.add(_item);
                        _cursorIndexOfInvoiceNumber = _cursorIndexOfInvoiceNumber3;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfNotes3 = _cursorIndexOfNotes;
                        _cursorIndexOfOriginalImagePath3 = _cursorIndexOfOriginalImagePath;
                        _cursorIndexOfRawOcrText3 = _cursorIndexOfRawOcrText;
                        _cursorIndexOfCustomerName = _cursorIndexOfCustomerName2;
                        _cursorIndexOfInvoiceNumber2 = _cursorIndexOfTimestamp;
                        _cursorIndexOfStatus3 = _cursorIndexOfStatus;
                        _cursorIndexOfPreprocessedImagePath3 = _cursorIndexOfPreprocessedImagePath;
                        _cursorIndexOfServiceType3 = _cursorIndexOfServiceType;
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

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public LiveData<Invoice> getInvoiceById(final int id) {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM invoices WHERE id = ?", 1);
        _statement.bindLong(1, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"invoices"}, false, new Callable<Invoice>() { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Invoice call() throws Exception {
                Invoice _result;
                String _tmpInvoiceNumber;
                String _tmpInvoiceNumber2;
                String _tmpCustomerName;
                String _tmpAddress;
                String _tmpPhone;
                String _tmpItems;
                String _tmpPodImagePath1;
                String _tmpPodImagePath2;
                String _tmpPodImagePath3;
                String _tmpPodImagePath4;
                String _tmpPodImagePath5;
                String _tmpPodImagePath6;
                String _tmpSignatureImagePath;
                String _tmpOriginalImagePath;
                String _tmpRawOcrText;
                String _tmpStatus;
                String _tmpPreprocessedImagePath;
                String _tmpServiceType;
                Cursor _cursor = DBUtil.query(InvoiceDao_Impl.this.__db, _statement, false, null);
                try {
                    int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
                    int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
                    int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
                    int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
                    int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
                    int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
                    int _cursorIndexOfPodImagePath1 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath1");
                    int _cursorIndexOfPodImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath2");
                    int _cursorIndexOfPodImagePath3 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath3");
                    int _cursorIndexOfPodImagePath4 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath4");
                    int _cursorIndexOfPodImagePath5 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath5");
                    int _cursorIndexOfPodImagePath6 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath6");
                    int _cursorIndexOfSignatureImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signatureImagePath");
                    int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
                    int _cursorIndexOfOriginalImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "originalImagePath");
                    int _cursorIndexOfRawOcrText = CursorUtil.getColumnIndexOrThrow(_cursor, "rawOcrText");
                    int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStopTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "stopTimeMinutes");
                    int _cursorIndexOfDeliverySequence = CursorUtil.getColumnIndexOrThrow(_cursor, "deliverySequence");
                    int _cursorIndexOfPreprocessedImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "preprocessedImagePath");
                    int _cursorIndexOfServiceType = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceType");
                    if (_cursor.moveToFirst()) {
                        Invoice _result2 = new Invoice();
                        int _tmpId = _cursor.getInt(_cursorIndexOfId);
                        _result2.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfInvoiceNumber)) {
                            _tmpInvoiceNumber = null;
                        } else {
                            String _tmpInvoiceNumber3 = _cursor.getString(_cursorIndexOfInvoiceNumber);
                            _tmpInvoiceNumber = _tmpInvoiceNumber3;
                        }
                        _result2.setInvoiceNumber(_tmpInvoiceNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpInvoiceNumber2 = null;
                        } else {
                            String _tmpCustomerName2 = _cursor.getString(_cursorIndexOfCustomerName);
                            _tmpInvoiceNumber2 = _tmpCustomerName2;
                        }
                        _result2.setCustomerName(_tmpInvoiceNumber2);
                        if (_cursor.isNull(_cursorIndexOfAddress)) {
                            _tmpCustomerName = null;
                        } else {
                            String _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress);
                            _tmpCustomerName = _tmpAddress2;
                        }
                        _result2.setAddress(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfPhone)) {
                            _tmpAddress = null;
                        } else {
                            String _tmpPhone2 = _cursor.getString(_cursorIndexOfPhone);
                            _tmpAddress = _tmpPhone2;
                        }
                        _result2.setPhone(_tmpAddress);
                        if (_cursor.isNull(_cursorIndexOfItems)) {
                            _tmpPhone = null;
                        } else {
                            String _tmpItems2 = _cursor.getString(_cursorIndexOfItems);
                            _tmpPhone = _tmpItems2;
                        }
                        _result2.setItems(_tmpPhone);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath1)) {
                            _tmpItems = null;
                        } else {
                            String _tmpPodImagePath12 = _cursor.getString(_cursorIndexOfPodImagePath1);
                            _tmpItems = _tmpPodImagePath12;
                        }
                        _result2.setPodImagePath1(_tmpItems);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath2)) {
                            _tmpPodImagePath1 = null;
                        } else {
                            String _tmpPodImagePath22 = _cursor.getString(_cursorIndexOfPodImagePath2);
                            _tmpPodImagePath1 = _tmpPodImagePath22;
                        }
                        _result2.setPodImagePath2(_tmpPodImagePath1);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath3)) {
                            _tmpPodImagePath2 = null;
                        } else {
                            String _tmpPodImagePath32 = _cursor.getString(_cursorIndexOfPodImagePath3);
                            _tmpPodImagePath2 = _tmpPodImagePath32;
                        }
                        _result2.setPodImagePath3(_tmpPodImagePath2);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath4)) {
                            _tmpPodImagePath3 = null;
                        } else {
                            String _tmpPodImagePath42 = _cursor.getString(_cursorIndexOfPodImagePath4);
                            _tmpPodImagePath3 = _tmpPodImagePath42;
                        }
                        _result2.setPodImagePath4(_tmpPodImagePath3);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath5)) {
                            _tmpPodImagePath4 = null;
                        } else {
                            String _tmpPodImagePath52 = _cursor.getString(_cursorIndexOfPodImagePath5);
                            _tmpPodImagePath4 = _tmpPodImagePath52;
                        }
                        _result2.setPodImagePath5(_tmpPodImagePath4);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath6)) {
                            _tmpPodImagePath5 = null;
                        } else {
                            String _tmpPodImagePath62 = _cursor.getString(_cursorIndexOfPodImagePath6);
                            _tmpPodImagePath5 = _tmpPodImagePath62;
                        }
                        _result2.setPodImagePath6(_tmpPodImagePath5);
                        if (_cursor.isNull(_cursorIndexOfSignatureImagePath)) {
                            _tmpPodImagePath6 = null;
                        } else {
                            String _tmpSignatureImagePath2 = _cursor.getString(_cursorIndexOfSignatureImagePath);
                            _tmpPodImagePath6 = _tmpSignatureImagePath2;
                        }
                        _result2.setSignatureImagePath(_tmpPodImagePath6);
                        if (_cursor.isNull(_cursorIndexOfNotes)) {
                            _tmpSignatureImagePath = null;
                        } else {
                            String _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
                            _tmpSignatureImagePath = _tmpNotes;
                        }
                        _result2.setNotes(_tmpSignatureImagePath);
                        if (_cursor.isNull(_cursorIndexOfOriginalImagePath)) {
                            _tmpOriginalImagePath = null;
                        } else {
                            String _tmpOriginalImagePath2 = _cursor.getString(_cursorIndexOfOriginalImagePath);
                            _tmpOriginalImagePath = _tmpOriginalImagePath2;
                        }
                        _result2.setOriginalImagePath(_tmpOriginalImagePath);
                        if (_cursor.isNull(_cursorIndexOfRawOcrText)) {
                            _tmpRawOcrText = null;
                        } else {
                            String _tmpRawOcrText2 = _cursor.getString(_cursorIndexOfRawOcrText);
                            _tmpRawOcrText = _tmpRawOcrText2;
                        }
                        _result2.setRawOcrText(_tmpRawOcrText);
                        long _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
                        _result2.setTimestamp(_tmpTimestamp);
                        if (_cursor.isNull(_cursorIndexOfStatus)) {
                            _tmpStatus = null;
                        } else {
                            String _tmpStatus2 = _cursor.getString(_cursorIndexOfStatus);
                            _tmpStatus = _tmpStatus2;
                        }
                        _result2.setStatus(_tmpStatus);
                        int _tmpStopTimeMinutes = _cursor.getInt(_cursorIndexOfStopTimeMinutes);
                        _result2.setStopTimeMinutes(_tmpStopTimeMinutes);
                        int _tmpDeliverySequence = _cursor.getInt(_cursorIndexOfDeliverySequence);
                        _result2.setDeliverySequence(_tmpDeliverySequence);
                        if (_cursor.isNull(_cursorIndexOfPreprocessedImagePath)) {
                            _tmpPreprocessedImagePath = null;
                        } else {
                            String _tmpPreprocessedImagePath2 = _cursor.getString(_cursorIndexOfPreprocessedImagePath);
                            _tmpPreprocessedImagePath = _tmpPreprocessedImagePath2;
                        }
                        _result2.setPreprocessedImagePath(_tmpPreprocessedImagePath);
                        if (_cursor.isNull(_cursorIndexOfServiceType)) {
                            _tmpServiceType = null;
                        } else {
                            String _tmpServiceType2 = _cursor.getString(_cursorIndexOfServiceType);
                            _tmpServiceType = _tmpServiceType2;
                        }
                        _result2.setServiceType(_tmpServiceType);
                        _result = _result2;
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

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public Invoice getInvoiceByIdSync(final int id) {
        RoomSQLiteQuery _statement;
        Invoice _result;
        String _tmpInvoiceNumber;
        String _tmpInvoiceNumber2;
        String _tmpCustomerName;
        String _tmpAddress;
        String _tmpPhone;
        String _tmpItems;
        String _tmpPodImagePath1;
        String _tmpPodImagePath2;
        String _tmpPodImagePath3;
        String _tmpPodImagePath5;
        String _tmpPodImagePath52;
        String _tmpPodImagePath6;
        String _tmpSignatureImagePath;
        String _tmpOriginalImagePath;
        String _tmpRawOcrText;
        String _tmpStatus;
        String _tmpPreprocessedImagePath;
        String _tmpServiceType;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM invoices WHERE id = ?", 1);
        _statement2.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement2, false, null);
        try {
            int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
            int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
            int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
            int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
            int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
            int _cursorIndexOfPodImagePath1 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath1");
            int _cursorIndexOfPodImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath2");
            int _cursorIndexOfPodImagePath3 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath3");
            int _cursorIndexOfPodImagePath4 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath4");
            int _cursorIndexOfPodImagePath5 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath5");
            try {
                int _cursorIndexOfPodImagePath6 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath6");
                try {
                    int _cursorIndexOfSignatureImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signatureImagePath");
                    int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
                    _statement = _statement2;
                    try {
                        int _cursorIndexOfOriginalImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "originalImagePath");
                        int _cursorIndexOfRawOcrText = CursorUtil.getColumnIndexOrThrow(_cursor, "rawOcrText");
                        int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
                        int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                        int _cursorIndexOfStopTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "stopTimeMinutes");
                        int _cursorIndexOfDeliverySequence = CursorUtil.getColumnIndexOrThrow(_cursor, "deliverySequence");
                        int _cursorIndexOfPreprocessedImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "preprocessedImagePath");
                        int _cursorIndexOfServiceType = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceType");
                        if (_cursor.moveToFirst()) {
                            Invoice _result2 = new Invoice();
                            int _tmpId = _cursor.getInt(_cursorIndexOfId);
                            _result2.setId(_tmpId);
                            if (_cursor.isNull(_cursorIndexOfInvoiceNumber)) {
                                _tmpInvoiceNumber = null;
                            } else {
                                String _tmpInvoiceNumber3 = _cursor.getString(_cursorIndexOfInvoiceNumber);
                                _tmpInvoiceNumber = _tmpInvoiceNumber3;
                            }
                            _result2.setInvoiceNumber(_tmpInvoiceNumber);
                            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                                _tmpInvoiceNumber2 = null;
                            } else {
                                String _tmpCustomerName2 = _cursor.getString(_cursorIndexOfCustomerName);
                                _tmpInvoiceNumber2 = _tmpCustomerName2;
                            }
                            _result2.setCustomerName(_tmpInvoiceNumber2);
                            if (_cursor.isNull(_cursorIndexOfAddress)) {
                                _tmpCustomerName = null;
                            } else {
                                String _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress);
                                _tmpCustomerName = _tmpAddress2;
                            }
                            _result2.setAddress(_tmpCustomerName);
                            if (_cursor.isNull(_cursorIndexOfPhone)) {
                                _tmpAddress = null;
                            } else {
                                String _tmpPhone2 = _cursor.getString(_cursorIndexOfPhone);
                                _tmpAddress = _tmpPhone2;
                            }
                            _result2.setPhone(_tmpAddress);
                            if (_cursor.isNull(_cursorIndexOfItems)) {
                                _tmpPhone = null;
                            } else {
                                String _tmpItems2 = _cursor.getString(_cursorIndexOfItems);
                                _tmpPhone = _tmpItems2;
                            }
                            _result2.setItems(_tmpPhone);
                            if (_cursor.isNull(_cursorIndexOfPodImagePath1)) {
                                _tmpItems = null;
                            } else {
                                String _tmpPodImagePath12 = _cursor.getString(_cursorIndexOfPodImagePath1);
                                _tmpItems = _tmpPodImagePath12;
                            }
                            _result2.setPodImagePath1(_tmpItems);
                            if (_cursor.isNull(_cursorIndexOfPodImagePath2)) {
                                _tmpPodImagePath1 = null;
                            } else {
                                String _tmpPodImagePath22 = _cursor.getString(_cursorIndexOfPodImagePath2);
                                _tmpPodImagePath1 = _tmpPodImagePath22;
                            }
                            _result2.setPodImagePath2(_tmpPodImagePath1);
                            if (_cursor.isNull(_cursorIndexOfPodImagePath3)) {
                                _tmpPodImagePath2 = null;
                            } else {
                                String _tmpPodImagePath32 = _cursor.getString(_cursorIndexOfPodImagePath3);
                                _tmpPodImagePath2 = _tmpPodImagePath32;
                            }
                            _result2.setPodImagePath3(_tmpPodImagePath2);
                            if (_cursor.isNull(_cursorIndexOfPodImagePath4)) {
                                _tmpPodImagePath3 = null;
                            } else {
                                String _tmpPodImagePath4 = _cursor.getString(_cursorIndexOfPodImagePath4);
                                _tmpPodImagePath3 = _tmpPodImagePath4;
                            }
                            _result2.setPodImagePath4(_tmpPodImagePath3);
                            if (_cursor.isNull(_cursorIndexOfPodImagePath5)) {
                                _tmpPodImagePath5 = null;
                            } else {
                                String _tmpPodImagePath53 = _cursor.getString(_cursorIndexOfPodImagePath5);
                                _tmpPodImagePath5 = _tmpPodImagePath53;
                            }
                            _result2.setPodImagePath5(_tmpPodImagePath5);
                            if (_cursor.isNull(_cursorIndexOfPodImagePath6)) {
                                _tmpPodImagePath52 = null;
                            } else {
                                String _tmpPodImagePath62 = _cursor.getString(_cursorIndexOfPodImagePath6);
                                _tmpPodImagePath52 = _tmpPodImagePath62;
                            }
                            _result2.setPodImagePath6(_tmpPodImagePath52);
                            if (_cursor.isNull(_cursorIndexOfSignatureImagePath)) {
                                _tmpPodImagePath6 = null;
                            } else {
                                String _tmpSignatureImagePath2 = _cursor.getString(_cursorIndexOfSignatureImagePath);
                                _tmpPodImagePath6 = _tmpSignatureImagePath2;
                            }
                            _result2.setSignatureImagePath(_tmpPodImagePath6);
                            if (_cursor.isNull(_cursorIndexOfNotes)) {
                                _tmpSignatureImagePath = null;
                            } else {
                                String _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
                                _tmpSignatureImagePath = _tmpNotes;
                            }
                            _result2.setNotes(_tmpSignatureImagePath);
                            if (_cursor.isNull(_cursorIndexOfOriginalImagePath)) {
                                _tmpOriginalImagePath = null;
                            } else {
                                String _tmpOriginalImagePath2 = _cursor.getString(_cursorIndexOfOriginalImagePath);
                                _tmpOriginalImagePath = _tmpOriginalImagePath2;
                            }
                            _result2.setOriginalImagePath(_tmpOriginalImagePath);
                            if (_cursor.isNull(_cursorIndexOfRawOcrText)) {
                                _tmpRawOcrText = null;
                            } else {
                                String _tmpRawOcrText2 = _cursor.getString(_cursorIndexOfRawOcrText);
                                _tmpRawOcrText = _tmpRawOcrText2;
                            }
                            _result2.setRawOcrText(_tmpRawOcrText);
                            long _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
                            _result2.setTimestamp(_tmpTimestamp);
                            if (_cursor.isNull(_cursorIndexOfStatus)) {
                                _tmpStatus = null;
                            } else {
                                _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
                            }
                            _result2.setStatus(_tmpStatus);
                            int _tmpStopTimeMinutes = _cursor.getInt(_cursorIndexOfStopTimeMinutes);
                            _result2.setStopTimeMinutes(_tmpStopTimeMinutes);
                            int _tmpDeliverySequence = _cursor.getInt(_cursorIndexOfDeliverySequence);
                            _result2.setDeliverySequence(_tmpDeliverySequence);
                            if (_cursor.isNull(_cursorIndexOfPreprocessedImagePath)) {
                                _tmpPreprocessedImagePath = null;
                            } else {
                                String _tmpPreprocessedImagePath2 = _cursor.getString(_cursorIndexOfPreprocessedImagePath);
                                _tmpPreprocessedImagePath = _tmpPreprocessedImagePath2;
                            }
                            _result2.setPreprocessedImagePath(_tmpPreprocessedImagePath);
                            if (_cursor.isNull(_cursorIndexOfServiceType)) {
                                _tmpServiceType = null;
                            } else {
                                String _tmpServiceType2 = _cursor.getString(_cursorIndexOfServiceType);
                                _tmpServiceType = _tmpServiceType2;
                            }
                            _result2.setServiceType(_tmpServiceType);
                            _result = _result2;
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

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public List<Invoice> getAllInvoicesSync() {
        RoomSQLiteQuery _statement;
        String _tmpInvoiceNumber;
        String _tmpInvoiceNumber2;
        String _tmpCustomerName;
        String _tmpAddress;
        String _tmpPhone;
        String _tmpItems;
        String _tmpPodImagePath1;
        String _tmpPodImagePath2;
        String _tmpPodImagePath3;
        String _tmpPodImagePath4;
        String _tmpPodImagePath5;
        String _tmpPodImagePath6;
        int _cursorIndexOfNotes;
        String _tmpNotes;
        int _cursorIndexOfOriginalImagePath;
        String _tmpOriginalImagePath;
        int _cursorIndexOfRawOcrText;
        String _tmpRawOcrText;
        String _tmpStatus;
        int _cursorIndexOfPreprocessedImagePath;
        String _tmpPreprocessedImagePath;
        int _cursorIndexOfServiceType;
        String _tmpServiceType;
        RoomSQLiteQuery _statement2 = RoomSQLiteQuery.acquire("SELECT * FROM invoices ORDER BY timestamp DESC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement2, false, null);
        try {
            int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
            int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
            int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
            int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
            int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
            int _cursorIndexOfPodImagePath1 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath1");
            int _cursorIndexOfPodImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath2");
            int _cursorIndexOfPodImagePath3 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath3");
            int _cursorIndexOfPodImagePath4 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath4");
            int _cursorIndexOfPodImagePath5 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath5");
            int _cursorIndexOfPodImagePath6 = CursorUtil.getColumnIndexOrThrow(_cursor, "podImagePath6");
            int _cursorIndexOfSignatureImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "signatureImagePath");
            try {
                int _cursorIndexOfNotes2 = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
                _statement = _statement2;
                try {
                    int _cursorIndexOfOriginalImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "originalImagePath");
                    int _cursorIndexOfOriginalImagePath3 = _cursorIndexOfOriginalImagePath2;
                    int _cursorIndexOfRawOcrText2 = CursorUtil.getColumnIndexOrThrow(_cursor, "rawOcrText");
                    int _cursorIndexOfRawOcrText3 = _cursorIndexOfRawOcrText2;
                    int _cursorIndexOfSignatureImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
                    int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, NotificationCompat.CATEGORY_STATUS);
                    int _cursorIndexOfStatus2 = _cursorIndexOfStatus;
                    int _cursorIndexOfStopTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "stopTimeMinutes");
                    int _cursorIndexOfStopTimeMinutes2 = _cursorIndexOfStopTimeMinutes;
                    int _cursorIndexOfDeliverySequence = CursorUtil.getColumnIndexOrThrow(_cursor, "deliverySequence");
                    int _cursorIndexOfDeliverySequence2 = _cursorIndexOfDeliverySequence;
                    int _cursorIndexOfPreprocessedImagePath2 = CursorUtil.getColumnIndexOrThrow(_cursor, "preprocessedImagePath");
                    int _cursorIndexOfPreprocessedImagePath3 = _cursorIndexOfPreprocessedImagePath2;
                    int _cursorIndexOfServiceType2 = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceType");
                    int _cursorIndexOfServiceType3 = _cursorIndexOfServiceType2;
                    int _cursorIndexOfNotes3 = _cursorIndexOfNotes2;
                    int _cursorIndexOfNotes4 = _cursor.getCount();
                    List<Invoice> _result = new ArrayList<>(_cursorIndexOfNotes4);
                    while (_cursor.moveToNext()) {
                        Invoice _item = new Invoice();
                        int _tmpId = _cursor.getInt(_cursorIndexOfId);
                        int _cursorIndexOfId2 = _cursorIndexOfId;
                        _item.setId(_tmpId);
                        if (_cursor.isNull(_cursorIndexOfInvoiceNumber)) {
                            _tmpInvoiceNumber = null;
                        } else {
                            String _tmpInvoiceNumber3 = _cursor.getString(_cursorIndexOfInvoiceNumber);
                            _tmpInvoiceNumber = _tmpInvoiceNumber3;
                        }
                        _item.setInvoiceNumber(_tmpInvoiceNumber);
                        if (_cursor.isNull(_cursorIndexOfCustomerName)) {
                            _tmpInvoiceNumber2 = null;
                        } else {
                            String _tmpCustomerName2 = _cursor.getString(_cursorIndexOfCustomerName);
                            _tmpInvoiceNumber2 = _tmpCustomerName2;
                        }
                        _item.setCustomerName(_tmpInvoiceNumber2);
                        if (_cursor.isNull(_cursorIndexOfAddress)) {
                            _tmpCustomerName = null;
                        } else {
                            String _tmpAddress2 = _cursor.getString(_cursorIndexOfAddress);
                            _tmpCustomerName = _tmpAddress2;
                        }
                        _item.setAddress(_tmpCustomerName);
                        if (_cursor.isNull(_cursorIndexOfPhone)) {
                            _tmpAddress = null;
                        } else {
                            String _tmpPhone2 = _cursor.getString(_cursorIndexOfPhone);
                            _tmpAddress = _tmpPhone2;
                        }
                        _item.setPhone(_tmpAddress);
                        if (_cursor.isNull(_cursorIndexOfItems)) {
                            _tmpPhone = null;
                        } else {
                            String _tmpItems2 = _cursor.getString(_cursorIndexOfItems);
                            _tmpPhone = _tmpItems2;
                        }
                        _item.setItems(_tmpPhone);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath1)) {
                            _tmpItems = null;
                        } else {
                            String _tmpPodImagePath12 = _cursor.getString(_cursorIndexOfPodImagePath1);
                            _tmpItems = _tmpPodImagePath12;
                        }
                        _item.setPodImagePath1(_tmpItems);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath2)) {
                            _tmpPodImagePath1 = null;
                        } else {
                            String _tmpPodImagePath22 = _cursor.getString(_cursorIndexOfPodImagePath2);
                            _tmpPodImagePath1 = _tmpPodImagePath22;
                        }
                        _item.setPodImagePath2(_tmpPodImagePath1);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath3)) {
                            _tmpPodImagePath2 = null;
                        } else {
                            String _tmpPodImagePath32 = _cursor.getString(_cursorIndexOfPodImagePath3);
                            _tmpPodImagePath2 = _tmpPodImagePath32;
                        }
                        _item.setPodImagePath3(_tmpPodImagePath2);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath4)) {
                            _tmpPodImagePath3 = null;
                        } else {
                            String _tmpPodImagePath42 = _cursor.getString(_cursorIndexOfPodImagePath4);
                            _tmpPodImagePath3 = _tmpPodImagePath42;
                        }
                        _item.setPodImagePath4(_tmpPodImagePath3);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath5)) {
                            _tmpPodImagePath4 = null;
                        } else {
                            String _tmpPodImagePath52 = _cursor.getString(_cursorIndexOfPodImagePath5);
                            _tmpPodImagePath4 = _tmpPodImagePath52;
                        }
                        _item.setPodImagePath5(_tmpPodImagePath4);
                        if (_cursor.isNull(_cursorIndexOfPodImagePath6)) {
                            _tmpPodImagePath5 = null;
                        } else {
                            String _tmpPodImagePath62 = _cursor.getString(_cursorIndexOfPodImagePath6);
                            _tmpPodImagePath5 = _tmpPodImagePath62;
                        }
                        _item.setPodImagePath6(_tmpPodImagePath5);
                        if (_cursor.isNull(_cursorIndexOfSignatureImagePath)) {
                            _tmpPodImagePath6 = null;
                        } else {
                            String _tmpSignatureImagePath = _cursor.getString(_cursorIndexOfSignatureImagePath);
                            _tmpPodImagePath6 = _tmpSignatureImagePath;
                        }
                        _item.setSignatureImagePath(_tmpPodImagePath6);
                        int _cursorIndexOfNotes5 = _cursorIndexOfNotes3;
                        if (_cursor.isNull(_cursorIndexOfNotes5)) {
                            _cursorIndexOfNotes = _cursorIndexOfNotes5;
                            _tmpNotes = null;
                        } else {
                            String _tmpNotes2 = _cursor.getString(_cursorIndexOfNotes5);
                            _cursorIndexOfNotes = _cursorIndexOfNotes5;
                            _tmpNotes = _tmpNotes2;
                        }
                        _item.setNotes(_tmpNotes);
                        int _cursorIndexOfOriginalImagePath4 = _cursorIndexOfOriginalImagePath3;
                        if (_cursor.isNull(_cursorIndexOfOriginalImagePath4)) {
                            _cursorIndexOfOriginalImagePath = _cursorIndexOfOriginalImagePath4;
                            _tmpOriginalImagePath = null;
                        } else {
                            String _tmpOriginalImagePath2 = _cursor.getString(_cursorIndexOfOriginalImagePath4);
                            _cursorIndexOfOriginalImagePath = _cursorIndexOfOriginalImagePath4;
                            _tmpOriginalImagePath = _tmpOriginalImagePath2;
                        }
                        _item.setOriginalImagePath(_tmpOriginalImagePath);
                        int _cursorIndexOfRawOcrText4 = _cursorIndexOfRawOcrText3;
                        if (_cursor.isNull(_cursorIndexOfRawOcrText4)) {
                            _cursorIndexOfRawOcrText = _cursorIndexOfRawOcrText4;
                            _tmpRawOcrText = null;
                        } else {
                            String _tmpRawOcrText2 = _cursor.getString(_cursorIndexOfRawOcrText4);
                            _cursorIndexOfRawOcrText = _cursorIndexOfRawOcrText4;
                            _tmpRawOcrText = _tmpRawOcrText2;
                        }
                        _item.setRawOcrText(_tmpRawOcrText);
                        int _cursorIndexOfTimestamp = _cursorIndexOfSignatureImagePath2;
                        long _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
                        int _cursorIndexOfSignatureImagePath3 = _cursorIndexOfSignatureImagePath;
                        _item.setTimestamp(_tmpTimestamp);
                        int _cursorIndexOfStatus3 = _cursorIndexOfStatus2;
                        if (_cursor.isNull(_cursorIndexOfStatus3)) {
                            _tmpStatus = null;
                        } else {
                            _tmpStatus = _cursor.getString(_cursorIndexOfStatus3);
                        }
                        _item.setStatus(_tmpStatus);
                        _cursorIndexOfStatus2 = _cursorIndexOfStatus3;
                        int _cursorIndexOfStatus4 = _cursorIndexOfStopTimeMinutes2;
                        _cursorIndexOfStopTimeMinutes2 = _cursorIndexOfStatus4;
                        _item.setStopTimeMinutes(_cursor.getInt(_cursorIndexOfStatus4));
                        int _tmpStopTimeMinutes = _cursorIndexOfDeliverySequence2;
                        _cursorIndexOfDeliverySequence2 = _tmpStopTimeMinutes;
                        _item.setDeliverySequence(_cursor.getInt(_tmpStopTimeMinutes));
                        int _tmpDeliverySequence = _cursorIndexOfPreprocessedImagePath3;
                        if (_cursor.isNull(_tmpDeliverySequence)) {
                            _cursorIndexOfPreprocessedImagePath = _tmpDeliverySequence;
                            _tmpPreprocessedImagePath = null;
                        } else {
                            String _tmpPreprocessedImagePath2 = _cursor.getString(_tmpDeliverySequence);
                            _cursorIndexOfPreprocessedImagePath = _tmpDeliverySequence;
                            _tmpPreprocessedImagePath = _tmpPreprocessedImagePath2;
                        }
                        _item.setPreprocessedImagePath(_tmpPreprocessedImagePath);
                        int _cursorIndexOfServiceType4 = _cursorIndexOfServiceType3;
                        if (_cursor.isNull(_cursorIndexOfServiceType4)) {
                            _cursorIndexOfServiceType = _cursorIndexOfServiceType4;
                            _tmpServiceType = null;
                        } else {
                            String _tmpServiceType2 = _cursor.getString(_cursorIndexOfServiceType4);
                            _cursorIndexOfServiceType = _cursorIndexOfServiceType4;
                            _tmpServiceType = _tmpServiceType2;
                        }
                        _item.setServiceType(_tmpServiceType);
                        _result.add(_item);
                        _cursorIndexOfSignatureImagePath = _cursorIndexOfSignatureImagePath3;
                        _cursorIndexOfId = _cursorIndexOfId2;
                        _cursorIndexOfNotes3 = _cursorIndexOfNotes;
                        _cursorIndexOfOriginalImagePath3 = _cursorIndexOfOriginalImagePath;
                        _cursorIndexOfRawOcrText3 = _cursorIndexOfRawOcrText;
                        _cursorIndexOfSignatureImagePath2 = _cursorIndexOfTimestamp;
                        _cursorIndexOfPreprocessedImagePath3 = _cursorIndexOfPreprocessedImagePath;
                        _cursorIndexOfServiceType3 = _cursorIndexOfServiceType;
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
    }

    @Override // com.mobileinvoice.ocr.database.InvoiceDao
    public LiveData<Integer> getInvoiceCount() {
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM invoices", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"invoices"}, false, new Callable<Integer>() { // from class: com.mobileinvoice.ocr.database.InvoiceDao_Impl.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                Integer _result;
                Cursor _cursor = DBUtil.query(InvoiceDao_Impl.this.__db, _statement, false, null);
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

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
