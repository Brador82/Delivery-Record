package com.mobileinvoice.ocr;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes7.dex */
public abstract class BaseActivity extends AppCompatActivity {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
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
                setTheme(R.style.Theme_MobileInvoiceOCR_LightMarble);
                break;
            case 2:
                setTheme(R.style.Theme_MobileInvoiceOCR_Blended);
                break;
            default:
                setTheme(R.style.Theme_MobileInvoiceOCR);
                break;
        }
    }

    public static void restartApp(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(268468224);
        context.startActivity(intent);
    }
}
