package com.mobileinvoice.ocr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityInvoiceDetailBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes7.dex */
public class InvoiceDetailActivity extends BaseActivity {
    private static final String[] AVAILABLE_ITEMS = {"Washer", "Dryer", "Refrigerator", "Dishwasher", "Freezer", "Range", "Washtower", "Microwave", "Other"};
    private ActivityInvoiceDetailBinding binding;
    private Invoice currentInvoice;
    private InvoiceDatabase database;
    private ActivityResultLauncher<Intent> extractionLauncher;
    private int invoiceId;
    private ActivityResultLauncher<Intent> podCameraLauncher;
    private ActivityResultLauncher<Intent> podGalleryLauncher;
    private String podImagePath1;
    private String podImagePath2;
    private String podImagePath3;
    private String podImagePath4;
    private String podImagePath5;
    private String podImagePath6;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<Intent> signatureLauncher;
    private String signaturePath;
    private List<DeliveryItem> selectedItems = new ArrayList();
    private AlertDialog currentItemDialog = null;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityInvoiceDetailBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.database = InvoiceDatabase.getInstance(this);
        setupActivityResultLaunchers();
        loadInvoiceFromDatabase();
        setupClickListeners();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        autoSaveInvoiceData();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        autoSaveInvoiceData();
        super.onBackPressed();
    }

    private void setupActivityResultLaunchers() {
        this.signatureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda10
            @Override // androidx.activity.result.ActivityResultCallback
            public void onActivityResult(Object obj) {
                InvoiceDetailActivity.this.lambda$setupActivityResultLaunchers$0((ActivityResult) obj);
            }
        });
        this.podCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda12
            @Override // androidx.activity.result.ActivityResultCallback
            public void onActivityResult(Object obj) {
                InvoiceDetailActivity.this.lambda$setupActivityResultLaunchers$1((ActivityResult) obj);
            }
        });
        this.podGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda13
            @Override // androidx.activity.result.ActivityResultCallback
            public void onActivityResult(Object obj) {
                InvoiceDetailActivity.this.lambda$setupActivityResultLaunchers$2((ActivityResult) obj);
            }
        });
        this.extractionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda14
            @Override // androidx.activity.result.ActivityResultCallback
            public void onActivityResult(Object obj) {
                InvoiceDetailActivity.this.lambda$setupActivityResultLaunchers$3((ActivityResult) obj);
            }
        });
        this.requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda15
            @Override // androidx.activity.result.ActivityResultCallback
            public void onActivityResult(Object obj) {
                InvoiceDetailActivity.this.lambda$setupActivityResultLaunchers$4((Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$0(ActivityResult result) {
        if (result.getResultCode() == -1 && result.getData() != null) {
            String formPath = result.getData().getStringExtra("form_path");
            this.signaturePath = formPath != null ? formPath : result.getData().getStringExtra("signature_path");
            Uri signatureUri = result.getData().getData();
            if (signatureUri != null) {
                this.binding.ivSignature.setImageURI(signatureUri);
                this.binding.ivSignature.setVisibility(0);
                this.binding.btnCaptureSignature.setText("Change Signature");
                Toast.makeText(this, "Delivery form saved!", 0).show();
                return;
            }
            if (this.signaturePath != null) {
                File sigFile = new File(this.signaturePath);
                if (sigFile.exists()) {
                    this.binding.ivSignature.setImageURI(Uri.fromFile(sigFile));
                    this.binding.ivSignature.setVisibility(0);
                    this.binding.btnCaptureSignature.setText("Change Signature");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$1(ActivityResult result) {
        Uri imageUri;
        if (result.getResultCode() == -1 && result.getData() != null && (imageUri = result.getData().getData()) != null) {
            handlePODImage(imageUri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$2(ActivityResult result) {
        Uri imageUri;
        if (result.getResultCode() == -1 && result.getData() != null && (imageUri = result.getData().getData()) != null) {
            handlePODImage(imageUri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$3(ActivityResult result) {
        if (result.getResultCode() == -1) {
            reloadInvoiceFromDatabase();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$4(Boolean isGranted) {
        if (isGranted.booleanValue()) {
            openPODCamera();
        } else {
            Toast.makeText(this, "Camera permission required for POD capture", 0).show();
        }
    }

    private void handlePODImage(Uri imageUri) {
        if (this.podImagePath1 == null) {
            this.podImagePath1 = saveImageToStorage(imageUri, "pod1_" + this.invoiceId + ".jpg");
            if (this.podImagePath1 != null) {
                this.binding.ivPod1.setImageURI(Uri.parse(this.podImagePath1));
                this.binding.ivPod1.setVisibility(0);
                updatePODButtonText();
                Toast.makeText(this, "POD photo 1 captured!", 0).show();
                return;
            }
            return;
        }
        if (this.podImagePath2 == null) {
            this.podImagePath2 = saveImageToStorage(imageUri, "pod2_" + this.invoiceId + ".jpg");
            if (this.podImagePath2 != null) {
                this.binding.ivPod2.setImageURI(Uri.parse(this.podImagePath2));
                this.binding.ivPod2.setVisibility(0);
                updatePODButtonText();
                Toast.makeText(this, "POD photo 2 captured!", 0).show();
                return;
            }
            return;
        }
        if (this.podImagePath3 == null) {
            this.podImagePath3 = saveImageToStorage(imageUri, "pod3_" + this.invoiceId + ".jpg");
            if (this.podImagePath3 != null) {
                this.binding.ivPod3.setImageURI(Uri.parse(this.podImagePath3));
                this.binding.ivPod3.setVisibility(0);
                updatePODButtonText();
                Toast.makeText(this, "POD photo 3 captured!", 0).show();
                return;
            }
            return;
        }
        if (this.podImagePath4 == null) {
            this.podImagePath4 = saveImageToStorage(imageUri, "pod4_" + this.invoiceId + ".jpg");
            if (this.podImagePath4 != null) {
                this.binding.ivPod4.setImageURI(Uri.parse(this.podImagePath4));
                this.binding.ivPod4.setVisibility(0);
                updatePODButtonText();
                Toast.makeText(this, "POD photo 4 captured!", 0).show();
                return;
            }
            return;
        }
        if (this.podImagePath5 == null) {
            this.podImagePath5 = saveImageToStorage(imageUri, "pod5_" + this.invoiceId + ".jpg");
            if (this.podImagePath5 != null) {
                this.binding.ivPod5.setImageURI(Uri.parse(this.podImagePath5));
                this.binding.ivPod5.setVisibility(0);
                updatePODButtonText();
                Toast.makeText(this, "POD photo 5 captured!", 0).show();
                return;
            }
            return;
        }
        if (this.podImagePath6 == null) {
            this.podImagePath6 = saveImageToStorage(imageUri, "pod6_" + this.invoiceId + ".jpg");
            if (this.podImagePath6 != null) {
                this.binding.ivPod6.setImageURI(Uri.parse(this.podImagePath6));
                this.binding.ivPod6.setVisibility(0);
                updatePODButtonText();
                Toast.makeText(this, "POD photo 6 captured!", 0).show();
                return;
            }
            return;
        }
        Toast.makeText(this, "Maximum 6 POD photos. Tap a photo to replace it.", 1).show();
    }

    private void loadInvoiceFromDatabase() {
        this.invoiceId = getIntent().getIntExtra("invoice_id", 0);
        if (this.invoiceId > 0) {
            new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public void run() {
                    InvoiceDetailActivity.this.lambda$loadInvoiceFromDatabase$6();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoiceFromDatabase$6() {
        this.currentInvoice = this.database.invoiceDao().getInvoiceByIdSync(this.invoiceId);
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public void run() {
                InvoiceDetailActivity.this.lambda$loadInvoiceFromDatabase$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoiceFromDatabase$5() {
        if (this.currentInvoice != null) {
            this.binding.etInvoiceNumber.setText(this.currentInvoice.getInvoiceNumber());
            this.binding.etCustomerName.setText(this.currentInvoice.getCustomerName());
            this.binding.etAddress.setText(this.currentInvoice.getAddress());
            this.binding.etPhone.setText(this.currentInvoice.getPhone());
            this.binding.etNotes.setText(this.currentInvoice.getNotes());
            if (this.currentInvoice.getItems() != null && !this.currentInvoice.getItems().isEmpty()) {
                this.selectedItems = ItemsHelper.fromJson(this.currentInvoice.getItems());
                updateSelectedItemsDisplay();
            }
            if (this.currentInvoice.getSignatureImagePath() != null) {
                this.signaturePath = this.currentInvoice.getSignatureImagePath();
                File sigFile = new File(this.signaturePath);
                if (sigFile.exists()) {
                    this.binding.ivSignature.setImageURI(Uri.fromFile(sigFile));
                    this.binding.ivSignature.setVisibility(0);
                    this.binding.btnCaptureSignature.setText("Change Signature");
                }
            }
            if (this.currentInvoice.getPodImagePath1() != null) {
                this.podImagePath1 = this.currentInvoice.getPodImagePath1();
                File podFile = new File(this.podImagePath1);
                if (podFile.exists()) {
                    this.binding.ivPod1.setImageURI(Uri.fromFile(podFile));
                    this.binding.ivPod1.setVisibility(0);
                }
            }
            if (this.currentInvoice.getPodImagePath2() != null) {
                this.podImagePath2 = this.currentInvoice.getPodImagePath2();
                File podFile2 = new File(this.podImagePath2);
                if (podFile2.exists()) {
                    this.binding.ivPod2.setImageURI(Uri.fromFile(podFile2));
                    this.binding.ivPod2.setVisibility(0);
                }
            }
            if (this.currentInvoice.getPodImagePath3() != null) {
                this.podImagePath3 = this.currentInvoice.getPodImagePath3();
                File podFile3 = new File(this.podImagePath3);
                if (podFile3.exists()) {
                    this.binding.ivPod3.setImageURI(Uri.fromFile(podFile3));
                    this.binding.ivPod3.setVisibility(0);
                }
            }
            if (this.currentInvoice.getPodImagePath4() != null) {
                this.podImagePath4 = this.currentInvoice.getPodImagePath4();
                File podFile4 = new File(this.podImagePath4);
                if (podFile4.exists()) {
                    this.binding.ivPod4.setImageURI(Uri.fromFile(podFile4));
                    this.binding.ivPod4.setVisibility(0);
                }
            }
            if (this.currentInvoice.getPodImagePath5() != null) {
                this.podImagePath5 = this.currentInvoice.getPodImagePath5();
                File podFile5 = new File(this.podImagePath5);
                if (podFile5.exists()) {
                    this.binding.ivPod5.setImageURI(Uri.fromFile(podFile5));
                    this.binding.ivPod5.setVisibility(0);
                }
            }
            if (this.currentInvoice.getPodImagePath6() != null) {
                this.podImagePath6 = this.currentInvoice.getPodImagePath6();
                File podFile6 = new File(this.podImagePath6);
                if (podFile6.exists()) {
                    this.binding.ivPod6.setImageURI(Uri.fromFile(podFile6));
                    this.binding.ivPod6.setVisibility(0);
                }
            }
            updatePODButtonText();
            if (this.currentInvoice.getOriginalImagePath() != null) {
                loadInvoiceImage(this.currentInvoice.getOriginalImagePath());
            }
        }
    }

    private void reloadInvoiceFromDatabase() {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public void run() {
                InvoiceDetailActivity.this.lambda$reloadInvoiceFromDatabase$8();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadInvoiceFromDatabase$8() {
        this.currentInvoice = this.database.invoiceDao().getInvoiceByIdSync(this.invoiceId);
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public void run() {
                InvoiceDetailActivity.this.lambda$reloadInvoiceFromDatabase$7();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadInvoiceFromDatabase$7() {
        if (this.currentInvoice != null) {
            this.binding.etInvoiceNumber.setText(this.currentInvoice.getInvoiceNumber());
            this.binding.etCustomerName.setText(this.currentInvoice.getCustomerName());
            this.binding.etAddress.setText(this.currentInvoice.getAddress());
            this.binding.etPhone.setText(this.currentInvoice.getPhone());
            this.binding.etNotes.setText(this.currentInvoice.getNotes());
            this.selectedItems.clear();
            if (this.currentInvoice.getItems() != null && !this.currentInvoice.getItems().isEmpty()) {
                this.selectedItems = ItemsHelper.fromJson(this.currentInvoice.getItems());
            }
            updateSelectedItemsDisplay();
            Toast.makeText(this, "Fields updated from extraction", 0).show();
        }
    }

    private void loadInvoiceImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                this.binding.ivInvoiceImage.setImageURI(Uri.fromFile(imageFile));
                this.binding.cardInvoiceImage.setVisibility(0);
            } else if (imagePath.startsWith("content://")) {
                this.binding.ivInvoiceImage.setImageURI(Uri.parse(imagePath));
                this.binding.cardInvoiceImage.setVisibility(0);
            }
        } catch (Exception e) {
            Log.e("InvoiceDetail", "Failed to load invoice image: " + e.getMessage());
        }
    }

    private void setupClickListeners() {
        this.binding.btnCaptureSignature.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$9(view);
            }
        });
        this.binding.btnCapturePOD.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$11(view);
            }
        });
        this.binding.ivPod1.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda31
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                boolean lambda$setupClickListeners$12;
                lambda$setupClickListeners$12 = InvoiceDetailActivity.this.lambda$setupClickListeners$12(view);
                return lambda$setupClickListeners$12;
            }
        });
        this.binding.ivPod2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                boolean lambda$setupClickListeners$13;
                lambda$setupClickListeners$13 = InvoiceDetailActivity.this.lambda$setupClickListeners$13(view);
                return lambda$setupClickListeners$13;
            }
        });
        this.binding.ivPod3.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                boolean lambda$setupClickListeners$14;
                lambda$setupClickListeners$14 = InvoiceDetailActivity.this.lambda$setupClickListeners$14(view);
                return lambda$setupClickListeners$14;
            }
        });
        this.binding.ivPod4.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                boolean lambda$setupClickListeners$15;
                lambda$setupClickListeners$15 = InvoiceDetailActivity.this.lambda$setupClickListeners$15(view);
                return lambda$setupClickListeners$15;
            }
        });
        this.binding.ivPod5.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                boolean lambda$setupClickListeners$16;
                lambda$setupClickListeners$16 = InvoiceDetailActivity.this.lambda$setupClickListeners$16(view);
                return lambda$setupClickListeners$16;
            }
        });
        this.binding.ivPod6.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                boolean lambda$setupClickListeners$17;
                lambda$setupClickListeners$17 = InvoiceDetailActivity.this.lambda$setupClickListeners$17(view);
                return lambda$setupClickListeners$17;
            }
        });
        this.binding.tvSelectedItems.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$18(view);
            }
        });
        this.binding.tilAddress.setEndIconOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$19(view);
            }
        });
        this.binding.tilPhone.setEndIconOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda27
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$20(view);
            }
        });
        this.binding.cardInvoiceImage.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$21(view);
            }
        });
        this.binding.btnSave.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$22(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$9(View v) {
        Intent intent = new Intent(this, SignatureActivity.class);
        if (this.currentInvoice != null) {
            intent.putExtra("customer_name", this.currentInvoice.getCustomerName());
            intent.putExtra("invoice_id", this.currentInvoice.getId());
        }
        if (this.currentInvoice != null && this.currentInvoice.getOriginalImagePath() != null) {
            String imagePath = this.currentInvoice.getOriginalImagePath();
            Log.d("InvoiceDetail", "Passing image path to signature: " + imagePath);
            intent.putExtra("invoice_image_path", imagePath);
            if (imagePath.startsWith("content://")) {
                intent.setData(Uri.parse(imagePath));
                intent.addFlags(1);
                Log.d("InvoiceDetail", "Granted READ permission for content URI");
            }
        } else {
            Log.d("InvoiceDetail", "No invoice image path available");
            if (this.currentInvoice == null) {
                Log.d("InvoiceDetail", "currentInvoice is null");
            } else {
                Log.d("InvoiceDetail", "originalImagePath is null");
            }
        }
        this.signatureLauncher.launch(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$11(View v) {
        new AlertDialog.Builder(this).setTitle(R.string.add_pod).setItems(new CharSequence[]{getString(R.string.capture_pod), getString(R.string.upload_pod)}, new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda23
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                InvoiceDetailActivity.this.lambda$setupClickListeners$10(dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$10(DialogInterface dialog, int which) {
        if (which == 0) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
                this.requestCameraPermissionLauncher.launch("android.permission.CAMERA");
                return;
            } else {
                openPODCamera();
                return;
            }
        }
        openPODGallery();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupClickListeners$12(View v) {
        showPODOptionsDialog(1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupClickListeners$13(View v) {
        showPODOptionsDialog(2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupClickListeners$14(View v) {
        showPODOptionsDialog(3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupClickListeners$15(View v) {
        showPODOptionsDialog(4);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupClickListeners$16(View v) {
        showPODOptionsDialog(5);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupClickListeners$17(View v) {
        showPODOptionsDialog(6);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$18(View v) {
        showItemSelectionDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$19(View v) {
        String address = this.binding.etAddress.getText().toString().trim();
        if (!address.isEmpty()) {
            openAddressInMaps(address);
        } else {
            Toast.makeText(this, "No address available", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$20(View v) {
        String phone = this.binding.etPhone.getText().toString().trim();
        if (!phone.isEmpty()) {
            openPhoneDialer(phone);
        } else {
            Toast.makeText(this, "No phone number available", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$21(View v) {
        if (this.currentInvoice != null) {
            autoSaveInvoiceData();
            Intent intent = new Intent(this, ManualExtractionActivity.class);
            intent.putExtra("invoice_id", this.currentInvoice.getId());
            this.extractionLauncher.launch(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$22(View v) {
        saveInvoiceData();
    }

    private void openPODCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.EXTRA_CAMERA_MODE, CameraActivity.MODE_POD);
        this.podCameraLauncher.launch(intent);
    }

    private void showItemSelectionDialog() {
        if (this.currentItemDialog != null && this.currentItemDialog.isShowing()) {
            this.currentItemDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delivered Items");
        ScrollView scrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        boolean z = true;
        linearLayout.setOrientation(1);
        int i = 16;
        linearLayout.setPadding(32, 16, 32, 16);
        linearLayout.setBackgroundColor(-13816531);
        float f = 14.0f;
        int i2 = 12;
        int i3 = 4;
        if (!this.selectedItems.isEmpty()) {
            TextView currentHeader = new TextView(this);
            currentHeader.setText("Currently Selected Items:");
            currentHeader.setTextSize(16.0f);
            currentHeader.setTextColor(-2838729);
            currentHeader.setPadding(0, 0, 0, 16);
            linearLayout.addView(currentHeader);
            Iterator it = new ArrayList(this.selectedItems).iterator();
            while (it.hasNext()) {
                final DeliveryItem di = (DeliveryItem) it.next();
                TextView itemView = new TextView(this);
                itemView.setText("• " + di.getDisplayName() + "  (tap to remove)");
                itemView.setTextSize(f);
                itemView.setTextColor(-1);
                itemView.setPadding(i, i2, i, i3);
                itemView.setBackgroundColor(-12763843);
                itemView.setClickable(z);
                LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(-1, -2);
                itemParams.setMargins(0, i3, 0, 0);
                itemView.setLayoutParams(itemParams);
                itemView.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        InvoiceDetailActivity.this.lambda$showItemSelectionDialog$23(di, view);
                    }
                });
                linearLayout.addView(itemView);
                EditText etModel = new EditText(this);
                etModel.setHint("Model #");
                etModel.setText(di.model);
                etModel.setTextColor(-1);
                etModel.setHintTextColor(-7829368);
                etModel.setTextSize(13.0f);
                etModel.setPadding(32, 6, i, 6);
                etModel.setBackgroundColor(-13816531);
                LinearLayout.LayoutParams modelParams = new LinearLayout.LayoutParams(-1, -2);
                modelParams.setMargins(0, 0, 0, 0);
                etModel.setLayoutParams(modelParams);
                etModel.addTextChangedListener(new TextWatcher() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity.1
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int st, int c, int a) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int st, int b, int c) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        di.model = s.toString().trim();
                        InvoiceDetailActivity.this.updateSelectedItemsDisplay();
                    }
                });
                linearLayout.addView(etModel);
                EditText etSerial = new EditText(this);
                etSerial.setHint("Serial #");
                etSerial.setText(di.serial);
                etSerial.setTextColor(-1);
                etSerial.setHintTextColor(-7829368);
                etSerial.setTextSize(13.0f);
                etSerial.setPadding(32, 6, 16, 6);
                etSerial.setBackgroundColor(-13816531);
                LinearLayout.LayoutParams serialParams = new LinearLayout.LayoutParams(-1, -2);
                serialParams.setMargins(0, 0, 0, 6);
                etSerial.setLayoutParams(serialParams);
                etSerial.addTextChangedListener(new TextWatcher() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity.2
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int st, int c, int a) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int st, int b, int c) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        di.serial = s.toString().trim();
                        InvoiceDetailActivity.this.updateSelectedItemsDisplay();
                    }
                });
                linearLayout.addView(etSerial);
                z = true;
                i = 16;
                f = 14.0f;
                i2 = 12;
                i3 = 4;
            }
            View divider = new View(this);
            divider.setBackgroundColor(-11184811);
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(-1, 2);
            dividerParams.setMargins(0, 16, 0, 16);
            divider.setLayoutParams(dividerParams);
            linearLayout.addView(divider);
        }
        TextView availableHeader = new TextView(this);
        availableHeader.setText("Available Items:");
        availableHeader.setTextSize(16.0f);
        availableHeader.setTextColor(-2838729);
        availableHeader.setPadding(0, 0, 0, 16);
        linearLayout.addView(availableHeader);
        for (final String availableItem : AVAILABLE_ITEMS) {
            boolean alreadySelected = false;
            Iterator<DeliveryItem> it2 = this.selectedItems.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (it2.next().item.equals(availableItem)) {
                        alreadySelected = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!alreadySelected) {
                TextView itemView2 = new TextView(this);
                itemView2.setText("+ " + availableItem);
                itemView2.setTextSize(14.0f);
                itemView2.setTextColor(-3355444);
                itemView2.setPadding(16, 12, 16, 12);
                itemView2.setBackgroundColor(-15066598);
                itemView2.setClickable(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
                params.setMargins(0, 4, 0, 4);
                itemView2.setLayoutParams(params);
                itemView2.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda11
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        InvoiceDetailActivity.this.lambda$showItemSelectionDialog$24(availableItem, view);
                    }
                });
                linearLayout.addView(itemView2);
            }
        }
        scrollView.addView(linearLayout);
        builder.setView(scrollView);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda22
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                InvoiceDetailActivity.this.lambda$showItemSelectionDialog$25(dialogInterface, i4);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda25
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i4) {
                dialogInterface.dismiss();
            }
        });
        this.currentItemDialog = builder.create();
        this.currentItemDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showItemSelectionDialog$23(DeliveryItem di, View v) {
        this.selectedItems.remove(di);
        updateSelectedItemsDisplay();
        Toast.makeText(this, di.item + " removed", 0).show();
        showItemSelectionDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showItemSelectionDialog$24(String availableItem, View v) {
        this.selectedItems.add(new DeliveryItem(availableItem));
        updateSelectedItemsDisplay();
        Toast.makeText(this, availableItem + " added", 0).show();
        showItemSelectionDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showItemSelectionDialog$25(DialogInterface dialog, int which) {
        updateSelectedItemsDisplay();
        dialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelectedItemsDisplay() {
        if (this.selectedItems.isEmpty()) {
            this.binding.tvSelectedItems.setText("Tap to select delivered items");
            this.binding.tvSelectedItems.setTextColor(getResources().getColor(android.R.color.darker_gray));
            return;
        }
        StringBuilder displayText = new StringBuilder();
        displayText.append("Delivered Items (").append(this.selectedItems.size()).append("):\n");
        for (int i = 0; i < this.selectedItems.size(); i++) {
            displayText.append("• ").append(this.selectedItems.get(i).getFullDetail());
            if (i < this.selectedItems.size() - 1) {
                displayText.append(StringUtils.LF);
            }
        }
        this.binding.tvSelectedItems.setText(displayText.toString());
        this.binding.tvSelectedItems.setTextColor(getResources().getColor(android.R.color.primary_text_light));
    }

    private void updatePODButtonText() {
        int podCount = 0;
        if (this.podImagePath1 != null) {
            podCount = 1;
        }
        if (this.podImagePath2 != null) {
            podCount++;
        }
        if (this.podImagePath3 != null) {
            podCount++;
        }
        if (this.podImagePath4 != null) {
            podCount++;
        }
        if (this.podImagePath5 != null) {
            podCount++;
        }
        if (this.podImagePath6 != null) {
            podCount++;
        }
        if (podCount == 0) {
            this.binding.btnCapturePOD.setText("Add POD Photo");
        } else if (podCount < 6) {
            this.binding.btnCapturePOD.setText("Add POD Photo " + (podCount + 1));
        } else {
            this.binding.btnCapturePOD.setText("6 Photos Captured");
        }
    }

    private void showPODOptionsDialog(final int podNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("POD Photo " + podNumber);
        builder.setItems(new String[]{"View Full Size", "Replace Photo", "Delete Photo"}, new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda19
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                InvoiceDetailActivity.this.lambda$showPODOptionsDialog$27(podNumber, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPODOptionsDialog$27(int podNumber, DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                viewPODFullSize(podNumber);
                break;
            case 1:
                replacePODPhoto(podNumber);
                break;
            case 2:
                deletePODPhoto(podNumber);
                break;
        }
    }

    private void openPODGallery() {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        this.podGalleryLauncher.launch(intent);
    }

    private void viewPODFullSize(int podNumber) {
        String podPath = null;
        if (podNumber == 1) {
            podPath = this.podImagePath1;
        } else if (podNumber == 2) {
            podPath = this.podImagePath2;
        } else if (podNumber == 3) {
            podPath = this.podImagePath3;
        } else if (podNumber == 4) {
            podPath = this.podImagePath4;
        } else if (podNumber == 5) {
            podPath = this.podImagePath5;
        } else if (podNumber == 6) {
            podPath = this.podImagePath6;
        }
        if (podPath != null) {
            File podFile = new File(podPath);
            if (podFile.exists()) {
                Intent intent = new Intent("android.intent.action.VIEW");
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", podFile);
                intent.setDataAndType(photoUri, "image/*");
                intent.addFlags(1);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Image file not found", 0).show();
        }
    }

    private void replacePODPhoto(int podNumber) {
        if (podNumber == 1) {
            this.podImagePath1 = null;
            this.binding.ivPod1.setVisibility(8);
        } else if (podNumber == 2) {
            this.podImagePath2 = null;
            this.binding.ivPod2.setVisibility(8);
        } else if (podNumber == 3) {
            this.podImagePath3 = null;
            this.binding.ivPod3.setVisibility(8);
        } else if (podNumber == 4) {
            this.podImagePath4 = null;
            this.binding.ivPod4.setVisibility(8);
        } else if (podNumber == 5) {
            this.podImagePath5 = null;
            this.binding.ivPod5.setVisibility(8);
        } else if (podNumber == 6) {
            this.podImagePath6 = null;
            this.binding.ivPod6.setVisibility(8);
        }
        updatePODButtonText();
        new AlertDialog.Builder(this).setTitle(R.string.add_pod).setItems(new CharSequence[]{getString(R.string.capture_pod), getString(R.string.upload_pod)}, new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                InvoiceDetailActivity.this.lambda$replacePODPhoto$28(dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$replacePODPhoto$28(DialogInterface dialog, int which) {
        if (which == 0) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
                this.requestCameraPermissionLauncher.launch("android.permission.CAMERA");
                return;
            } else {
                openPODCamera();
                return;
            }
        }
        openPODGallery();
    }

    private void deletePODPhoto(int podNumber) {
        String podPath = null;
        if (podNumber == 1) {
            podPath = this.podImagePath1;
            this.podImagePath1 = null;
            this.binding.ivPod1.setVisibility(8);
        } else if (podNumber == 2) {
            podPath = this.podImagePath2;
            this.podImagePath2 = null;
            this.binding.ivPod2.setVisibility(8);
        } else if (podNumber == 3) {
            podPath = this.podImagePath3;
            this.podImagePath3 = null;
            this.binding.ivPod3.setVisibility(8);
        } else if (podNumber == 4) {
            podPath = this.podImagePath4;
            this.podImagePath4 = null;
            this.binding.ivPod4.setVisibility(8);
        } else if (podNumber == 5) {
            podPath = this.podImagePath5;
            this.podImagePath5 = null;
            this.binding.ivPod5.setVisibility(8);
        } else if (podNumber == 6) {
            podPath = this.podImagePath6;
            this.podImagePath6 = null;
            this.binding.ivPod6.setVisibility(8);
        }
        if (podPath != null) {
            File podFile = new File(podPath);
            if (podFile.exists()) {
                podFile.delete();
            }
        }
        updatePODButtonText();
        Toast.makeText(this, "POD photo " + podNumber + " deleted", 0).show();
    }

    private void autoSaveInvoiceData() {
        if (this.currentInvoice == null) {
            return;
        }
        String invoiceNumber = this.binding.etInvoiceNumber.getText().toString().trim();
        String customerName = this.binding.etCustomerName.getText().toString().trim();
        String address = this.binding.etAddress.getText().toString().trim();
        String phone = this.binding.etPhone.getText().toString().trim();
        this.currentInvoice.setInvoiceNumber(invoiceNumber);
        this.currentInvoice.setCustomerName(customerName);
        this.currentInvoice.setAddress(address);
        this.currentInvoice.setPhone(phone);
        this.currentInvoice.setNotes(this.binding.etNotes.getText().toString().trim());
        this.currentInvoice.setItems(ItemsHelper.toJson(this.selectedItems));
        this.currentInvoice.setSignatureImagePath(this.signaturePath);
        this.currentInvoice.setPodImagePath1(this.podImagePath1);
        this.currentInvoice.setPodImagePath2(this.podImagePath2);
        this.currentInvoice.setPodImagePath3(this.podImagePath3);
        this.currentInvoice.setPodImagePath4(this.podImagePath4);
        this.currentInvoice.setPodImagePath5(this.podImagePath5);
        this.currentInvoice.setPodImagePath6(this.podImagePath6);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public void run() {
                InvoiceDetailActivity.this.lambda$autoSaveInvoiceData$29();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$autoSaveInvoiceData$29() {
        this.database.invoiceDao().update(this.currentInvoice);
    }

    private void saveInvoiceData() {
        if (this.currentInvoice == null) {
            Toast.makeText(this, "Error: Invoice not loaded", 0).show();
            return;
        }
        String invoiceNumber = this.binding.etInvoiceNumber.getText().toString().trim();
        String customerName = this.binding.etCustomerName.getText().toString().trim();
        String address = this.binding.etAddress.getText().toString().trim();
        String phone = this.binding.etPhone.getText().toString().trim();
        if (invoiceNumber.isEmpty()) {
            this.binding.etInvoiceNumber.setError("Invoice number is required");
            this.binding.etInvoiceNumber.requestFocus();
            return;
        }
        if (customerName.isEmpty()) {
            this.binding.etCustomerName.setError("Customer name is required");
            this.binding.etCustomerName.requestFocus();
            return;
        }
        this.currentInvoice.setInvoiceNumber(invoiceNumber);
        this.currentInvoice.setCustomerName(customerName);
        this.currentInvoice.setAddress(address);
        this.currentInvoice.setPhone(phone);
        this.currentInvoice.setNotes(this.binding.etNotes.getText().toString().trim());
        this.currentInvoice.setItems(ItemsHelper.toJson(this.selectedItems));
        this.currentInvoice.setSignatureImagePath(this.signaturePath);
        this.currentInvoice.setPodImagePath1(this.podImagePath1);
        this.currentInvoice.setPodImagePath2(this.podImagePath2);
        this.currentInvoice.setPodImagePath3(this.podImagePath3);
        this.currentInvoice.setPodImagePath4(this.podImagePath4);
        this.currentInvoice.setPodImagePath5(this.podImagePath5);
        this.currentInvoice.setPodImagePath6(this.podImagePath6);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public void run() {
                InvoiceDetailActivity.this.lambda$saveInvoiceData$31();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveInvoiceData$31() {
        this.database.invoiceDao().update(this.currentInvoice);
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.InvoiceDetailActivity$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public void run() {
                InvoiceDetailActivity.this.lambda$saveInvoiceData$30();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveInvoiceData$30() {
        Toast.makeText(this, "Invoice saved successfully!", 0).show();
        finish();
    }

    private String saveImageToStorage(Uri imageUri, String filename) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                return null;
            }
            File storageDir = new File(getFilesDir(), "images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File imageFile = new File(storageDir, filename);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = inputStream.read(buffer);
                if (length > 0) {
                    outputStream.write(buffer, 0, length);
                } else {
                    outputStream.close();
                    inputStream.close();
                    return imageFile.getAbsolutePath();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image", 0).show();
            return null;
        }
    }

    private void openAddressInMaps(String address) {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            Intent mapIntent = new Intent("android.intent.action.VIEW", gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                mapIntent.setPackage(null);
                startActivity(mapIntent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open maps application", 0).show();
        }
    }

    private void openPhoneDialer(String phone) {
        try {
            String cleanPhone = phone.replaceAll("[^\\d+]", "");
            Uri phoneUri = Uri.parse("tel:" + cleanPhone);
            Intent dialIntent = new Intent("android.intent.action.DIAL", phoneUri);
            startActivity(dialIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open phone dialer", 0).show();
        }
    }
}
