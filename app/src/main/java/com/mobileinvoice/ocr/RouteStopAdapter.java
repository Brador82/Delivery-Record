package com.mobileinvoice.ocr;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileinvoice.ocr.RouteOptimizer;
import com.mobileinvoice.ocr.RouteStopAdapter;
import com.mobileinvoice.ocr.database.Invoice;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes7.dex */
public class RouteStopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int COLOR_GOLD = -2838729;
    private static final int COLOR_GRAY = -6710887;
    private static final int COLOR_LIGHT_GRAY = -3355444;
    private static final int COLOR_WHITE = -1;
    private OnStopChangeListener changeListener;
    private final OnStartDragListener dragListener;
    private final OnStopClickListener listener;
    private final List<RouteListItem> items = new ArrayList();
    private final List<RouteOptimizer.RoutePoint> activeStops = new ArrayList();
    private final List<RouteOptimizer.RoutePoint> completedStops = new ArrayList();
    private boolean completedExpanded = false;
    private final Set<Integer> expandedPositions = new HashSet();

    interface OnItemExpandListener {
        void onToggleExpand(int position);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public interface OnStopChangeListener {
        void onCompletedChanged(RouteOptimizer.RoutePoint stop, boolean completed);

        void onMakeFirst(RouteOptimizer.RoutePoint stop);

        void onMakeLast(RouteOptimizer.RoutePoint stop);

        void onRouteOrderChanged();

        void onStopTimeChanged(RouteOptimizer.RoutePoint stop, int newTimeMinutes);
    }

    public interface OnStopClickListener {
        void onCallClick(RouteOptimizer.RoutePoint stop);

        void onNavigateClick(RouteOptimizer.RoutePoint stop);
    }

    public RouteStopAdapter(OnStopClickListener listener, OnStartDragListener dragListener) {
        this.listener = listener;
        this.dragListener = dragListener;
    }

    public void setOnStopChangeListener(OnStopChangeListener listener) {
        this.changeListener = listener;
    }

    public void setStops(List<RouteOptimizer.RoutePoint> stops) {
        this.activeStops.clear();
        this.completedStops.clear();
        this.expandedPositions.clear();
        for (RouteOptimizer.RoutePoint stop : stops) {
            if (stop.invoice.isCompleted()) {
                this.completedStops.add(stop);
            } else {
                this.activeStops.add(stop);
            }
        }
        updateStopNumbers();
        rebuildItemsList();
    }

    private void rebuildItemsList() {
        this.items.clear();
        for (RouteOptimizer.RoutePoint stop : this.activeStops) {
            this.items.add(RouteListItem.createStopItem(stop));
        }
        if (!this.completedStops.isEmpty()) {
            this.items.add(RouteListItem.createHeaderItem(this.completedStops.size(), this.completedExpanded));
            if (this.completedExpanded) {
                for (RouteOptimizer.RoutePoint stop2 : this.completedStops) {
                    this.items.add(RouteListItem.createStopItem(stop2));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void toggleCompletedSection() {
        this.completedExpanded = !this.completedExpanded;
        rebuildItemsList();
    }

    public List<RouteOptimizer.RoutePoint> getActiveStops() {
        return new ArrayList(this.activeStops);
    }

    public List<RouteOptimizer.RoutePoint> getAllStops() {
        List<RouteOptimizer.RoutePoint> all = new ArrayList<>(this.activeStops);
        all.addAll(this.completedStops);
        return all;
    }

    public List<RouteOptimizer.RoutePoint> getStops() {
        return getActiveStops();
    }

    public boolean isCompletedItem(int position) {
        if (position < 0 || position >= this.items.size()) {
            return false;
        }
        RouteListItem item = this.items.get(position);
        if (item.getType() == 1) {
            return true;
        }
        return item.getRoutePoint().invoice.isCompleted();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= this.items.size() || toPosition < 0 || toPosition >= this.items.size()) {
            return false;
        }
        RouteListItem fromItem = this.items.get(fromPosition);
        RouteListItem toItem = this.items.get(toPosition);
        if (fromItem.getType() == 1 || toItem.getType() == 1 || fromItem.getRoutePoint().invoice.isCompleted() || toItem.getRoutePoint().invoice.isCompleted() || fromPosition >= this.activeStops.size() || toPosition >= this.activeStops.size()) {
            return false;
        }
        RouteOptimizer.RoutePoint movedItem = this.activeStops.remove(fromPosition);
        this.activeStops.add(toPosition, movedItem);
        updateStopNumbers();
        RouteListItem movedListItem = this.items.remove(fromPosition);
        this.items.add(toPosition, movedListItem);
        notifyItemMoved(fromPosition, toPosition);
        if (this.changeListener != null) {
            this.changeListener.onRouteOrderChanged();
        }
        return true;
    }

    private void updateStopNumbers() {
        int order = 1;
        for (RouteOptimizer.RoutePoint stop : this.activeStops) {
            stop.orderIndex = order;
            order++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return this.items.get(position).getType();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_stop, parent, false);
        return new StopViewHolder(view2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RouteListItem item = this.items.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(item, new Runnable() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public void run() {
                    RouteStopAdapter.this.toggleCompletedSection();
                }
            });
        } else if (holder instanceof StopViewHolder) {
            RouteOptimizer.RoutePoint stop = item.getRoutePoint();
            boolean isCompleted = stop.invoice.isCompleted();
            boolean isExpanded = this.expandedPositions.contains(Integer.valueOf(position));
            ((StopViewHolder) holder).bind(stop, this.listener, this.dragListener, this.changeListener, isCompleted, isExpanded, position, new OnItemExpandListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$$ExternalSyntheticLambda1
                @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnItemExpandListener
                public void onToggleExpand(int i) {
                    RouteStopAdapter.this.toggleItemExpansion(i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleItemExpansion(int position) {
        if (this.expandedPositions.contains(Integer.valueOf(position))) {
            this.expandedPositions.remove(Integer.valueOf(position));
        } else {
            this.expandedPositions.add(Integer.valueOf(position));
        }
        notifyItemChanged(position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivExpandIcon;
        private final TextView tvHeaderTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.tvHeaderTitle = itemView.findViewById(R.id.tvHeaderTitle);
            this.ivExpandIcon = itemView.findViewById(R.id.ivExpandIcon);
        }

        public void bind(RouteListItem item, final Runnable onClickListener) {
            this.tvHeaderTitle.setText(item.getHeaderTitle() + " (" + item.getCompletedCount() + ")");
            int iconRes = item.isExpanded() ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            this.ivExpandIcon.setImageResource(iconRes);
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$HeaderViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    onClickListener.run();
                }
            });
        }
    }

    static class StopViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton btnCall;
        private final ImageButton btnDecreaseTime;
        private final ImageButton btnExpand;
        private final ImageButton btnIncreaseTime;
        private final Button btnMakeFirst;
        private final Button btnMakeLast;
        private final ImageButton btnNavigate;
        private final Button btnTime120;
        private final Button btnTime15;
        private final Button btnTime30;
        private final Button btnTime45;
        private final Button btnTime60;
        private final Button btnTime90;
        private final CheckBox cbCompleted;
        private final ImageView dragHandle;
        private final LinearLayout expandablePanel;
        private final LinearLayout mainContent;
        private final TextView tvAddress;
        private final TextView tvCustomerName;
        private final TextView tvETA;
        private final TextView tvPriorityBadge;
        private final TextView tvStopInfo;
        private final TextView tvStopNumber;
        private final TextView tvStopTime;
        private final TextView tvStopTimeValue;

        public StopViewHolder(View itemView) {
            super(itemView);
            this.mainContent = itemView.findViewById(R.id.mainContent);
            this.tvStopNumber = itemView.findViewById(R.id.tvStopNumber);
            this.tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            this.tvAddress = itemView.findViewById(R.id.tvAddress);
            this.tvStopInfo = itemView.findViewById(R.id.tvStopInfo);
            this.tvETA = itemView.findViewById(R.id.tvETA);
            this.tvStopTime = itemView.findViewById(R.id.tvStopTime);
            this.tvPriorityBadge = itemView.findViewById(R.id.tvPriorityBadge);
            this.btnCall = itemView.findViewById(R.id.btnCall);
            this.btnNavigate = itemView.findViewById(R.id.btnNavigate);
            this.btnExpand = itemView.findViewById(R.id.btnExpand);
            this.dragHandle = itemView.findViewById(R.id.dragHandle);
            this.cbCompleted = itemView.findViewById(R.id.cbCompleted);
            this.expandablePanel = itemView.findViewById(R.id.expandablePanel);
            this.btnMakeFirst = itemView.findViewById(R.id.btnMakeFirst);
            this.btnMakeLast = itemView.findViewById(R.id.btnMakeLast);
            this.btnDecreaseTime = itemView.findViewById(R.id.btnDecreaseTime);
            this.btnIncreaseTime = itemView.findViewById(R.id.btnIncreaseTime);
            this.tvStopTimeValue = itemView.findViewById(R.id.tvStopTimeValue);
            this.btnTime15 = itemView.findViewById(R.id.btnTime15);
            this.btnTime30 = itemView.findViewById(R.id.btnTime30);
            this.btnTime45 = itemView.findViewById(R.id.btnTime45);
            this.btnTime60 = itemView.findViewById(R.id.btnTime60);
            this.btnTime90 = itemView.findViewById(R.id.btnTime90);
            this.btnTime120 = itemView.findViewById(R.id.btnTime120);
        }

        public void bind(final RouteOptimizer.RoutePoint stop, final OnStopClickListener listener, final OnStartDragListener dragListener, final OnStopChangeListener changeListener, final boolean isCompleted, boolean isExpanded, final int position, final OnItemExpandListener expandListener) {
            Invoice invoice = stop.invoice;
            this.tvStopNumber.setText(String.valueOf(stop.orderIndex));
            this.tvCustomerName.setText(invoice.getCustomerName());
            this.tvAddress.setText(invoice.getAddress());
            String items = invoice.getItems();
            if (items != null && !items.isEmpty()) {
                this.tvStopInfo.setText("Items: " + items);
            } else {
                this.tvStopInfo.setText("No items specified");
            }
            String eta = stop.getFormattedETA();
            if (eta != null && !eta.equals("N/A")) {
                this.tvETA.setText("ETA: " + eta);
                this.tvETA.setVisibility(0);
            } else {
                this.tvETA.setVisibility(8);
            }
            this.tvStopTime.setText("(" + stop.stopTimeMinutes + " min stop)");
            String priorityText = stop.getPriorityText();
            if (priorityText != null) {
                this.tvPriorityBadge.setText(priorityText);
                this.tvPriorityBadge.setVisibility(0);
            } else {
                this.tvPriorityBadge.setVisibility(8);
            }
            this.expandablePanel.setVisibility(isExpanded ? 0 : 8);
            this.btnExpand.setImageResource(isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
            this.tvStopTimeValue.setText(stop.stopTimeMinutes + " minutes");
            if (isCompleted) {
                this.itemView.setAlpha(0.7f);
                this.tvCustomerName.setTextColor(-7829368);
                this.tvAddress.setTextColor(-7829368);
                this.tvStopInfo.setTextColor(-7829368);
                this.tvETA.setTextColor(-7829368);
                this.tvStopTime.setTextColor(-7829368);
                this.tvCustomerName.setPaintFlags(this.tvCustomerName.getPaintFlags() | 16);
                this.tvAddress.setPaintFlags(this.tvAddress.getPaintFlags() | 16);
                this.tvStopInfo.setPaintFlags(this.tvStopInfo.getPaintFlags() | 16);
                this.tvETA.setPaintFlags(this.tvETA.getPaintFlags() | 16);
                this.tvStopTime.setPaintFlags(this.tvStopTime.getPaintFlags() | 16);
                this.tvStopNumber.setTextColor(-10066330);
                this.dragHandle.setVisibility(8);
                this.btnExpand.setVisibility(8);
            } else {
                this.itemView.setAlpha(1.0f);
                this.tvCustomerName.setTextColor(RouteStopAdapter.COLOR_GOLD);
                this.tvAddress.setTextColor(-1);
                this.tvStopInfo.setTextColor(RouteStopAdapter.COLOR_LIGHT_GRAY);
                this.tvETA.setTextColor(-10496);
                this.tvStopTime.setTextColor(RouteStopAdapter.COLOR_GRAY);
                this.tvStopNumber.setTextColor(-15066598);
                this.tvCustomerName.setPaintFlags(this.tvCustomerName.getPaintFlags() & (-17));
                this.tvAddress.setPaintFlags(this.tvAddress.getPaintFlags() & (-17));
                this.tvStopInfo.setPaintFlags(this.tvStopInfo.getPaintFlags() & (-17));
                this.tvETA.setPaintFlags(this.tvETA.getPaintFlags() & (-17));
                this.tvStopTime.setPaintFlags(this.tvStopTime.getPaintFlags() & (-17));
                this.dragHandle.setVisibility(0);
                this.btnExpand.setVisibility(0);
            }
            this.cbCompleted.setOnCheckedChangeListener(null);
            this.cbCompleted.setChecked(isCompleted);
            this.cbCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda2
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$0(changeListener, stop, compoundButton, z);
                }
            });
            this.btnCall.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$1(listener, stop, view);
                }
            });
            this.btnNavigate.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$2(listener, stop, view);
                }
            });
            this.btnExpand.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$3(expandListener, position, view);
                }
            });
            this.mainContent.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$4(isCompleted, expandListener, position, view);
                }
            });
            if (!isCompleted) {
                this.dragHandle.setOnTouchListener(new View.OnTouchListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda7
                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        boolean lambda$bind$5;
                        lambda$bind$5 = RouteStopAdapter.StopViewHolder.this.lambda$bind$5(dragListener, view, motionEvent);
                        return lambda$bind$5;
                    }
                });
            } else {
                this.dragHandle.setOnTouchListener(null);
            }
            this.btnMakeFirst.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$6(changeListener, stop, view);
                }
            });
            this.btnMakeLast.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda9
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$7(changeListener, stop, view);
                }
            });
            this.btnDecreaseTime.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$8(stop, changeListener, view);
                }
            });
            this.btnIncreaseTime.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$bind$9(stop, changeListener, view);
                }
            });
            setupTimePresetButton(this.btnTime15, 15, stop, changeListener);
            setupTimePresetButton(this.btnTime30, 30, stop, changeListener);
            setupTimePresetButton(this.btnTime45, 45, stop, changeListener);
            setupTimePresetButton(this.btnTime60, 60, stop, changeListener);
            setupTimePresetButton(this.btnTime90, 90, stop, changeListener);
            setupTimePresetButton(this.btnTime120, 120, stop, changeListener);
            updateTimePresetSelection(stop.stopTimeMinutes);
        }

        static /* synthetic */ void lambda$bind$0(OnStopChangeListener changeListener, RouteOptimizer.RoutePoint stop, CompoundButton buttonView, boolean checked) {
            if (changeListener != null) {
                changeListener.onCompletedChanged(stop, checked);
            }
        }

        static /* synthetic */ void lambda$bind$1(OnStopClickListener listener, RouteOptimizer.RoutePoint stop, View v) {
            if (listener != null) {
                listener.onCallClick(stop);
            }
        }

        static /* synthetic */ void lambda$bind$2(OnStopClickListener listener, RouteOptimizer.RoutePoint stop, View v) {
            if (listener != null) {
                listener.onNavigateClick(stop);
            }
        }

        static /* synthetic */ void lambda$bind$3(OnItemExpandListener expandListener, int position, View v) {
            if (expandListener != null) {
                expandListener.onToggleExpand(position);
            }
        }

        static /* synthetic */ void lambda$bind$4(boolean isCompleted, OnItemExpandListener expandListener, int position, View v) {
            if (!isCompleted && expandListener != null) {
                expandListener.onToggleExpand(position);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$bind$5(OnStartDragListener dragListener, View v, MotionEvent event) {
            if (event.getAction() == 0 && dragListener != null) {
                dragListener.onStartDrag(this);
                return true;
            }
            return false;
        }

        static /* synthetic */ void lambda$bind$6(OnStopChangeListener changeListener, RouteOptimizer.RoutePoint stop, View v) {
            if (changeListener != null) {
                changeListener.onMakeFirst(stop);
            }
        }

        static /* synthetic */ void lambda$bind$7(OnStopChangeListener changeListener, RouteOptimizer.RoutePoint stop, View v) {
            if (changeListener != null) {
                changeListener.onMakeLast(stop);
            }
        }

        static /* synthetic */ void lambda$bind$8(RouteOptimizer.RoutePoint stop, OnStopChangeListener changeListener, View v) {
            int newTime = Math.max(15, stop.stopTimeMinutes - 15);
            if (changeListener != null) {
                changeListener.onStopTimeChanged(stop, newTime);
            }
        }

        static /* synthetic */ void lambda$bind$9(RouteOptimizer.RoutePoint stop, OnStopChangeListener changeListener, View v) {
            int newTime = Math.min(120, stop.stopTimeMinutes + 15);
            if (changeListener != null) {
                changeListener.onStopTimeChanged(stop, newTime);
            }
        }

        private void setupTimePresetButton(Button button, final int minutes, final RouteOptimizer.RoutePoint stop, final OnStopChangeListener changeListener) {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteStopAdapter$StopViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    RouteStopAdapter.StopViewHolder.lambda$setupTimePresetButton$10(changeListener, stop, minutes, view);
                }
            });
        }

        static /* synthetic */ void lambda$setupTimePresetButton$10(OnStopChangeListener changeListener, RouteOptimizer.RoutePoint stop, int minutes, View v) {
            if (changeListener != null) {
                changeListener.onStopTimeChanged(stop, minutes);
            }
        }

        private void updateTimePresetSelection(int selectedMinutes) {
            setPresetButtonSelected(this.btnTime15, selectedMinutes == 15);
            setPresetButtonSelected(this.btnTime30, selectedMinutes == 30);
            setPresetButtonSelected(this.btnTime45, selectedMinutes == 45);
            setPresetButtonSelected(this.btnTime60, selectedMinutes == 60);
            setPresetButtonSelected(this.btnTime90, selectedMinutes == 90);
            setPresetButtonSelected(this.btnTime120, selectedMinutes == 120);
        }

        private void setPresetButtonSelected(Button button, boolean selected) {
            if (selected) {
                button.setBackgroundResource(R.drawable.button_time_preset_selected);
                button.setTextColor(RouteStopAdapter.COLOR_GOLD);
            } else {
                button.setBackgroundResource(R.drawable.button_time_preset);
                button.setTextColor(RouteStopAdapter.COLOR_LIGHT_GRAY);
            }
        }
    }
}
