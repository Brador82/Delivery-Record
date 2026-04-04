package com.mobileinvoice.ocr;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.databinding.ItemInvoiceLibraryBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes7.dex */
public class InvoiceLibraryAdapter extends RecyclerView.Adapter<InvoiceLibraryAdapter.ViewHolder> {
    private List<Invoice> invoices = new ArrayList();
    private final OnInvoiceImageClickListener listener;

    public interface OnInvoiceImageClickListener {
        void onInvoiceImageClick(Invoice invoice);
    }

    public InvoiceLibraryAdapter(OnInvoiceImageClickListener listener) {
        this.listener = listener;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemInvoiceLibraryBinding binding = ItemInvoiceLibraryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(this.invoices.get(position));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.invoices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemInvoiceLibraryBinding binding;

        ViewHolder(ItemInvoiceLibraryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Invoice invoice) {
            String imagePath = invoice.getOriginalImagePath();
            if (imagePath != null && new File(imagePath).exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                this.binding.ivInvoiceImage.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
            }
            this.binding.tvInvoiceNumber.setText(invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "INV-" + invoice.getId());
            this.binding.tvCustomerName.setText(invoice.getCustomerName() != null ? invoice.getCustomerName() : "Unknown");
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceLibraryAdapter$ViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceLibraryAdapter.ViewHolder.this.lambda$bind$0(invoice, view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bind$0(Invoice invoice, View v) {
            if (InvoiceLibraryAdapter.this.listener != null) {
                InvoiceLibraryAdapter.this.listener.onInvoiceImageClick(invoice);
            }
        }
    }
}
