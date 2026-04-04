package com.mobileinvoice.ocr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import com.mobileinvoice.ocr.database.Invoice;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes7.dex */
public class ExportHelper {
    private static final String ICON_CAMERA = "<svg width='28' height='28' viewBox='0 0 28 28' style='vertical-align:middle;margin-right:8px;'><defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs><rect x='3' y='8' width='22' height='16' rx='3' fill='url(#goldGrad)'/><rect x='10' y='4' width='8' height='4' rx='1' fill='url(#goldShine)'/><circle cx='14' cy='16' r='5' fill='#0D0D0D'/><circle cx='14' cy='16' r='3.5' fill='url(#goldShine)'/><circle cx='14' cy='16' r='1.5' fill='#0D0D0D' opacity='0.5'/><circle cx='21' y='11' r='1.5' fill='#FFD700'/></svg>";
    private static final String ICON_CHECK = "<svg width='24' height='24' viewBox='0 0 24 24' style='vertical-align:middle;margin-right:6px;'><defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs><circle cx='12' cy='12' r='10' fill='url(#goldGrad)'/><path d='M7 12 L10 15 L17 8' stroke='#FFD700' stroke-width='2.5' fill='none' stroke-linecap='round' stroke-linejoin='round'/></svg>";
    private static final String ICON_DOCUMENT = "<svg width='28' height='28' viewBox='0 0 28 28' style='vertical-align:middle;margin-right:8px;'><defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs><path d='M6 2 L18 2 L22 6 L22 26 L6 26 Z' fill='url(#goldGrad)'/><path d='M18 2 L18 6 L22 6 Z' fill='url(#goldShine)'/><rect x='9' y='10' width='10' height='1.5' rx='0.5' fill='#0D0D0D' opacity='0.6'/><rect x='9' y='14' width='10' height='1.5' rx='0.5' fill='#0D0D0D' opacity='0.6'/><rect x='9' y='18' width='7' height='1.5' rx='0.5' fill='#0D0D0D' opacity='0.6'/></svg>";
    private static final String ICON_PACKAGE = "<svg width='32' height='32' viewBox='0 0 32 32' style='vertical-align:middle;margin-right:8px;'><defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs><path d='M16 2 L28 8 L28 24 L16 30 L4 24 L4 8 Z' fill='url(#goldGrad)'/><path d='M16 2 L28 8 L16 14 L4 8 Z' fill='url(#goldShine)'/><path d='M16 14 L16 30 L4 24 L4 8 Z' fill='url(#goldGrad)' opacity='0.8'/><line x1='16' y1='14' x2='16' y2='30' stroke='#FFD700' stroke-width='1'/><line x1='4' y1='8' x2='16' y2='14' stroke='#FFD700' stroke-width='0.5'/></svg>";
    private static final String ICON_SIGNATURE = "<svg width='28' height='28' viewBox='0 0 28 28' style='vertical-align:middle;margin-right:8px;'><defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs><path d='M4 24 Q8 20 12 22 Q16 24 20 20 Q22 18 24 18' stroke='url(#goldShine)' stroke-width='2.5' fill='none' stroke-linecap='round'/><path d='M20 4 L24 8 L12 20 L8 20 L8 16 Z' fill='url(#goldGrad)'/><path d='M20 4 L24 8 L22 10 L18 6 Z' fill='url(#goldShine)'/><path d='M8 20 L10 22 L8 24 L6 22 Z' fill='#D4AF37'/></svg>";
    private static final String ICON_TRUCK = "<svg width='32' height='32' viewBox='0 0 32 32' style='vertical-align:middle;margin-right:8px;'><defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs><rect x='2' y='10' width='18' height='12' rx='2' fill='url(#goldGrad)'/><path d='M20 14 L20 22 L28 22 L28 17 L24 14 Z' fill='url(#goldShine)'/><rect x='21' y='15' width='5' height='4' rx='1' fill='#0D0D0D' opacity='0.7'/><circle cx='8' cy='24' r='3' fill='url(#goldShine)'/><circle cx='8' cy='24' r='1.5' fill='#0D0D0D'/><circle cx='24' cy='24' r='3' fill='url(#goldShine)'/><circle cx='24' cy='24' r='1.5' fill='#0D0D0D'/><text x='7' y='18' font-size='6' font-weight='bold' fill='#0D0D0D' font-family='Arial'>A4L</text></svg>";
    private static final String SVG_GRADIENT_DEFS = "<defs><linearGradient id='goldGrad' x1='0%' y1='100%' x2='0%' y2='0%'><stop offset='0%' style='stop-color:#0D0D0D'/><stop offset='100%' style='stop-color:#D4AF37'/></linearGradient><linearGradient id='goldShine' x1='0%' y1='0%' x2='100%' y2='100%'><stop offset='0%' style='stop-color:#FFD700'/><stop offset='50%' style='stop-color:#D4AF37'/><stop offset='100%' style='stop-color:#B8860B'/></linearGradient></defs>";
    private ExportCompleteCallback callback;
    private final Context context;

    public interface ExportCompleteCallback {
        void onExportComplete(File exportFolder, int invoiceCount);
    }

    public ExportHelper(Context context) {
        this.context = context;
    }

    public void setExportCompleteCallback(ExportCompleteCallback callback) {
        this.callback = callback;
    }

    public void exportToMarkdown(List<Invoice> invoices) {
        SimpleDateFormat dateFormat;
        File downloadsDir;
        char c = 0;
        if (invoices == null || invoices.isEmpty()) {
            Toast.makeText(this.context, "No invoices to export", 0).show();
            return;
        }
        StringBuilder md = new StringBuilder();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        File downloadsDir2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File exportDir = new File(downloadsDir2, AppSettings.getInstance(this.context).getExportFolderName());
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        int cardNum = 1;
        for (Invoice invoice : invoices) {
            md.append("---\n");
            md.append("### Delivery Card " + cardNum + "\n\n");
            md.append("**Invoice Number:** " + escapeMD(invoice.getInvoiceNumber()) + "  \n");
            md.append("**Customer Name:** " + escapeMD(invoice.getCustomerName()) + "  \n");
            md.append("**Address:** " + escapeMD(invoice.getAddress()) + "  \n");
            md.append("**Phone:** " + escapeMD(invoice.getPhone()) + "  \n");
            md.append("**Service Type:** " + escapeMD(invoice.getServiceType()) + "  \n\n");
            md.append("**Timestamp:** " + escapeMD(dateFormat2.format(new Date(invoice.getTimestamp()))) + "  \n");
            md.append("**Items:** " + escapeMD(getItemsDisplay(invoice)) + "  \n");
            String[] podPaths = new String[6];
            podPaths[c] = invoice.getPodImagePath1();
            podPaths[1] = invoice.getPodImagePath2();
            podPaths[2] = invoice.getPodImagePath3();
            podPaths[3] = invoice.getPodImagePath4();
            podPaths[4] = invoice.getPodImagePath5();
            podPaths[5] = invoice.getPodImagePath6();
            int i = 0;
            while (i < podPaths.length) {
                String podPath = podPaths[i];
                if (podPath == null || podPath.isEmpty()) {
                    dateFormat = dateFormat2;
                    downloadsDir = downloadsDir2;
                } else {
                    File podSrc = new File(podPath);
                    dateFormat = dateFormat2;
                    downloadsDir = downloadsDir2;
                    File podDest = new File(exportDir, "pod_photo_" + cardNum + "_" + (i + 1) + ".jpg");
                    try {
                        copyImageFile(podSrc, podDest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    md.append("**POD Photo " + (i + 1) + ":** ![](pod_photo_" + cardNum + "_" + (i + 1) + ".jpg)  \n");
                }
                i++;
                dateFormat2 = dateFormat;
                downloadsDir2 = downloadsDir;
            }
            SimpleDateFormat dateFormat3 = dateFormat2;
            File downloadsDir3 = downloadsDir2;
            if (invoice.getSignatureImagePath() != null && !invoice.getSignatureImagePath().isEmpty()) {
                File sigSrc = new File(invoice.getSignatureImagePath());
                File sigDest = new File(exportDir, "signature_" + cardNum + ".jpg");
                try {
                    copyImageFile(sigSrc, sigDest);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                md.append("**Signature:** ![](signature_" + cardNum + ".jpg)  \n");
            }
            if (invoice.getOriginalImagePath() != null && !invoice.getOriginalImagePath().isEmpty()) {
                File invSrc = new File(invoice.getOriginalImagePath());
                File invDest = new File(exportDir, "invoice_" + cardNum + ".jpg");
                try {
                    copyImageFile(invSrc, invDest);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                md.append("**Invoice Image:** ![](invoice_" + cardNum + ".jpg)  \n");
            }
            md.append("**Notes:** " + escapeMD(invoice.getNotes()) + "  \n");
            md.append("\n---\n\n");
            cardNum++;
            dateFormat2 = dateFormat3;
            downloadsDir2 = downloadsDir3;
            c = 0;
        }
        String filename = "invoices_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".md";
        File file = saveToFile(exportDir, filename, md.toString());
        if (file != null) {
            Toast.makeText(this.context, "Exported " + invoices.size() + " invoices to:\nDownloads/" + AppSettings.getInstance(this.context).getExportFolderName() + PackagingURIHelper.FORWARD_SLASH_STRING + filename, 1).show();
            if (this.callback != null) {
                this.callback.onExportComplete(exportDir, invoices.size());
            }
        }
    }

    public void exportToHTMLZip(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            Toast.makeText(this.context, "No invoices to export", 0).show();
            return;
        }
        try {
            String dateFolder = new SimpleDateFormat("'Delivery_Docket_'MM-dd-yyyy", Locale.US).format(new Date());
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File mainExportDir = new File(downloadsDir, AppSettings.getInstance(this.context).getExportFolderName());
            File deliveryDir = new File(mainExportDir, dateFolder);
            if (!deliveryDir.exists()) {
                deliveryDir.mkdirs();
            }
            int successCount = 0;
            for (Invoice invoice : invoices) {
                try {
                    String htmlContent = generateHTMLDeliveryCard(invoice);
                    String filename = sanitizeFilename(invoice.getCustomerName() + "_" + invoice.getInvoiceNumber()) + ".html";
                    File htmlFile = new File(deliveryDir, filename);
                    FileOutputStream fos = new FileOutputStream(htmlFile);
                    fos.write(htmlContent.getBytes(StandardCharsets.UTF_8));
                    fos.close();
                    successCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (successCount <= 0) {
                Toast.makeText(this.context, "Failed to export delivery cards", 0).show();
                return;
            }
            String indexHTML = generateIndexHTML(invoices);
            File indexFile = new File(deliveryDir, "index.html");
            FileOutputStream fos2 = new FileOutputStream(indexFile);
            fos2.write(indexHTML.getBytes(StandardCharsets.UTF_8));
            fos2.close();
            String zipFilename = dateFolder + ".zip";
            File zipFile = new File(mainExportDir, zipFilename);
            zipFolder(deliveryDir, zipFile);
            showShareDialog(zipFile, successCount);
            if (this.callback != null) {
                this.callback.onExportComplete(zipFile, successCount);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this.context, "Error during export: " + e2.getMessage(), 1).show();
        }
    }

    public void exportToPDFZip(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            Toast.makeText(this.context, "No invoices to export", 0).show();
            return;
        }
        try {
            String dateFolder = new SimpleDateFormat("'Delivery_Docket_'MM-dd-yyyy", Locale.US).format(new Date());
            File baseDir = this.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (baseDir == null) {
                baseDir = this.context.getFilesDir();
            }
            File mainExportDir = new File(baseDir, AppSettings.getInstance(this.context).getExportFolderName());
            File deliveryDir = new File(mainExportDir, dateFolder);
            if (!deliveryDir.exists()) {
                deliveryDir.mkdirs();
            }
            int successCount = 0;
            for (Invoice invoice : invoices) {
                try {
                    String filename = sanitizeFilename(invoice.getCustomerName() + "_" + invoice.getInvoiceNumber()) + ".pdf";
                    File pdfFile = new File(deliveryDir, filename);
                    generatePDFDeliveryCard(invoice, pdfFile);
                    successCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (successCount <= 0) {
                Toast.makeText(this.context, "Failed to export delivery cards", 0).show();
                return;
            }
            File summaryFile = new File(deliveryDir, "00_Delivery_Summary.pdf");
            generateSummaryPDF(invoices, summaryFile);
            String zipFilename = dateFolder + ".zip";
            File zipFile = new File(mainExportDir, zipFilename);
            zipFolder(deliveryDir, zipFile);
            showShareDialog(zipFile, successCount);
            if (this.callback != null) {
                this.callback.onExportComplete(zipFile, successCount);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this.context, "Error during export: " + e2.getMessage(), 1).show();
        }
    }

    private void generatePDFDeliveryCard(Invoice invoice, File outputFile) throws IOException {
        Paint valuePaint;
        ExportHelper exportHelper;
        Paint headerPaint;
        String[] podPaths;
        boolean hasPOD;
        String str;
        String mapLink;
        String[][] fields;
        int i;
        PdfDocument document;
        int pageHeight;
        int pageWidth;
        Paint valuePaint2;
        int pageWidth2 = 612;
        int pageHeight2 = 792;
        PdfDocument document2 = new PdfDocument();
        String mapLink2 = "MM/dd/yyyy hh:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#2c3e50"));
        titlePaint.setTextSize(24.0f);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint headerPaint2 = new Paint();
        headerPaint2.setColor(Color.parseColor("#34495e"));
        headerPaint2.setTextSize(16.0f);
        headerPaint2.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint labelPaint = new Paint();
        labelPaint.setColor(Color.parseColor("#7f8c8d"));
        labelPaint.setTextSize(12.0f);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint valuePaint3 = new Paint();
        valuePaint3.setColor(Color.parseColor("#2c3e50"));
        valuePaint3.setTextSize(12.0f);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#3498db"));
        linePaint.setStrokeWidth(3.0f);
        Paint footerPaint = new Paint();
        footerPaint.setColor(Color.parseColor("#95a5a6"));
        footerPaint.setTextSize(10.0f);
        footerPaint.setTextAlign(Paint.Align.CENTER);
        final int[] currentY = {50};
        final int[] pageNum = {1};
        final PdfDocument.Page[] currentPage = {document2.startPage(new PdfDocument.PageInfo.Builder(612, 792, pageNum[0]).create())};
        final Canvas[] canvas = {currentPage[0].getCanvas()};
        Paint valuePaint4 = valuePaint3;
        final int pageHeightFinal = pageHeight2;
        final PdfDocument documentFinal = document2;
        final int pageWidthFinal = pageWidth2;
        Runnable checkNewPage = new Runnable() { // from class: com.mobileinvoice.ocr.ExportHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public void run() {
                ExportHelper.lambda$generatePDFDeliveryCard$0(currentY, pageHeightFinal, documentFinal, currentPage, pageNum, pageWidthFinal, canvas);
            }
        };
        int contentWidth = 612 - (40 * 2);
        canvas[0].drawText("Delivery Card", 40, currentY[0], titlePaint);
        currentY[0] = currentY[0] + 10;
        canvas[0].drawLine(40, currentY[0], 612 - 40, currentY[0], linePaint);
        currentY[0] = currentY[0] + 30;
        String mapLink3 = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(invoice.getAddress());
        String[][] fields2 = {new String[]{"Invoice #:", invoice.getInvoiceNumber()}, new String[]{"Customer:", invoice.getCustomerName()}, new String[]{"Address:", invoice.getAddress()}, new String[]{"Directions:", mapLink3}, new String[]{"Phone:", invoice.getPhone()}, new String[]{"Service Type:", invoice.getServiceType()}, new String[]{"Date/Time:", dateFormat.format(new Date(invoice.getTimestamp()))}, new String[]{"Items:", getItemsDisplay(invoice)}};
        int pageHeight3 = fields2.length;
        int i2 = 0;
        while (i2 < pageHeight3) {
            String[] field = fields2[i2];
            checkNewPage.run();
            Paint linePaint2 = linePaint;
            Paint titlePaint2 = titlePaint;
            SimpleDateFormat dateFormat2 = dateFormat;
            canvas[0].drawText(field[0], 40, currentY[0], labelPaint);
            String value = field[1] != null ? field[1] : "";
            if (value.length() <= 50) {
                str = mapLink2;
                mapLink = mapLink3;
                fields = fields2;
                i = pageHeight3;
                document = document2;
                pageHeight = pageHeight2;
                pageWidth = pageWidth2;
                valuePaint2 = valuePaint4;
                canvas[0].drawText(value, 40 + 80, currentY[0], valuePaint2);
                currentY[0] = currentY[0] + 20;
            } else {
                mapLink = mapLink3;
                fields = fields2;
                document = document2;
                i = pageHeight3;
                pageHeight = pageHeight2;
                str = mapLink2;
                pageWidth = pageWidth2;
                valuePaint2 = valuePaint4;
                drawWrappedText(canvas[0], value, 40 + 80, currentY[0], contentWidth - 80, valuePaint2);
                currentY[0] = currentY[0] + (((value.length() / 50) + 1) * 16);
            }
            i2++;
            valuePaint4 = valuePaint2;
            pageWidth2 = pageWidth;
            document2 = document;
            pageHeight2 = pageHeight;
            linePaint = linePaint2;
            titlePaint = titlePaint2;
            dateFormat = dateFormat2;
            mapLink2 = str;
            pageHeight3 = i;
            fields2 = fields;
            mapLink3 = mapLink;
        }
        String str2 = mapLink2;
        int pageHeight4 = pageHeight2;
        PdfDocument document3 = document2;
        int pageWidth3 = pageWidth2;
        Paint valuePaint5 = valuePaint4;
        if (invoice.getNotes() == null || invoice.getNotes().isEmpty()) {
            valuePaint = valuePaint5;
        } else {
            checkNewPage.run();
            canvas[0].drawText("Notes:", 40, currentY[0], labelPaint);
            valuePaint = valuePaint5;
            drawWrappedText(canvas[0], invoice.getNotes(), 40 + 80, currentY[0], contentWidth - 80, valuePaint5);
            currentY[0] = currentY[0] + 30;
        }
        currentY[0] = currentY[0] + 20;
        if (invoice.getOriginalImagePath() == null || invoice.getOriginalImagePath().isEmpty()) {
            exportHelper = this;
            headerPaint = headerPaint2;
        } else {
            checkNewPage.run();
            headerPaint = headerPaint2;
            canvas[0].drawText("Original Invoice", 40, currentY[0], headerPaint);
            currentY[0] = currentY[0] + 15;
            exportHelper = this;
            Bitmap originalImg = exportHelper.loadAndScaleImage(invoice.getOriginalImagePath(), contentWidth, pageHeight4 - 100);
            if (originalImg != null) {
                if (currentY[0] + originalImg.getHeight() > pageHeight4 - 80) {
                    document3.finishPage(currentPage[0]);
                    pageNum[0] = pageNum[0] + 1;
                    currentPage[0] = document3.startPage(new PdfDocument.PageInfo.Builder(pageWidth3, pageHeight4, pageNum[0]).create());
                    canvas[0] = currentPage[0].getCanvas();
                    currentY[0] = 50;
                }
                canvas[0].drawBitmap(originalImg, 40, currentY[0], null);
                currentY[0] = currentY[0] + originalImg.getHeight() + 20;
                originalImg.recycle();
            }
        }
        String[] podPaths2 = {invoice.getPodImagePath1(), invoice.getPodImagePath2(), invoice.getPodImagePath3(), invoice.getPodImagePath4(), invoice.getPodImagePath5(), invoice.getPodImagePath6()};
        boolean hasPOD2 = false;
        int i3 = 0;
        while (i3 < podPaths2.length) {
            if (podPaths2[i3] == null || podPaths2[i3].isEmpty()) {
                podPaths = podPaths2;
                hasPOD = hasPOD2;
            } else {
                if (!hasPOD2) {
                    checkNewPage.run();
                    canvas[0].drawText("Proof of Delivery Photos", 40, currentY[0], headerPaint);
                    currentY[0] = currentY[0] + 15;
                    hasPOD2 = true;
                }
                Bitmap podImg = exportHelper.loadAndScaleImage(podPaths2[i3], contentWidth, pageHeight4 - 100);
                if (podImg == null) {
                    podPaths = podPaths2;
                    hasPOD = hasPOD2;
                } else {
                    if (currentY[0] + podImg.getHeight() > pageHeight4 - 80) {
                        document3.finishPage(currentPage[0]);
                        pageNum[0] = pageNum[0] + 1;
                        currentPage[0] = document3.startPage(new PdfDocument.PageInfo.Builder(pageWidth3, pageHeight4, pageNum[0]).create());
                        canvas[0] = currentPage[0].getCanvas();
                        currentY[0] = 50;
                    }
                    podPaths = podPaths2;
                    hasPOD = hasPOD2;
                    canvas[0].drawText("Photo " + (i3 + 1), 40, currentY[0], valuePaint);
                    currentY[0] = currentY[0] + 15;
                    canvas[0].drawBitmap(podImg, 40, currentY[0], null);
                    currentY[0] = currentY[0] + podImg.getHeight() + 15;
                    podImg.recycle();
                }
            }
            hasPOD2 = hasPOD;
            i3++;
            podPaths2 = podPaths;
        }
        if (invoice.getSignatureImagePath() != null && !invoice.getSignatureImagePath().isEmpty()) {
            document3.finishPage(currentPage[0]);
            pageNum[0] = pageNum[0] + 1;
            currentPage[0] = document3.startPage(new PdfDocument.PageInfo.Builder(pageWidth3, pageHeight4, pageNum[0]).create());
            canvas[0] = currentPage[0].getCanvas();
            currentY[0] = 30;
            canvas[0].drawText("Delivery Acceptance Form", 40, currentY[0], headerPaint);
            currentY[0] = currentY[0] + 20;
            Bitmap formImg = exportHelper.loadAndScaleImage(invoice.getSignatureImagePath(), contentWidth, pageHeight4 - 100);
            if (formImg != null) {
                canvas[0].drawBitmap(formImg, 40, currentY[0], null);
                currentY[0] = currentY[0] + formImg.getHeight() + 20;
                formImg.recycle();
            }
        }
        String footerText = "Generated by Mobile Invoice OCR • Export Date: " + new SimpleDateFormat(str2, Locale.US).format(new Date());
        canvas[0].drawText(footerText, pageWidth3 / 2, pageHeight4 - 30, footerPaint);
        document3.finishPage(currentPage[0]);
        FileOutputStream fos = new FileOutputStream(outputFile);
        document3.writeTo(fos);
        document3.close();
        fos.close();
    }

    static /* synthetic */ void lambda$generatePDFDeliveryCard$0(int[] currentY, int pageHeight, PdfDocument document, PdfDocument.Page[] currentPage, int[] pageNum, int pageWidth, Canvas[] canvas) {
        if (currentY[0] > pageHeight - 80) {
            document.finishPage(currentPage[0]);
            pageNum[0] = pageNum[0] + 1;
            currentPage[0] = document.startPage(new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum[0]).create());
            canvas[0] = currentPage[0].getCanvas();
            currentY[0] = 50;
        }
    }

    private void generateSummaryPDF(List<Invoice> invoices, File outputFile) throws IOException {
        Paint subtitlePaint;
        Canvas canvas;
        int margin = 612;
        int pageHeight = 792;
        PdfDocument document = new PdfDocument();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#667eea"));
        titlePaint.setTextSize(28.0f);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint subtitlePaint2 = new Paint();
        subtitlePaint2.setColor(Color.parseColor("#666666"));
        subtitlePaint2.setTextSize(14.0f);
        Paint headerPaint = new Paint();
        headerPaint.setColor(Color.parseColor("#2c3e50"));
        headerPaint.setTextSize(14.0f);
        headerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#333333"));
        textPaint.setTextSize(12.0f);
        Paint numberPaint = new Paint();
        numberPaint.setColor(Color.parseColor("#3498db"));
        numberPaint.setTextSize(16.0f);
        numberPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#ecf0f1"));
        linePaint.setStrokeWidth(1.0f);
        int margin2 = 40;
        PdfDocument.Page page = document.startPage(new PdfDocument.PageInfo.Builder(612, 792, 1).create());
        Canvas canvas2 = page.getCanvas();
        int pageNum = 1;
        canvas2.drawText("Delivery Docket", 40, 50, titlePaint);
        int currentY = 50 + 25;
        canvas2.drawText("Date: " + dateFormat.format(new Date()), 40, currentY, subtitlePaint2);
        int currentY2 = currentY + 40;
        int withPOD = 0;
        int withPOD2 = 0;
        for (Invoice inv : invoices) {
            if ((inv.getPodImagePath1() != null && !inv.getPodImagePath1().isEmpty()) || ((inv.getPodImagePath2() != null && !inv.getPodImagePath2().isEmpty()) || ((inv.getPodImagePath3() != null && !inv.getPodImagePath3().isEmpty()) || ((inv.getPodImagePath4() != null && !inv.getPodImagePath4().isEmpty()) || ((inv.getPodImagePath5() != null && !inv.getPodImagePath5().isEmpty()) || (inv.getPodImagePath6() != null && !inv.getPodImagePath6().isEmpty())))))) {
                withPOD++;
            }
            if (inv.getSignatureImagePath() != null && !inv.getSignatureImagePath().isEmpty()) {
                withPOD2++;
            }
        }
        canvas2.drawText("Summary: " + invoices.size() + " deliveries | " + withPOD + " with POD | " + withPOD2 + " with signatures", 40, currentY2, subtitlePaint2);
        int currentY3 = currentY2 + 30;
        canvas2.drawText("Deliveries", 40, currentY3, headerPaint);
        int currentY4 = currentY3 + 20;
        int i = 0;
        PdfDocument.Page page2 = page;
        while (i < invoices.size()) {
            if (currentY4 <= pageHeight - 100) {
                subtitlePaint = subtitlePaint2;
                canvas = canvas2;
            } else {
                document.finishPage(page2);
                int pageNum2 = pageNum + 1;
                subtitlePaint = subtitlePaint2;
                page2 = document.startPage(new PdfDocument.PageInfo.Builder(margin, pageHeight, pageNum2).create());
                canvas = page2.getCanvas();
                currentY4 = 50;
                pageNum = pageNum2;
            }
            Invoice inv2 = invoices.get(i);
            int pageHeight2 = pageHeight;
            PdfDocument.Page page3 = page2;
            int withSignature = withPOD2;
            canvas.drawText("#" + (i + 1), margin2, currentY4, numberPaint);
            String invoiceNum = inv2.getInvoiceNumber() != null ? inv2.getInvoiceNumber() : "N/A";
            canvas.drawText(invoiceNum + " - " + (inv2.getCustomerName() != null ? inv2.getCustomerName() : "Unknown"), margin2 + 40, currentY4, headerPaint);
            int currentY5 = currentY4 + 16;
            if (inv2.getAddress() != null) {
                canvas.drawText(inv2.getAddress(), margin2 + 40, currentY5, textPaint);
                currentY5 += 14;
            }
            String details = (inv2.getPhone() != null ? inv2.getPhone() : "No phone") + " • Items: " + getItemsDisplay(inv2);
            canvas.drawText(details, margin2 + 40, currentY5, textPaint);
            int currentY6 = currentY5 + 20;
            int pageWidth = margin;
            canvas.drawLine(margin2, currentY6, margin - margin2, currentY6, linePaint);
            currentY4 = currentY6 + 15;
            i++;
            margin2 = margin2;
            canvas2 = canvas;
            subtitlePaint2 = subtitlePaint;
            pageHeight = pageHeight2;
            page2 = page3;
            withPOD2 = withSignature;
            margin = pageWidth;
            withPOD = withPOD;
        }
        document.finishPage(page2);
        FileOutputStream fos = new FileOutputStream(outputFile);
        document.writeTo(fos);
        document.close();
        fos.close();
    }

    private Bitmap loadAndScaleImage(String imagePath, int maxWidth, int maxHeight) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            int sampleSize = 1;
            if (options.outWidth > maxWidth || options.outHeight > maxHeight) {
                int widthRatio = Math.round(options.outWidth / maxWidth);
                int heightRatio = Math.round(options.outHeight / maxHeight);
                sampleSize = Math.max(widthRatio, heightRatio);
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            if (bitmap == null) {
                return null;
            }
            float scale = Math.min(maxWidth / bitmap.getWidth(), maxHeight / bitmap.getHeight());
            if (scale < 1.0f) {
                int newWidth = Math.round(bitmap.getWidth() * scale);
                int newHeight = Math.round(bitmap.getHeight() * scale);
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                bitmap.recycle();
                return scaled;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void drawWrappedText(Canvas canvas, String text, float x, float y, int maxWidth, Paint paint) {
        if (text != null && !text.isEmpty()) {
            String[] words = text.split(StringUtils.SPACE);
            StringBuilder line = new StringBuilder();
            float lineY = y;
            for (String word : words) {
                String testLine = line.length() > 0 ? line + StringUtils.SPACE + word : word;
                float testWidth = paint.measureText(testLine);
                if (testWidth > maxWidth && line.length() > 0) {
                    canvas.drawText(line.toString(), x, lineY, paint);
                    line = new StringBuilder(word);
                    lineY += 16.0f;
                } else {
                    line = new StringBuilder(testLine);
                }
            }
            if (line.length() > 0) {
                canvas.drawText(line.toString(), x, lineY, paint);
            }
        }
    }

    private String generateHTMLDeliveryCard(Invoice invoice) {
        String base64;
        String base642;
        StringBuilder html = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        html.append("<title>Delivery Card - ").append(escapeHTML(invoice.getCustomerName())).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; max-width: 800px; margin: 20px auto; padding: 20px; background: #f5f5f5; }\n");
        html.append(".card { background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append("h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }\n");
        html.append("h2 { color: #34495e; margin-top: 30px; }\n");
        html.append(".info-grid { display: grid; grid-template-columns: 150px 1fr; gap: 15px; margin: 20px 0; }\n");
        html.append(".label { font-weight: bold; color: #7f8c8d; }\n");
        html.append(".value { color: #2c3e50; }\n");
        html.append(".image-section { margin: 30px 0; }\n");
        html.append(".image-section img { max-width: 100%; border-radius: 4px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin: 10px 0; }\n");
        html.append(".footer { margin-top: 40px; padding-top: 20px; border-top: 2px solid #ecf0f1; text-align: center; color: #95a5a6; font-size: 12px; }\n");
        html.append("@media print { body { background: white; } .card { box-shadow: none; } }\n");
        html.append("</style>\n</head>\n<body>\n");
        html.append("<div class='card'>\n");
        html.append("<h1>").append(ICON_TRUCK).append("Delivery Card</h1>\n");
        html.append("<div class='info-grid'>\n");
        html.append("<div class='label'>Invoice #:</div><div class='value'>").append(escapeHTML(invoice.getInvoiceNumber())).append("</div>\n");
        html.append("<div class='label'>Customer:</div><div class='value'>").append(escapeHTML(invoice.getCustomerName())).append("</div>\n");
        String mapLink = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(invoice.getAddress());
        html.append("<div class='label'>Address:</div><div class='value'>").append("<a href='").append(mapLink).append("' target='_blank'>").append(escapeHTML(invoice.getAddress())).append("</a>").append(" <span style='font-size: 0.9em;'>[<a href='").append(mapLink).append("' target='_blank'>Click here for live directions</a>]</span>").append("</div>\n");
        html.append("<div class='label'>Phone:</div><div class='value'>").append(escapeHTML(invoice.getPhone())).append("</div>\n");
        html.append("<div class='label'>Service Type:</div><div class='value'>").append(escapeHTML(invoice.getServiceType())).append("</div>\n");
        html.append("<div class='label'>Date/Time:</div><div class='value'>").append(dateFormat.format(new Date(invoice.getTimestamp()))).append("</div>\n");
        html.append("<div class='label'>Items:</div><div class='value'>").append(escapeHTML(getItemsDisplay(invoice))).append("</div>\n");
        if (invoice.getNotes() != null && !invoice.getNotes().isEmpty()) {
            html.append("<div class='label'>Notes:</div><div class='value'>").append(escapeHTML(invoice.getNotes())).append("</div>\n");
        }
        html.append("</div>\n");
        if (invoice.getOriginalImagePath() != null && !invoice.getOriginalImagePath().isEmpty() && (base642 = imageToBase64(invoice.getOriginalImagePath())) != null) {
            html.append("<h2>").append(ICON_DOCUMENT).append("Original Invoice</h2>\n");
            html.append("<div class='image-section'>\n");
            html.append("<img src='data:image/jpeg;base64,").append(base642).append("' alt='Original Invoice'>\n");
            html.append("</div>\n");
        }
        boolean hasPOD = false;
        String[] podPaths = {invoice.getPodImagePath1(), invoice.getPodImagePath2(), invoice.getPodImagePath3(), invoice.getPodImagePath4(), invoice.getPodImagePath5(), invoice.getPodImagePath6()};
        for (int i = 0; i < podPaths.length; i++) {
            if (podPaths[i] != null && !podPaths[i].isEmpty()) {
                if (!hasPOD) {
                    html.append("<h2>").append(ICON_CAMERA).append("Proof of Delivery Photos</h2>\n");
                    html.append("<div class='image-section'>\n");
                    hasPOD = true;
                }
                String base643 = imageToBase64(podPaths[i]);
                if (base643 != null) {
                    html.append("<h3>Photo ").append(i + 1).append("</h3>\n");
                    html.append("<img src='data:image/jpeg;base64,").append(base643).append("' alt='POD Photo ").append(i + 1).append("'>\n");
                }
            }
        }
        if (hasPOD) {
            html.append("</div>\n");
        }
        if (invoice.getSignatureImagePath() != null && !invoice.getSignatureImagePath().isEmpty() && (base64 = imageToBase64(invoice.getSignatureImagePath())) != null) {
            html.append("<h2>").append(ICON_SIGNATURE).append("Delivery Acceptance Form</h2>\n");
            html.append("<div class='image-section'>\n");
            html.append("<img src='data:image/jpeg;base64,").append(base64).append("' alt='Delivery Acceptance Form'>\n");
            html.append("</div>\n");
        }
        html.append("<div class='footer'>\n");
        html.append("<p>Generated by Mobile Invoice OCR App<br>");
        html.append("Export Date: ").append(new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US).format(new Date())).append("</p>\n");
        html.append("</div>\n");
        html.append("</div>\n</body>\n</html>");
        return html.toString();
    }

    private String generateIndexHTML(List<Invoice> invoices) {
        StringBuilder html = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        html.append("<title>Delivery Docket - ").append(dateFormat.format(new Date())).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; max-width: 1000px; margin: 20px auto; padding: 20px; background: #f5f5f5; }\n");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; border-radius: 8px; margin-bottom: 30px; }\n");
        html.append("h1 { margin: 0; }\n");
        html.append(".summary { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }\n");
        html.append(".delivery-list { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append(".delivery-item { padding: 20px; border-bottom: 1px solid #ecf0f1; }\n");
        html.append(".delivery-item:hover { background: #f8f9fa; }\n");
        html.append(".delivery-item:last-child { border-bottom: none; }\n");
        html.append(".delivery-number { font-size: 24px; font-weight: bold; color: #3498db; }\n");
        html.append(".customer-name { font-size: 18px; margin: 10px 0; }\n");
        html.append(".details { color: #7f8c8d; font-size: 14px; }\n");
        html.append("a { color: #3498db; text-decoration: none; font-weight: bold; }\n");
        html.append("a:hover { text-decoration: underline; }\n");
        html.append(".stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; }\n");
        html.append(".stat-box { text-align: center; padding: 15px; background: #ecf0f1; border-radius: 4px; }\n");
        html.append(".stat-number { font-size: 32px; font-weight: bold; color: #2c3e50; }\n");
        html.append(".stat-label { color: #7f8c8d; font-size: 14px; }\n");
        html.append("</style>\n</head>\n<body>\n");
        html.append("<div class='header'>\n");
        html.append("<h1>").append(ICON_PACKAGE).append("Delivery Docket</h1>\n");
        html.append("<p>Date: ").append(dateFormat.format(new Date())).append("</p>\n");
        html.append("</div>\n");
        int totalInvoices = invoices.size();
        int withPOD = 0;
        int withSignature = 0;
        for (Invoice inv : invoices) {
            if ((inv.getPodImagePath1() != null && !inv.getPodImagePath1().isEmpty()) || ((inv.getPodImagePath2() != null && !inv.getPodImagePath2().isEmpty()) || ((inv.getPodImagePath3() != null && !inv.getPodImagePath3().isEmpty()) || ((inv.getPodImagePath4() != null && !inv.getPodImagePath4().isEmpty()) || ((inv.getPodImagePath5() != null && !inv.getPodImagePath5().isEmpty()) || (inv.getPodImagePath6() != null && !inv.getPodImagePath6().isEmpty())))))) {
                withPOD++;
            }
            if (inv.getSignatureImagePath() != null && !inv.getSignatureImagePath().isEmpty()) {
                withSignature++;
            }
        }
        html.append("<div class='summary'>\n");
        html.append("<h2>Summary</h2>\n");
        html.append("<div class='stats'>\n");
        html.append("<div class='stat-box'><div class='stat-number'>").append(totalInvoices).append("</div><div class='stat-label'>Total Deliveries</div></div>\n");
        html.append("<div class='stat-box'><div class='stat-number'>").append(withPOD).append("</div><div class='stat-label'>With POD Photos</div></div>\n");
        html.append("<div class='stat-box'><div class='stat-number'>").append(withSignature).append("</div><div class='stat-label'>With Signatures</div></div>\n");
        html.append("</div>\n</div>\n");
        html.append("<div class='delivery-list'>\n");
        for (int i = 0; i < invoices.size(); i++) {
            Invoice inv2 = invoices.get(i);
            String filename = sanitizeFilename(inv2.getCustomerName() + "_" + inv2.getInvoiceNumber()) + ".html";
            html.append("<div class='delivery-item'>\n");
            html.append("<div class='delivery-number'>#").append(i + 1).append(" - ").append(escapeHTML(inv2.getInvoiceNumber())).append("</div>\n");
            html.append("<div class='customer-name'>").append(escapeHTML(inv2.getCustomerName())).append("</div>\n");
            html.append("<div class='details'>").append(escapeHTML(inv2.getAddress())).append("</div>\n");
            html.append("<div class='details'>").append(escapeHTML(inv2.getPhone())).append(" • Items: ").append(escapeHTML(getItemsDisplay(inv2))).append("</div>\n");
            html.append("<div style='margin-top: 10px;'><a href='").append(filename).append("'>View Delivery Card →</a></div>\n");
            html.append("</div>\n");
        }
        html.append("</div>\n");
        html.append("</body>\n</html>");
        return html.toString();
    }

    private String imageToBase64(String imagePath) {
        Bitmap bitmap;
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists() || (bitmap = BitmapFactory.decodeFile(imagePath)) == null) {
                return null;
            }
            if (bitmap.getWidth() > 2400 || bitmap.getHeight() > 2400) {
                float scale = Math.min(2400 / bitmap.getWidth(), 2400 / bitmap.getHeight());
                int newWidth = Math.round(bitmap.getWidth() * scale);
                int newHeight = Math.round(bitmap.getHeight() * scale);
                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, baos);
            byte[] imageBytes = baos.toByteArray();
            bitmap.recycle();
            return Base64.encodeToString(imageBytes, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void zipFolder(File sourceFolder, File zipFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zipFolderRecursive(sourceFolder, sourceFolder.getName(), zos);
        zos.close();
    }

    private void zipFolderRecursive(File folder, String parentPath, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                zipFolderRecursive(file, parentPath + PackagingURIHelper.FORWARD_SLASH_STRING + file.getName(), zos);
            } else {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(parentPath + PackagingURIHelper.FORWARD_SLASH_STRING + file.getName());
                zos.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = fis.read(buffer);
                    if (length <= 0) {
                        break;
                    } else {
                        zos.write(buffer, 0, length);
                    }
                }
                zos.closeEntry();
                fis.close();
            }
        }
    }

    private void showShareDialog(final File zipFile, int invoiceCount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Export Complete");
        builder.setMessage("Successfully exported " + invoiceCount + " delivery cards.\n\nFile: " + zipFile.getName() + "\nSize: " + (zipFile.length() / FileUtils.ONE_KB) + " KB\n\nHow would you like to share this?");
        builder.setPositiveButton("Share Now", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.ExportHelper$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ExportHelper.this.lambda$showShareDialog$1(zipFile, dialogInterface, i);
            }
        });
        builder.setNeutralButton("View Location", new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.ExportHelper$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ExportHelper.this.lambda$showShareDialog$2(zipFile, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Done", null);
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showShareDialog$1(File zipFile, DialogInterface dialog, int which) {
        shareZipFile(zipFile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showShareDialog$2(File zipFile, DialogInterface dialog, int which) {
        Toast.makeText(this.context, "Saved to:\n" + zipFile.getAbsolutePath(), 1).show();
    }

    private void shareZipFile(File zipFile) {
        try {
            Uri zipUri = FileProvider.getUriForFile(this.context, this.context.getPackageName() + ".fileprovider", zipFile);
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType("application/zip");
            shareIntent.putExtra("android.intent.extra.STREAM", zipUri);
            shareIntent.putExtra("android.intent.extra.SUBJECT", "Delivery Docket - " + new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date()));
            shareIntent.putExtra("android.intent.extra.TEXT", "Attached delivery docket with " + zipFile.getName());
            shareIntent.addFlags(1);
            this.context.startActivity(Intent.createChooser(shareIntent, "Share Delivery Docket"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error sharing file: " + e.getMessage(), 0).show();
        }
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) {
            return "Unknown";
        }
        String safe = filename.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        if (safe.length() > 50) {
            return safe.substring(0, 50);
        }
        return safe;
    }

    private String escapeHTML(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    private String escapeMD(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "\\|").replace("*", "\\*").replace("_", "\\_");
    }

    public void exportToCardFolders(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            Toast.makeText(this.context, "No invoices to export", 0).show();
            return;
        }
        String dateFolder = new SimpleDateFormat("'Deliveries ('MM/dd/yyyy')'", Locale.US).format(new Date());
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File mainExportDir = new File(downloadsDir, AppSettings.getInstance(this.context).getExportFolderName() + PackagingURIHelper.FORWARD_SLASH_STRING + dateFolder);
        if (!mainExportDir.exists()) {
            mainExportDir.mkdirs();
        }
        int successCount = 0;
        for (Invoice invoice : invoices) {
            try {
                String cardFolderName = createCardFolderName(invoice);
                File cardDir = new File(mainExportDir, cardFolderName);
                cardDir.mkdirs();
                saveInvoiceDataToCard(cardDir, invoice);
                copyImagesToCard(cardDir, invoice);
                successCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (successCount <= 0) {
            Toast.makeText(this.context, "Failed to export any delivery cards", 0).show();
            return;
        }
        createExportSummary(mainExportDir, invoices, successCount);
        Toast.makeText(this.context, "Exported " + successCount + " delivery cards to:\nDownloads/" + AppSettings.getInstance(this.context).getExportFolderName() + PackagingURIHelper.FORWARD_SLASH_STRING + mainExportDir.getName() + "\n\nOpen Files app to share", 1).show();
        if (this.callback != null) {
            this.callback.onExportComplete(mainExportDir, successCount);
        }
    }

    public void exportToExcel(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            Toast.makeText(this.context, "No invoices to export", 0).show();
            return;
        }
        StringBuilder tsv = new StringBuilder();
        tsv.append("Invoice #\tCustomer Name\tAddress\tPhone\tService Type\tItems\tPOD Image\tSignature\tNotes\tTimestamp\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        for (Invoice invoice : invoices) {
            tsv.append(escapeTSV(invoice.getInvoiceNumber())).append("\t");
            tsv.append(escapeTSV(invoice.getCustomerName())).append("\t");
            tsv.append(escapeTSV(invoice.getAddress())).append("\t");
            tsv.append(escapeTSV(invoice.getPhone())).append("\t");
            tsv.append(escapeTSV(invoice.getServiceType())).append("\t");
            tsv.append(escapeTSV(getItemsDisplay(invoice))).append("\t");
            String str = "No";
            tsv.append(escapeTSV(((invoice.getPodImagePath1() == null || invoice.getPodImagePath1().isEmpty()) && (invoice.getPodImagePath2() == null || invoice.getPodImagePath2().isEmpty()) && ((invoice.getPodImagePath3() == null || invoice.getPodImagePath3().isEmpty()) && ((invoice.getPodImagePath4() == null || invoice.getPodImagePath4().isEmpty()) && ((invoice.getPodImagePath5() == null || invoice.getPodImagePath5().isEmpty()) && (invoice.getPodImagePath6() == null || invoice.getPodImagePath6().isEmpty()))))) ? "No" : "Yes")).append("\t");
            if (invoice.getSignatureImagePath() != null) {
                str = "Yes";
            }
            tsv.append(escapeTSV(str)).append("\t");
            tsv.append(escapeTSV(invoice.getNotes())).append("\t");
            tsv.append(escapeTSV(dateFormat.format(new Date(invoice.getTimestamp())))).append(StringUtils.LF);
        }
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File exportDir = new File(downloadsDir, AppSettings.getInstance(this.context).getExportFolderName());
        String filename = "invoices_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".xls";
        File file = saveToFile(exportDir, filename, tsv.toString());
        if (file != null) {
            Toast.makeText(this.context, "Exported " + invoices.size() + " invoices to:\nDownloads/" + AppSettings.getInstance(this.context).getExportFolderName() + PackagingURIHelper.FORWARD_SLASH_STRING + filename + "\n\nOpen Files app to share", 1).show();
        }
    }

    public void exportToJSON(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            Toast.makeText(this.context, "No invoices to export", 0).show();
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("export_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));
            jSONObject.put("total_invoices", invoices.size());
            JSONArray jSONArray = new JSONArray();
            for (Invoice invoice : invoices) {
                JSONObject invoiceObj = new JSONObject();
                invoiceObj.put("id", invoice.getId());
                invoiceObj.put("invoice_number", invoice.getInvoiceNumber());
                invoiceObj.put("customer_name", invoice.getCustomerName());
                invoiceObj.put("address", invoice.getAddress());
                invoiceObj.put("phone", invoice.getPhone());
                invoiceObj.put("service_type", invoice.getServiceType());
                invoiceObj.put("items", invoice.getItems());
                JSONArray podImages = new JSONArray();
                if (invoice.getPodImagePath1() != null) {
                    podImages.put(invoice.getPodImagePath1());
                }
                if (invoice.getPodImagePath2() != null) {
                    podImages.put(invoice.getPodImagePath2());
                }
                if (invoice.getPodImagePath3() != null) {
                    podImages.put(invoice.getPodImagePath3());
                }
                if (invoice.getPodImagePath4() != null) {
                    podImages.put(invoice.getPodImagePath4());
                }
                if (invoice.getPodImagePath5() != null) {
                    podImages.put(invoice.getPodImagePath5());
                }
                if (invoice.getPodImagePath6() != null) {
                    podImages.put(invoice.getPodImagePath6());
                }
                invoiceObj.put("pod_image_paths", podImages);
                invoiceObj.put("signature_image_path", invoice.getSignatureImagePath());
                invoiceObj.put("notes", invoice.getNotes());
                invoiceObj.put("original_image_path", invoice.getOriginalImagePath());
                invoiceObj.put("raw_ocr_text", invoice.getRawOcrText());
                invoiceObj.put("timestamp", invoice.getTimestamp());
                jSONArray.put(invoiceObj);
            }
            jSONObject.put("invoices", jSONArray);
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File exportDir = new File(downloadsDir, AppSettings.getInstance(this.context).getExportFolderName());
            String filename = "invoices_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".json";
            File file = saveToFile(exportDir, filename, jSONObject.toString(4));
            if (file != null) {
                Toast.makeText(this.context, "Exported " + invoices.size() + " invoices to:\nDownloads/" + AppSettings.getInstance(this.context).getExportFolderName() + PackagingURIHelper.FORWARD_SLASH_STRING + filename + "\n\nOpen Files app to share", 1).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error creating JSON export", 0).show();
        }
    }

    public String getExportSummary(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            return "No invoices available for export";
        }
        int totalInvoices = invoices.size();
        int withPOD = 0;
        int withSignature = 0;
        int withItems = 0;
        for (Invoice invoice : invoices) {
            if ((invoice.getPodImagePath1() != null && !invoice.getPodImagePath1().isEmpty()) || ((invoice.getPodImagePath2() != null && !invoice.getPodImagePath2().isEmpty()) || ((invoice.getPodImagePath3() != null && !invoice.getPodImagePath3().isEmpty()) || ((invoice.getPodImagePath4() != null && !invoice.getPodImagePath4().isEmpty()) || ((invoice.getPodImagePath5() != null && !invoice.getPodImagePath5().isEmpty()) || (invoice.getPodImagePath6() != null && !invoice.getPodImagePath6().isEmpty())))))) {
                withPOD++;
            }
            if (invoice.getSignatureImagePath() != null && !invoice.getSignatureImagePath().isEmpty()) {
                withSignature++;
            }
            if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
                withItems++;
            }
        }
        return String.format(Locale.US, "Export Summary:\nTotal Invoices: %d\nWith POD Photos: %d (%.1f%%)\nWith Signatures: %d (%.1f%%)\nWith Items: %d (%.1f%%)", Integer.valueOf(totalInvoices), Integer.valueOf(withPOD), Float.valueOf((withPOD * 100.0f) / totalInvoices), Integer.valueOf(withSignature), Float.valueOf((withSignature * 100.0f) / totalInvoices), Integer.valueOf(withItems), Float.valueOf((withItems * 100.0f) / totalInvoices));
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains(StringUtils.LF)) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String escapeTSV(String value) {
        return value == null ? "" : value.replace("\t", StringUtils.SPACE).replace(StringUtils.LF, StringUtils.SPACE).replace(StringUtils.CR, "");
    }

    private File saveToFile(File directory, String filename, String content) {
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error saving export file: " + e.getMessage(), 0).show();
            return null;
        }
    }

    private void shareFile(File file, String mimeType, String title) {
        try {
            Uri fileUri = FileProvider.getUriForFile(this.context, this.context.getPackageName() + ".fileprovider", file);
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType(mimeType);
            shareIntent.putExtra("android.intent.extra.STREAM", fileUri);
            shareIntent.putExtra("android.intent.extra.SUBJECT", "Invoice Export");
            shareIntent.addFlags(1);
            this.context.startActivity(Intent.createChooser(shareIntent, title));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error sharing file: " + e.getMessage(), 0).show();
        }
    }

    public void shareViaEmail(File file, String mimeType, String subject, String body) {
        try {
            Uri fileUri = FileProvider.getUriForFile(this.context, this.context.getPackageName() + ".fileprovider", file);
            Intent emailIntent = new Intent("android.intent.action.SEND");
            emailIntent.setType(mimeType);
            emailIntent.putExtra("android.intent.extra.STREAM", fileUri);
            emailIntent.putExtra("android.intent.extra.SUBJECT", subject != null ? subject : "Invoice Export - " + file.getName());
            emailIntent.putExtra("android.intent.extra.TEXT", body != null ? body : "Please find attached invoice export.");
            emailIntent.addFlags(1);
            Intent chooser = Intent.createChooser(emailIntent, "Send via Email");
            if (emailIntent.resolveActivity(this.context.getPackageManager()) == null) {
                Toast.makeText(this.context, "No email app found", 0).show();
            } else {
                this.context.startActivity(chooser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error sending via email: " + e.getMessage(), 0).show();
        }
    }

    public void shareViaQuickShare(File file, String mimeType) {
        try {
            Uri fileUri = FileProvider.getUriForFile(this.context, this.context.getPackageName() + ".fileprovider", file);
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType(mimeType);
            shareIntent.putExtra("android.intent.extra.STREAM", fileUri);
            shareIntent.putExtra("android.intent.extra.SUBJECT", "Invoice Export");
            shareIntent.putExtra("android.intent.extra.TEXT", "Sharing invoice export file: " + file.getName());
            shareIntent.addFlags(1);
            this.context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error sharing file: " + e.getMessage(), 0).show();
        }
    }

    public void shareViaSMS(File file, String mimeType) {
        try {
            Uri fileUri = FileProvider.getUriForFile(this.context, this.context.getPackageName() + ".fileprovider", file);
            Intent smsIntent = new Intent("android.intent.action.SEND");
            smsIntent.setType(mimeType);
            smsIntent.putExtra("android.intent.extra.STREAM", fileUri);
            smsIntent.putExtra("android.intent.extra.TEXT", "Invoice export attached: " + file.getName());
            smsIntent.addFlags(1);
            Intent chooser = Intent.createChooser(smsIntent, "Send via SMS/MMS");
            if (smsIntent.resolveActivity(this.context.getPackageManager()) == null) {
                Toast.makeText(this.context, "No SMS app found", 0).show();
            } else {
                this.context.startActivity(chooser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error sending via SMS: " + e.getMessage(), 0).show();
        }
    }

    public void showExportOptionsDialog(final File exportedFile, final String mimeType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Share: " + exportedFile.getName());
        String[] options = {"Email", "QuickShare/Nearby", "SMS/MMS", "More Options"};
        builder.setItems(options, new DialogInterface.OnClickListener() { // from class: com.mobileinvoice.ocr.ExportHelper$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ExportHelper.this.lambda$showExportOptionsDialog$3(exportedFile, mimeType, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showExportOptionsDialog$3(File exportedFile, String mimeType, DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                shareViaEmail(exportedFile, mimeType, "Invoice Export - " + new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date()), "Please find attached the invoice export file.");
                break;
            case 1:
                shareViaQuickShare(exportedFile, mimeType);
                break;
            case 2:
                shareViaSMS(exportedFile, mimeType);
                break;
            case 3:
                shareFile(exportedFile, mimeType, "Share Export");
                break;
        }
    }

    private String createCardFolderName(Invoice invoice) {
        String customerName;
        String invoiceNum;
        if (invoice.getCustomerName() != null) {
            customerName = invoice.getCustomerName().replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        } else {
            customerName = "Unknown";
        }
        if (invoice.getInvoiceNumber() != null) {
            invoiceNum = invoice.getInvoiceNumber().replaceAll("[^a-zA-Z0-9]", "");
        } else {
            invoiceNum = "INV" + invoice.getId();
        }
        if (customerName.length() > 20) {
            customerName = customerName.substring(0, 20);
        }
        return customerName + "_" + invoiceNum;
    }

    private void saveInvoiceDataToCard(File cardDir, Invoice invoice) throws Exception {
        JSONObject invoiceObj = new JSONObject();
        invoiceObj.put("id", invoice.getId());
        invoiceObj.put("invoice_number", invoice.getInvoiceNumber());
        invoiceObj.put("customer_name", invoice.getCustomerName());
        invoiceObj.put("address", invoice.getAddress());
        invoiceObj.put("phone", invoice.getPhone());
        invoiceObj.put("service_type", invoice.getServiceType());
        invoiceObj.put("items", invoice.getItems());
        invoiceObj.put("notes", invoice.getNotes());
        invoiceObj.put("timestamp", invoice.getTimestamp());
        invoiceObj.put("export_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));
        if (invoice.getPodImagePath1() != null) {
            invoiceObj.put("pod_image_1", "pod_photo_1.jpg");
        }
        if (invoice.getPodImagePath2() != null) {
            invoiceObj.put("pod_image_2", "pod_photo_2.jpg");
        }
        if (invoice.getPodImagePath3() != null) {
            invoiceObj.put("pod_image_3", "pod_photo_3.jpg");
        }
        if (invoice.getPodImagePath4() != null) {
            invoiceObj.put("pod_image_4", "pod_photo_4.jpg");
        }
        if (invoice.getPodImagePath5() != null) {
            invoiceObj.put("pod_image_5", "pod_photo_5.jpg");
        }
        if (invoice.getPodImagePath6() != null) {
            invoiceObj.put("pod_image_6", "pod_photo_6.jpg");
        }
        if (invoice.getSignatureImagePath() != null) {
            invoiceObj.put("signature_image", "signature.jpg");
        }
        if (invoice.getOriginalImagePath() != null) {
            invoiceObj.put("original_invoice_image", "original_invoice.jpg");
        }
        File dataFile = new File(cardDir, "delivery_info.json");
        FileOutputStream fos = new FileOutputStream(dataFile);
        fos.write(invoiceObj.toString(4).getBytes());
        fos.close();
    }

    private void copyImagesToCard(File cardDir, Invoice invoice) {
        try {
            String[] podPaths = {invoice.getPodImagePath1(), invoice.getPodImagePath2(), invoice.getPodImagePath3(), invoice.getPodImagePath4(), invoice.getPodImagePath5(), invoice.getPodImagePath6()};
            for (int i = 0; i < podPaths.length; i++) {
                if (podPaths[i] != null) {
                    copyImageFile(new File(podPaths[i]), new File(cardDir, "pod_photo_" + (i + 1) + ".jpg"));
                }
            }
            if (invoice.getSignatureImagePath() != null) {
                copyImageFile(new File(invoice.getSignatureImagePath()), new File(cardDir, "signature.jpg"));
            }
            if (invoice.getOriginalImagePath() != null) {
                copyImageFile(new File(invoice.getOriginalImagePath()), new File(cardDir, "original_invoice.jpg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyImageFile(File source, File destination) throws IOException {
        if (!source.exists()) {
            return;
        }
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = fis.read(buffer);
            if (length > 0) {
                fos.write(buffer, 0, length);
            } else {
                fis.close();
                fos.close();
                return;
            }
        }
    }

    private File createExportSummary(File mainExportDir, List<Invoice> invoices, int successCount) {
        try {
            JSONObject summary = new JSONObject();
            summary.put("export_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));
            summary.put("total_invoices", invoices.size());
            summary.put("successful_exports", successCount);
            summary.put("export_format", "folder_cards");
            JSONArray cardFolders = new JSONArray();
            File[] cardDirs = mainExportDir.listFiles(new FileFilter() { // from class: com.mobileinvoice.ocr.ExportHelper$$ExternalSyntheticLambda2
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    boolean isDirectory;
                    isDirectory = file.isDirectory();
                    return isDirectory;
                }
            });
            if (cardDirs != null) {
                for (File cardDir : cardDirs) {
                    cardFolders.put(cardDir.getName());
                }
            }
            summary.put("card_folders", cardFolders);
            File summaryFile = new File(mainExportDir, "export_summary.json");
            FileOutputStream fos = new FileOutputStream(summaryFile);
            fos.write(summary.toString(4).getBytes());
            fos.close();
            return summaryFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getItemsDisplay(Invoice invoice) {
        return ItemsHelper.toDisplayString(ItemsHelper.fromJson(invoice.getItems()));
    }

    private void shareFolder(File folder, String title) {
        try {
            File summaryFile = new File(folder, "export_summary.json");
            if (summaryFile.exists()) {
                shareFile(summaryFile, "application/json", title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
