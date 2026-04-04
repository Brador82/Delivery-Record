package com.mobileinvoice.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.mobileinvoice.ocr.PaddleOCREngine;
import com.mobileinvoice.ocr.SelectionOverlayView;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityManualExtractionBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes7.dex */
public class ManualExtractionActivity extends BaseActivity {
    private ActivityManualExtractionBinding binding;
    private Invoice currentInvoice;
    private InvoiceDatabase database;
    private Bitmap fullBitmap;
    private TextRecognizer recognizer;
    private static final Pattern MODEL_PATTERN = Pattern.compile("(?i)(?:model|mdl|mod)\\s*(?:no\\.?|num\\.?|#|:)?\\s*[:#]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})");
    private static final Pattern SERIAL_PATTERN = Pattern.compile("(?i)(?:s/n|serial|ser\\.?|sn)\\s*[:#]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\(?\\d{3}[)\\s.\\-]\\s*\\d{3}[\\s.\\-]\\d{4}");
    private static final Pattern INVOICE_PATTERN = Pattern.compile("(?i)(?:INV|invoice)[\\s#\\-]?[A-Z0-9]{4,}");
    private Map<FieldType, String> extractedValues = new EnumMap(FieldType.class);
    private Map<FieldType, Chip> fieldChips = new EnumMap(FieldType.class);
    private Map<FieldType, ChipState> chipStates = new EnumMap(FieldType.class);
    private Map<FieldType, ChipState> initialChipStates = new EnumMap(FieldType.class);
    private List<DeliveryItem> extractedItems = new ArrayList();

    private enum ChipState {
        EMPTY,
        DETECTED,
        CONFIRMED
    }

    /* JADX INFO: Access modifiers changed from: private */
    enum FieldType {
        INVOICE_NUMBER("invoiceNumber", "Invoice #"),
        CUSTOMER_NAME("customerName", "Name"),
        ADDRESS("address", "Address"),
        PHONE("phone", "Phone"),
        NOTES("notes", "Notes"),
        ITEMS("items", "Items (Add)"),
        MODEL_NUMBER("modelNumber", "Model #"),
        SERIAL_NUMBER("serialNumber", "Serial #");

        final String key;
        final String label;

        FieldType(String key, String label) {
            this.key = key;
            this.label = label;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityManualExtractionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.database = InvoiceDatabase.getInstance(this);
        this.recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        int invoiceId = getIntent().getIntExtra("invoice_id", -1);
        if (invoiceId == -1) {
            Toast.makeText(this, "Invalid invoice", 0).show();
            finish();
            return;
        }
        for (FieldType f : FieldType.values()) {
            this.chipStates.put(f, ChipState.EMPTY);
        }
        loadInvoice(invoiceId);
        setupFieldChips();
        setupButtons();
        this.binding.selectionOverlay.setOnTextSelectedListener(new SelectionOverlayView.OnTextSelectedListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda0
            @Override // com.mobileinvoice.ocr.SelectionOverlayView.OnTextSelectedListener
            public final void onTextSelected(String str, Rect rect) {
                ManualExtractionActivity.this.lambda$onCreate$0(str, rect);
            }
        });
        this.binding.tvHint.setText("Tap any highlighted text · zoom/pan with two fingers");
        this.binding.tvResultPreview.setVisibility(8);
    }

    private void loadInvoice(final int invoiceId) {
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$loadInvoice$3(invoiceId);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoice$3(int invoiceId) {
        this.currentInvoice = this.database.invoiceDao().getInvoiceByIdSync(invoiceId);
        if (this.currentInvoice == null) {
            runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ManualExtractionActivity.this.lambda$loadInvoice$1();
                }
            });
            return;
        }
        if (this.currentInvoice.getInvoiceNumber() != null && !this.currentInvoice.getInvoiceNumber().isEmpty()) {
            this.extractedValues.put(FieldType.INVOICE_NUMBER, this.currentInvoice.getInvoiceNumber());
            this.chipStates.put(FieldType.INVOICE_NUMBER, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getCustomerName() != null && !this.currentInvoice.getCustomerName().isEmpty() && !"Unknown Customer".equals(this.currentInvoice.getCustomerName())) {
            this.extractedValues.put(FieldType.CUSTOMER_NAME, this.currentInvoice.getCustomerName());
            this.chipStates.put(FieldType.CUSTOMER_NAME, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getAddress() != null && !this.currentInvoice.getAddress().isEmpty() && !"No address found".equals(this.currentInvoice.getAddress())) {
            this.extractedValues.put(FieldType.ADDRESS, this.currentInvoice.getAddress());
            this.chipStates.put(FieldType.ADDRESS, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getPhone() != null && !this.currentInvoice.getPhone().isEmpty() && !"No phone".equals(this.currentInvoice.getPhone())) {
            this.extractedValues.put(FieldType.PHONE, this.currentInvoice.getPhone());
            this.chipStates.put(FieldType.PHONE, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getNotes() != null && !this.currentInvoice.getNotes().isEmpty()) {
            this.extractedValues.put(FieldType.NOTES, this.currentInvoice.getNotes());
            this.chipStates.put(FieldType.NOTES, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getItems() != null && !this.currentInvoice.getItems().isEmpty()) {
            this.extractedItems = ItemsHelper.fromJson(this.currentInvoice.getItems());
            if (!this.extractedItems.isEmpty()) {
                this.chipStates.put(FieldType.ITEMS, ChipState.CONFIRMED);
            }
        }
        this.initialChipStates.putAll(this.chipStates);
        String imagePath = this.currentInvoice.getOriginalImagePath();
        if (imagePath != null && new File(imagePath).exists()) {
            this.fullBitmap = BitmapFactory.decodeFile(imagePath);
        }
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$loadInvoice$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoice$1() {
        Toast.makeText(this, "Invoice not found", 0).show();
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoice$2() {
        if (this.fullBitmap != null) {
            this.binding.selectionOverlay.setImage(this.fullBitmap);
            scanFullImageForRegions(this.fullBitmap);
        } else {
            Toast.makeText(this, "Could not load invoice image", 0).show();
            finish();
        }
        for (Map.Entry<FieldType, ChipState> entry : this.chipStates.entrySet()) {
            updateChipState(entry.getKey(), entry.getValue());
        }
    }

    private void setupFieldChips() {
        ChipGroup chipGroup = this.binding.chipGroupFields;
        for (FieldType field : FieldType.values()) {
            Chip chip = new Chip(this);
            chip.setText(field.label);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColor(null);
            chip.setBackground(getResources().getDrawable(R.drawable.chip_gradient_bg, null));
            chip.setTextColor(-11184811);
            this.fieldChips.put(field, chip);
            chipGroup.addView(chip);
        }
    }

    private void updateChipState(FieldType field, ChipState state) {
        this.chipStates.put(field, state);
        Chip chip = this.fieldChips.get(field);
        if (chip == null) {
        }
        chip.setChipBackgroundColor(null);
        chip.setBackground(getResources().getDrawable(R.drawable.chip_gradient_bg, null));
        switch (state) {
            case EMPTY:
                chip.setTextColor(-11184811);
                chip.setChipIconVisible(false);
                break;
            case DETECTED:
                chip.setTextColor(-3355444);
                chip.setChipIconVisible(false);
                break;
            case CONFIRMED:
                chip.setTextColor(-2838729);
                chip.setChipIconResource(android.R.drawable.checkbox_on_background);
                chip.setChipIconVisible(true);
                chip.setChipIconTintResource(R.color.field_completed);
                break;
        }
    }

    private Set<FieldType> autoDetectFields(String text) {
        Set<FieldType> detected = new LinkedHashSet<>();
        if (PHONE_PATTERN.matcher(text).find()) {
            detected.add(FieldType.PHONE);
        }
        if (INVOICE_PATTERN.matcher(text).find()) {
            detected.add(FieldType.INVOICE_NUMBER);
        }
        if (MODEL_PATTERN.matcher(text).find()) {
            detected.add(FieldType.MODEL_NUMBER);
        }
        if (SERIAL_PATTERN.matcher(text).find()) {
            detected.add(FieldType.SERIAL_NUMBER);
        }
        if ((PHONE_PATTERN.matcher(text).find() || (text.matches("(?s).*\\d{1,5}\\s+[A-Za-z].*") && text.matches("(?si).*\\b(?:St|Ave|Blvd|Dr|Rd|Ln|Way|Ct|Pl|Apt|Suite|Hwy)\\b.*"))) && text.matches("(?s).*\\d{1,5}\\s+[A-Za-z].*") && text.matches("(?si).*\\b(?:St|Ave|Blvd|Dr|Rd|Ln|Way|Ct|Pl|Apt|Suite|Hwy)\\b.*")) {
            detected.add(FieldType.ADDRESS);
        }
        String lower = text.toLowerCase();
        int i = 0;
        String[] strArr = {"washer", "dryer", "refrigerator", "dishwasher", "freezer", "range", "washtower", "microwave"};
        while (true) {
            if (i >= 8) {
                break;
            }
            String name = strArr[i];
            if (!lower.contains(name)) {
                i++;
            } else {
                detected.add(FieldType.ITEMS);
                break;
            }
        }
        return detected;
    }

    private void scanFullImageForRegions(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        this.recognizer.process(image).addOnSuccessListener(new OnSuccessListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda7
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                ManualExtractionActivity.this.lambda$scanFullImageForRegions$4((Text) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda8
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                ManualExtractionActivity.lambda$scanFullImageForRegions$5(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scanFullImageForRegions$4(Text visionText) {
        List<PaddleOCREngine.TextRegion> regions = new ArrayList<>();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                Rect box = line.getBoundingBox();
                if (box != null && !line.getText().trim().isEmpty()) {
                    regions.add(new PaddleOCREngine.TextRegion(line.getText().trim(), box));
                }
            }
        }
        this.binding.selectionOverlay.setTextRegions(regions);
    }

    static /* synthetic */ void lambda$scanFullImageForRegions$5(Exception e) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: showPreviewSheet, reason: merged with bridge method [inline-methods] */
    public void lambda$onCreate$0(String extractedText, Rect bitmapRect) {
        Set<FieldType> detected = autoDetectFields(extractedText);
        for (FieldType f : detected) {
            if (this.chipStates.get(f) != ChipState.CONFIRMED) {
                updateChipState(f, ChipState.DETECTED);
            }
        }
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(32, 24, 32, 32);
        linearLayout.setBackgroundColor(-15066598);
        TextView title = new TextView(this);
        title.setText("Extracted Text");
        title.setTextSize(16.0f);
        title.setTextColor(-2838729);
        title.setPadding(0, 0, 0, 12);
        linearLayout.addView(title);
        final EditText etText = new EditText(this);
        etText.setText(extractedText);
        etText.setTextColor(-1);
        etText.setHintTextColor(-10066330);
        etText.setTextSize(14.0f);
        etText.setPadding(16, 12, 16, 12);
        etText.setBackgroundColor(-13816531);
        etText.setMinLines(3);
        etText.setMaxLines(8);
        etText.setGravity(8388659);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(-1, -2);
        etParams.setMargins(0, 0, 0, 14);
        etText.setLayoutParams(etParams);
        linearLayout.addView(etText);
        if (!detected.isEmpty()) {
            StringBuilder hint = new StringBuilder("Recognized: ");
            boolean first = true;
            for (FieldType f2 : detected) {
                if (!first) {
                    hint.append(" · ");
                }
                hint.append(f2.label);
                first = false;
            }
            TextView detectedHint = new TextView(this);
            detectedHint.setText(hint.toString());
            detectedHint.setTextSize(12.0f);
            detectedHint.setTextColor(-3355444);
            detectedHint.setPadding(0, 0, 0, 10);
            linearLayout.addView(detectedHint);
        }
        View divider = new View(this);
        divider.setBackgroundColor(-13421773);
        LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(-1, 1);
        divParams.setMargins(0, 0, 0, 12);
        divider.setLayoutParams(divParams);
        linearLayout.addView(divider);
        TextView assignLabel = new TextView(this);
        assignLabel.setText("Assign to:");
        assignLabel.setTextSize(13.0f);
        assignLabel.setTextColor(-7829368);
        assignLabel.setPadding(0, 0, 0, 8);
        linearLayout.addView(assignLabel);
        FieldType[] values = FieldType.values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            final FieldType field = values[i];
            boolean isDetected = detected.contains(field);
            final TextView btn = new TextView(this);
            Set<FieldType> detected2 = detected;
            btn.setText(field.label);
            btn.setTextSize(14.0f);
            btn.setTextColor(isDetected ? -1 : -8947849);
            btn.setBackgroundColor(isDetected ? -12961222 : -14408668);
            btn.setPadding(16, 14, 16, 14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.setMargins(0, 3, 0, 3);
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ManualExtractionActivity.this.lambda$showPreviewSheet$6(etText, field, btn, view);
                }
            });
            linearLayout.addView(btn);
            i++;
            detected = detected2;
            divParams = divParams;
            title = title;
        }
        TextView done = new TextView(this);
        done.setText("Done");
        done.setTextSize(14.0f);
        done.setTextColor(-2838729);
        done.setPadding(16, 14, 16, 14);
        done.setTextAlignment(4);
        LinearLayout.LayoutParams doneParams = new LinearLayout.LayoutParams(-1, -2);
        doneParams.setMargins(0, 16, 0, 0);
        done.setLayoutParams(doneParams);
        done.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BottomSheetDialog.this.dismiss();
            }
        });
        linearLayout.addView(done);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(linearLayout);
        bottomSheetDialog.setContentView(scrollView);
        bottomSheetDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPreviewSheet$6(EditText etText, FieldType field, TextView btn, View v) {
        String finalText = etText.getText().toString().trim();
        if (!finalText.isEmpty()) {
            applyTextToField(field, finalText);
            updateChipState(field, ChipState.CONFIRMED);
            btn.setTextColor(-2838729);
            btn.setBackgroundColor(-14016512);
            hapticFeedback();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupButtons$8(View v) {
        saveAndFinish();
    }

    private void setupButtons() {
        this.binding.btnDone.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ManualExtractionActivity.this.lambda$setupButtons$8(view);
            }
        });
        this.binding.btnReset.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ManualExtractionActivity.this.lambda$setupButtons$9(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupButtons$9(View v) {
        this.extractedValues.clear();
        this.extractedItems.clear();
        this.binding.selectionOverlay.clearSelections();
        for (FieldType field : FieldType.values()) {
            ChipState initial = this.initialChipStates.containsKey(field) ? this.initialChipStates.get(field) : ChipState.EMPTY;
            updateChipState(field, initial);
        }
        this.binding.tvResultPreview.setVisibility(8);
        this.binding.tvHint.setText("Zoom/pan with two fingers · Draw a box to extract text");
        Toast.makeText(this, "Extractions cleared", 0).show();
    }

    private void applyTextToField(FieldType field, String text) {
        switch (field.ordinal()) {
            case 4:
                String existing = this.extractedValues.get(FieldType.NOTES);
                this.extractedValues.put(FieldType.NOTES, (existing == null || existing.isEmpty()) ? text : existing + StringUtils.LF + text);
                break;
            case 5:
                this.extractedItems.add(new DeliveryItem(text));
                break;
            case 6:
                if (this.extractedItems.isEmpty()) {
                    this.extractedItems.add(new DeliveryItem("Unknown", text, ""));
                    break;
                } else {
                    this.extractedItems.get(this.extractedItems.size() - 1).model = text;
                    break;
                }
            case 7:
                if (this.extractedItems.isEmpty()) {
                    this.extractedItems.add(new DeliveryItem("Unknown", "", text));
                    break;
                } else {
                    this.extractedItems.get(this.extractedItems.size() - 1).serial = text;
                    break;
                }
            default:
                this.extractedValues.put(field, text);
                break;
        }
    }

    private void hapticFeedback() {
        if (Build.VERSION.SDK_INT >= 30) {
            this.binding.selectionOverlay.performHapticFeedback(16);
        } else {
            this.binding.selectionOverlay.performHapticFeedback(0);
        }
    }

    private void saveAndFinish() {
        if (this.currentInvoice == null) {
            return;
        }
        String val = this.extractedValues.get(FieldType.INVOICE_NUMBER);
        if (val != null) {
            this.currentInvoice.setInvoiceNumber(val);
        }
        String val2 = this.extractedValues.get(FieldType.CUSTOMER_NAME);
        if (val2 != null) {
            this.currentInvoice.setCustomerName(val2);
        }
        String val3 = this.extractedValues.get(FieldType.ADDRESS);
        if (val3 != null) {
            this.currentInvoice.setAddress(val3);
        }
        String val4 = this.extractedValues.get(FieldType.PHONE);
        if (val4 != null) {
            this.currentInvoice.setPhone(val4);
        }
        String val5 = this.extractedValues.get(FieldType.NOTES);
        if (val5 != null) {
            String existingNotes = this.currentInvoice.getNotes();
            this.currentInvoice.setNotes((existingNotes == null || existingNotes.isEmpty()) ? val5 : existingNotes + StringUtils.LF + val5);
        }
        if (!this.extractedItems.isEmpty()) {
            List<DeliveryItem> existing = ItemsHelper.fromJson(this.currentInvoice.getItems());
            existing.addAll(this.extractedItems);
            this.currentInvoice.setItems(ItemsHelper.toJson(existing));
        }
        new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$saveAndFinish$11();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveAndFinish$11() {
        this.database.invoiceDao().update(this.currentInvoice);
        runOnUiThread(new Runnable() { // from class: com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$saveAndFinish$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveAndFinish$10() {
        Toast.makeText(this, R.string.extraction_saved, 0).show();
        setResult(-1);
        finish();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.recognizer != null) {
            this.recognizer.close();
        }
        if (this.fullBitmap == null || this.fullBitmap.isRecycled()) {
            return;
        }
        this.fullBitmap.recycle();
    }
}
