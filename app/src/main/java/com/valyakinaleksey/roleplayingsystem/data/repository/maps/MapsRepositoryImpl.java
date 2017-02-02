package com.valyakinaleksey.roleplayingsystem.data.repository.maps;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.NotificationUtils;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import id.zelory.compressor.Compressor;
import java.io.File;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ERROR;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_MAPS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.STATUS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.SUCCESS;

public class MapsRepositoryImpl implements MapsRepository {

  private PathManager pathManager;
  private StorageReference storageReference;

  public MapsRepositoryImpl(PathManager pathManager) {
    this.pathManager = pathManager;
    storageReference = FirebaseStorage.getInstance().getReference();
  }

  @Override public Observable<File> createLocalFileCopy(String gameId, String key, File file) {

    return Observable.just(file).compose(RxTransformers.applyIoSchedulers()).map(file1 -> {
      String newDirectory = getLocalDirectory(gameId, key);
      Compressor compressor =
          new Compressor.Builder(RpsApp.app()).setDestinationDirectoryPath(newDirectory)
              .setQuality(100)
              .build();
      return compressor.compressToFile(file);
    });
  }

  @Override
  public Observable<Integer> uploadMapToFirebase(Uri fileUri, String gameId, String mapId) {
    final StorageReference photoRef = storageReference.child(GAME_MAPS)
        .child(gameId)
        .child(mapId)
        .child(fileUri.getLastPathSegment());
    PublishSubject<Integer> integerPublishSubject = PublishSubject.create();
    // [END get_child_ref]

    // Upload file to Firebase Storage
    Timber.d("uploadMap:dst:" + photoRef.getPath());

    int notifId = mapId.hashCode();
    photoRef.putFile(fileUri).addOnProgressListener(taskSnapshot -> {
      Timber.d("uploadMap:inProgress " + (taskSnapshot.getBytesTransferred()
          / taskSnapshot.getTotalByteCount() * 100));
      NotificationUtils.showProgressNotification(notifId,
          StringUtils.getStringById(R.string.progress_uploading),
          taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
    }).addOnSuccessListener(taskSnapshot -> {
      // Upload succeeded
      Timber.d("uploadMap:onSuccess");
      NotificationUtils.dismissNotification(notifId);
      getReference(gameId).child(mapId).child(STATUS).setValue(SUCCESS);
      integerPublishSubject.onNext(SUCCESS);
    }).addOnFailureListener(exception -> {
      // Upload failed
      Timber.d("uploadMap:onFailed");
      NotificationUtils.dismissNotification(notifId);
      getReference(gameId).child(mapId).child(STATUS).setValue(ERROR);
      integerPublishSubject.onNext(FireBaseUtils.ERROR);
      Timber.d(exception);
    });
    return integerPublishSubject;
  }

  @Override public Observable<Void> deleteMap(String gameId, String mapId, String fileName) {
    return RxFirebaseStorage.delete(
        storageReference.child(GAME_MAPS).child(gameId).child(mapId).child(fileName))
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
    return FirebaseDatabase.getInstance().getReference().child(GAME_MAPS).child(gameModelId);
  }

  @NonNull private String getLocalDirectory(String gameId, String key) {
    return pathManager.getCachePath()
        .concat(StringUtils.formatWithSlashes(GAME_MAPS))
        .concat(gameId)
        .concat("/")
        .concat(key);
  }
}
      