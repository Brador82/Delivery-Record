package com.mobileinvoice.ocr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mobileinvoice.ocr.DriveHelper;
import com.mobileinvoice.ocr.SettingsActivity;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes7.dex */
public class SettingsActivity extends BaseActivity {
    private static final String[] SERVICE_TYPES = {"Delivery", "Installation", "Exchange", "Pickup", "Service Call"};
    private MaterialButton btnClearAllData;
    private MaterialButton btnDriveConnect;
    private Button btnEta15;
    private Button btnEta30;
    private Button btnEta45;
    private Button btnEta60;
    private MaterialButton btnGeocode;
    private LinearLayout chipBlended;
    private LinearLayout chipDarkMarble;
    private LinearLayout chipLightMarble;
    private TextInputEditText etCompanyName;
    private TextInputEditText etExportFolder;
    private TextInputEditText etWarehouseAddress;
    private RadioButton rbAfterArchive;
    private RadioButton rbAfterAsk;
    private RadioButton rbAfterNavigate;
    private RadioGroup rgAfterDelivery;
    private AppSettings settings;
    private ActivityResultLauncher<Intent> signInLauncher;
    private Spinner spinnerServiceType;
    private TextView tvChipBlendedCheck;
    private TextView tvChipDarkMarbleCheck;
    private TextView tvChipLightMarbleCheck;
    private TextView tvDriveStatus;
    private TextView tvGeoResult;
    private TextView tvVersion;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.settings = AppSettings.getInstance(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda9
            @Override // androidx.activity.result.ActivityResultCallback
            public final void onActivityResult(Object obj) {
                SettingsActivity.this.lambda$onCreate$0((ActivityResult) obj);
            }
        });
        bindViews();
        loadSettings();
        setupListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(ActivityResult result) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        DriveHelper.handleSignInResult(this, task, new AnonymousClass1());
    }

    /* renamed from: com.mobileinvoice.ocr.SettingsActivity$1, reason: invalid class name */
    class AnonymousClass1 implements DriveHelper.Callback {
        AnonymousClass1() {
        }

        @Override // com.mobileinvoice.ocr.DriveHelper.Callback
        public void onSuccess(final String message) {
            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsActivity.AnonymousClass1.this.lambda$onSuccess$0(message);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0(String message) {
            Toast.makeText(SettingsActivity.this, message, 0).show();
            SettingsActivity.this.updateDriveStatus();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1(String error) {
            Toast.makeText(SettingsActivity.this, error, 1).show();
        }

        @Override // com.mobileinvoice.ocr.DriveHelper.Callback
        public void onFailure(final String error) {
            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsActivity.AnonymousClass1.this.lambda$onFailure$1(error);
                }
            });
        }
    }

    private void bindViews() {
        this.etCompanyName = (TextInputEditText) findViewById(R.id.etCompanyName);
        this.etWarehouseAddress = (TextInputEditText) findViewById(R.id.etWarehouseAddress);
        this.tvGeoResult = (TextView) findViewById(R.id.tvGeoResult);
        this.btnGeocode = (MaterialButton) findViewById(R.id.btnGeocode);
        this.btnEta15 = (Button) findViewById(R.id.btnEta15);
        this.btnEta30 = (Button) findViewById(R.id.btnEta30);
        this.btnEta45 = (Button) findViewById(R.id.btnEta45);
        this.btnEta60 = (Button) findViewById(R.id.btnEta60);
        this.spinnerServiceType = (Spinner) findViewById(R.id.spinnerServiceType);
        this.chipDarkMarble = (LinearLayout) findViewById(R.id.chipDarkMarble);
        this.chipLightMarble = (LinearLayout) findViewById(R.id.chipLightMarble);
        this.chipBlended = (LinearLayout) findViewById(R.id.chipBlended);
        this.tvChipDarkMarbleCheck = (TextView) findViewById(R.id.tvChipDarkMarbleCheck);
        this.tvChipLightMarbleCheck = (TextView) findViewById(R.id.tvChipLightMarbleCheck);
        this.tvChipBlendedCheck = (TextView) findViewById(R.id.tvChipBlendedCheck);
        this.etExportFolder = (TextInputEditText) findViewById(R.id.etExportFolder);
        this.tvDriveStatus = (TextView) findViewById(R.id.tvDriveStatus);
        this.btnDriveConnect = (MaterialButton) findViewById(R.id.btnDriveConnect);
        this.rgAfterDelivery = (RadioGroup) findViewById(R.id.rgAfterDelivery);
        this.rbAfterAsk = (RadioButton) findViewById(R.id.rbAfterAsk);
        this.rbAfterArchive = (RadioButton) findViewById(R.id.rbAfterArchive);
        this.rbAfterNavigate = (RadioButton) findViewById(R.id.rbAfterNavigate);
        this.tvVersion = (TextView) findViewById(R.id.tvVersion);
        this.btnClearAllData = (MaterialButton) findViewById(R.id.btnClearAllData);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void loadSettings() {
        char c;
        this.etCompanyName.setText(this.settings.getCompanyName());
        this.etWarehouseAddress.setText(this.settings.getWarehouseAddress());
        double lat = this.settings.getWarehouseLat();
        double lng = this.settings.getWarehouseLng();
        if (lat != 37.1819d || lng != -93.3147d) {
            this.tvGeoResult.setText(String.format(Locale.US, "%.5f, %.5f", Double.valueOf(lat), Double.valueOf(lng)));
        }
        updateEtaButtons(this.settings.getEtaWindowMinutes());
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SERVICE_TYPES);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerServiceType.setAdapter((SpinnerAdapter) serviceAdapter);
        String savedService = this.settings.getDefaultServiceType();
        int i = 0;
        while (true) {
            if (i >= SERVICE_TYPES.length) {
                break;
            }
            if (!SERVICE_TYPES[i].equals(savedService)) {
                i++;
            } else {
                this.spinnerServiceType.setSelection(i);
                break;
            }
        }
        updateThemeChips(this.settings.getAppTheme());
        this.etExportFolder.setText(this.settings.getExportFolderName());
        updateDriveStatus();
        String afterDeliveryAction = this.settings.getAfterDeliveryAction();
        switch (afterDeliveryAction.hashCode()) {
            case -400851711:
                if (afterDeliveryAction.equals(AppSettings.AFTER_DELIVERY_NAVIGATE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1371620914:
                if (afterDeliveryAction.equals(AppSettings.AFTER_DELIVERY_AUTO_ARCHIVE)) {
                    c = 0;
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
                this.rbAfterArchive.setChecked(true);
                break;
            case 1:
                this.rbAfterNavigate.setChecked(true);
                break;
            default:
                this.rbAfterAsk.setChecked(true);
                break;
        }
        String versionName = "1.3.3";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        this.tvVersion.setText("Version " + versionName);
    }

    private void setupListeners() {
        this.etCompanyName.addTextChangedListener(new SimpleWatcher() { // from class: com.mobileinvoice.ocr.SettingsActivity.2
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                SettingsActivity.this.settings.setCompanyName(s.toString().trim());
            }
        });
        this.etWarehouseAddress.addTextChangedListener(new SimpleWatcher() { // from class: com.mobileinvoice.ocr.SettingsActivity.3
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                SettingsActivity.this.settings.setWarehouseAddress(s.toString().trim());
            }
        });
        this.btnGeocode.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$1(view);
            }
        });
        this.btnEta15.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$2(view);
            }
        });
        this.btnEta30.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$3(view);
            }
        });
        this.btnEta45.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$4(view);
            }
        });
        this.btnEta60.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$5(view);
            }
        });
        this.spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.mobileinvoice.ocr.SettingsActivity.4
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                SettingsActivity.this.settings.setDefaultServiceType(SettingsActivity.SERVICE_TYPES[pos]);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> p) {
            }
        });
        this.chipDarkMarble.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$6(view);
            }
        });
        this.chipLightMarble.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$7(view);
            }
        });
        this.chipBlended.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$8(view);
            }
        });
        this.etExportFolder.addTextChangedListener(new SimpleWatcher() { // from class: com.mobileinvoice.ocr.SettingsActivity.5
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                String val = s.toString().trim();
                if (!val.isEmpty()) {
                    SettingsActivity.this.settings.setExportFolderName(val);
                }
            }
        });
        this.btnDriveConnect.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$9(view);
            }
        });
        this.rgAfterDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda4
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                SettingsActivity.this.lambda$setupListeners$10(radioGroup, i);
            }
        });
        this.btnClearAllData.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsActivity.this.lambda$setupListeners$11(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$1(View v) {
        geocodeAddress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View v) {
        this.settings.setEtaWindowMinutes(15);
        updateEtaButtons(15);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View v) {
        this.settings.setEtaWindowMinutes(30);
        updateEtaButtons(30);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$4(View v) {
        this.settings.setEtaWindowMinutes(45);
        updateEtaButtons(45);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$5(View v) {
        this.settings.setEtaWindowMinutes(60);
        updateEtaButtons(60);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$6(View v) {
        applyTheme(AppSettings.THEME_DARK_MARBLE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$7(View v) {
        applyTheme(AppSettings.THEME_LIGHT_MARBLE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$8(View v) {
        applyTheme(AppSettings.THEME_BLENDED);
    }

    /* renamed from: com.mobileinvoice.ocr.SettingsActivity$6, reason: invalid class name */
    class AnonymousClass6 implements DriveHelper.Callback {
        AnonymousClass6() {
        }

        @Override // com.mobileinvoice.ocr.DriveHelper.Callback
        public void onSuccess(final String msg) {
            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$6$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsActivity.AnonymousClass6.this.lambda$onSuccess$0(msg);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0(String msg) {
            Toast.makeText(SettingsActivity.this, msg, 0).show();
            SettingsActivity.this.updateDriveStatus();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1(String err) {
            Toast.makeText(SettingsActivity.this, err, 1).show();
        }

        @Override // com.mobileinvoice.ocr.DriveHelper.Callback
        public void onFailure(final String err) {
            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsActivity.AnonymousClass6.this.lambda$onFailure$1(err);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$9(View v) {
        if (DriveHelper.isSignedIn(this)) {
            DriveHelper.signOut(this, new AnonymousClass6());
        } else if (!DriveHelper.isConfigured(this)) {
            Toast.makeText(this, "Drive not yet configured. Add your OAuth client ID to strings.xml.", 1).show();
        } else {
            this.signInLauncher.launch(DriveHelper.buildSignInClient(this).getSignInIntent());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$10(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rbAfterArchive) {
            this.settings.setAfterDeliveryAction(AppSettings.AFTER_DELIVERY_AUTO_ARCHIVE);
        } else if (checkedId == R.id.rbAfterNavigate) {
            this.settings.setAfterDeliveryAction(AppSettings.AFTER_DELIVERY_NAVIGATE);
        } else {
            this.settings.setAfterDeliveryAction(AppSettings.AFTER_DELIVERY_ASK);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$11(View v) {
        confirmClearAllData();
    }

    private void applyTheme(String theme) {
        this.settings.setAppTheme(theme);
        updateThemeChips(theme);
        BaseActivity.restartApp(this);
    }

    private void updateThemeChips(String current) {
        boolean isLight = true;
        boolean isDark = AppSettings.THEME_DARK_MARBLE.equals(current) || AppSettings.THEME_IMPERIAL_MARBLE.equals(current) || AppSettings.THEME_BLACK_GOLD.equals(current);
        if (!AppSettings.THEME_LIGHT_MARBLE.equals(current) && !AppSettings.THEME_MARBLE.equals(current)) {
            isLight = false;
        }
        boolean isBlended = AppSettings.THEME_BLENDED.equals(current);
        this.tvChipDarkMarbleCheck.setVisibility(isDark ? 0 : 8);
        this.tvChipLightMarbleCheck.setVisibility(isLight ? 0 : 8);
        this.tvChipBlendedCheck.setVisibility(isBlended ? 0 : 8);
    }

    private void updateEtaButtons(int selected) {
        styleEtaBtn(this.btnEta15, selected == 15);
        styleEtaBtn(this.btnEta30, selected == 30);
        styleEtaBtn(this.btnEta45, selected == 45);
        styleEtaBtn(this.btnEta60, selected == 60);
    }

    private void styleEtaBtn(Button btn, boolean active) {
        if (active) {
            btn.setBackgroundColor(-2838729);
            btn.setTextColor(-15066598);
        } else {
            btn.setBackgroundColor(-14013910);
            btn.setTextColor(-2838729);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDriveStatus() {
        if (DriveHelper.isSignedIn(this)) {
            String email = DriveHelper.getSignedInEmail(this);
            this.tvDriveStatus.setText("● " + email);
            this.tvDriveStatus.setTextColor(-11751600);
            this.btnDriveConnect.setText("DISCONNECT");
            this.btnDriveConnect.setBackgroundTintList(ColorStateList.valueOf(-11184811));
            this.btnDriveConnect.setTextColor(-1);
            return;
        }
        this.tvDriveStatus.setText("Not connected");
        this.tvDriveStatus.setTextColor(-5592406);
        this.btnDriveConnect.setText("CONNECT GOOGLE DRIVE");
        this.btnDriveConnect.setBackgroundTintList(ColorStateList.valueOf(-2838729));
        this.btnDriveConnect.setTextColor(-15066598);
    }

    private void geocodeAddress() {
        String address;
        if (this.etWarehouseAddress.getText() != null) {
            address = this.etWarehouseAddress.getText().toString().trim();
        } else {
            address = "";
        }
        if (address.isEmpty()) {
            this.tvGeoResult.setText("Enter an address first.");
            return;
        }
        this.tvGeoResult.setText("Geocoding...");
        final String addr = address;
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SettingsActivity.this.lambda$geocodeAddress$15(addr);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$geocodeAddress$15(String addr) {
        try {
            Geocoder geo = new Geocoder(this, Locale.getDefault());
            List<Address> results = geo.getFromLocationName(addr, 1);
            if (results != null && !results.isEmpty()) {
                final double lat = results.get(0).getLatitude();
                final double lng = results.get(0).getLongitude();
                this.settings.setWarehouseLat(lat);
                this.settings.setWarehouseLng(lng);
                runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        SettingsActivity.this.lambda$geocodeAddress$12(lat, lng);
                    }
                });
            } else {
                runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        SettingsActivity.this.lambda$geocodeAddress$13();
                    }
                });
            }
        } catch (IOException e) {
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsActivity.this.lambda$geocodeAddress$14(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$geocodeAddress$12(double lat, double lng) {
        this.tvGeoResult.setText(String.format(Locale.US, "✓ %.5f, %.5f", Double.valueOf(lat), Double.valueOf(lng)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$geocodeAddress$13() {
        this.tvGeoResult.setText("Address not found.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$geocodeAddress$14(IOException e) {
        this.tvGeoResult.setText("Geocoder error: " + e.getMessage());
    }

    private void confirmClearAllData() {
        new AlertDialog.Builder(this).setTitle("Clear All Data").setMessage("This will permanently delete all invoices. This cannot be undone.").setPositiveButton("DELETE EVERYTHING", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SettingsActivity.this.lambda$confirmClearAllData$18(dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$confirmClearAllData$18(DialogInterface dialog, int which) {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SettingsActivity.this.lambda$confirmClearAllData$17();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$confirmClearAllData$17() {
        InvoiceDatabase.getInstance(this).clearAllTables();
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.SettingsActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                SettingsActivity.this.lambda$confirmClearAllData$16();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$confirmClearAllData$16() {
        Toast.makeText(this, "All data cleared.", 0).show();
        finish();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static abstract class SimpleWatcher implements TextWatcher {
        private SimpleWatcher() {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
