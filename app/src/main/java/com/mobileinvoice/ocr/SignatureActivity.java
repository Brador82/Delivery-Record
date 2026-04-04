package com.mobileinvoice.ocr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import com.mobileinvoice.ocr.databinding.ActivitySignatureBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes7.dex */
public class SignatureActivity extends BaseActivity {
    private ActivitySignatureBinding binding;
    private String customerName;
    private String deliveryDate;
    private long invoiceId = -1;
    private SignatureView signatureView;

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
                setTheme(R.style.Theme_MobileInvoiceOCR_LightMarble_Fullscreen);
                break;
            case 2:
                setTheme(R.style.Theme_MobileInvoiceOCR_Blended_Fullscreen);
                break;
            default:
                setTheme(R.style.Theme_MobileInvoiceOCR_Fullscreen);
                break;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        setRequestedOrientation(4);
        getWindow().addFlags(128);
        this.binding = ActivitySignatureBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.signatureView = this.binding.signatureCanvas;
        this.customerName = getIntent().getStringExtra("customer_name");
        this.invoiceId = getIntent().getLongExtra("invoice_id", -1L);
        this.deliveryDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date());
        this.binding.tvDeliveryDate.setText(this.deliveryDate);
        if (this.customerName != null && !this.customerName.isEmpty()) {
            this.binding.tvCustomerName.setText(this.customerName);
        } else {
            this.binding.tvCustomerName.setText("Customer");
        }
        setupButtons();
    }

    private void setupButtons() {
        this.binding.btnClear.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SignatureActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SignatureActivity.this.lambda$setupButtons$0(view);
            }
        });
        this.binding.btnSave.setOnClickListener(new View.OnClickListener() { // from class: com.mobileinvoice.ocr.SignatureActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SignatureActivity.this.lambda$setupButtons$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupButtons$0(View v) {
        this.signatureView.clear();
        Toast.makeText(this, "Signature cleared", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupButtons$1(View v) {
        saveSignature();
    }

    private void saveSignature() {
        if (!this.signatureView.hasSignature()) {
            Toast.makeText(this, "Please sign before saving", 0).show();
            return;
        }
        Bitmap signatureBitmap = this.signatureView.getSignatureBitmap();
        if (signatureBitmap == null) {
            Toast.makeText(this, "No signature to save", 0).show();
            return;
        }
        try {
            File signaturesDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Signatures");
            if (!signaturesDir.exists()) {
                signaturesDir.mkdirs();
            }
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String signatureFilename = "signature_" + (this.invoiceId > 0 ? this.invoiceId + "_" : "") + timestamp + ".png";
            File signatureFile = new File(signaturesDir, signatureFilename);
            FileOutputStream fos = new FileOutputStream(signatureFile);
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            String formFilename = "delivery_form_" + (this.invoiceId > 0 ? this.invoiceId + "_" : "") + timestamp + ".png";
            File formFile = new File(signaturesDir, formFilename);
            Bitmap formBitmap = generateDeliveryForm(signatureBitmap);
            FileOutputStream formFos = new FileOutputStream(formFile);
            formBitmap.compress(Bitmap.CompressFormat.PNG, 100, formFos);
            formFos.flush();
            formFos.close();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("signature_path", signatureFile.getAbsolutePath());
            resultIntent.putExtra("form_path", formFile.getAbsolutePath());
            resultIntent.putExtra("delivery_date", this.deliveryDate);
            resultIntent.putExtra("customer_name", this.customerName);
            resultIntent.putExtra("invoice_id", this.invoiceId);
            resultIntent.setData(Uri.fromFile(formFile));
            setResult(-1, resultIntent);
            Toast.makeText(this, "Delivery accepted & form saved!", 0).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving signature: " + e.getMessage(), 0).show();
        }
    }

    private Bitmap generateDeliveryForm(Bitmap signatureBitmap) {
        Bitmap formBitmap = Bitmap.createBitmap(1275, 1650, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(formBitmap);
        canvas.drawColor(-1);
        Paint headerPaint = new Paint(1);
        headerPaint.setColor(Color.parseColor("#D4AF37"));
        headerPaint.setTextSize(36.0f);
        headerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint titlePaint = new Paint(1);
        titlePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        titlePaint.setTextSize(28.0f);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint labelPaint = new Paint(1);
        labelPaint.setColor(Color.parseColor("#D4AF37"));
        labelPaint.setTextSize(22.0f);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint valuePaint = new Paint(1);
        valuePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        valuePaint.setTextSize(22.0f);
        Paint termsPaint = new Paint(1);
        termsPaint.setColor(-12303292);
        termsPaint.setTextSize(18.0f);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#D4AF37"));
        linePaint.setStrokeWidth(3.0f);
        float headerWidth = headerPaint.measureText("DELIVERY ACCEPTANCE SIGN OFF");
        canvas.drawText("DELIVERY ACCEPTANCE SIGN OFF", (1275 - headerWidth) / 2.0f, 60 + 36, headerPaint);
        int y = 60 + 60;
        canvas.drawLine(60, y, 1275 - 60, y, linePaint);
        int y2 = y + 40;
        canvas.drawText("Delivery Date:", 60, y2, labelPaint);
        canvas.drawText(this.deliveryDate, 60 + 180, y2, valuePaint);
        int y3 = y2 + 35;
        canvas.drawText("Customer Name:", 60, y3, labelPaint);
        canvas.drawText(this.customerName != null ? this.customerName : "N/A", 60 + 200, y3, valuePaint);
        int y4 = y3 + 50;
        canvas.drawText("Terms of Acceptance:", 60, y4, titlePaint);
        int y5 = y4 + 35;
        String[] terms = {"1. Customer or person(s) over the age of 18 present upon delivery &", "   installation of appliances, and agree to accepting are installed correctly", "   without any harm to the home.", "", "2. Customer or person(s) over the age of 18 was present for delivery and", "   installation agrees that appliances, a test run/overview was provided,", "   no damages were noted & that they were installed correctly without any", "   damage arising to the house or appliance from the use of customers old", "   appliance hoses, and/or improperly maintained or damaged", "   faucets/drains/valves, etc."};
        for (String line : terms) {
            canvas.drawText(line, 60, y5, termsPaint);
            y5 += 24;
        }
        int y6 = y5 + 20;
        canvas.drawLine(60, y6, 1275 - 60, y6, linePaint);
        int y7 = y6 + 30;
        canvas.drawText("TERMS AND CONDITIONS", 60, y7, titlePaint);
        int y8 = y7 + 35;
        String[] conditions = {"Scratch and Dent Goods:", "• All delivery sales are final. There is no return or refund on scratch or", "  dent product, unless such item is non-repairable.", "• Purchaser is solely responsible for appliance(s) back to the store for", "  goods exchange.", "", "Warranty:", "• Warranty must begin within 30 days after delivery by contacting the", "  manufacturer or call 800-905-0443.", "• Customer is responsible for calling customer care as soon as any issue", "  arises within the warranty period.", "• After 30 days - Customer is still responsible for repair issues and", "  delivery/pickup, and labor.", "• Warranty does not cover negligence, power surges, or pest infestation."};
        for (String line2 : conditions) {
            canvas.drawText(line2, 60, y8, termsPaint);
            y8 += 24;
        }
        int y9 = y8 + 30;
        canvas.drawLine(60, y9, 1275 - 60, y9, linePaint);
        int y10 = y9 + 30;
        canvas.drawText("Customer Signature:", 60, y10, labelPaint);
        int y11 = y10 + 20;
        int sigWidth = Math.min(signatureBitmap.getWidth(), 1275 - (60 * 2));
        int sigHeight = Math.min(signatureBitmap.getHeight(), 200);
        float scale = Math.min(sigWidth / signatureBitmap.getWidth(), sigHeight / signatureBitmap.getHeight());
        int scaledWidth = (int) (signatureBitmap.getWidth() * scale);
        int scaledHeight = (int) (signatureBitmap.getHeight() * scale);
        Bitmap scaledSig = Bitmap.createScaledBitmap(signatureBitmap, scaledWidth, scaledHeight, true);
        canvas.drawBitmap(scaledSig, 60, y11, (Paint) null);
        int y12 = y11 + scaledHeight + 10;
        canvas.drawLine(60, y12, 1275 / 2, y12, linePaint);
        int y13 = y12 + 25;
        canvas.drawText("Date: " + this.deliveryDate, 60, y13, valuePaint);
        int i = y13 + 50;
        Paint footerPaint = new Paint(1);
        footerPaint.setColor(Color.parseColor("#D4AF37"));
        footerPaint.setTextSize(16.0f);
        float footerWidth = footerPaint.measureText("Please refer to www.ccsarms.com or call 800-905-0443 for warranty registration.");
        canvas.drawText("Please refer to www.ccsarms.com or call 800-905-0443 for warranty registration.", (1275 - footerWidth) / 2.0f, 1650 - 60, footerPaint);
        return formBitmap;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.signatureView.hasSignature()) {
            new AlertDialog.Builder(this).setTitle("Discard Signature?").setMessage("You have an unsigned delivery. Are you sure you want to go back?").setPositiveButton("Discard", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.SignatureActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SignatureActivity.this.lambda$onBackPressed$2(dialogInterface, i);
                }
            }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
        } else {
            super.onBackPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBackPressed$2(DialogInterface d, int w) {
        super.onBackPressed();
    }
}
