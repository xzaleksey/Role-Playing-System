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
import com.valyakinaleksey.roleplayingsystem.data.repository.maps.MapsRepository;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.ParentActivity;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import javax.inject.Inject;
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
  public static final String REPOSITORY_TYPE = "REPOSITORY_TYPE";

  @Inject MapsRepository mapsRepository;

  public MyUploadService() {
    super(MyUploadService.class.getSimpleName());
  }
  // [END declare_ref]

  @Override public void onCreate() {
    super.onCreate();
    RpsApp.getAppComponent(RpsApp.app()).inject(this);
  }

  @Override protected void onHandleIntent(Intent intent) {

  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Timber.d("onStartCommand:" + intent + ":" + startId);
    if (ACTION_UPLOAD.equals(intent.getAction())) {
      switch (intent.getStringExtra(REPOSITORY_TYPE)) {
        case MapsRepository.MAP_REPOSOTORY:
          Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
          String gameId = intent.getStringExtra(FireBaseUtils.ID);
          String mapId = intent.getStringExtra(MapModel.MAP_MODEL_ID);
          mapsRepository.uploadMapToFirebase(fileUri, gameId, mapId);
          break;
      }
    }

    return START_REDELIVER_INTENT;
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

  // [END upload_from_uri]
  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
      