package com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.ParentActivity;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import timber.log.Timber;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class MyUploadService extends MyBaseTaskService {

  private static final String TAG = "MyUploadService";

  /** Intent Actions **/
  public static final String ACTION_UPLOAD = "action_upload";
  public static final String UPLOAD_COMPLETED = "upload_completed";
  public static final String UPLOAD_ERROR = "upload_error";

  /** Intent Extras **/
  public static final String EXTRA_FILE_URI = "extra_file_uri";
  public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

  // [START declare_ref]
  private StorageReference mStorageRef;
  // [END declare_ref]

  @Override public void onCreate() {
    super.onCreate();

    // [START get_storage_ref]
    mStorageRef = FirebaseStorage.getInstance().getReference();
    // [END get_storage_ref]
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Timber.d("onStartCommand:" + intent + ":" + startId);
    if (ACTION_UPLOAD.equals(intent.getAction())) {
      Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
      String gameId = intent.getStringExtra(FireBaseUtils.ID);
      uploadMap(fileUri, gameId);
    }

    return START_REDELIVER_INTENT;
  }

  // [START upload_from_uri]
  private void uploadMap(final Uri fileUri, String gameId) {
    Timber.d("uploadMap:src:" + fileUri.toString());

    // [START_EXCLUDE]
    taskStarted();
    showProgressNotification(getString(R.string.progress_uploading), 0, 0);
    // [END_EXCLUDE]

    // [START get_child_ref]
    // Get a reference to store file at photos/<FILENAME>.jpg
    final StorageReference photoRef = mStorageRef.child(gameId)
        .child(FireBaseUtils.GAME_MAPS)
        .child(fileUri.getLastPathSegment());
    // [END get_child_ref]

    // Upload file to Firebase Storage
    Timber.d(TAG, "uploadMap:dst:" + photoRef.getPath());
    photoRef.putFile(fileUri)
        .
            addOnProgressListener(
                taskSnapshot -> showProgressNotification(getString(R.string.progress_uploading),
                    taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount()))
        .addOnSuccessListener(taskSnapshot -> {
          // Upload succeeded
          Timber.d("uploadMap:onSuccess");

          // Get the public download URL
          Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

          // [START_EXCLUDE]
          broadcastUploadFinished(downloadUri, fileUri);
          showUploadFinishedNotification(downloadUri, fileUri);
          taskCompleted();
          // [END_EXCLUDE]
        })
        .addOnFailureListener(exception -> {
          // Upload failed
          Log.w(TAG, "uploadMap:onFailure", exception);

          // [START_EXCLUDE]
          broadcastUploadFinished(null, fileUri);
          showUploadFinishedNotification(null, fileUri);
          taskCompleted();
          // [END_EXCLUDE]
        });
  }
  // [END upload_from_uri]

  /**
   * Broadcast finished upload (success or failure).
   *
   * @return true if a running receiver received the broadcast.
   */
  private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
    boolean success = downloadUrl != null;

    String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

    Intent broadcast = new Intent(action).putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
        .putExtra(EXTRA_FILE_URI, fileUri);
    return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
  }

  /**
   * Show a notification for a finished upload.
   */
  private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
    // Hide the progress notification
    dismissProgressNotification();

    // Make Intent to MainActivity
    Intent intent = new Intent(this, ParentActivity.class).putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
        .putExtra(EXTRA_FILE_URI, fileUri)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    boolean success = downloadUrl != null;
    String caption = success ? getString(R.string.success) : getString(R.string.fail);
    showFinishedNotification(caption, intent, success);
  }

  public static IntentFilter getIntentFilter() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(UPLOAD_COMPLETED);
    filter.addAction(UPLOAD_ERROR);

    return filter;
  }
}
      