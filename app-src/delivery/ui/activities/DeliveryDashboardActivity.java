package com.mobileinvoice.delivery.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity;
import com.mobileinvoice.delivery.ui.adapters.DeliveryAdapter;
import com.mobileinvoice.delivery.viewmodel.DeliveryViewModel;
import com.mobileinvoice.ocr.R;
import java.util.List;

/* loaded from: classes7.dex */
public class DeliveryDashboardActivity extends AppCompatActivity {
    private DeliveryAdapter adapter;
    private ChipGroup chipGroupFilters;
    private View emptyView;
    private ExtendedFloatingActionButton fabAddDelivery;
    private RecyclerView recyclerView;
    private DeliveryViewModel viewModel;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Deliveries");
        this.viewModel = (DeliveryViewModel) new ViewModelProvider(this).get(DeliveryViewModel.class);
        initializeViews();
        setupRecyclerView();
        setupFilters();
        observeData();
        setupFab();
    }

    private void initializeViews() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_deliveries);
        this.emptyView = findViewById(R.id.layout_empty);
        this.fabAddDelivery = (ExtendedFloatingActionButton) findViewById(R.id.fab_add_delivery);
        this.chipGroupFilters = (ChipGroup) findViewById(R.id.chip_group_filters);
    }

    private void setupRecyclerView() {
        this.adapter = new DeliveryAdapter();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setHasFixedSize(false);
        this.adapter.setOnDeliveryClickListener(new DeliveryAdapter.OnDeliveryClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda10
            @Override // com.mobileinvoice.delivery.ui.adapters.DeliveryAdapter.OnDeliveryClickListener
            public final void onDeliveryClick(Delivery delivery) {
                DeliveryDashboardActivity.this.lambda$setupRecyclerView$0(delivery);
            }
        });
        this.adapter.setOnDeliveryLongClickListener(new DeliveryAdapter.OnDeliveryLongClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda1
            @Override // com.mobileinvoice.delivery.ui.adapters.DeliveryAdapter.OnDeliveryLongClickListener
            public final void onDeliveryLongClick(Delivery delivery) {
                DeliveryDashboardActivity.this.lambda$setupRecyclerView$1(delivery);
            }
        });
        setupSwipeActions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRecyclerView$0(Delivery delivery) {
        Toast.makeText(this, "Detail view coming soon", 0).show();
    }

    private void setupSwipeActions() {
        ItemTouchHelper.SimpleCallback swipeCallback = new AnonymousClass1(0, 12);
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(this.recyclerView);
    }

    /* renamed from: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$1, reason: invalid class name */
    class AnonymousClass1 extends ItemTouchHelper.SimpleCallback {
        AnonymousClass1(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            final Delivery delivery = DeliveryDashboardActivity.this.adapter.getCurrentList().get(position);
            if (direction == 8) {
                if (delivery.getStatus() == DeliveryStatus.PENDING || delivery.getStatus() == DeliveryStatus.IN_TRANSIT) {
                    DeliveryDashboardActivity.this.viewModel.updateStatus(delivery.getId(), DeliveryStatus.DELIVERED);
                    Snackbar.make(DeliveryDashboardActivity.this.recyclerView, "Delivery marked as completed", -1).show();
                    return;
                } else {
                    DeliveryDashboardActivity.this.adapter.notifyItemChanged(position);
                    return;
                }
            }
            DeliveryDashboardActivity.this.viewModel.delete(delivery);
            Snackbar.make(DeliveryDashboardActivity.this.recyclerView, "Delivery deleted", 0).setAction("UNDO", new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$1$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DeliveryDashboardActivity.AnonymousClass1.this.lambda$onSwiped$0(delivery, view);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSwiped$0(Delivery delivery, View v) {
            DeliveryDashboardActivity.this.viewModel.insert(delivery);
        }
    }

    private void setupFilters() {
        final Chip chipAll = (Chip) findViewById(R.id.chip_all);
        chipAll.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeliveryDashboardActivity.this.lambda$setupFilters$2(chipAll, view);
            }
        });
        Chip chipPending = (Chip) findViewById(R.id.chip_pending);
        chipPending.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeliveryDashboardActivity.this.lambda$setupFilters$3(view);
            }
        });
        Chip chipInTransit = (Chip) findViewById(R.id.chip_in_transit);
        chipInTransit.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeliveryDashboardActivity.this.lambda$setupFilters$4(view);
            }
        });
        Chip chipDelivered = (Chip) findViewById(R.id.chip_delivered);
        chipDelivered.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeliveryDashboardActivity.this.lambda$setupFilters$5(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilters$2(Chip chipAll, View v) {
        this.viewModel.clearFilters();
        chipAll.setChecked(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilters$3(View v) {
        this.viewModel.setStatusFilter(DeliveryStatus.PENDING);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilters$4(View v) {
        this.viewModel.setStatusFilter(DeliveryStatus.IN_TRANSIT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilters$5(View v) {
        this.viewModel.setStatusFilter(DeliveryStatus.DELIVERED);
    }

    private void observeData() {
        this.viewModel.getFilteredDeliveries().observe(this, new Observer() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                DeliveryDashboardActivity.this.lambda$observeData$6((List) obj);
            }
        });
        this.viewModel.getTotalCount().observe(this, new Observer() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda2
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                DeliveryDashboardActivity.this.lambda$observeData$7((Integer) obj);
            }
        });
        this.viewModel.isLoading().observe(this, new Observer() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda3
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                DeliveryDashboardActivity.lambda$observeData$8((Boolean) obj);
            }
        });
        this.viewModel.getErrorMessage().observe(this, new Observer() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda4
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                DeliveryDashboardActivity.this.lambda$observeData$9((String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$observeData$6(List deliveries) {
        if (deliveries != null && !deliveries.isEmpty()) {
            this.recyclerView.setVisibility(0);
            this.emptyView.setVisibility(8);
            this.adapter.submitList(deliveries);
        } else {
            this.recyclerView.setVisibility(8);
            this.emptyView.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$observeData$7(Integer count) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(count + " total deliveries");
        }
    }

    static /* synthetic */ void lambda$observeData$8(Boolean isLoading) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$observeData$9(String error) {
        if (error != null && !error.isEmpty()) {
            Toast.makeText(this, error, 0).show();
            this.viewModel.clearError();
        }
    }

    private void setupFab() {
        this.fabAddDelivery.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeliveryDashboardActivity.this.lambda$setupFab$10(view);
            }
        });
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.mobileinvoice.delivery.ui.activities.DeliveryDashboardActivity.2
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    DeliveryDashboardActivity.this.fabAddDelivery.shrink();
                } else if (dy < 0) {
                    DeliveryDashboardActivity.this.fabAddDelivery.extend();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFab$10(View v) {
        Toast.makeText(this, "Create delivery coming soon", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: showDeliveryOptionsDialog, reason: merged with bridge method [inline-methods] */
    public void lambda$setupRecyclerView$1(Delivery delivery) {
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void showExportDialog() {
        Toast.makeText(this, "Export feature coming soon", 0).show();
    }
}
