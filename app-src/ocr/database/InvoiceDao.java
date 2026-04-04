package com.mobileinvoice.ocr.database;

import androidx.lifecycle.LiveData;
import java.util.List;

/* loaded from: classes11.dex */
public interface InvoiceDao {
    void delete(Invoice invoice);

    void deleteAll();

    LiveData<List<Invoice>> getAllInvoices();

    List<Invoice> getAllInvoicesSync();

    LiveData<Invoice> getInvoiceById(int id);

    Invoice getInvoiceByIdSync(int id);

    LiveData<Integer> getInvoiceCount();

    long insert(Invoice invoice);

    void update(Invoice invoice);
}
