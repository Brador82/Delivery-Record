package com.mobileinvoice.delivery.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.models.DeliveryStatus;

import com.mobileinvoice.ocr.R;
import java.text.SimpleDateFormat;
import java.util.Locale;

/* loaded from: classes9.dex */
public class DeliveryAdapter extends ListAdapter<Delivery, DeliveryAdapter.DeliveryViewHolder> {
    private static final DiffUtil.ItemCallback<Delivery> DIFF_CALLBACK = new DiffUtil.ItemCallback<Delivery>() { // from class: com.mobileinvoice.delivery.ui.adapters.DeliveryAdapter.1
        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        public boolean areItemsTheSame(Delivery oldItem, Delivery newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.ItemCallback
        public boolean areContentsTheSame(Delivery oldItem, Delivery newItem) {
            return oldItem.getStatus() == newItem.getStatus() && oldItem.getRouteOrder() == newItem.getRouteOrder() && oldItem.getPriority() == newItem.getPriority() && equalsOrBothNull(oldItem.getCustomerName(), newItem.getCustomerName());
        }

        private boolean equalsOrBothNull(Object a, Object b) {
            if (a == null && b == null) {
                return true;
            }
            if (a == null || b == null) {
                return false;
            }
            return a.equals(b);
        }
    };
    private OnDeliveryClickListener clickListener;
    private final SimpleDateFormat dateFormat;
    private OnDeliveryLongClickListener longClickListener;
    private final SimpleDateFormat timeFormat;

    public interface OnDeliveryClickListener {
        void onDeliveryClick(Delivery delivery);
    }

    public interface OnDeliveryLongClickListener {
        void onDeliveryLongClick(Delivery delivery);
    }

    public DeliveryAdapter() {
        super(DIFF_CALLBACK);
        this.timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_card, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Delivery delivery = getItem(position);
        holder.bind(delivery);
    }

    class DeliveryViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageView ivNavigate;
        private final ImageView ivPhone;
        private final ImageView ivPriority;
        private final View statusIndicator;
        private final TextView tvAddress;
        private final TextView tvCustomerName;
        private final TextView tvPackageCount;
        private final TextView tvStatus;
        private final TextView tvTimeWindow;
        private final TextView tvTrackingNumber;

        public DeliveryViewHolder(View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.card_delivery);
            this.tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            this.tvAddress = itemView.findViewById(R.id.tv_address);
            this.tvTrackingNumber = itemView.findViewById(R.id.tv_tracking_number);
            this.tvTimeWindow = itemView.findViewById(R.id.tv_time_window);
            this.tvPackageCount = itemView.findViewById(R.id.tv_package_count);
            this.tvStatus = itemView.findViewById(R.id.tv_status);
            this.ivPriority = itemView.findViewById(R.id.iv_priority);
            this.ivPhone = itemView.findViewById(R.id.iv_phone);
            this.ivNavigate = itemView.findViewById(R.id.iv_navigate);
            this.statusIndicator = itemView.findViewById(R.id.view_status_indicator);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.adapters.DeliveryAdapter$DeliveryViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    DeliveryAdapter.DeliveryViewHolder.this.lambda$new$0(view);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.delivery.ui.adapters.DeliveryAdapter$DeliveryViewHolder$$ExternalSyntheticLambda1
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    boolean lambda$new$1;
                    lambda$new$1 = DeliveryAdapter.DeliveryViewHolder.this.lambda$new$1(view);
                    return lambda$new$1;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View v) {
            int position = getAdapterPosition();
            if (position != -1 && DeliveryAdapter.this.clickListener != null) {
                DeliveryAdapter.this.clickListener.onDeliveryClick(DeliveryAdapter.this.getItem(position));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$1(View v) {
            int position = getAdapterPosition();
            if (position != -1 && DeliveryAdapter.this.longClickListener != null) {
                DeliveryAdapter.this.longClickListener.onDeliveryLongClick(DeliveryAdapter.this.getItem(position));
                return true;
            }
            return false;
        }

        public void bind(Delivery delivery) {
            String str;
            TextView textView = this.tvCustomerName;
            if (delivery.getCustomerName() != null) {
                str = delivery.getCustomerName();
            } else {
                str = "Unknown Customer";
            }
            textView.setText(str);
            this.tvAddress.setText(delivery.getFullAddress());
            this.tvTrackingNumber.setText(delivery.getTrackingNumber());
            if (delivery.getTimeWindowStart() != null) {
                this.tvTimeWindow.setText(delivery.getTimeWindow());
                this.tvTimeWindow.setVisibility(0);
            } else {
                this.tvTimeWindow.setVisibility(8);
            }
            String packageText = delivery.getPackageCount() + " package" + (delivery.getPackageCount() != 1 ? "s" : "");
            this.tvPackageCount.setText(packageText);
            DeliveryStatus status = delivery.getStatus();
            this.tvStatus.setText(status.getDisplayName());
            this.tvStatus.setTextColor(Color.parseColor(status.getColorHex()));
            this.statusIndicator.setBackgroundColor(Color.parseColor(status.getColorHex()));
            if (delivery.getPriority().getValue() >= 3) {
                this.ivPriority.setVisibility(0);
                this.ivPriority.setColorFilter(Color.parseColor(delivery.getPriority().getColorHex()));
            } else {
                this.ivPriority.setVisibility(8);
            }
            if (delivery.isOverdue()) {
                this.cardView.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
            } else {
                this.cardView.setCardBackgroundColor(-1);
            }
        }
    }

    public void setOnDeliveryClickListener(OnDeliveryClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnDeliveryLongClickListener(OnDeliveryLongClickListener listener) {
        this.longClickListener = listener;
    }
}
