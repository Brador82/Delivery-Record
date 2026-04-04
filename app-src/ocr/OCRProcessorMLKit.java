package com.mobileinvoice.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.google.android.gms.location.DeviceOrientationRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes7.dex */
public class OCRProcessorMLKit {
    private static final String TAG = "OCRProcessorMLKit";
    private final Context context;
    private final TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\(?\\d{3}\\)?[-\\s.]?\\d{3}[-\\s.]?\\d{4}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final Pattern LABELED_INVOICE_PATTERN = Pattern.compile("(?i)(?:invoice|order|inv|ref)\\s*[#:.-]?\\s*(\\d{4,8})", 2);
    private static final Pattern INVOICE_CODE_PATTERN = Pattern.compile("\\b([A-Z]{2}\\d[A-Z]\\d{4}[A-Z]?)\\b");
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("\\b\\d{5}(?:-\\d{4})?\\b");
    private static final Pattern ID_PATTERN = Pattern.compile("\\(?ID:?\\s*[^)]+\\)|/\\s*Salesperson:?\\s*\\w+|\\([^)]*Salesperson[^)]*\\)", 2);
    private static final String[] APPLIANCE_TYPES = {"Washer", "Dryer", "Refrigerator", "Dishwasher", "Freezer", "Range", "Washtower", "Microwave", "Other"};
    private static final Pattern MODEL_PATTERN = Pattern.compile("(?i)(?:model|mdl|mod)\\s*(?:no\\.?|num\\.?|#|:)?\\s*[:#]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})", 2);
    private static final Pattern SERIAL_PATTERN = Pattern.compile("(?i)(?:s/n|serial|ser\\.?|sn)\\s*[:#]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})", 2);

    public static class OCRResult {
        public String customerName = "";
        public String address = "";
        public String phone = "";
        public String invoiceNumber = "";
        public String items = "";
        public String rawText = "";
    }

    public OCRProcessorMLKit(Context context) {
        this.context = context;
    }

    public OCRResult processImage(Uri imageUri) {
        OCRResult result = new OCRResult();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), imageUri);
            if (bitmap == null) {
                result.rawText = "Error: Could not load image";
                return result;
            }
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            Text mlKitText = processImageSync(image);
            if (mlKitText == null) {
                result.rawText = "Error: ML Kit recognition failed";
                return result;
            }
            OCRResult result2 = extractInvoiceData(mlKitText);
            Log.d(TAG, "========= EXTRACTION RESULTS =========");
            Log.d(TAG, "Customer: " + result2.customerName);
            Log.d(TAG, "Address: " + result2.address);
            Log.d(TAG, "Phone: " + result2.phone);
            Log.d(TAG, "Invoice #: " + result2.invoiceNumber);
            Log.d(TAG, "Items: " + result2.items);
            Log.d(TAG, "=====================================");
            return result2;
        } catch (IOException e) {
            Log.e(TAG, "Error loading image", e);
            result.rawText = "Error: " + e.getMessage();
            return result;
        }
    }

    private Text processImageSync(InputImage image) {
        final Object lock = new Object();
        final boolean[] done = {false};
        final Text[] result = {null};
        this.recognizer.process(image).addOnSuccessListener(new OnSuccessListener() { // from class: com.mobileinvoice.ocr.OCRProcessorMLKit$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                OCRProcessorMLKit.lambda$processImageSync$0(lock, result, done, (Text) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.mobileinvoice.ocr.OCRProcessorMLKit$$ExternalSyntheticLambda1
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                OCRProcessorMLKit.lambda$processImageSync$1(lock, done, exc);
            }
        });
        synchronized (lock) {
            while (!done[0]) {
                try {
                    lock.wait(DeviceOrientationRequest.OUTPUT_PERIOD_MEDIUM);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while waiting for ML Kit", e);
                }
            }
        }
        return result[0];
    }

    static /* synthetic */ void lambda$processImageSync$0(Object lock, Text[] result, boolean[] done, Text text) {
        synchronized (lock) {
            result[0] = text;
            done[0] = true;
            lock.notify();
        }
    }

    static /* synthetic */ void lambda$processImageSync$1(Object lock, boolean[] done, Exception e) {
        synchronized (lock) {
            Log.e(TAG, "ML Kit recognition failed", e);
            done[0] = true;
            lock.notify();
        }
    }

    private OCRResult extractInvoiceData(Text text) {
        OCRResult result = new OCRResult();
        List<String> allLines = new ArrayList<>();
        StringBuilder rawText = new StringBuilder();
        for (Text.TextBlock block : text.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText().trim();
                if (!lineText.isEmpty()) {
                    allLines.add(lineText);
                    rawText.append(lineText).append(StringUtils.LF);
                }
            }
        }
        result.rawText = rawText.toString();
        result.invoiceNumber = extractInvoiceNumber(allLines);
        int billToIndex = findLineContaining(allLines, "BILL TO");
        if (billToIndex == -1) {
            Log.w(TAG, "BILL TO not found, using fallback extraction");
            extractWithFallback(allLines, result);
        } else {
            extractFromBillToSection(allLines, billToIndex, result);
        }
        result.items = extractItems(allLines);
        if (result.customerName.isEmpty()) {
            result.customerName = "Unknown Customer";
        }
        if (result.address.isEmpty()) {
            result.address = "No address found";
        }
        if (result.phone.isEmpty()) {
            result.phone = "No phone";
        }
        if (result.invoiceNumber.isEmpty()) {
            result.invoiceNumber = "INV-" + System.currentTimeMillis();
        }
        return result;
    }

    private void extractFromBillToSection(List<String> lines, int billToIndex, OCRResult result) {
        for (int i = billToIndex + 1; i < Math.min(billToIndex + 10, lines.size()); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                if (result.customerName.isEmpty() && line.toLowerCase().startsWith("name:")) {
                    result.customerName = extractCustomerName(line);
                } else if (result.address.isEmpty() && line.toLowerCase().startsWith("address:")) {
                    result.address = extractAddress(line);
                } else if (result.phone.isEmpty() && (line.toLowerCase().contains("phone") || PHONE_PATTERN.matcher(line).find())) {
                    result.phone = extractPhone(line);
                }
            }
        }
    }

    private void extractWithFallback(List<String> lines, OCRResult result) {
        for (String line : lines) {
            if (result.customerName.isEmpty() && line.toLowerCase().startsWith("name:")) {
                result.customerName = extractCustomerName(line);
            }
            if (result.address.isEmpty() && (line.toLowerCase().startsWith("address:") || (line.matches(".*\\d+\\s+[A-Z].*") && line.length() > 10))) {
                result.address = extractAddress(line);
            }
            if (result.phone.isEmpty() && PHONE_PATTERN.matcher(line).find()) {
                result.phone = extractPhone(line);
            }
        }
    }

    private String extractCustomerName(String line) {
        String name = line.replaceFirst("(?i)^name:\\s*", "");
        String name2 = splitConcatenatedName(ID_PATTERN.matcher(name).replaceAll("").replaceAll("\\s*/\\s*", StringUtils.SPACE).replaceAll("\\s+", StringUtils.SPACE).trim());
        if (!name2.isEmpty()) {
            return toTitleCase(name2);
        }
        return name2;
    }

    private String splitConcatenatedName(String name) {
        if (name.contains(StringUtils.SPACE) || !name.equals(name.toUpperCase()) || name.length() < 6) {
            return name;
        }
        String[] commonFirstNames = {"KEN", "JON", "JOHN", "DAVID", "MIKE", "ROBERT", "JAMES", "MARY", "JUDY", "LINDA", "PATRICIA", "JENNIFER", "SUSAN"};
        for (String firstName : commonFirstNames) {
            if (name.startsWith(firstName) && name.length() > firstName.length()) {
                String lastName = name.substring(firstName.length());
                if (lastName.length() >= 3) {
                    return firstName + StringUtils.SPACE + lastName;
                }
            }
        }
        if (name.length() >= 8 && name.length() <= 15) {
            int midPoint = name.length() / 2;
            for (int i = midPoint - 1; i <= midPoint + 1; i++) {
                if (i > 2 && i < name.length() - 2) {
                    String first = name.substring(0, i);
                    String last = name.substring(i);
                    if (first.length() >= 3 && last.length() >= 3) {
                        return first + StringUtils.SPACE + last;
                    }
                }
            }
        }
        return name;
    }

    private String extractAddress(String line) {
        String address = line.replaceFirst("(?i)^address:\\s*", "");
        Matcher emailMatcher = EMAIL_PATTERN.matcher(address);
        if (emailMatcher.find()) {
            address = address.substring(0, emailMatcher.start()).trim();
        }
        Matcher phoneMatcher = PHONE_PATTERN.matcher(address);
        if (phoneMatcher.find()) {
            address = address.substring(0, phoneMatcher.start()).trim();
        }
        return address.replaceAll("\\s+", StringUtils.SPACE).trim();
    }

    private String extractPhone(String line) {
        Matcher matcher = PHONE_PATTERN.matcher(line);
        if (!matcher.find()) {
            return "";
        }
        String phone = matcher.group();
        String digits = phone.replaceAll("\\D", "");
        if (digits.length() == 10) {
            return String.format("(%s) %s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6, 10));
        }
        return phone;
    }

    private String extractInvoiceNumber(List<String> lines) {
        List<String> headerLines = new ArrayList<>();
        for (int i = 0; i < Math.min(15, lines.size()); i++) {
            String line = lines.get(i);
            String lower = line.toLowerCase();
            if (!lower.contains("address:") && !lower.contains("bill to") && !lower.contains("missouri") && !lower.contains("springfield") && !lower.contains("street") && !lower.contains("avenue") && !lower.contains("road") && !lower.contains("drive") && !lower.contains("city") && !lower.contains("state")) {
                headerLines.add(line);
            }
        }
        Iterator<String> it = headerLines.iterator();
        while (it.hasNext()) {
            Matcher labeledMatcher = LABELED_INVOICE_PATTERN.matcher(it.next());
            if (labeledMatcher.find()) {
                String invoiceNum = labeledMatcher.group(1);
                if (!ZIP_CODE_PATTERN.matcher(invoiceNum).matches() || invoiceNum.length() > 5) {
                    Log.d(TAG, "Found labeled invoice number: " + invoiceNum);
                    return invoiceNum;
                }
            }
        }
        Iterator<String> it2 = headerLines.iterator();
        while (it2.hasNext()) {
            Matcher codeMatcher = INVOICE_CODE_PATTERN.matcher(it2.next());
            if (codeMatcher.find()) {
                String invoiceCode = codeMatcher.group(1);
                Log.d(TAG, "Found invoice code: " + invoiceCode);
                return invoiceCode;
            }
        }
        Pattern standaloneNumber = Pattern.compile("\\b(\\d{6,10})\\b");
        for (String line2 : headerLines) {
            if (!line2.toLowerCase().contains("phone") && !line2.toLowerCase().contains("email")) {
                Matcher numMatcher = standaloneNumber.matcher(line2);
                if (numMatcher.find()) {
                    String num = numMatcher.group(1);
                    if (num.length() != 10 || (!num.startsWith("417") && !num.startsWith("573") && !num.startsWith("816") && !num.startsWith("314"))) {
                        Log.d(TAG, "Found standalone invoice number: " + num);
                        return num;
                    }
                } else {
                    continue;
                }
            }
        }
        return "";
    }

    private String extractItems(List<String> lines) {
        List<DeliveryItem> foundItems = new ArrayList<>();
        List<Integer> applianceLineIndices = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.toLowerCase().startsWith("type:")) {
                String item = line.replaceFirst("(?i)^type:\\s*", "").trim().split("(?i)\\s+(model|serial|s/n)")[0].trim();
                if (!item.isEmpty() && isValidAppliance(item)) {
                    String normalized = normalizeAppliance(item);
                    boolean exists = false;
                    Iterator<DeliveryItem> it = foundItems.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (it.next().item.equals(normalized)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        foundItems.add(new DeliveryItem(normalized));
                        applianceLineIndices.add(Integer.valueOf(i));
                    }
                }
            }
        }
        if (foundItems.isEmpty()) {
            for (int i2 = 0; i2 < lines.size(); i2++) {
                String line2 = lines.get(i2);
                for (String appliance : APPLIANCE_TYPES) {
                    if (!appliance.equals("Other") && line2.toLowerCase().contains(appliance.toLowerCase())) {
                        boolean exists2 = false;
                        Iterator<DeliveryItem> it2 = foundItems.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            if (it2.next().item.equals(appliance)) {
                                exists2 = true;
                                break;
                            }
                        }
                        if (!exists2) {
                            foundItems.add(new DeliveryItem(appliance));
                            applianceLineIndices.add(Integer.valueOf(i2));
                        }
                    }
                }
            }
        }
        int idx = 0;
        while (idx < foundItems.size()) {
            DeliveryItem di = foundItems.get(idx);
            int applianceLine = applianceLineIndices.size() > idx ? applianceLineIndices.get(idx).intValue() : -1;
            int windowStart = Math.max(0, applianceLine >= 0 ? applianceLine - 2 : 0);
            int windowEnd = Math.min(lines.size(), applianceLine >= 0 ? applianceLine + 8 : lines.size());
            for (int i3 = windowStart; i3 < windowEnd; i3++) {
                String line3 = lines.get(i3);
                if (di.model.isEmpty()) {
                    Matcher m = MODEL_PATTERN.matcher(line3);
                    if (m.find()) {
                        di.model = m.group(1).trim();
                    }
                }
                if (di.serial.isEmpty()) {
                    Matcher m2 = SERIAL_PATTERN.matcher(line3);
                    if (m2.find()) {
                        di.serial = m2.group(1).trim();
                    }
                }
                if (di.model.isEmpty() || di.serial.isEmpty()) {
                }
            }
            idx++;
        }
        return ItemsHelper.toJson(foundItems);
    }

    private String normalizeAppliance(String item) {
        String lower = item.toLowerCase();
        for (String appliance : APPLIANCE_TYPES) {
            if (lower.contains(appliance.toLowerCase())) {
                return appliance;
            }
        }
        return item;
    }

    private boolean isValidAppliance(String item) {
        for (String appliance : APPLIANCE_TYPES) {
            if (item.toLowerCase().contains(appliance.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private int findLineContaining(List<String> lines, String searchText) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).toLowerCase().contains(searchText.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(StringUtils.SPACE);
            }
        }
        return result.toString().trim();
    }

    public void close() {
        if (this.recognizer != null) {
            this.recognizer.close();
        }
    }

    public static List<String> parseItemsList(String itemsString) {
        List<String> items = new ArrayList<>();
        if (itemsString != null && !itemsString.isEmpty()) {
            String[] parts = itemsString.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    items.add(trimmed);
                }
            }
        }
        return items;
    }
}
