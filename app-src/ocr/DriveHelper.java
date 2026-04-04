package com.mobileinvoice.ocr;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import java.io.File;
import java.util.Collections;

/* loaded from: classes7.dex */
public class DriveHelper {
    private static final String DRIVE_FOLDER_NAME = "Mobile Invoice OCR";
    private static final String PLACEHOLDER_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com";
    private static final String TAG = "DriveHelper";

    public interface Callback {
        void onFailure(String error);

        void onSuccess(String message);
    }

    public static boolean isConfigured(Context context) {
        String clientId = context.getString(R.string.google_oauth_client_id);
        return (clientId.isEmpty() || clientId.equals(PLACEHOLDER_CLIENT_ID)) ? false : true;
    }

    public static boolean isSignedIn(Context context) {
        GoogleSignInAccount account;
        return isConfigured(context) && (account = GoogleSignIn.getLastSignedInAccount(context)) != null && GoogleSignIn.hasPermissions(account, new Scope("https://www.googleapis.com/auth/drive.file"));
    }

    public static String getSignedInEmail(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        return (account == null || account.getEmail() == null) ? "" : account.getEmail();
    }

    public static GoogleSignInClient buildSignInClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope[0]).build();
        return GoogleSignIn.getClient(context, gso);
    }

    public static void handleSignInResult(Context context, Task<GoogleSignInAccount> completedTask, Callback callback) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null && account.getEmail() != null) {
                AppSettings.getInstance(context).setDriveAccountEmail(account.getEmail());
                AppSettings.getInstance(context).setDriveSyncEnabled(true);
                callback.onSuccess("Connected: " + account.getEmail());
            } else {
                callback.onFailure("Sign-in succeeded but no account returned.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Sign-in failed: " + e.getMessage());
            callback.onFailure("Sign-in failed: " + e.getMessage());
        }
    }

    public static void signOut(final Context context, final Callback callback) {
        buildSignInClient(context).signOut().addOnCompleteListener(new OnCompleteListener() { // from class: com.mobileinvoice.ocr.DriveHelper$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                DriveHelper.lambda$signOut$0(context, callback, task);
            }
        });
    }

    static /* synthetic */ void lambda$signOut$0(Context context, Callback callback, Task task) {
        AppSettings.getInstance(context).setDriveSyncEnabled(false);
        AppSettings.getInstance(context).setDriveAccountEmail("");
        callback.onSuccess("Disconnected from Google Drive.");
    }

    public static void uploadFile(final Context context, final File localFile, final String mimeType, final Callback callback) {
        if (!isSignedIn(context)) {
            callback.onFailure("Not signed in to Google Drive.");
        } else {
            new Thread(new Runnable() { // from class: com.mobileinvoice.ocr.DriveHelper$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DriveHelper.lambda$uploadFile$1(context, callback, localFile, mimeType);
                }
            }).start();
        }
    }

    static /* synthetic */ void lambda$uploadFile$1(Context context, Callback callback, File localFile, String mimeType) {
        try {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
            if (account == null) {
                callback.onFailure("Drive account not available.");
                return;
            }
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(context, Collections.singletonList("https://www.googleapis.com/auth/drive.file"));
            credential.setSelectedAccount(account.getAccount());
            Drive driveService = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential).setApplicationName(DRIVE_FOLDER_NAME).build();
            String folderId = getOrCreateFolder(driveService, DRIVE_FOLDER_NAME);
            com.google.api.services.drive.model.File metadata = new com.google.api.services.drive.model.File();
            metadata.setName(localFile.getName());
            metadata.setMimeType(mimeType);
            if (folderId != null) {
                metadata.setParents(Collections.singletonList(folderId));
            }
            FileContent content = new FileContent(mimeType, localFile);
            com.google.api.services.drive.model.File uploaded = driveService.files().create(metadata, content).setFields2("id, name, webViewLink").execute();
            Log.d(TAG, "Uploaded: " + uploaded.getName() + " → " + uploaded.getId());
            callback.onSuccess("Saved to Drive: " + uploaded.getName());
        } catch (Exception e) {
            Log.e(TAG, "Drive upload failed: " + e.getMessage());
            callback.onFailure("Drive upload failed: " + e.getMessage());
        }
    }

    private static String getOrCreateFolder(Drive driveService, String folderName) {
        try {
            FileList result = driveService.files().list().setQ("mimeType='application/vnd.google-apps.folder' and name='" + folderName + "' and trashed=false").setFields2("files(id, name)").execute();
            if (!result.getFiles().isEmpty()) {
                return result.getFiles().get(0).getId();
            }
            com.google.api.services.drive.model.File folderMeta = new com.google.api.services.drive.model.File();
            folderMeta.setName(folderName);
            folderMeta.setMimeType("application/vnd.google-apps.folder");
            com.google.api.services.drive.model.File created = driveService.files().create(folderMeta).setFields2("id").execute();
            return created.getId();
        } catch (Exception e) {
            Log.e(TAG, "Failed to get/create folder: " + e.getMessage());
            return null;
        }
    }
}
