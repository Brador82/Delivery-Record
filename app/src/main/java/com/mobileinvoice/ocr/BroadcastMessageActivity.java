package com.mobileinvoice.ocr;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import com.mobileinvoice.ocr.RouteOptimizer;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityBroadcastMessageBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes7.dex */
public class BroadcastMessageActivity extends BaseActivity {
    private static final String KEY_LAST_AM_BROADCAST_DATE = "last_am_broadcast_date";
    private static final String KEY_LAST_PM_BROADCAST_DATE = "last_pm_broadcast_date";
    private static final String MAPS_API_KEY = "AIzaSyBFCX7mXQi-BijDfQlIa8nC6EbOJaT2uv4";
    private static final String PREFS_NAME = "broadcast_prefs";
    private static final String TAG = "BroadcastMessage";
    private ActivityBroadcastMessageBinding binding;
    private InvoiceDatabase database;
    private RouteOptimizer.OptimizedRoute optimizedRoute;
    private ActivityResultLauncher<String> requestSmsPermissionLauncher;
    private String routeLink;
    private List<Invoice> activeInvoices = new ArrayList();
    private boolean isSending = false;
    private boolean pendingSendIsPM = false;
    private final List<Boolean> customerIsPM = new ArrayList();

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityBroadcastMessageBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Broadcast Message");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.database = InvoiceDatabase.getInstance(this);
        setupPermissionLauncher();
        this.binding.btnSendAM.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BroadcastMessageActivity.this.lambda$onCreate$0(view);
            }
        });
        this.binding.btnSendPM.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BroadcastMessageActivity.this.lambda$onCreate$1(view);
            }
        });
        prepareRouteData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View v) {
        onSendClicked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View v) {
        onSendClicked(true);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupPermissionLauncher() {
        this.requestSmsPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda13
            @Override // androidx.activity.result.ActivityResultCallback
            public void onActivityResult(Object obj) {
                BroadcastMessageActivity.this.lambda$setupPermissionLauncher$2((Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupPermissionLauncher$2(Boolean isGranted) {
        if (isGranted.booleanValue()) {
            proceedWithSend(this.pendingSendIsPM);
        } else {
            Toast.makeText(this, "SMS permission is required to send broadcast messages", 1).show();
        }
    }

    private void prepareRouteData() {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public void run() {
                BroadcastMessageActivity.this.lambda$prepareRouteData$6();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareRouteData$6() {
        List<Invoice> allInvoices = this.database.invoiceDao().getAllInvoicesSync();
        final List<Invoice> active = new ArrayList<>();
        for (Invoice inv : allInvoices) {
            if (!inv.isCompleted() && inv.getPhone() != null && !inv.getPhone().trim().isEmpty() && !"No phone".equals(inv.getPhone().trim())) {
                active.add(inv);
            }
        }
        this.activeInvoices = active;
        if (active.isEmpty()) {
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public void run() {
                    BroadcastMessageActivity.this.lambda$prepareRouteData$3();
                }
            });
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public void run() {
                BroadcastMessageActivity.this.lambda$prepareRouteData$4();
            }
        });
        try {
            RouteOptimizer optimizer = new RouteOptimizer(this);
            double warehouseLat = AppSettings.getInstance(this).getWarehouseLat();
            double warehouseLng = AppSettings.getInstance(this).getWarehouseLng();
            this.optimizedRoute = optimizer.optimizeRoute(active, warehouseLat, warehouseLng);
            RouteOptimizer.calculateETAs(this.optimizedRoute, warehouseLat, warehouseLng, System.currentTimeMillis());
            this.routeLink = buildRouteLink();
        } catch (Exception e) {
            Log.e(TAG, "Route optimization failed: " + e.getMessage());
            this.optimizedRoute = null;
        }
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public void run() {
                BroadcastMessageActivity.this.lambda$prepareRouteData$5(active);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareRouteData$3() {
        this.binding.loadingSection.setVisibility(8);
        this.binding.tvSubtitle.setText("No active customers with phone numbers found.");
        this.binding.contentSection.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareRouteData$4() {
        this.binding.tvLoadingStatus.setText("Geocoding addresses...");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareRouteData$5(List active) {
        this.binding.loadingSection.setVisibility(8);
        this.binding.contentSection.setVisibility(0);
        this.binding.tvSubtitle.setText(active.size() + " customer" + (active.size() != 1 ? "s" : "") + " in today's route");
        if (this.optimizedRoute != null && !this.optimizedRoute.orderedPoints.isEmpty()) {
            RouteOptimizer.RoutePoint firstStop = this.optimizedRoute.orderedPoints.get(0);
            String previewMsg = buildMessageForStop(firstStop, 1, this.optimizedRoute.orderedPoints.size(), false);
            this.binding.tvPreview.setText(previewMsg);
        } else {
            this.binding.tvPreview.setText(buildFallbackMessage((Invoice) active.get(0)));
        }
        populateCustomerList();
        updateSendButtonCounts();
    }

    private String buildMessageForStop(RouteOptimizer.RoutePoint stop, int position, int totalStops, boolean isPM) {
        StringBuilder msg = new StringBuilder();
        msg.append("Hello! This is your delivery team from ").append(AppSettings.getInstance(this).getCompanyName()).append(".");
        msg.append("\n\nYou are #").append(position).append(" of ").append(totalStops);
        msg.append(isPM ? " in today's afternoon delivery sequence." : " in today's morning delivery sequence.");
        if (stop.etaMillis > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            String etaStart = sdf.format(new Date(stop.etaMillis));
            int etaWindow = AppSettings.getInstance(this).getEtaWindowMinutes();
            String etaEnd = sdf.format(new Date(stop.etaMillis + ((long) etaWindow * 60 * 1000)));
            msg.append("\n\nEstimated arrival: ").append(etaStart).append("–").append(etaEnd);
        }
        if (this.routeLink != null) {
            msg.append("\n\nClick here for live directions:\n").append(this.routeLink);
        }
        msg.append("\n\nWe will call you when we are on our way!");
        return msg.toString();
    }

    private String buildFallbackMessage(Invoice invoice) {
        return "Hello! This is your delivery team from " + AppSettings.getInstance(this).getCompanyName() + ".\n\nYour delivery is scheduled for today. We will call you when we are on our way!";
    }

    private void populateCustomerList() {
        this.binding.customerList.removeAllViews();
        this.customerIsPM.clear();
        if (this.optimizedRoute != null && !this.optimizedRoute.orderedPoints.isEmpty()) {
            for (int i = 0; i < this.optimizedRoute.orderedPoints.size(); i++) {
                this.customerIsPM.add(false);
                RouteOptimizer.RoutePoint stop = this.optimizedRoute.orderedPoints.get(i);
                addCustomerRow(i + 1, stop.invoice.getCustomerName(), stop.getFormattedETA(), stop.invoice.getPhone());
            }
            return;
        }
        for (int i2 = 0; i2 < this.activeInvoices.size(); i2++) {
            this.customerIsPM.add(false);
            Invoice inv = this.activeInvoices.get(i2);
            addCustomerRow(i2 + 1, inv.getCustomerName(), "N/A", inv.getPhone());
        }
    }

    private void addCustomerRow(int position, String name, String eta, String phone) {
        final int idx = position - 1;
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(0, 8, 0, 8);
        TextView tvPos = new TextView(this);
        tvPos.setText("#" + position);
        tvPos.setTextColor(-2838729);
        tvPos.setTextSize(15.0f);
        tvPos.setTypeface(null, 1);
        tvPos.setWidth(48);
        row.addView(tvPos);
        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(-530482);
        tvName.setTextSize(14.0f);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        row.addView(tvName);
        TextView tvEta = new TextView(this);
        tvEta.setText((eta == null || eta.equals("N/A")) ? "--" : eta);
        tvEta.setTextColor(-2838729);
        tvEta.setTextSize(13.0f);
        tvEta.setGravity(GravityCompat.END);
        tvEta.setPadding(0, 0, 12, 0);
        row.addView(tvEta);
        final TextView tvToggle = new TextView(this);
        tvToggle.setPadding(18, 6, 18, 6);
        tvToggle.setTextSize(12.0f);
        tvToggle.setTypeface(null, 1);
        tvToggle.setClickable(true);
        tvToggle.setFocusable(true);
        applyToggleStyle(tvToggle, false);
        tvToggle.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BroadcastMessageActivity.this.lambda$addCustomerRow$7(idx, tvToggle, view);
            }
        });
        row.addView(tvToggle);
        this.binding.customerList.addView(row);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addCustomerRow$7(int idx, TextView tvToggle, View v) {
        boolean nowPM = !this.customerIsPM.get(idx).booleanValue();
        this.customerIsPM.set(idx, Boolean.valueOf(nowPM));
        applyToggleStyle(tvToggle, nowPM);
        updateSendButtonCounts();
    }

    private void applyToggleStyle(TextView tv, boolean isPM) {
        if (isPM) {
            tv.setText("PM");
            tv.setTextColor(-15066598);
            tv.setBackgroundColor(-2838729);
        } else {
            tv.setText("AM");
            tv.setTextColor(-2838729);
            tv.setBackgroundColor(584363831);
        }
    }

    private void updateSendButtonCounts() {
        int amCount = 0;
        int pmCount = 0;
        Iterator<Boolean> it = this.customerIsPM.iterator();
        while (it.hasNext()) {
            boolean isPM = it.next().booleanValue();
            if (isPM) {
                pmCount++;
            } else {
                amCount++;
            }
        }
        this.binding.btnSendAM.setText("SEND AM (" + amCount + ")");
        this.binding.btnSendPM.setText("SEND PM (" + pmCount + ")");
    }

    private void onSendClicked(boolean sendPM) {
        String label;
        if (this.isSending) {
            return;
        }
        if (this.activeInvoices.isEmpty()) {
            Toast.makeText(this, "No active customers with phone numbers", 0).show();
            return;
        }
        int count = 0;
        Iterator<Boolean> it = this.customerIsPM.iterator();
        while (it.hasNext()) {
            boolean isPM = it.next().booleanValue();
            if (isPM == sendPM) {
                count++;
            }
        }
        if (count == 0) {
            StringBuilder append = new StringBuilder().append("No customers assigned to ");
            label = sendPM ? "PM" : "AM";
            Toast.makeText(this, append.append(label).toString(), 0).show();
            return;
        }
        this.pendingSendIsPM = sendPM;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        String key = sendPM ? KEY_LAST_PM_BROADCAST_DATE : KEY_LAST_AM_BROADCAST_DATE;
        String lastDate = prefs.getString(key, "");
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        label = sendPM ? "PM" : "AM";
        if (today.equals(lastDate)) {
            new AlertDialog.Builder(this).setTitle("Already Sent " + label + " Today").setMessage("You've already sent the " + label + " broadcast today. Send again?").setPositiveButton("Send Again", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    BroadcastMessageActivity.this.lambda$onSendClicked$8(dialogInterface, i);
                }
            }).setNegativeButton("Cancel", null).show();
        } else {
            checkPermissionAndSend();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendClicked$8(DialogInterface dialog, int which) {
        checkPermissionAndSend();
    }

    private void checkPermissionAndSend() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.SEND_SMS") != 0) {
            this.requestSmsPermissionLauncher.launch("android.permission.SEND_SMS");
        } else {
            proceedWithSend(this.pendingSendIsPM);
        }
    }

    private void proceedWithSend(final boolean sendPM) {
        this.isSending = true;
        this.binding.btnSendAM.setEnabled(false);
        this.binding.btnSendPM.setEnabled(false);
        this.binding.sendingProgress.setVisibility(0);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public void run() {
                BroadcastMessageActivity.this.lambda$proceedWithSend$12(sendPM);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$proceedWithSend$12(boolean sendPM) {
        int sentCount;
        int failCount;
        final List<String> failedCustomers = new ArrayList<>();
        if (this.optimizedRoute != null && !this.optimizedRoute.orderedPoints.isEmpty()) {
            List<RouteOptimizer.RoutePoint> filteredStops = new ArrayList<>();
            int i = 0;
            while (i < this.optimizedRoute.orderedPoints.size()) {
                boolean isPM = i < this.customerIsPM.size() && this.customerIsPM.get(i).booleanValue();
                if (isPM == sendPM) {
                    filteredStops.add(this.optimizedRoute.orderedPoints.get(i));
                }
                i++;
            }
            final int total = filteredStops.size();
            failCount = 0;
            sentCount = 0;
            for (int i2 = 0; i2 < total; i2++) {
                RouteOptimizer.RoutePoint stop = filteredStops.get(i2);
                final int progress = i2 + 1;
                runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastMessageActivity.this.lambda$proceedWithSend$9(progress, total);
                    }
                });
                String message = buildMessageForStop(stop, i2 + 1, total, sendPM);
                try {
                    sendSms(stop.invoice.getPhone(), message);
                    sentCount++;
                } catch (Exception e) {
                    Log.e(TAG, "Failed: " + stop.invoice.getCustomerName() + " - " + e.getMessage());
                    failCount++;
                    failedCustomers.add(stop.invoice.getCustomerName());
                    sentCount = sentCount;
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e2) {
                }
            }
        } else {
            List<Invoice> filtered = new ArrayList<>();
            int i3 = 0;
            while (i3 < this.activeInvoices.size()) {
                boolean isPM2 = i3 < this.customerIsPM.size() && this.customerIsPM.get(i3).booleanValue();
                if (isPM2 == sendPM) {
                    filtered.add(this.activeInvoices.get(i3));
                }
                i3++;
            }
            final int total2 = filtered.size();
            int failCount2 = 0;
            sentCount = 0;
            for (int i4 = 0; i4 < total2; i4++) {
                Invoice inv = filtered.get(i4);
                final int progress2 = i4 + 1;
                runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastMessageActivity.this.lambda$proceedWithSend$10(progress2, total2);
                    }
                });
                String message2 = buildFallbackMessage(inv);
                try {
                    sendSms(inv.getPhone(), message2);
                    sentCount++;
                } catch (Exception e3) {
                    Log.e(TAG, "Failed: " + inv.getCustomerName() + " - " + e3.getMessage());
                    failCount2++;
                    failedCustomers.add(inv.getCustomerName());
                    sentCount = sentCount;
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e4) {
                }
            }
            failCount = failCount2;
        }
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        String saveKey = sendPM ? KEY_LAST_PM_BROADCAST_DATE : KEY_LAST_AM_BROADCAST_DATE;
        prefs.edit().putString(saveKey, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())).apply();
        final int finalSent = sentCount;
        final int finalFailed = failCount;
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public void run() {
                BroadcastMessageActivity.this.lambda$proceedWithSend$11(finalSent, finalFailed, failedCustomers);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$proceedWithSend$9(int progress, int total) {
        this.binding.tvSendingStatus.setText("Sending " + progress + " of " + total + "...");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$proceedWithSend$10(int progress, int total) {
        this.binding.tvSendingStatus.setText("Sending " + progress + " of " + total + "...");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$proceedWithSend$11(int finalSent, int finalFailed, List finalFailedCustomers) {
        this.isSending = false;
        this.binding.sendingProgress.setVisibility(8);
        this.binding.btnSendAM.setEnabled(true);
        this.binding.btnSendPM.setEnabled(true);
        showSendSummary(finalSent, finalFailed, finalFailedCustomers);
    }

    private String buildRouteLink() {
        if (this.optimizedRoute == null || this.optimizedRoute.orderedPoints.isEmpty()) {
            return null;
        }
        StringBuilder url = new StringBuilder("https://www.google.com/maps/dir/?api=1");
        url.append("&origin=").append(AppSettings.getInstance(this).getWarehouseLat()).append(",").append(AppSettings.getInstance(this).getWarehouseLng());
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
        return url.toString();
    }

    private void sendSms(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        String cleanPhone = phoneNumber.replaceAll("[^0-9+]", "");
        if (cleanPhone.isEmpty()) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        ArrayList<String> parts = smsManager.divideMessage(message);
        if (parts.size() > 1) {
            ArrayList<PendingIntent> sentIntents = new ArrayList<>();
            for (int i = 0; i < parts.size(); i++) {
                sentIntents.add(PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL));
            }
            smsManager.sendMultipartTextMessage(cleanPhone, null, parts, sentIntents, null);
        } else {
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
            smsManager.sendTextMessage(cleanPhone, null, message, sentPI, null);
        }
        Log.d(TAG, "SMS sent to: " + cleanPhone);
    }

    private void showSendSummary(int sent, final int failed, List<String> failedCustomers) {
        StringBuilder summary = new StringBuilder();
        summary.append("Sent successfully: ").append(sent);
        if (failed > 0) {
            summary.append("\nFailed: ").append(failed);
            summary.append("\n\nFailed customers:");
            for (String name : failedCustomers) {
                summary.append("\n  • ").append(name);
            }
        }
        new AlertDialog.Builder(this).setTitle(failed == 0 ? "Broadcast Complete" : "Broadcast Complete (with errors)").setMessage(summary.toString()).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.BroadcastMessageActivity$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                BroadcastMessageActivity.this.lambda$showSendSummary$13(failed, dialogInterface, i);
            }
        }).setIcon(failed == 0 ? android.R.drawable.ic_dialog_info : android.R.drawable.ic_dialog_alert).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSendSummary$13(int failed, DialogInterface dialog, int which) {
        if (failed == 0) {
            finish();
        }
    }
}
