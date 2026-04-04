package com.mobileinvoice.ocr;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.databinding.ItemInvoiceBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes7.dex */
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {
    private List<Invoice> invoices = new ArrayList();
    private final OnInvoiceClickListener listener;

    public interface OnInvoiceClickListener {
        void onDelete(Invoice invoice);

        void onDeliveryCompleteChanged(Invoice invoice, boolean isComplete);

        void onOrderChanged(List<Invoice> reorderedList);

        void onServiceTypeChanged(Invoice invoice, String serviceType);

        void onViewDetails(Invoice invoice);
    }

    public InvoiceAdapter(OnInvoiceClickListener listener) {
        this.listener = listener;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
        notifyDataSetChanged();
    }

    public List<Invoice> getInvoices() {
        return this.invoices;
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.invoices, i, i + 1);
            }
        } else {
            for (int i2 = fromPosition; i2 > toPosition; i2--) {
                Collections.swap(this.invoices, i2, i2 - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onItemMoveComplete() {
        if (this.listener != null) {
            this.listener.onOrderChanged(new ArrayList(this.invoices));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemInvoiceBinding binding = ItemInvoiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                false);
        return new InvoiceViewHolder(binding);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(InvoiceViewHolder holder, int position) {
        holder.bind(this.invoices.get(position), position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.invoices.size();
    }

    class InvoiceViewHolder extends RecyclerView.ViewHolder {
        private static final int[] MARBLE_CARDS = { R.drawable.bg_marble_card, R.drawable.bg_marble_card_2,
                R.drawable.bg_marble_card_3 };
        private final ItemInvoiceBinding binding;

        InvoiceViewHolder(ItemInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Invoice invoice, int position) {
            applyThemeStyling(position);
            this.binding.tvInvoiceNumber.setText(
                    invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "INV-" + invoice.getId());
            this.binding.tvCustomerName
                    .setText(invoice.getCustomerName() != null ? invoice.getCustomerName() : "Unknown Customer");
            String address = invoice.getAddress() != null ? invoice.getAddress() : "No address";
            this.binding.tvAddress.setText(address);
            this.binding.tvAddress.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                   // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceAdapter.InvoiceViewHolder.lambda$bind$0(invoice, view);
                }
            });
            this.binding.btnViewDetails.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                        // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceAdapter.InvoiceViewHolder.this.lambda$bind$1(invoice, view);
                }
            });
            this.binding.btnCall.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                 // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceAdapter.InvoiceViewHolder.lambda$bind$2(invoice, view);
                }
            });
            this.binding.btnNavigate.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                     // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceAdapter.InvoiceViewHolder.lambda$bind$3(invoice, view);
                }
            });
            this.binding.btnDelete.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                   // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceAdapter.InvoiceViewHolder.this.lambda$bind$4(invoice, view);
                }
            });
            this.binding.cbDeliveryComplete.setOnCheckedChangeListener(null);
            this.binding.cbDeliveryComplete.setChecked(invoice.isCompleted());
            this.binding.cbDeliveryComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from
                                                                                                                      // class:
                                                                                                                      // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda5
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    InvoiceAdapter.InvoiceViewHolder.this.lambda$bind$5(invoice, compoundButton, z);
                }
            });
            String serviceType = invoice.getServiceType();
            this.binding.tvServiceType.setText(abbrevServiceType(serviceType) + " ▾");
            this.binding.tvServiceType.setTextColor(serviceTypeColor(serviceType));
            updateServiceBadges(serviceType);
            this.binding.tvServiceType.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                       // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    InvoiceAdapter.InvoiceViewHolder.this.lambda$bind$7(invoice, view);
                }
            });
            // Grey out completed deliveries as a visual aid
            applyCompletedOverlay(invoice.isCompleted());
        }

        static /* synthetic */ void lambda$bind$0(Invoice invoice, View v) {
            if (invoice.getAddress() != null && !invoice.getAddress().isEmpty()) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(invoice.getAddress()));
                Intent mapIntent = new Intent("android.intent.action.VIEW", gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(mapIntent);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bind$1(Invoice invoice, View v) {
            if (InvoiceAdapter.this.listener != null) {
                InvoiceAdapter.this.listener.onViewDetails(invoice);
            }
        }

        static /* synthetic */ void lambda$bind$2(Invoice invoice, View v) {
            String phone = invoice.getPhone();
            if (phone != null && !phone.isEmpty() && !"No phone".equals(phone)) {
                v.getContext().startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone)));
            } else {
                Toast.makeText(v.getContext(), "No phone number on file", 0).show();
            }
        }

        static /* synthetic */ void lambda$bind$3(Invoice invoice, View v) {
            String addr = invoice.getAddress();
            if (addr != null && !addr.isEmpty() && !"No address found".equals(addr) && !"No address".equals(addr)) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(addr));
                Intent mapIntent = new Intent("android.intent.action.VIEW", gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(mapIntent);
                    return;
                } else {
                    v.getContext().startActivity(new Intent("android.intent.action.VIEW",
                            Uri.parse("https://maps.google.com/?q=" + Uri.encode(addr))));
                    return;
                }
            }
            Toast.makeText(v.getContext(), "No address on file", 0).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bind$4(Invoice invoice, View v) {
            if (InvoiceAdapter.this.listener != null) {
                InvoiceAdapter.this.listener.onDelete(invoice);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bind$5(Invoice invoice, CompoundButton buttonView, boolean isChecked) {
            applyCompletedOverlay(isChecked);
            if (InvoiceAdapter.this.listener != null) {
                InvoiceAdapter.this.listener.onDeliveryCompleteChanged(invoice, isChecked);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bind$7(final Invoice invoice, View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add(0, 0, 0, "Delivery");
            popup.getMenu().add(0, 1, 1, "Delivery and Install");
            popup.getMenu().add(0, 2, 2, "Delivery/Haul-Away");
            popup.getMenu().add(0, 3, 3, "Delivery/Install/Haul-Away");
            popup.getMenu().add(0, 4, 4, "Service Call");
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class:
                                                                                       // com.mobileinvoice.ocr.InvoiceAdapter$InvoiceViewHolder$$ExternalSyntheticLambda7
                @Override // android.widget.PopupMenu.OnMenuItemClickListener
                public boolean onMenuItemClick(MenuItem menuItem) {
                    boolean lambda$bind$6;
                    lambda$bind$6 = InvoiceAdapter.InvoiceViewHolder.this.lambda$bind$6(invoice, menuItem);
                    return lambda$bind$6;
                }
            });
            popup.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$bind$6(Invoice invoice, MenuItem item) {
            String selected = item.getTitle().toString();
            invoice.setServiceType(selected);
            this.binding.tvServiceType.setText(abbrevServiceType(selected) + " ▾");
            this.binding.tvServiceType.setTextColor(serviceTypeColor(selected));
            updateServiceBadges(selected);
            if (InvoiceAdapter.this.listener != null) {
                InvoiceAdapter.this.listener.onServiceTypeChanged(invoice, selected);
                return true;
            }
            return true;
        }

        private void updateServiceBadges(String serviceType) {
            boolean isDelivery = serviceType == null || "Delivery".equals(serviceType)
                    || "Delivery and Install".equals(serviceType)
                    || "Delivery/Haul-Away".equals(serviceType)
                    || "Delivery/Install/Haul-Away".equals(serviceType);
            boolean hasInstall = "Delivery and Install".equals(serviceType)
                    || "Delivery/Install/Haul-Away".equals(serviceType);
            boolean hasHaul = "Delivery/Haul-Away".equals(serviceType)
                    || "Delivery/Install/Haul-Away".equals(serviceType);
            boolean hasService = "Service Call".equals(serviceType);
            setServiceBadge(this.binding.tvBadgeDelivery, isDelivery);
            setServiceBadge(this.binding.tvBadgeInstall, hasInstall);
            setServiceBadge(this.binding.tvBadgeHaulAway, hasHaul);
            setServiceBadge(this.binding.tvBadgeService, hasService);
        }

        private void setServiceBadge(TextView badge, boolean active) {
            if (active) {
                badge.setTextColor(0xFFD4AF37);
                badge.setBackgroundResource(R.drawable.badge_background);
            } else {
                badge.setTextColor(0xFF404040);
                badge.setBackgroundResource(R.drawable.badge_service_dim);
            }
        }

        /** Grey out the entire card when delivery is completed; restore when unchecked. */
        private void applyCompletedOverlay(boolean completed) {
            float alpha = completed ? 0.45f : 1.0f;
            this.binding.cardContent.setAlpha(alpha);
        }

        private void applyThemeStyling(int position) {
            Context ctx = this.binding.getRoot().getContext();
            String theme = AppSettings.getInstance(ctx).getAppTheme();
            MaterialCardView card = this.binding.getRoot();
            if (AppSettings.THEME_LIGHT_MARBLE.equals(theme) || AppSettings.THEME_MARBLE.equals(theme)
                    || AppSettings.THEME_BLENDED.equals(theme)) {
                this.binding.cardContent.setBackground(ContextCompat.getDrawable(ctx, MARBLE_CARDS[position % 3]));
                this.binding.tvInvoiceNumber.setTextColor(-2838729);
                this.binding.tvCustomerName.setTextColor(-14935528);
                this.binding.tvAddress.setTextColor(-10859984);
                this.binding.btnDelete.setImageTintList(ColorStateList.valueOf(-15066598));
                card.setStrokeColor(436207616);
                card.setStrokeWidth(2);
                if (Build.VERSION.SDK_INT >= 28) {
                    card.setOutlineSpotShadowColor(1624551223);
                    card.setOutlineAmbientShadowColor(1087680311);
                    return;
                }
                return;
            }
            TypedValue tv = new TypedValue();
            ctx.getTheme().resolveAttribute(R.attr.themeCardBackground, tv, true);
            this.binding.cardContent.setBackgroundResource(tv.resourceId);
            this.binding.tvInvoiceNumber.setTextColor(-1);
            this.binding.tvCustomerName.setTextColor(-1);
            this.binding.tvAddress.setTextColor(-1710619);
            card.setStrokeColor(ContextCompat.getColor(ctx, R.color.rich_gold));
            card.setStrokeWidth(2);
            card.setCardElevation(4.0f);
            if (Build.VERSION.SDK_INT >= 28) {
                card.setOutlineSpotShadowColor(ViewCompat.MEASURED_STATE_MASK);
                card.setOutlineAmbientShadowColor(ViewCompat.MEASURED_STATE_MASK);
            }
        }

        private String abbrevServiceType(String type) {
            if (type == null || "Delivery".equals(type))
                return "Delivery";
            if ("Delivery and Install".equals(type))
                return "Del/Install";
            if ("Delivery/Haul-Away".equals(type))
                return "Del/Haul";
            if ("Delivery/Install/Haul-Away".equals(type))
                return "Del/Inst/HA";
            if ("Service Call".equals(type))
                return "Service";
            return type;
        }

        private int serviceTypeColor(String type) {
            return "Service Call".equals(type) ? -44462 : -2838729;
        }
    }
}
