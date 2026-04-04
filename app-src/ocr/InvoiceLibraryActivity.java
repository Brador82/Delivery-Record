package com.mobileinvoice.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mobileinvoice.ocr.InvoiceLibraryAdapter;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityInvoiceLibraryBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes7.dex */
public class InvoiceLibraryActivity extends BaseActivity implements InvoiceLibraryAdapter.OnInvoiceImageClickListener {
    private InvoiceLibraryAdapter adapter;
    private ActivityInvoiceLibraryBinding binding;
    private InvoiceDatabase database;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityInvoiceLibraryBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceLibraryActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InvoiceLibraryActivity.this.lambda$onCreate$0(view);
            }
        });
        this.database = InvoiceDatabase.getInstance(this);
        this.adapter = new InvoiceLibraryAdapter(this);
        this.binding.recyclerInvoices.setLayoutManager(new GridLayoutManager(this, 2));
        this.binding.recyclerInvoices.setAdapter(this.adapter);
        loadInvoices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View v) {
        finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        loadInvoices();
    }

    private void loadInvoices() {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceLibraryActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                InvoiceLibraryActivity.this.lambda$loadInvoices$2();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoices$2() {
        List<Invoice> allInvoices = this.database.invoiceDao().getAllInvoicesSync();
        final List<Invoice> withImages = new ArrayList<>();
        for (Invoice inv : allInvoices) {
            String path = inv.getOriginalImagePath();
            if (path != null && !path.isEmpty() && new File(path).exists()) {
                withImages.add(inv);
            }
        }
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceLibraryActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                InvoiceLibraryActivity.this.lambda$loadInvoices$1(withImages);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoices$1(List withImages) {
        this.adapter.setInvoices(withImages);
        this.binding.tvEmpty.setVisibility(withImages.isEmpty() ? 0 : 8);
        this.binding.recyclerInvoices.setVisibility(withImages.isEmpty() ? 8 : 0);
    }

    @Override // com.mobileinvoice.ocr.InvoiceLibraryAdapter.OnInvoiceImageClickListener
    public void onInvoiceImageClick(Invoice invoice) {
        Intent intent = new Intent(this, (Class<?>) ManualExtractionActivity.class);
        intent.putExtra("invoice_id", invoice.getId());
        startActivity(intent);
    }
}
