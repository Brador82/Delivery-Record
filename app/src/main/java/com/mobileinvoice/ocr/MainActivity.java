package com.mobileinvoice.ocr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileinvoice.ocr.ExportHelper;
import com.mobileinvoice.ocr.InvoiceAdapter;
import com.mobileinvoice.ocr.ItemMoveCallback;
import com.mobileinvoice.ocr.OCRProcessorMLKit;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityMainBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;

/* loaded from: classes7.dex */
public class MainActivity extends BaseActivity implements InvoiceAdapter.OnInvoiceClickListener {
    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private InvoiceDatabase database;
    private ActivityResultLauncher<Uri> folderPickerLauncher;
    private InvoiceAdapter invoiceAdapter;
    private ActivityResultLauncher<String> pickImagesLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private List<Uri> selectedImages = new ArrayList();
    private List<Invoice> invoices = new ArrayList();

    /*
     * JADX WARN: Can't fix incorrect switch cases order, some code will duplicate
     */
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

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity,
              // androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.database = InvoiceDatabase.getInstance(this);
        this.binding.btnBroadcast.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                  // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$onCreate$0(view);
            }
        });
        this.binding.fabSettings.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                 // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$onCreate$1(view);
            }
        });
        setupActivityResultLaunchers();
        setupClickListeners();
        setupRecyclerViews();
        loadInvoicesFromDatabase();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View v) {
        openBroadcastMessage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View v) {
        startActivity(new Intent(this, (Class<?>) SettingsActivity.class));
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        loadInvoicesFromDatabase();
    }

    private void setupActivityResultLaunchers() {
        this.pickImagesLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(),
                new ActivityResultCallback() { // from class:
                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda7
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        MainActivity.this.lambda$setupActivityResultLaunchers$2((List) obj);
                    }
                });
        this.cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback() { // from class:
                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda8
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        MainActivity.this.lambda$setupActivityResultLaunchers$3((ActivityResult) obj);
                    }
                });
        this.requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class:
                                                                                                // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda9
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        MainActivity.this.lambda$setupActivityResultLaunchers$4((Boolean) obj);
                    }
                });
        this.folderPickerLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(),
                new ActivityResultCallback() { // from class:
                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda10
                    @Override // androidx.activity.result.ActivityResultCallback
                    public final void onActivityResult(Object obj) {
                        MainActivity.this.lambda$setupActivityResultLaunchers$5((Uri) obj);
                    }
                });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$2(List uris) {
        if (uris != null && !uris.isEmpty()) {
            Iterator it = uris.iterator();
            while (it.hasNext()) {
                Uri uri = (Uri) it.next();
                Uri correctedUri = rotateImageIfNeeded(uri);
                this.selectedImages.add(correctedUri);
            }
            updateImageCount();
            this.binding.btnClearQueue.setVisibility(0);
            this.binding.imagePreviewRecycler.setVisibility(0);
            Toast.makeText(this, "Selected " + uris.size() + " images (rotation corrected)", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$3(ActivityResult result) {
        Uri imageUri;
        if (result.getResultCode() == -1 && result.getData() != null
                && (imageUri = result.getData().getData()) != null) {
            Uri correctedUri = rotateImageIfNeeded(imageUri);
            this.selectedImages.add(correctedUri);
            updateImageCount();
            this.binding.btnClearQueue.setVisibility(0);
            this.binding.imagePreviewRecycler.setVisibility(0);
            Toast.makeText(this, "Photo captured and corrected!", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$4(Boolean isGranted) {
        if (isGranted.booleanValue()) {
            openCamera();
        } else {
            Toast.makeText(this, "Camera permission is required", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActivityResultLaunchers$5(Uri treeUri) {
        if (treeUri != null) {
            getContentResolver().takePersistableUriPermission(treeUri, 3);
            exportToSelectedFolder(treeUri);
        }
    }

    private void openBroadcastMessage() {
        startActivity(new Intent(this, (Class<?>) BroadcastMessageActivity.class));
    }

    private void setupClickListeners() {
        this.binding.btnUpload.setOnClickListener(new View.OnClickListener() { // from class:
                                                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda24
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$6(view);
            }
        });
        this.binding.btnCamera.setOnClickListener(new View.OnClickListener() { // from class:
                                                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda25
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$7(view);
            }
        });
        this.binding.btnClearQueue.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                   // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$9(view);
            }
        });
        this.binding.btnProcessOCR.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                   // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda27
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$13(view);
            }
        });
        this.binding.btnExportCSV.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                  // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$17(view);
            }
        });
        this.binding.btnExportMarkdown.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                       // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$18(view);
            }
        });
        this.binding.btnOptimizeRoute.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                      // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$19(view);
            }
        });
        this.binding.btnInvoiceLibrary.setOnClickListener(new View.OnClickListener() { // from class:
                                                                                       // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda31
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupClickListeners$20(view);
            }
        });
        ImageButton btnLibraryIcon = (ImageButton) findViewById(R.id.btnInvoiceLibraryIcon);
        if (btnLibraryIcon != null) {
            btnLibraryIcon.setOnClickListener(new View.OnClickListener() { // from class:
                                                                           // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda32
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.this.lambda$setupClickListeners$21(view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$6(View v) {
        this.pickImagesLauncher.launch("image/*");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$7(View v) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
            this.requestCameraPermissionLauncher.launch("android.permission.CAMERA");
        } else {
            openCamera();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$9(View v) {
        if (!this.selectedImages.isEmpty()) {
            new AlertDialog.Builder(this).setTitle("Clear Image Queue?")
                    .setMessage("This will clear " + this.selectedImages.size()
                            + " selected image(s) that haven't been processed yet.")
                    .setPositiveButton("Clear", new DialogInterface.OnClickListener() { // from class:
                                                                                        // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.lambda$setupClickListeners$8(dialogInterface, i);
                        }
                    }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$8(DialogInterface dialog, int which) {
        this.selectedImages.clear();
        updateImageCount();
        this.binding.btnClearQueue.setVisibility(8);
        Toast.makeText(this, "Image queue cleared", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$13(View v) {
        Toast.makeText(this, "Processing " + this.selectedImages.size() + " images with ML Kit...", 1).show();
        this.binding.progressBar.setVisibility(0);
        this.binding.progressBar.setMax(this.selectedImages.size());
        this.binding.tvProgress.setVisibility(0);
        this.binding.tvProgress.setText("Processing 0/" + this.selectedImages.size());
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$setupClickListeners$12();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$12() {
        String str;
        OCRProcessorMLKit ocrProcessor = new OCRProcessorMLKit(this);
        for (int i = 0; i < this.selectedImages.size(); i++) {
            final int index = i;
            Uri imageUri = this.selectedImages.get(i);
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.lambda$setupClickListeners$10(index);
                }
            });
            OCRProcessorMLKit.OCRResult result = ocrProcessor.processImage(imageUri);
            Invoice invoice = new Invoice();
            boolean hasItems = false;
            boolean hasInvoiceNumber = (result.invoiceNumber == null || result.invoiceNumber.trim().isEmpty()
                    || result.invoiceNumber.equalsIgnoreCase("No invoice number")) ? false : true;
            if (hasInvoiceNumber) {
                str = result.invoiceNumber.trim();
            } else {
                str = "INV-" + String.format("%06d", Integer.valueOf(this.invoices.size() + 1));
            }
            invoice.setInvoiceNumber(str);
            invoice.setCustomerName(result.customerName.isEmpty() ? "Unknown Customer" : result.customerName);
            invoice.setAddress(result.address.isEmpty() ? "No address found" : result.address);
            invoice.setPhone(result.phone.isEmpty() ? "No phone" : result.phone);
            if (result.altPhone != null && !result.altPhone.isEmpty()) {
                invoice.setAltPhone(result.altPhone);
            }
            if (result.services != null && !result.services.isEmpty()) {
                invoice.setServiceType(result.services);
            }
            if (result.items != null && !result.items.trim().isEmpty()
                    && !result.items.equalsIgnoreCase("No items detected")) {
                hasItems = true;
            }
            invoice.setItems(hasItems ? result.items.trim() : "");
            invoice.setRawOcrText(result.rawText);
            invoice.setTimestamp(System.currentTimeMillis());
            long newId = this.database.invoiceDao().insert(invoice);
            invoice.setId((int) newId);
            String savedPath = saveImageToAppStorage(imageUri, "invoice_original_" + newId + ".jpg");
            if (savedPath != null) {
                invoice.setOriginalImagePath(savedPath);
            } else {
                invoice.setOriginalImagePath(imageUri.toString());
            }
            Bitmap originalBitmap = BitmapFactory.decodeFile(savedPath != null ? savedPath : imageUri.getPath());
            if (originalBitmap != null) {
                try {
                    Bitmap preprocessed = ImagePreprocessor.pipeline(originalBitmap);
                    String preprocessedPath = ImagePreprocessor.savePreprocessed(this, preprocessed);
                    if (preprocessedPath != null) {
                        invoice.setPreprocessedImagePath(preprocessedPath);
                    }
                    if (preprocessed != originalBitmap) {
                        preprocessed.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.database.invoiceDao().update(invoice);
            this.invoices.add(invoice);
        }
        ocrProcessor.close();
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$setupClickListeners$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$10(int index) {
        this.binding.progressBar.setProgress(index);
        this.binding.tvProgress
                .setText("Processing " + index + PackagingURIHelper.FORWARD_SLASH_STRING + this.selectedImages.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$11() {
        this.binding.progressBar.setVisibility(8);
        this.binding.tvProgress.setVisibility(8);
        this.binding.tvStatus.setText("Processing complete! " + this.invoices.size() + " invoices extracted.");
        this.invoiceAdapter.setInvoices(this.invoices);
        updateRecordCount();
        this.selectedImages.clear();
        updateImageCount();
        Toast.makeText(this, "OCR processing completed", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$17(View v) {
        if (this.invoices.isEmpty()) {
            Toast.makeText(this, "No invoices to export", 0).show();
            return;
        }
        final ExportHelper exportHelper = new ExportHelper(this);
        exportHelper.setExportCompleteCallback(new ExportHelper.ExportCompleteCallback() { // from class:
                                                                                           // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda16
            @Override // com.mobileinvoice.ocr.ExportHelper.ExportCompleteCallback
            public final void onExportComplete(File file, int i) {
                MainActivity.this.lambda$setupClickListeners$14(file, i);
            }
        });
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$setupClickListeners$16(exportHelper);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$14(File exportFolder, int count) {
        lambda$exportToSelectedFolder$25(count);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$16(final ExportHelper exportHelper) {
        final List<Invoice> exportInvoices = this.database.invoiceDao().getAllInvoicesSync();
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                exportHelper.exportToPDFZip(exportInvoices);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$18(View v) {
        if (this.invoices.isEmpty()) {
            Toast.makeText(this, "No invoices to export", 0).show();
        } else {
            this.folderPickerLauncher.launch(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$19(View v) {
        if (this.invoices.isEmpty()) {
            Toast.makeText(this, "No deliveries to route. Add invoices first.", 0).show();
        } else {
            Intent intent = new Intent(this, (Class<?>) RouteMapActivity.class);
            startActivity(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$20(View v) {
        Intent intent = new Intent(this, (Class<?>) InvoiceLibraryActivity.class);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickListeners$21(View v) {
        Intent intent = new Intent(this, (Class<?>) InvoiceLibraryActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerViews() {
        this.invoiceAdapter = new InvoiceAdapter(this);
        this.binding.invoiceRecycler.setLayoutManager(new LinearLayoutManager(this));
        this.binding.invoiceRecycler.setAdapter(this.invoiceAdapter);
        ItemMoveCallback itemMoveCallback = new ItemMoveCallback(new ItemMoveCallback.ItemTouchHelperContract() { // from
                                                                                                                  // class:
                                                                                                                  // com.mobileinvoice.ocr.MainActivity.1
            @Override // com.mobileinvoice.ocr.ItemMoveCallback.ItemTouchHelperContract
            public void onRowMoved(int fromPosition, int toPosition) {
                MainActivity.this.invoiceAdapter.onItemMove(fromPosition, toPosition);
            }

            @Override // com.mobileinvoice.ocr.ItemMoveCallback.ItemTouchHelperContract
            public void onRowSelected(RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.setAlpha(0.7f);
                viewHolder.itemView.setScaleX(1.05f);
                viewHolder.itemView.setScaleY(1.05f);
            }

            @Override // com.mobileinvoice.ocr.ItemMoveCallback.ItemTouchHelperContract
            public void onRowClear(RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.setAlpha(1.0f);
                viewHolder.itemView.setScaleX(1.0f);
                viewHolder.itemView.setScaleY(1.0f);
                MainActivity.this.invoiceAdapter.onItemMoveComplete();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemMoveCallback);
        itemTouchHelper.attachToRecyclerView(this.binding.invoiceRecycler);
        this.binding.imagePreviewRecycler.setLayoutManager(new LinearLayoutManager(this, 0, false));
        updateRecordCount();
    }

    private void createSampleInvoices(int count) {
        String[] sampleNames = { "John Smith", "Jane Doe", "Bob Johnson", "Alice Williams", "Mike Brown" };
        String[] sampleAddresses = { "123 Main St, New York, NY 10001", "456 Oak Ave, Los Angeles, CA 90001",
                "789 Pine Rd, Chicago, IL 60601", "321 Elm St, Houston, TX 77001", "654 Maple Dr, Phoenix, AZ 85001" };
        String[] samplePhones = { "555-0101", "555-0102", "555-0103", "555-0104", "555-0105" };
        for (int i = 0; i < count; i++) {
            Invoice invoice = new Invoice();
            invoice.setId(this.invoices.size() + 1);
            invoice.setInvoiceNumber("INV-" + String.format("%06d", Integer.valueOf(this.invoices.size() + 1)));
            invoice.setCustomerName(sampleNames[i % sampleNames.length]);
            invoice.setAddress(sampleAddresses[i % sampleAddresses.length]);
            invoice.setPhone(samplePhones[i % samplePhones.length]);
            invoice.setItems("Sample items");
            this.invoices.add(invoice);
        }
        this.invoiceAdapter.setInvoices(this.invoices);
        updateRecordCount();
    }

    private void updateRecordCount() {
        String countText = this.invoices.isEmpty() ? "No invoices yet" : this.invoices.size() + " invoice(s)";
        this.binding.tvRecordCount.setText(countText);
    }

    private void updateImageCount() {
        if (this.selectedImages.isEmpty()) {
            this.binding.tvStatus.setText("Ready. Upload or capture invoice images.");
            this.binding.btnProcessOCR.setEnabled(false);
        } else {
            this.binding.tvStatus.setText("Selected " + this.selectedImages.size() + " images");
            this.binding.btnProcessOCR.setEnabled(true);
        }
    }

    private void loadInvoicesFromDatabase() {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$loadInvoicesFromDatabase$24();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoicesFromDatabase$24() {
        final List<Invoice> dbInvoices = this.database.invoiceDao().getAllInvoicesSync();
        dbInvoices.sort(new Comparator() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda19
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return MainActivity.lambda$loadInvoicesFromDatabase$22((Invoice) obj, (Invoice) obj2);
            }
        });
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$loadInvoicesFromDatabase$23(dbInvoices);
            }
        });
    }

    static /* synthetic */ int lambda$loadInvoicesFromDatabase$22(Invoice a, Invoice b) {
        int sa = a.getDeliverySequence();
        int sb = b.getDeliverySequence();
        if (sa > 0 && sb > 0) {
            return Integer.compare(sa, sb);
        }
        if (sa > 0) {
            return -1;
        }
        if (sb > 0) {
            return 1;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoicesFromDatabase$23(List dbInvoices) {
        this.invoices.clear();
        this.invoices.addAll(dbInvoices);
        this.invoiceAdapter.setInvoices(this.invoices);
        updateRecordCount();
    }

    private void openCamera() {
        Intent intent = new Intent(this, (Class<?>) CameraActivity.class);
        intent.putExtra(CameraActivity.EXTRA_CAMERA_MODE, CameraActivity.MODE_INVOICE);
        this.cameraLauncher.launch(intent);
    }

    private void exportToSelectedFolder(final Uri treeUri) {
        Toast.makeText(this, "Exporting to selected folder...", 0).show();
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$exportToSelectedFolder$30(treeUri);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$exportToSelectedFolder$30(Uri treeUri) {
        final List<Invoice> exportInvoices = this.database.invoiceDao().getAllInvoicesSync();
        final ExportHelper exportHelper = new ExportHelper(this);
        exportHelper.setExportCompleteCallback(new ExportHelper.ExportCompleteCallback() { // from class:
                                                                                           // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda11
            @Override // com.mobileinvoice.ocr.ExportHelper.ExportCompleteCallback
            public final void onExportComplete(File file, int i) {
                MainActivity.this.lambda$exportToSelectedFolder$26(file, i);
            }
        });
        try {
            DocumentFile destDir = DocumentFile.fromTreeUri(this, treeUri);
            if (destDir != null && destDir.canWrite()) {
                runOnUiThread(new Runnable() { // from class:
                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda22
                    @Override // java.lang.Runnable
                    public final void run() {
                        exportHelper.exportToHTMLZip(exportInvoices);
                    }
                });
            } else {
                runOnUiThread(new Runnable() { // from class:
                                               // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda33
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.lambda$exportToSelectedFolder$28();
                    }
                });
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.lambda$exportToSelectedFolder$29(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$exportToSelectedFolder$26(File exportFolder, final int count) {
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$exportToSelectedFolder$25(count);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$exportToSelectedFolder$28() {
        Toast.makeText(this, "Cannot write to selected folder", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$exportToSelectedFolder$29(Exception e) {
        Toast.makeText(this, "Export failed: " + e.getMessage(), 0).show();
    }

    @Override // com.mobileinvoice.ocr.InvoiceAdapter.OnInvoiceClickListener
    public void onViewDetails(Invoice invoice) {
        Intent intent = new Intent(this, (Class<?>) InvoiceDetailActivity.class);
        intent.putExtra("invoice_id", invoice.getId());
        intent.putExtra("customer_name", invoice.getCustomerName());
        intent.putExtra("address", invoice.getAddress());
        intent.putExtra("phone", invoice.getPhone());
        startActivity(intent);
    }

    @Override // com.mobileinvoice.ocr.InvoiceAdapter.OnInvoiceClickListener
    public void onDelete(final Invoice invoice) {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$onDelete$32(invoice);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDelete$32(Invoice invoice) {
        this.database.invoiceDao().delete(invoice);
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$onDelete$31();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDelete$31() {
        loadInvoicesFromDatabase();
        Toast.makeText(this, "Invoice deleted", 0).show();
    }

    @Override // com.mobileinvoice.ocr.InvoiceAdapter.OnInvoiceClickListener
    public void onOrderChanged(List<Invoice> reorderedList) {
        this.invoices.clear();
        this.invoices.addAll(reorderedList);
        Toast.makeText(this, "Order updated - Long press to drag invoices", 0).show();
    }

    @Override // com.mobileinvoice.ocr.InvoiceAdapter.OnInvoiceClickListener
    public void onServiceTypeChanged(final Invoice invoice, String serviceType) {
        invoice.setServiceType(serviceType);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$onServiceTypeChanged$33(invoice);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onServiceTypeChanged$33(Invoice invoice) {
        this.database.invoiceDao().update(invoice);
    }

    @Override // com.mobileinvoice.ocr.InvoiceAdapter.OnInvoiceClickListener
    public void onDeliveryCompleteChanged(final Invoice invoice, final boolean isComplete) {
        String newStatus = isComplete ? "DELIVERED" : "PENDING";
        invoice.setStatus(newStatus);
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$onDeliveryCompleteChanged$35(invoice, isComplete);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDeliveryCompleteChanged$35(final Invoice invoice, final boolean isComplete) {
        this.database.invoiceDao().update(invoice);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isComplete) {
                    Toast.makeText(MainActivity.this, "Delivery marked complete!", 0).show();
                    offerFollowUpMessage(invoice);
                } else {
                    Toast.makeText(MainActivity.this, "Delivery marked pending", 0).show();
                }
            }
        });
    }

    private void offerFollowUpMessage(final Invoice invoice) {
        AppSettings settings = AppSettings.getInstance(this);
        if (!settings.isFollowUpEnabled())
            return;
        String phone = invoice.getPhone();
        if (phone == null || phone.isEmpty() || "No phone".equals(phone))
            return;

        String customerName = invoice.getCustomerName();
        String itemsDisplay = "";
        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            List<DeliveryItem> items = ItemsHelper.fromJson(invoice.getItems());
            itemsDisplay = ItemsHelper.toNamesString(items);
        }

        final String mainMsg = settings.expandMessage(settings.getBroadcastMessage(), customerName, itemsDisplay);
        final String quick1 = settings.expandMessage(settings.getCustomMessage1(), customerName, itemsDisplay);
        final String quick2 = settings.expandMessage(settings.getCustomMessage2(), customerName, itemsDisplay);
        final String phoneNumber = phone;

        String[] options = { "Send Follow-Up", "Quick Message 1", "Quick Message 2", "Skip" };
        new AlertDialog.Builder(this)
                .setTitle("Send Customer Message?")
                .setItems(options, new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        String smsBody = null;
                        switch (which) {
                            case 0:
                                smsBody = mainMsg;
                                break;
                            case 1:
                                smsBody = quick1;
                                break;
                            case 2:
                                smsBody = quick2;
                                break;
                            default:
                                return;
                        }
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setData(Uri.parse("sms:" + phoneNumber));
                        smsIntent.putExtra("sms_body", smsBody);
                        startActivity(smsIntent);
                    }
                })
                .show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*
     * renamed from: showCleanupDialog, reason: merged with bridge method
     * [inline-methods]
     */
    public void lambda$exportToSelectedFolder$25(int exportedCount) {
        new AlertDialog.Builder(this).setTitle("Export Complete").setMessage(exportedCount
                + " invoices exported successfully to Downloads/MobileInvoiceOCR.\n\nWould you like to clear all data to start fresh?")
                .setPositiveButton("Clear All Data", new DialogInterface.OnClickListener() { // from class:
                                                                                             // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda36
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.lambda$showCleanupDialog$36(dialogInterface, i);
                    }
                }).setNegativeButton("Keep Data", new DialogInterface.OnClickListener() { // from class:
                                                                                          // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda37
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.lambda$showCleanupDialog$37(dialogInterface, i);
                    }
                }).setIcon(android.R.drawable.ic_dialog_info).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCleanupDialog$36(DialogInterface dialog, int which) {
        clearAllData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCleanupDialog$37(DialogInterface dialog, int which) {
        Toast.makeText(this, "Data retained. Ready for next export.", 0).show();
    }

    private void clearAllData() {
        new AlertDialog.Builder(this).setTitle("Confirm Clear All")
                .setMessage("This will permanently delete all " + this.invoices.size()
                        + " invoices from the app. Exported data in Downloads will NOT be affected.\n\nAre you sure?")
                .setPositiveButton("Yes, Clear All", new DialogInterface.OnClickListener() { // from class:
                                                                                             // com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.this.lambda$clearAllData$40(dialogInterface, i);
                    }
                }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearAllData$40(DialogInterface dialog, int which) {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$clearAllData$39();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearAllData$39() {
        this.database.invoiceDao().deleteAll();
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.MainActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$clearAllData$38();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearAllData$38() {
        this.invoices.clear();
        this.invoiceAdapter.setInvoices(this.invoices);
        updateRecordCount();
        Toast.makeText(this, "All data cleared. Ready for new deliveries!", 1).show();
    }

    private String saveImageToAppStorage(Uri imageUri, String filename) {
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
        } catch (Exception e) {
            Log.e("MainActivity", "Error saving original invoice image: " + e.getMessage());
            return null;
        }
    }

    private Uri rotateImageIfNeeded(Uri sourceUri) {
        int rotation;
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            if (originalBitmap != null) {
                InputStream exifStream = getContentResolver().openInputStream(sourceUri);
                ExifInterface exif = new ExifInterface(exifStream);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                exifStream.close();
                switch (orientation) {
                    case 3:
                        rotation = 180;
                        break;
                    case 6:
                        rotation = 90;
                        break;
                    case 8:
                        rotation = 270;
                        break;
                    default:
                        rotation = 0;
                        break;
                }
                if (rotation == 0) {
                    Log.d("MainActivity", "Image already properly oriented");
                    originalBitmap.recycle();
                    return sourceUri;
                }
                Log.d("MainActivity", "Rotating image by " + rotation + " degrees");
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                        originalBitmap.getHeight(), matrix, true);
                originalBitmap.recycle();
                try {
                    File imagesDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Invoices");
                    if (!imagesDir.exists()) {
                        imagesDir.mkdirs();
                    }
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                    File rotatedFile = new File(imagesDir, "IMG_" + timestamp + "_corrected.jpg");
                    FileOutputStream fos = new FileOutputStream(rotatedFile);
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
                    fos.flush();
                    fos.close();
                    rotatedBitmap.recycle();
                    Log.d("MainActivity", "Saved rotated image to: " + rotatedFile.getAbsolutePath());
                    return Uri.fromFile(rotatedFile);
                } catch (Exception e) {
                    Log.e("MainActivity", "Error rotating image: " + e.getMessage());
                    e.printStackTrace();
                    return sourceUri;
                }
            }
            Log.e("MainActivity", "Failed to load bitmap from URI");
            return sourceUri;
        } catch (Exception e2) {
            Log.e("MainActivity", "Error rotating image: " + e2.getMessage());
            e2.printStackTrace();
            return sourceUri;
        }
    }
}
