package com.valyakinaleksey.roleplayingsystem.data.repository.game.map;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.FirebaseTable;
import com.valyakinaleksey.roleplayingsystem.utils.NotificationUtils;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel.PHOTO_URL;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ERROR;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.STATUS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.SUCCESS;

public class FileMapsRepositoryImpl implements FileMapsRepository {

    private PathManager pathManager;
    private StorageReference storageReference;

    public FileMapsRepositoryImpl(PathManager pathManager) {
        this.pathManager = pathManager;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public Observable<File> createLocalFileCopy(String gameId, String key, File file) {

        return Observable.just(file).compose(RxTransformers.applyIoSchedulers()).map(file1 -> {
            String newDirectory = getLocalDirectory(gameId, key);
            Compressor compressor = new Compressor.Builder(RpsApp.app())
                    .setDestinationDirectoryPath(newDirectory)
                    .setQuality(100)
                    .build();
            return compressor.compressToFile(file);
        });
    }

    @Override
    public Observable<Integer> uploadMapToFirebase(Uri fileUri, String gameId, String mapId) {
        final StorageReference photoRef = storageReference.child(FirebaseTable.GAME_MAPS)
                .child(gameId)
                .child(mapId)
                .child(fileUri.getLastPathSegment());
        PublishSubject<Integer> integerPublishSubject = PublishSubject.create();

        Timber.d("uploadMap:dst:" + photoRef.getPath());

        int notificationOd = mapId.hashCode();
        photoRef.putFile(fileUri).addOnProgressListener(taskSnapshot -> {
            Timber.d("uploadMap:inProgress " + (taskSnapshot.getBytesTransferred()
                    / taskSnapshot.getTotalByteCount() * 100));
            NotificationUtils.showProgressNotification(notificationOd,
                    StringUtils.getStringById(R.string.progress_uploading),
                    taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
        }).addOnSuccessListener(taskSnapshot -> {
            onSuccess(gameId, mapId, integerPublishSubject, notificationOd, taskSnapshot);
        }).addOnFailureListener(exception -> {
            onFail(gameId, mapId, integerPublishSubject, notificationOd, exception);
        });
        return integerPublishSubject;
    }

    private void onFail(String gameId, String mapId, PublishSubject<Integer> integerPublishSubject,
                        int notificationOd, Exception exception) {
        Timber.d("uploadMap:onFailed");
        NotificationUtils.dismissNotification(notificationOd);
        getReference(gameId).child(mapId).child(STATUS).setValue(ERROR);
        integerPublishSubject.onNext(FireBaseUtils.ERROR);
        Timber.d(exception);
    }

    private void onSuccess(String gameId, String mapId, PublishSubject<Integer> integerPublishSubject,
                           int notificationOd, UploadTask.TaskSnapshot taskSnapshot) {
        Timber.d("uploadMap:onSuccess");
        NotificationUtils.dismissNotification(notificationOd);
        Uri downloadUrl = taskSnapshot.getDownloadUrl();
        String downloadUrlString = StringUtils.EMPTY_STRING;
        if (downloadUrl != null) {
            downloadUrlString = downloadUrl.toString();
        }

        DatabaseReference reference = getReference(gameId).child(mapId);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(STATUS, SUCCESS);
        childUpdates.put(PHOTO_URL, downloadUrlString);
        reference.updateChildren(childUpdates);
        integerPublishSubject.onNext(SUCCESS);
    }

    @Override
    public Observable<Void> deleteMap(String gameId, String mapId, String fileName) {
        return RxFirebaseStorage.delete(
                storageReference.child(FirebaseTable.GAME_MAPS).child(gameId).child(mapId).child(fileName))
                .doOnNext(aVoid -> {
                    String newDirectory = getLocalDirectory(gameId, mapId);
                    File file = new File(newDirectory);
                    if (file.exists()) {
                        boolean success = file.delete();
                        Timber.d("delete local file success " + success);
                    }
                });
    }

    private DatabaseReference getReference(String gameModelId) {
        return FirebaseDatabase.getInstance().getReference().child(FirebaseTable.GAME_MAPS).child(gameModelId);
    }

    @NonNull
    private String getLocalDirectory(String gameId, String key) {
        return pathManager.getImagesCachePath()
                .concat(StringUtils.formatWithSlashes(FirebaseTable.GAME_MAPS))
                .concat(gameId)
                .concat("/")
                .concat(key);
    }
}
      