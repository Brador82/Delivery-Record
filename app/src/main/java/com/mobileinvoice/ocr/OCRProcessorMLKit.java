package com.mobileinvoice.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
    private static final Pattern LABELED_INVOICE_PATTERN = Pattern
            .compile("(?i)(?:invoice|order|inv|ref)\\s*[#:.-]?\\s*(\\d{4,8})", 2);
    // KY013002 / MOMO040401 style — 2-6 uppercase letters + 4-10 digits
    private static final Pattern INVOICE_CODE_PATTERN = Pattern.compile("\\b([A-Z]{2,6}\\d{4,10})\\b");
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("\\b\\d{5}(?:-\\d{4})?\\b");
    private static final Pattern ID_PATTERN = Pattern
            .compile("\\(?ID:?\\s*[^)]+\\)|/\\s*Salesperson:?\\s*\\w+|\\([^)]*Salesperson[^)]*\\)", 2);
    private static final String[] APPLIANCE_TYPES = { "Washer", "Dryer", "Refrigerator", "Dishwasher", "Freezer",
            "Range", "Washtower", "Microwave", "Other" };
    private static final Pattern MODEL_PATTERN = Pattern
            .compile("(?i)(?:model|mdl|mod)\\s*(?:no\\.?|num\\.?|#|:)?\\s*[:#]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})", 2);
    private static final Pattern SERIAL_PATTERN = Pattern
            .compile("(?i)(?:s/n|serial|ser\\.?|sn|a4l/serial)\\s*[#:]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})", 2);
    // Matches A4L followed by 4-14 uppercase alphanumeric chars: A4LSBYXWD0WX,
    // A4LQ0Q2XL11
    private static final Pattern A4L_PATTERN = Pattern.compile("\\b(A4L[A-Z0-9]{4,14})\\b");
    // Matches RE/VA style parenthetical secondary serials: (REQ211002), (VA387966),
    // (Z2150060)
    private static final Pattern PAREN_SERIAL_PATTERN = Pattern.compile("\\(([A-Z]{1,3}\\d{5,9})\\)");
    // Standalone model numbers without keyword prefix: WEE51550LB, GNE27JYMFS,
    // PGE29BY1FS
    private static final Pattern BARE_MODEL_PATTERN = Pattern.compile("\\b(?!A4L)([A-Z]{2,5}\\d{1,7}[A-Z0-9]{0,7})\\b");
    // Street address: starts with house number + street suffix
    private static final Pattern STREET_ADDRESS_PATTERN = Pattern.compile(
        "^\\d{1,5}\\s+.+\\b(?:St(?:reet)?|Ave(?:nue)?|Blvd|Boulevard|Dr(?:ive)?|Rd|Road|Ln|Lane|Way|Ct|Court|Pl(?:ace)?|Cir(?:cle)?|Ter(?:r)?(?:ace)?|Pike|Hwy|Highway|Pkwy|Parkway|Apt|Suite|Ste|Unit)\\b",
        Pattern.CASE_INSENSITIVE);
    private static final Pattern STREET_NUMBER_START_PATTERN = Pattern.compile("^\\d{1,5}\\s+[A-Za-z]");
    // City, State ZIP
    private static final Pattern CITY_STATE_ZIP_PATTERN = Pattern.compile(
        "(?i)\\b[A-Za-z]+(?:\\s+[A-Za-z]+)*\\s*,?\\s*\\b(?:AL|AK|AZ|AR|CA|CO|CT|DC|DE|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY)\\s+\\d{5}(?:-\\d{4})?\\b");
    private static final String[] NON_NAME_WORDS = {"invoice", "order", "date", "total", "bill to", "ship to", "sold to", "deliver",
        "description", "qty", "quantity", "type:", "model", "serial", "phone", "tel", "fax",
        "email", "page", "payment", "amount", "tax", "subtotal", "delivery", "www.", ".com", "@",
        "thank", "terms", "due", "balance", "receipt", "attention", "po box", "p.o."};

    // Known appliance brands for make extraction
    private static final String[] KNOWN_BRANDS = {
            "LG", "GE", "Samsung", "Whirlpool", "Maytag", "Frigidaire", "Bosch",
            "KitchenAid", "Amana", "Kenmore", "Electrolux", "Hotpoint", "Haier",
            "Fisher & Paykel", "Fisher and Paykel", "Speed Queen", "Miele",
            "Thermador", "Viking", "Sub-Zero", "Sub Zero", "Wolf", "Dacor",
            "Jenn-Air", "JennAir", "Cafe", "Monogram", "Profile", "Crosley",
            "Danby", "Magic Chef", "Insignia", "Hisense", "Midea", "Beko",
            "Bertazzoni", "BlueStar", "DERA"
    };

    // Company / store header keywords — phones near these lines are store phones
    private static final String[] STORE_KEYWORDS = {
            "appliances", "electronics", "furniture", "4 less", " inc", " llc", " corp",
            "showroom", "warehouse", "salesperson", "store", "a4l", "fax", "toll free",
            "customer service", "office"
    };

    public static class OCRResult {
        public String customerName = "";
        public String address = "";
        public String phone = "";
        public String altPhone = "";
        public String invoiceNumber = "";
        public String items = "";
        /** Global service flags: "DELIVERY,INSTALL,HAUL AWAY" etc. */
        public String services = "";
        public String rawText = "";
    }

    public OCRProcessorMLKit(Context context) {
        this.context = context;
    }

    public OCRResult processImage(Uri imageUri) {
        OCRResult result = new OCRResult();
        try {
            Bitmap bitmap;
            try (java.io.InputStream stream = this.context.getContentResolver().openInputStream(imageUri)) {
                if (stream == null) {
                    result.rawText = "Error: Could not open image stream";
                    return result;
                }
                bitmap = BitmapFactory.decodeStream(stream);
            }
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
            Log.d(TAG, "Alt Phone: " + result2.altPhone);
            Log.d(TAG, "Invoice #: " + result2.invoiceNumber);
            Log.d(TAG, "Items: " + result2.items);
            Log.d(TAG, "Services: " + result2.services);
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
        final boolean[] done = { false };
        final Text[] result = { null };
        this.recognizer.process(image).addOnSuccessListener(new OnSuccessListener() { // from class:
                                                                                      // com.mobileinvoice.ocr.OCRProcessorMLKit$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public void onSuccess(Object obj) {
                OCRProcessorMLKit.lambda$processImageSync$0(lock, result, done, (Text) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class:
                                                          // com.mobileinvoice.ocr.OCRProcessorMLKit$$ExternalSyntheticLambda1
            @Override // com.google.android.gms.tasks.OnFailureListener
            public void onFailure(Exception exc) {
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

        // Collect store/company phones first so we never assign them to the customer
        Set<String> storePhoneDigits = collectStorePhones(allLines);

        int billToIndex = findCustomerSectionStart(allLines);
        if (billToIndex >= 0) {
            extractFromBillToSection(allLines, billToIndex, storePhoneDigits, result);
        }
        // Always run fallback for any fields still missing
        if (result.customerName.isEmpty() || result.address.isEmpty() || result.phone.isEmpty()) {
            extractWithFallback(allLines, storePhoneDigits, result);
        }

        result.services = extractGlobalServices(allLines);
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

    /**
     * Collects the digit-strings of every phone found in the company/store header
     * block so they can be excluded from customer-phone extraction.
     * Heuristic: any phone on a line that contains a store keyword, or any phone
     * in the first few lines before the BILL-TO block.
     */
    private Set<String> collectStorePhones(List<String> lines) {
        Set<String> storeDigits = new LinkedHashSet<>();
        int billToIdx = findLineContaining(lines, "BILL TO");
        // Treat the top‑N lines (before BILL TO) as potential store-header lines
        int headerEnd = billToIdx >= 0 ? Math.min(billToIdx, 15) : Math.min(lines.size(), 10);
        for (int i = 0; i < headerEnd; i++) {
            String line = lines.get(i);
            String lower = line.toLowerCase();
            boolean isStoreCtx = false;
            for (String kw : STORE_KEYWORDS) {
                if (lower.contains(kw)) {
                    isStoreCtx = true;
                    break;
                }
            }
            if (isStoreCtx || i < 5) {
                Matcher m = PHONE_PATTERN.matcher(line);
                while (m.find())
                    storeDigits.add(m.group().replaceAll("\\D", ""));
            }
        }
        // Also scan every line for store keywords and grab phones from those lines
        for (String line : lines) {
            String lower = line.toLowerCase();
            for (String kw : STORE_KEYWORDS) {
                if (lower.contains(kw)) {
                    Matcher m = PHONE_PATTERN.matcher(line);
                    while (m.find())
                        storeDigits.add(m.group().replaceAll("\\D", ""));
                    break;
                }
            }
        }
        return storeDigits;
    }

    /** Extract a phone string from a raw match, skipping store digits. */
    private String formatPhone(String digits) {
        if (digits.length() == 10) {
            return String.format("(%s) %s-%s",
                    digits.substring(0, 3), digits.substring(3, 6), digits.substring(6));
        }
        return digits;
    }

    /**
     * Scans lines in [windowStart, windowEnd) for phone numbers not in storeDigits.
     * Sets result.phone if empty, result.altPhone if a second distinct number
     * found.
     */
    private void assignPhones(List<String> lines, int windowStart, int windowEnd,
            Set<String> storeDigits, OCRResult result) {
        List<String> found = new ArrayList<>();
        for (int i = windowStart; i < windowEnd; i++) {
            String line = lines.get(i);
            Matcher m = PHONE_PATTERN.matcher(line);
            while (m.find()) {
                String digits = m.group().replaceAll("\\D", "");
                if (storeDigits.contains(digits))
                    continue;
                String formatted = formatPhone(digits);
                if (!found.contains(formatted))
                    found.add(formatted);
            }
        }
        if (!found.isEmpty() && result.phone.isEmpty())
            result.phone = found.get(0);
        if (found.size() > 1 && result.altPhone.isEmpty())
            result.altPhone = found.get(1);
    }

    /** Global service flag detection across all invoice lines. */
    private String extractGlobalServices(List<String> lines) {
        Set<String> flags = new LinkedHashSet<>();
        for (String line : lines) {
            String lower = line.toLowerCase();
            if (lower.matches(".*\\bdeliver(?:y|ed)?\\b.*"))
                flags.add("DELIVERY");
            if (lower.matches(".*\\binstall(?:ation|ed)?\\b.*"))
                flags.add("INSTALL");
            if (lower.matches(".*\\bhaul\\s*(?:away)?\\b.*"))
                flags.add("HAUL AWAY");
            if (lower.matches(".*\\bservice\\s*(?:call|fee)?\\b.*")
                    && !lower.contains("customer service"))
                flags.add("SERVICE");
        }
        StringBuilder sb = new StringBuilder();
        for (String f : flags) {
            if (sb.length() > 0)
                sb.append(",");
            sb.append(f);
        }
        return sb.toString();
    }

    private void extractFromBillToSection(List<String> lines, int billToIndex,
            Set<String> storePhoneDigits, OCRResult result) {
        int windowStart = Math.max(0, billToIndex - 10);
        int windowEnd = Math.min(lines.size(), billToIndex + 15);

        // Collect section lines (after marker, up to next section boundary)
        List<String> sectionLines = new ArrayList<>();
        String headerLine = lines.get(billToIndex);
        String afterHeader = headerLine.replaceFirst("(?i).*(?:bill\\s*to|sold\\s*to|deliver\\s*to|customer|client|ship\\s*to|buyer|delivery\\s*address)\\s*[:.]?\\s*", "").trim();
        if (!afterHeader.isEmpty() && !afterHeader.equalsIgnoreCase(headerLine)) {
            sectionLines.add(afterHeader);
        }
        for (int i = billToIndex + 1; i < Math.min(billToIndex + 12, lines.size()); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            String lower = line.toLowerCase();
            if (lower.contains("ship to") || lower.contains("deliver to") ||
                lower.contains("description") || lower.contains("qty") ||
                lower.contains("quantity") || lower.contains("type:") ||
                lower.contains("total") || lower.contains("amount") ||
                lower.contains("subtotal")) break;
            sectionLines.add(line);
        }

        // Pass 1: labeled fields
        for (String line : sectionLines) {
            String lower = line.toLowerCase();
            if (result.customerName.isEmpty() && lower.startsWith("name:")) {
                result.customerName = extractCustomerName(line);
            }
            if (result.address.isEmpty() && lower.startsWith("address:")) {
                result.address = extractAddress(line);
            }
        }

        // Pass 2: heuristic detection for unlabeled fields
        for (int i = 0; i < sectionLines.size(); i++) {
            String line = sectionLines.get(i);
            if (PHONE_PATTERN.matcher(line).find()) continue;
            String stripped = stripFieldLabel(line);
            if (result.address.isEmpty() && isStreetAddress(stripped)) {
                result.address = extractAddress(stripped);
                // Check next line for city/state/zip
                if (i + 1 < sectionLines.size() && isCityStateZip(sectionLines.get(i + 1))) {
                    result.address = result.address + ", " + sectionLines.get(i + 1).trim();
                }
            } else if (!result.address.isEmpty() && !hasZipCode(result.address) && isCityStateZip(stripped)) {
                result.address = result.address + ", " + stripped;
            } else if (result.customerName.isEmpty() && isLikelyPersonName(stripped)) {
                result.customerName = toTitleCase(stripped);
            }
        }

        // Phone: dedicate a separate pass with store-phone suppression
        assignPhones(lines, windowStart, windowEnd, storePhoneDigits, result);
        if (result.phone.isEmpty()) {
            assignPhones(lines, 0, lines.size(), storePhoneDigits, result);
        }
    }

    private void extractWithFallback(List<String> lines, Set<String> storePhoneDigits, OCRResult result) {
        // Labeled-field first pass
        for (String line : lines) {
            if (result.customerName.isEmpty() && line.toLowerCase().startsWith("name:")) {
                result.customerName = extractCustomerName(line);
            }
            if (result.address.isEmpty() && line.toLowerCase().startsWith("address:")) {
                result.address = extractAddress(line);
            }
        }
        // Street address heuristic — find an actual street address pattern
        if (result.address.isEmpty()) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (isStreetAddress(line)) {
                    result.address = extractAddress(line);
                    if (i + 1 < lines.size() && isCityStateZip(lines.get(i + 1))) {
                        result.address = result.address + ", " + lines.get(i + 1).trim();
                    }
                    Log.d(TAG, "Fallback found address: " + result.address);
                    break;
                }
            }
        }
        // City/state/zip fallback — search backwards for street line
        if (result.address.isEmpty()) {
            for (int i = 0; i < lines.size(); i++) {
                if (isCityStateZip(lines.get(i))) {
                    result.address = lines.get(i).trim();
                    if (i > 0 && STREET_NUMBER_START_PATTERN.matcher(lines.get(i - 1)).find()) {
                        result.address = lines.get(i - 1).trim() + ", " + result.address;
                    }
                    Log.d(TAG, "Fallback found address via city/state/zip: " + result.address);
                    break;
                }
            }
        }
        // Name heuristic — find first name-like line
        if (result.customerName.isEmpty()) {
            for (String line : lines) {
                String stripped = stripFieldLabel(line);
                if (isLikelyPersonName(stripped) && !stripped.equals(result.address) &&
                        !result.address.contains(stripped)) {
                    result.customerName = toTitleCase(stripped);
                    Log.d(TAG, "Fallback found name: " + result.customerName);
                    break;
                }
            }
        }
        // Phone: skip store phones, collect customer + alt
        assignPhones(lines, 0, lines.size(), storePhoneDigits, result);
    }

    private String extractCustomerName(String line) {
        String name = line.replaceFirst("(?i)^name:\\s*", "");
        // Remove ID / Salesperson annotations via pattern
        name = ID_PATTERN.matcher(name).replaceAll("");
        // Truncate at any labeled field boundary (Phone:, Address:, Email:, Cell:, Fax:)
        name = name.replaceFirst("(?i)\\s+(phone|cell|address|email|fax|zip|city|state)\\s*:.*", "");
        // Truncate at email address if still present
        Matcher emailM = EMAIL_PATTERN.matcher(name);
        if (emailM.find()) name = name.substring(0, emailM.start());
        // Truncate at phone number if still present
        Matcher phoneM = PHONE_PATTERN.matcher(name);
        if (phoneM.find()) name = name.substring(0, phoneM.start());
        // Fallback: if an opening paren remains (OCR split the closing paren onto
        // the next line so the pattern couldn't match), strip from '(' onward
        int parenIdx = name.indexOf('(');
        if (parenIdx > 0) {
            name = name.substring(0, parenIdx);
        }
        String name2 = splitConcatenatedName(
                name.replaceAll("\\s*/\\s*", StringUtils.SPACE)
                    .replaceAll("\\s+", StringUtils.SPACE).trim());
        if (!name2.isEmpty()) {
            return toTitleCase(name2);
        }
        return name2;
    }

    private String splitConcatenatedName(String name) {
        if (name.contains(StringUtils.SPACE) || !name.equals(name.toUpperCase()) || name.length() < 6) {
            return name;
        }
        String[] commonFirstNames = { "KEN", "JON", "JOHN", "DAVID", "MIKE", "ROBERT", "JAMES", "MARY", "JUDY", "LINDA",
                "PATRICIA", "JENNIFER", "SUSAN" };
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

    private String extractInvoiceNumber(List<String> lines) {
        List<String> headerLines = new ArrayList<>();
        for (int i = 0; i < Math.min(20, lines.size()); i++) {
            String line = lines.get(i);
            String lower = line.toLowerCase();
            if (!lower.contains("address:") && !lower.contains("bill to") && !lower.contains("missouri")
                    && !lower.contains("springfield") && !lower.contains("street") && !lower.contains("avenue")
                    && !lower.contains("road") && !lower.contains("drive") && !lower.contains("city")
                    && !lower.contains("state")) {
                headerLines.add(line);
            }
        }
        // Pass 1: Look for lines immediately following an "INVOICE" header (KY013002
        // style)
        int invHeaderIdx = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().equalsIgnoreCase("INVOICE") || lines.get(i).toUpperCase().contains("INVOICE")) {
                invHeaderIdx = i;
                break;
            }
        }
        if (invHeaderIdx >= 0) {
            // Check the header line itself first (invoice code sometimes on same line)
            for (int i = invHeaderIdx; i < Math.min(invHeaderIdx + 5, lines.size()); i++) {
                String candidate = lines.get(i);
                // Skip lines that are just the word "INVOICE"
                if (i == invHeaderIdx && candidate.trim().equalsIgnoreCase("INVOICE"))
                    continue;
                Matcher m = INVOICE_CODE_PATTERN.matcher(candidate);
                if (m.find()) {
                    Log.d(TAG, "Found invoice code near INVOICE header: " + m.group(1));
                    return m.group(1);
                }
            }
        }
        // Pass 2: labeled pattern (invoice: 12345)
        for (String line : headerLines) {
            Matcher labeledMatcher = LABELED_INVOICE_PATTERN.matcher(line);
            if (labeledMatcher.find()) {
                String invoiceNum = labeledMatcher.group(1);
                if (!ZIP_CODE_PATTERN.matcher(invoiceNum).matches() || invoiceNum.length() > 5) {
                    Log.d(TAG, "Found labeled invoice number: " + invoiceNum);
                    return invoiceNum;
                }
            }
        }
        // Pass 3: KY-style code pattern on any header line
        for (String line : headerLines) {
            Matcher codeMatcher = INVOICE_CODE_PATTERN.matcher(line);
            if (codeMatcher.find()) {
                Log.d(TAG, "Found invoice code: " + codeMatcher.group(1));
                return codeMatcher.group(1);
            }
        }
        // Pass 4: standalone 6-10 digit number
        Pattern standaloneNumber = Pattern.compile("\\b(\\d{6,10})\\b");
        for (String line2 : headerLines) {
            if (!line2.toLowerCase().contains("phone") && !line2.toLowerCase().contains("email")) {
                Matcher numMatcher = standaloneNumber.matcher(line2);
                if (numMatcher.find()) {
                    String num = numMatcher.group(1);
                    if (num.length() != 10 || (!num.startsWith("417") && !num.startsWith("573")
                            && !num.startsWith("816") && !num.startsWith("314"))) {
                        Log.d(TAG, "Found standalone invoice number: " + num);
                        return num;
                    }
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
                String item = line.replaceFirst("(?i)^type:\\s*", "").trim().split("(?i)\\s+(model|serial|s/n)")[0]
                        .trim();
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
                    if (!appliance.equals("Other") && line2.toLowerCase()
                            .matches(".*\\b" + appliance.toLowerCase() + "\\b.*")) {
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
            int windowEnd = Math.min(lines.size(), applianceLine >= 0 ? applianceLine + 12 : lines.size());
            for (int i3 = windowStart; i3 < windowEnd; i3++) {
                String line3 = lines.get(i3).trim();
                // Try labeled model pattern first, then standalone bare model
                if (di.model.isEmpty()) {
                    Matcher m = MODEL_PATTERN.matcher(line3);
                    if (m.find()) {
                        di.model = m.group(1).trim();
                    } else {
                        // Bare model: alphanumeric 7-15 chars, not A4L, has both letters+digits
                        Matcher mBare = BARE_MODEL_PATTERN.matcher(line3);
                        while (mBare.find()) {
                            String candidate = mBare.group(1);
                            if (candidate.length() >= 7 && candidate.matches(".*[A-Z].*")
                                    && candidate.matches(".*\\d.*")) {
                                di.model = candidate;
                                break;
                            }
                        }
                    }
                }
                // Try A4L pattern first (most specific), then labeled serial pattern
                if (di.serial.isEmpty()) {
                    Matcher mA4L = A4L_PATTERN.matcher(line3);
                    if (mA4L.find()) {
                        di.serial = mA4L.group(1).trim();
                        // Also check for parenthetical RE/VA# on the same or next line
                        Matcher mParen = PAREN_SERIAL_PATTERN.matcher(line3);
                        if (!mParen.find() && i3 + 1 < windowEnd) {
                            mParen = PAREN_SERIAL_PATTERN.matcher(lines.get(i3 + 1).trim());
                        }
                        if (mParen.find()) {
                            di.serial = di.serial + " (" + mParen.group(1) + ")";
                        }
                    } else {
                        Matcher m2 = SERIAL_PATTERN.matcher(line3);
                        if (m2.find()) {
                            di.serial = m2.group(1).trim();
                        }
                    }
                }
                // Detect brand/make from line
                if (di.make.isEmpty()) {
                    di.make = detectMake(line3);
                }
            }
            // Detect service flags from the entire window for this item
            if (di.services.isEmpty()) {
                List<String> windowLines = lines.subList(windowStart, windowEnd);
                di.services = detectItemServices(windowLines);
            }
            idx++;
        }
        return ItemsHelper.toJson(foundItems);
    }

    /** Returns the brand/make found in the given line, or "". */
    private String detectMake(String line) {
        String lower = line.toLowerCase();
        for (String brand : KNOWN_BRANDS) {
            if (lower.contains(brand.toLowerCase())) {
                return brand;
            }
        }
        return "";
    }

    /**
     * Scans a window of lines and returns a comma-delimited string of service flags
     * detected: DELIVERY, INSTALL, HAUL AWAY, SERVICE.
     */
    private String detectItemServices(List<String> windowLines) {
        Set<String> flags = new LinkedHashSet<>();
        for (String line : windowLines) {
            String lower = line.toLowerCase();
            if (lower.matches(".*\\bdeliver(?:y|ed)?\\b.*"))
                flags.add("DELIVERY");
            if (lower.matches(".*\\binstall(?:ation|ed)?\\b.*"))
                flags.add("INSTALL");
            if (lower.matches(".*\\bhaul\\s*(?:away)?\\b.*"))
                flags.add("HAUL AWAY");
            if (lower.matches(".*\\bservice\\s*(?:call|fee)?\\b.*")
                    && !lower.contains("customer service"))
                flags.add("SERVICE");
        }
        StringBuilder sb = new StringBuilder();
        for (String f : flags) {
            if (sb.length() > 0)
                sb.append(",");
            sb.append(f);
        }
        return sb.toString();
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

    private int findCustomerSectionStart(List<String> lines) {
        String[] markers = { "BILL TO", "SHIP TO", "DELIVER TO", "SOLD TO", "CUSTOMER:", "DELIVERY ADDRESS" };
        for (String marker : markers) {
            int idx = findLineContaining(lines, marker);
            if (idx >= 0) return idx;
        }
        return -1;
    }

    private boolean isStoreHeaderLine(String line) {
        String lower = line.toLowerCase();
        for (String kw : STORE_KEYWORDS) {
            if (lower.contains(kw)) return true;
        }
        return false;
    }

    /** True if the line looks like a street address with a recognizable suffix. */
    private boolean isStreetAddress(String line) {
        if (line.isEmpty() || !Character.isDigit(line.charAt(0))) return false;
        return STREET_ADDRESS_PATTERN.matcher(line).find() ||
               (STREET_NUMBER_START_PATTERN.matcher(line).find() && line.length() > 10);
    }

    private boolean isCityStateZip(String line) {
        return CITY_STATE_ZIP_PATTERN.matcher(line).find();
    }

    private boolean hasZipCode(String text) {
        return ZIP_CODE_PATTERN.matcher(text).find();
    }

    /** True if the line looks like a person name: mostly alpha, 1-5 words, no numbers/addresses/business terms. */
    private boolean isLikelyPersonName(String line) {
        if (line.isEmpty() || Character.isDigit(line.charAt(0))) return false;
        if (PHONE_PATTERN.matcher(line).find()) return false;
        if (isCityStateZip(line)) return false;
        if (isStreetAddress(line)) return false;
        if (isStoreHeaderLine(line)) return false;
        String lower = line.toLowerCase();
        for (String nope : NON_NAME_WORDS) {
            if (lower.contains(nope)) return false;
        }
        String cleaned = line.replaceAll("[^A-Za-z\\s'\\-]", "").trim();
        String[] words = cleaned.split("\\s+");
        if (words.length < 1 || words.length > 5) return false;
        if (cleaned.isEmpty()) return false;
        int alphaCount = 0;
        for (char c : line.toCharArray()) {
            if (Character.isLetter(c) || c == ' ' || c == '\'' || c == '-') alphaCount++;
        }
        return alphaCount > line.length() * 0.7;
    }

    private String stripFieldLabel(String line) {
        return line.replaceFirst("(?i)^(?:name|customer|address|addr|phone|tel|fax|email|sold to|bill to|deliver(?:y)?\\s*(?:to|address)?|ship to)\\s*[:.]?\\s*", "").trim();
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
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1))
                        .append(StringUtils.SPACE);
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
