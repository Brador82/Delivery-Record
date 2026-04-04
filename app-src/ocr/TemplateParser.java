package com.mobileinvoice.ocr;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.mobileinvoice.ocr.InvoiceTemplate;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/* loaded from: classes7.dex */
public class TemplateParser {
    private static final String TAG = "TemplateParser";
    private Context context;

    public TemplateParser(Context context) {
        this.context = context;
    }

    public InvoiceTemplate loadTemplate(String templateName) {
        try {
            InputStream is = this.context.getAssets().open(templateName);
            InputStreamReader reader = new InputStreamReader(is);
            Gson gson = new Gson();
            InvoiceTemplate template = (InvoiceTemplate) gson.fromJson((Reader) reader, InvoiceTemplate.class);
            reader.close();
            is.close();
            Log.d(TAG, "Loaded template: " + templateName);
            Log.d(TAG, "Grid: " + template.grid.cols + "x" + template.grid.rows);
            Log.d(TAG, "Fields: " + template.fields.size());
            return template;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load template: " + templateName, e);
            return null;
        }
    }

    public InvoiceTemplate.Field getField(InvoiceTemplate template, String fieldName) {
        if (template == null || template.fields == null) {
            return null;
        }
        for (InvoiceTemplate.Field field : template.fields) {
            if (field.name.equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public InvoiceTemplate.Field getFieldByType(InvoiceTemplate template, String fieldType) {
        if (template == null || template.fields == null) {
            return null;
        }
        for (InvoiceTemplate.Field field : template.fields) {
            if (field.type.equals(fieldType)) {
                return field;
            }
        }
        return null;
    }
}
