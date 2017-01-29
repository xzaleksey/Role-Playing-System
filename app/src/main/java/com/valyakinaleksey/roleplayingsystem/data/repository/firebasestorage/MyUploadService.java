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
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
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
  }

  @Override public void onCreate() {
    super.onCreate();

    RpsApp.getAppComponent(RpsApp.app()).inject(this);
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (ACTION_UPLOAD.equals(intent.getAction())) {
      switch (intent.getStringExtra(REPOSITORY_TYPE)) {
        case MapsRepository.MAP_REPOSOTORY:
          Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
          String gameId = intent.getStringExtra(FireBaseUtils.ID);
          String mapId = intent.getStringExtra(MapModel.MAP_MODEL_ID);
          mapsRepository.uploadMapToFirebase(fileUri, gameId, mapId)
              .compose(RxTransformers.applyIoSchedulers())
              .doOnSubscribe(this::taskStarted)
              .subscribe(integer -> {
                taskCompleted();
              });
          break;
      }
    }
    return START_REDELIVER_INTENT;
  }

  @Override public void onDestroy() {
    Timber.d("OnDestroy Service");
    super.onDestroy();
  }

  // [END upload_from_uri]
  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }
}
      