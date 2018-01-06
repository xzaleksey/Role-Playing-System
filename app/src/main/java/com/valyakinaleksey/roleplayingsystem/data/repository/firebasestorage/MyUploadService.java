package com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FileMapsRepository;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import timber.log.Timber;

import javax.inject.Inject;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class MyUploadService extends MyBaseTaskService {

    private static final String TAG = "MyUploadService";

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /**
     * Intent Extras
     **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String REPOSITORY_TYPE = "REPOSITORY_TYPE";

    @Inject
    FileMapsRepository fileMapsRepository;

    public MyUploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RpsApp.getAppComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            switch (intent.getStringExtra(REPOSITORY_TYPE)) {
                case FileMapsRepository.MAP_REPOSOTORY:
                    Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
                    String gameId = intent.getStringExtra(FireBaseUtils.ID);
                    String mapId = intent.getStringExtra(MapModel.MAP_MODEL_ID);

                    fileMapsRepository.uploadMapToFirebase(fileUri, gameId, mapId)
                            .compose(RxTransformers.applyIoSchedulers())
                            .doOnSubscribe(this::taskStarted)
                            .subscribe(integer -> taskCompleted());
                    break;
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Timber.d("OnDestroy Service");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
      