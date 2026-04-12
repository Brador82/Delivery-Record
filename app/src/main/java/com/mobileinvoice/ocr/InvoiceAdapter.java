package com.mobileinvoice.ocr;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
            int seq = invoice.getDeliverySequence();
            if (seq > 0) {
                this.binding.tvStopNumber.setText("#" + seq);
                this.binding.tvStopNumber.setVisibility(View.VISIBLE);
            } else {
                this.binding.tvStopNumber.setVisibility(View.GONE);
            }
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
            updateServiceBadges(serviceType);
            this.binding.tvBadgeDelivery.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { toggleBadge(invoice, "delivery"); }
            });
            this.binding.tvBadgeInstall.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { toggleBadge(invoice, "install"); }
            });
            this.binding.tvBadgeHaulAway.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { toggleBadge(invoice, "haul"); }
            });
            this.binding.tvBadgeService.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { toggleBadge(invoice, "service"); }
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

        private void toggleBadge(Invoice invoice, String flag) {
            String current = invoice.getServiceType();
            boolean hasDelivery = current == null || "Delivery".equals(current)
                    || "Delivery and Install".equals(current)
                    || "Delivery/Haul-Away".equals(current)
                    || "Delivery/Install/Haul-Away".equals(current);
            boolean hasInstall = "Delivery and Install".equals(current)
                    || "Delivery/Install/Haul-Away".equals(current);
            boolean hasHaul = "Delivery/Haul-Away".equals(current)
                    || "Delivery/Install/Haul-Away".equals(current);
            boolean hasService = "Service Call".equals(current);
            switch (flag) {
                case "delivery": hasDelivery = !hasDelivery; break;
                case "install":  hasInstall = !hasInstall; break;
                case "haul":     hasHaul = !hasHaul; break;
                case "service":  hasService = !hasService; break;
            }
            String newType = computeServiceType(hasDelivery, hasInstall, hasHaul, hasService);
            invoice.setServiceType(newType);
            updateServiceBadges(newType);
            if (InvoiceAdapter.this.listener != null) {
                InvoiceAdapter.this.listener.onServiceTypeChanged(invoice, newType);
            }
        }

        private String computeServiceType(boolean delivery, boolean install, boolean haul, boolean service) {
            if (service) return "Service Call";
            if (delivery && install && haul) return "Delivery/Install/Haul-Away";
            if (delivery && haul) return "Delivery/Haul-Away";
            if (delivery && install) return "Delivery and Install";
            return "Delivery";
        }

        private void setServiceBadge(TextView badge, boolean active) {
            if (active) {
                badge.setTextColor(0xFFD4AF37);
                badge.setBackgroundResource(R.drawable.badge_background);
            } else {
                badge.setTextColor(0xFF888888);
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
                this.binding.tvCustomerName.setTextColor(ContextCompat.getColor(ctx, R.color.rich_gold));
                this.binding.tvAddress.setTextColor(ContextCompat.getColor(ctx, R.color.calacatta_warm_mid));
                this.binding.btnDelete.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.marble_dark_text)));
                card.setStrokeColor(ContextCompat.getColor(ctx, R.color.marble_card_stroke));
                card.setStrokeWidth(2);
                if (Build.VERSION.SDK_INT >= 28) {
                    card.setOutlineSpotShadowColor(ContextCompat.getColor(ctx, R.color.marble_shadow_spot));
                    card.setOutlineAmbientShadowColor(ContextCompat.getColor(ctx, R.color.marble_shadow_ambient));
                    return;
                }
                return;
            }
            TypedValue tv = new TypedValue();
            ctx.getTheme().resolveAttribute(R.attr.themeCardBackground, tv, true);
            this.binding.cardContent.setBackgroundResource(tv.resourceId);
            this.binding.tvCustomerName.setTextColor(ContextCompat.getColor(ctx, R.color.white));
            this.binding.tvAddress.setTextColor(ContextCompat.getColor(ctx, R.color.light_gray));
            card.setStrokeColor(ContextCompat.getColor(ctx, R.color.rich_gold));
            card.setStrokeWidth(2);
            card.setCardElevation(4.0f);
            if (Build.VERSION.SDK_INT >= 28) {
                card.setOutlineSpotShadowColor(ContextCompat.getColor(ctx, R.color.black));
                card.setOutlineAmbientShadowColor(ContextCompat.getColor(ctx, R.color.black));
            }
        }

    }
}
