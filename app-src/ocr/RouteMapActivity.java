package com.mobileinvoice.ocr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobileinvoice.ocr.DraggableDividerTouchListener;
import com.mobileinvoice.ocr.RouteItemTouchHelper;
import com.mobileinvoice.ocr.RouteOptimizer;
import com.mobileinvoice.ocr.RouteStopAdapter;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityRouteMapBinding;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes7.dex */
public class RouteMapActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String TAG = "RouteMapActivity";
    private ActivityRouteMapBinding binding;
    private Location currentLocation;
    private DraggableDividerTouchListener.SnapPosition currentSnapPosition = DraggableDividerTouchListener.SnapPosition.MIDDLE;
    private InvoiceDatabase database;
    private DraggableDividerTouchListener dividerTouchListener;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private ItemTouchHelper itemTouchHelper;
    private RouteOptimizer.OptimizedRoute optimizedRoute;
    private ActivityResultLauncher<String> requestLocationPermissionLauncher;
    private RouteStopAdapter stopAdapter;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.mobileinvoice.ocr.BaseActivity
    protected void applyAppTheme() {
        char c;
        String theme = AppSettings.getInstance(this).getAppTheme();
        switch (theme.hashCode()) {
            case -1950527450:
                if (theme.equals(AppSettings.THEME_DARK_MARBLE)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1638346592:
                if (theme.equals(AppSettings.THEME_BLACK_GOLD)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1081314499:
                if (theme.equals(AppSettings.THEME_MARBLE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -30352208:
                if (theme.equals(AppSettings.THEME_BLENDED)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1226667654:
                if (theme.equals(AppSettings.THEME_LIGHT_MARBLE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1253503425:
                if (theme.equals(AppSettings.THEME_IMPERIAL_MARBLE)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                setTheme(R.style.Theme_MobileInvoiceOCR_LightMarble_NoActionBar);
                break;
            case 2:
                setTheme(R.style.Theme_MobileInvoiceOCR_Blended_NoActionBar);
                break;
            default:
                setTheme(R.style.Theme_MobileInvoiceOCR_NoActionBar);
                break;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityRouteMapBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.database = InvoiceDatabase.getInstance(this);
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        setupPermissionLauncher();
        setupRecyclerView();
        setupClickListeners();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        this.binding.progressBar.setVisibility(0);
        this.binding.tvRouteSummary.setText("Calculating optimal route...");
    }

    private void setupRecyclerView() {
        this.stopAdapter = new RouteStopAdapter(new RouteStopAdapter.OnStopClickListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity.1
            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopClickListener
            public void onCallClick(RouteOptimizer.RoutePoint stop) {
                RouteMapActivity.this.handleCallCustomer(stop);
            }

            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopClickListener
            public void onNavigateClick(RouteOptimizer.RoutePoint stop) {
                RouteMapActivity.this.handleNavigateToStop(stop);
            }
        }, new RouteStopAdapter.OnStartDragListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity.2
            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStartDragListener
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                if (RouteMapActivity.this.itemTouchHelper != null) {
                    RouteMapActivity.this.itemTouchHelper.startDrag(viewHolder);
                }
            }
        });
        this.stopAdapter.setOnStopChangeListener(new RouteStopAdapter.OnStopChangeListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity.3
            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopChangeListener
            public void onMakeFirst(RouteOptimizer.RoutePoint stop) {
                RouteMapActivity.this.handleMakeFirst(stop);
            }

            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopChangeListener
            public void onMakeLast(RouteOptimizer.RoutePoint stop) {
                RouteMapActivity.this.handleMakeLast(stop);
            }

            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopChangeListener
            public void onStopTimeChanged(RouteOptimizer.RoutePoint stop, int newTimeMinutes) {
                RouteMapActivity.this.handleStopTimeChanged(stop, newTimeMinutes);
            }

            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopChangeListener
            public void onRouteOrderChanged() {
                RouteMapActivity.this.updateMapAfterReorder();
            }

            @Override // com.mobileinvoice.ocr.RouteStopAdapter.OnStopChangeListener
            public void onCompletedChanged(RouteOptimizer.RoutePoint stop, boolean completed) {
                RouteMapActivity.this.handleCompletedChanged(stop, completed);
            }
        });
        this.binding.recyclerViewStops.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recyclerViewStops.setAdapter(this.stopAdapter);
        RouteItemTouchHelper.OnItemMovedListener moveListener = new RouteItemTouchHelper.OnItemMovedListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity.4
            @Override // com.mobileinvoice.ocr.RouteItemTouchHelper.OnItemMovedListener
            public void onItemMoved(int fromPosition, int toPosition) {
                RouteMapActivity.this.updateMapAfterReorder();
            }
        };
        RouteItemTouchHelper callback = new RouteItemTouchHelper(this.stopAdapter, moveListener);
        this.itemTouchHelper = new ItemTouchHelper(callback);
        this.itemTouchHelper.attachToRecyclerView(this.binding.recyclerViewStops);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMakeFirst(RouteOptimizer.RoutePoint stop) {
        List<RouteOptimizer.RoutePoint> activeStops = this.stopAdapter.getActiveStops();
        RouteOptimizer.makeFirst(activeStops, stop);
        for (RouteOptimizer.RoutePoint s : activeStops) {
            if (s != stop && s.priority == 1) {
                s.priority = 0;
            }
        }
        this.optimizedRoute.orderedPoints = activeStops;
        recalculateETAsAndRefresh();
        Toast.makeText(this, stop.invoice.getCustomerName() + " moved to first", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMakeLast(RouteOptimizer.RoutePoint stop) {
        List<RouteOptimizer.RoutePoint> activeStops = this.stopAdapter.getActiveStops();
        RouteOptimizer.makeLast(activeStops, stop);
        for (RouteOptimizer.RoutePoint s : activeStops) {
            if (s != stop && s.priority == 2) {
                s.priority = 0;
            }
        }
        this.optimizedRoute.orderedPoints = activeStops;
        recalculateETAsAndRefresh();
        Toast.makeText(this, stop.invoice.getCustomerName() + " moved to last", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopTimeChanged(final RouteOptimizer.RoutePoint stop, int newTimeMinutes) {
        stop.stopTimeMinutes = newTimeMinutes;
        stop.invoice.setStopTimeMinutes(newTimeMinutes);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                RouteMapActivity.this.lambda$handleStopTimeChanged$0(stop);
            }
        }).start();
        recalculateETAsAndRefresh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleStopTimeChanged$0(RouteOptimizer.RoutePoint stop) {
        this.database.invoiceDao().update(stop.invoice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCompletedChanged(final RouteOptimizer.RoutePoint stop, final boolean completed) {
        stop.invoice.setCompleted(completed);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RouteMapActivity.this.lambda$handleCompletedChanged$2(stop, completed);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleCompletedChanged$2(final RouteOptimizer.RoutePoint stop, final boolean completed) {
        this.database.invoiceDao().update(stop.invoice);
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                RouteMapActivity.this.lambda$handleCompletedChanged$1(completed, stop);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleCompletedChanged$1(boolean completed, RouteOptimizer.RoutePoint stop) {
        this.stopAdapter.setStops(this.stopAdapter.getAllStops());
        recalculateETAsAndRefresh();
        String status = completed ? "completed" : "active";
        Toast.makeText(this, stop.invoice.getCustomerName() + " marked as " + status, 0).show();
    }

    private void recalculateETAsAndRefresh() {
        double startLat;
        double startLng;
        if (this.optimizedRoute == null || this.optimizedRoute.orderedPoints.isEmpty()) {
            return;
        }
        if (this.currentLocation != null) {
            startLat = this.currentLocation.getLatitude();
        } else {
            startLat = this.optimizedRoute.orderedPoints.get(0).latitude;
        }
        if (this.currentLocation != null) {
            startLng = this.currentLocation.getLongitude();
        } else {
            startLng = this.optimizedRoute.orderedPoints.get(0).longitude;
        }
        RouteOptimizer.recalculateETAs(this.optimizedRoute, startLat, startLng);
        recalculateRouteDistance();
        List<RouteOptimizer.RoutePoint> allPoints = new ArrayList<>(this.optimizedRoute.orderedPoints);
        try {
            List<Invoice> allInvoices = this.database.invoiceDao().getAllInvoicesSync();
            for (Invoice invoice : allInvoices) {
                if (invoice.isCompleted()) {
                    RouteOptimizer.RoutePoint completedPoint = new RouteOptimizer.RoutePoint(invoice, 0.0d, 0.0d, invoice.getAddress());
                    completedPoint.orderIndex = 0;
                    allPoints.add(completedPoint);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading completed invoices", e);
        }
        this.stopAdapter.setStops(allPoints);
        displayRouteOnMap(startLat, startLng);
        updateRouteSummary();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMapAfterReorder() {
        if (this.googleMap == null || this.optimizedRoute == null) {
            return;
        }
        this.optimizedRoute.orderedPoints = this.stopAdapter.getActiveStops();
        recalculateRouteDistance();
        double startLat = this.currentLocation != null ? this.currentLocation.getLatitude() : this.optimizedRoute.orderedPoints.get(0).latitude;
        double startLng = this.currentLocation != null ? this.currentLocation.getLongitude() : this.optimizedRoute.orderedPoints.get(0).longitude;
        displayRouteOnMap(startLat, startLng);
        updateRouteSummary();
    }

    private void recalculateRouteDistance() {
        double prevLat;
        double prevLng;
        if (this.optimizedRoute == null || this.optimizedRoute.orderedPoints.isEmpty()) {
            return;
        }
        double totalDistance = 0.0d;
        if (this.currentLocation != null) {
            prevLat = this.currentLocation.getLatitude();
        } else {
            prevLat = this.optimizedRoute.orderedPoints.get(0).latitude;
        }
        if (this.currentLocation != null) {
            prevLng = this.currentLocation.getLongitude();
        } else {
            prevLng = this.optimizedRoute.orderedPoints.get(0).longitude;
        }
        for (RouteOptimizer.RoutePoint point : this.optimizedRoute.orderedPoints) {
            double distance = RouteOptimizer.calculateDistance(prevLat, prevLng, point.latitude, point.longitude);
            totalDistance += distance;
            prevLat = point.latitude;
            prevLng = point.longitude;
        }
        this.optimizedRoute.totalDistance = totalDistance;
    }

    private void updateRouteSummary() {
        String summary;
        if (this.optimizedRoute == null || this.optimizedRoute.orderedPoints.isEmpty()) {
            return;
        }
        String endTime = this.optimizedRoute.getFormattedEndTime();
        if (endTime != null && !endTime.equals("N/A")) {
            summary = String.format("Optimized Route\n%d stops • %.1f mi • Finish by %s", Integer.valueOf(this.optimizedRoute.orderedPoints.size()), Double.valueOf(this.optimizedRoute.totalDistance), endTime);
        } else {
            summary = String.format("Optimized Route\n%d stops • %.1f mi • %s estimated", Integer.valueOf(this.optimizedRoute.orderedPoints.size()), Double.valueOf(this.optimizedRoute.totalDistance), RouteOptimizer.estimateTravelTime(this.optimizedRoute.totalDistance));
        }
        this.binding.tvRouteSummary.setText(summary);
    }

    private void setupPermissionLauncher() {
        this.requestLocationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda15
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                RouteMapActivity.this.lambda$setupPermissionLauncher$3((Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupPermissionLauncher$3(Boolean isGranted) {
        if (isGranted.booleanValue()) {
            getCurrentLocationAndOptimize();
        } else {
            Toast.makeText(this, "Location permission required for route optimization", 0).show();
            optimizeRouteFromDefaultLocation();
        }
    }

    private void setupClickListeners() {
        setupDraggableDivider();
        this.binding.btnToggleMapSize.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RouteMapActivity.this.lambda$setupClickListeners$4(view);
            }
        });
        this.binding.btnRecenterMap.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RouteMapActivity.this.lambda$setupClickListeners$5(view);
            }
        });
        this.binding.btnStartNavigation.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RouteMapActivity.this.lambda$setupClickListeners$6(view);
            }
        });
        this.binding.btnReorderList.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                RouteMapActivity.this.lambda$setupClickListeners$7(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$4(View v) {
        toggleMapSize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$5(View v) {
        recenterMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$6(View v) {
        if (this.optimizedRoute != null && !this.optimizedRoute.orderedPoints.isEmpty()) {
            startGoogleMapsNavigation();
        } else {
            Toast.makeText(this, "No route available", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$7(View v) {
        if (this.optimizedRoute != null && !this.optimizedRoute.orderedPoints.isEmpty()) {
            reorderInvoicesInDatabase();
        } else {
            Toast.makeText(this, "No route to apply", 0).show();
        }
    }

    private void setupDraggableDivider() {
        this.dividerTouchListener = new DraggableDividerTouchListener(this.binding.getRoot(), this.binding.mapContainer, this.binding.summaryBar, this.binding.stopsListContainer, new DraggableDividerTouchListener.OnDividerDragListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity.5
            @Override // com.mobileinvoice.ocr.DraggableDividerTouchListener.OnDividerDragListener
            public void onDragStart() {
            }

            @Override // com.mobileinvoice.ocr.DraggableDividerTouchListener.OnDividerDragListener
            public void onDragUpdate(float mapWeight, float listWeight) {
            }

            @Override // com.mobileinvoice.ocr.DraggableDividerTouchListener.OnDividerDragListener
            public void onSnapComplete(DraggableDividerTouchListener.SnapPosition position) {
                RouteMapActivity.this.currentSnapPosition = position;
                RouteMapActivity.this.recenterMap();
            }
        });
        this.binding.summaryBar.setOnTouchListener(this.dividerTouchListener);
    }

    private void toggleMapSize() {
        if (this.dividerTouchListener == null) {
        }
        switch (this.currentSnapPosition) {
            case MIDDLE:
                this.dividerTouchListener.animateToPosition(DraggableDividerTouchListener.SnapPosition.BOTTOM);
                break;
            case BOTTOM:
                this.dividerTouchListener.animateToPosition(DraggableDividerTouchListener.SnapPosition.TOP);
                break;
            case TOP:
                this.dividerTouchListener.animateToPosition(DraggableDividerTouchListener.SnapPosition.MIDDLE);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recenterMap() {
        if (this.googleMap != null && this.optimizedRoute != null && !this.optimizedRoute.orderedPoints.isEmpty()) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            if (this.currentLocation != null) {
                boundsBuilder.include(new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude()));
            }
            for (RouteOptimizer.RoutePoint point : this.optimizedRoute.orderedPoints) {
                boundsBuilder.include(new LatLng(point.latitude, point.longitude));
            }
            try {
                LatLngBounds bounds = boundsBuilder.build();
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            } catch (Exception e) {
                Log.e(TAG, "Error recentering map", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCallCustomer(RouteOptimizer.RoutePoint stop) {
        String phoneNumber = stop.invoice.getPhone();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            Intent callIntent = new Intent("android.intent.action.DIAL");
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
            return;
        }
        Toast.makeText(this, "No phone number available", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNavigateToStop(RouteOptimizer.RoutePoint stop) {
        String uri = String.format("google.navigation:q=%f,%f", Double.valueOf(stop.latitude), Double.valueOf(stop.longitude));
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            return;
        }
        String fallbackUri = String.format("geo:%f,%f?q=%f,%f(%s)", Double.valueOf(stop.latitude), Double.valueOf(stop.longitude), Double.valueOf(stop.latitude), Double.valueOf(stop.longitude), Uri.encode(stop.invoice.getCustomerName()));
        Intent fallbackIntent = new Intent("android.intent.action.VIEW", Uri.parse(fallbackUri));
        startActivity(fallbackIntent);
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            this.requestLocationPermissionLauncher.launch("android.permission.ACCESS_FINE_LOCATION");
        } else {
            this.googleMap.setMyLocationEnabled(true);
            getCurrentLocationAndOptimize();
        }
    }

    private void getCurrentLocationAndOptimize() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            return;
        }
        this.fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda1
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                RouteMapActivity.this.lambda$getCurrentLocationAndOptimize$8((Location) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda2
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                RouteMapActivity.this.lambda$getCurrentLocationAndOptimize$9(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentLocationAndOptimize$8(Location location) {
        if (location == null) {
            Log.w(TAG, "Location is null, using default");
            optimizeRouteFromDefaultLocation();
        } else {
            this.currentLocation = location;
            Log.d(TAG, "Current location: " + location.getLatitude() + ", " + location.getLongitude());
            optimizeAndDisplayRoute(location.getLatitude(), location.getLongitude());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentLocationAndOptimize$9(Exception e) {
        Log.e(TAG, "Failed to get location", e);
        optimizeRouteFromDefaultLocation();
    }

    private void optimizeRouteFromDefaultLocation() {
        AppSettings settings = AppSettings.getInstance(this);
        optimizeAndDisplayRoute(settings.getWarehouseLat(), settings.getWarehouseLng());
    }

    private void optimizeAndDisplayRoute(final double startLat, final double startLng) {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                RouteMapActivity.this.lambda$optimizeAndDisplayRoute$14(startLat, startLng);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$optimizeAndDisplayRoute$14(final double startLat, final double startLng) {
        try {
            final List<Invoice> allInvoices = this.database.invoiceDao().getAllInvoicesSync();
            if (allInvoices.isEmpty()) {
                runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        RouteMapActivity.this.lambda$optimizeAndDisplayRoute$10();
                    }
                });
                return;
            }
            List<Invoice> activeInvoices = new ArrayList<>();
            for (Invoice invoice : allInvoices) {
                if (invoice.isActive()) {
                    activeInvoices.add(invoice);
                }
            }
            if (activeInvoices.isEmpty()) {
                runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        RouteMapActivity.this.lambda$optimizeAndDisplayRoute$11();
                    }
                });
                return;
            }
            RouteOptimizer optimizer = new RouteOptimizer(this);
            this.optimizedRoute = optimizer.optimizeRoute(activeInvoices, startLat, startLng);
            long startTimeMillis = System.currentTimeMillis();
            RouteOptimizer.calculateETAs(this.optimizedRoute, startLat, startLng, startTimeMillis);
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    RouteMapActivity.this.lambda$optimizeAndDisplayRoute$12(startLat, startLng, allInvoices);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error optimizing route", e);
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    RouteMapActivity.this.lambda$optimizeAndDisplayRoute$13(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$optimizeAndDisplayRoute$10() {
        this.binding.progressBar.setVisibility(8);
        this.binding.tvRouteSummary.setText("No deliveries to route");
        Toast.makeText(this, "No invoices found", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$optimizeAndDisplayRoute$11() {
        this.binding.progressBar.setVisibility(8);
        this.binding.tvRouteSummary.setText("All deliveries completed");
        Toast.makeText(this, "No active deliveries to route", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$optimizeAndDisplayRoute$12(double startLat, double startLng, List allInvoices) {
        this.binding.progressBar.setVisibility(8);
        if (this.optimizedRoute.orderedPoints.isEmpty()) {
            this.binding.tvRouteSummary.setText("Could not geocode any addresses");
            Toast.makeText(this, "No valid addresses found for routing", 0).show();
            return;
        }
        String summary = String.format("Optimized Route\n%d stops • %.1f mi • %s estimated", Integer.valueOf(this.optimizedRoute.totalStops), Double.valueOf(this.optimizedRoute.totalDistance), RouteOptimizer.estimateTravelTime(this.optimizedRoute.totalDistance));
        this.binding.tvRouteSummary.setText(summary);
        displayRouteOnMap(startLat, startLng);
        this.binding.btnStartNavigation.setEnabled(true);
        this.binding.btnReorderList.setEnabled(true);
        List<RouteOptimizer.RoutePoint> allPoints = new ArrayList<>(this.optimizedRoute.orderedPoints);
        Iterator it = allInvoices.iterator();
        while (it.hasNext()) {
            Invoice invoice = (Invoice) it.next();
            if (invoice.isCompleted()) {
                RouteOptimizer.RoutePoint completedPoint = new RouteOptimizer.RoutePoint(invoice, 0.0d, 0.0d, invoice.getAddress());
                completedPoint.orderIndex = 0;
                allPoints.add(completedPoint);
            }
        }
        this.stopAdapter.setStops(allPoints);
        if (!this.optimizedRoute.failedInvoices.isEmpty()) {
            showGeocodingFailuresDialog(this.optimizedRoute.failedInvoices);
        } else {
            Toast.makeText(this, "Route optimized successfully!", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$optimizeAndDisplayRoute$13(Exception e) {
        this.binding.progressBar.setVisibility(8);
        this.binding.tvRouteSummary.setText("Error optimizing route");
        Toast.makeText(this, "Error: " + e.getMessage(), 0).show();
    }

    private void displayRouteOnMap(double startLat, double startLng) {
        if (this.googleMap == null || this.optimizedRoute == null) {
            return;
        }
        this.googleMap.clear();
        List<LatLng> routePoints = new ArrayList<>();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        LatLng startPoint = new LatLng(startLat, startLng);
        this.googleMap.addMarker(new MarkerOptions().position(startPoint).title("Start").icon(BitmapDescriptorFactory.defaultMarker(120.0f)));
        routePoints.add(startPoint);
        boundsBuilder.include(startPoint);
        for (int i = 0; i < this.optimizedRoute.orderedPoints.size(); i++) {
            RouteOptimizer.RoutePoint point = this.optimizedRoute.orderedPoints.get(i);
            LatLng position = new LatLng(point.latitude, point.longitude);
            this.googleMap.addMarker(new MarkerOptions().position(position).title(point.orderIndex + ". " + point.invoice.getCustomerName()).snippet(point.invoice.getAddress() + "\nItems: " + point.invoice.getItems()).icon(BitmapDescriptorFactory.defaultMarker(0.0f)));
            routePoints.add(position);
            boundsBuilder.include(position);
        }
        int i2 = routePoints.size();
        if (i2 > 1) {
            PolylineOptions polylineOptions = new PolylineOptions().addAll(routePoints).color(-16776961).width(10.0f).geodesic(true);
            this.googleMap.addPolyline(polylineOptions);
        }
        try {
            LatLngBounds bounds = boundsBuilder.build();
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        } catch (Exception e) {
            Log.e(TAG, "Error adjusting camera bounds", e);
        }
    }

    private void startGoogleMapsNavigation() {
        if (this.optimizedRoute == null || this.optimizedRoute.orderedPoints.isEmpty()) {
            return;
        }
        StringBuilder url = new StringBuilder("https://www.google.com/maps/dir/?api=1");
        if (this.currentLocation != null) {
            url.append("&origin=").append(this.currentLocation.getLatitude()).append(",").append(this.currentLocation.getLongitude());
        }
        RouteOptimizer.RoutePoint lastPoint = this.optimizedRoute.orderedPoints.get(this.optimizedRoute.orderedPoints.size() - 1);
        url.append("&destination=").append(lastPoint.latitude).append(",").append(lastPoint.longitude);
        if (this.optimizedRoute.orderedPoints.size() > 1) {
            url.append("&waypoints=");
            for (int i = 0; i < this.optimizedRoute.orderedPoints.size() - 1; i++) {
                RouteOptimizer.RoutePoint point = this.optimizedRoute.orderedPoints.get(i);
                if (i > 0) {
                    url.append("|");
                }
                url.append(point.latitude).append(",").append(point.longitude);
            }
        }
        url.append("&travelmode=driving");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url.toString()));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    private void reorderInvoicesInDatabase() {
        if (this.optimizedRoute == null || this.optimizedRoute.orderedPoints.isEmpty()) {
            return;
        }
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                RouteMapActivity.this.lambda$reorderInvoicesInDatabase$17();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reorderInvoicesInDatabase$17() {
        try {
            List<Invoice> updates = new ArrayList<>();
            for (int i = 0; i < this.optimizedRoute.orderedPoints.size(); i++) {
                RouteOptimizer.RoutePoint point = this.optimizedRoute.orderedPoints.get(i);
                point.invoice.setDeliverySequence(i + 1);
                updates.add(point.invoice);
            }
            for (Invoice inv : updates) {
                this.database.invoiceDao().update(inv);
            }
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    RouteMapActivity.this.lambda$reorderInvoicesInDatabase$15();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error reordering invoices", e);
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.RouteMapActivity$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    RouteMapActivity.this.lambda$reorderInvoicesInDatabase$16();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reorderInvoicesInDatabase$15() {
        Toast.makeText(this, "Route locked! " + this.optimizedRoute.totalStops + " stops in sequence", 0).show();
        updateMapAfterReorder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reorderInvoicesInDatabase$16() {
        Toast.makeText(this, "Error saving route order", 0).show();
    }

    private void showGeocodingFailuresDialog(List<RouteOptimizer.GeocodingFailure> failures) {
        StringBuilder message = new StringBuilder();
        message.append("The following invoices could not be added to the route:\n\n");
        for (RouteOptimizer.GeocodingFailure failure : failures) {
            String customerName = failure.invoice.getCustomerName();
            String invoiceNum = failure.invoice.getInvoiceNumber();
            message.append("• ").append(customerName != null ? customerName : "Unknown");
            if (invoiceNum != null && !invoiceNum.isEmpty()) {
                message.append(" (#").append(invoiceNum).append(")");
            }
            message.append("\n   ").append(failure.reason).append("\n\n");
        }
        message.append("Please check the addresses for these invoices.");
        new AlertDialog.Builder(this).setTitle("Missing Stops (" + failures.size() + ")").setMessage(message.toString()).setPositiveButton("OK", (DialogInterface.OnClickListener) null).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
