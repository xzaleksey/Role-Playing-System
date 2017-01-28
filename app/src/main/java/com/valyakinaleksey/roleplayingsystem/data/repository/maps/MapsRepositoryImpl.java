package com.valyakinaleksey.roleplayingsystem.data.repository.maps;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import id.zelory.compressor.Compressor;
import java.io.File;
import rx.Observable;
import timber.log.Timber;

public class MapsRepositoryImpl implements MapsRepository {

  private PathManager pathManager;
  private StorageReference storageReference;

  public MapsRepositoryImpl(PathManager pathManager) {
    this.pathManager = pathManager;
    storageReference = FirebaseStorage.getInstance().getReference();
  }

  @Override public Observable<File> createLocalFileCopy(String gameId, String key, File file) {

    return Observable.just(file).compose(RxTransformers.applyIoSchedulers()).map(file1 -> {
      String newDirectory = pathManager.getCachePath()
          .concat(StringUtils.formatWithSlashes(FireBaseUtils.GAME_MAPS))
          .concat(gameId)
          .concat("/")
          .concat(key);
      Compressor compressor =
          new Compressor.Builder(RpsApp.app()).setDestinationDirectoryPath(newDirectory).build();
      return compressor.compressToFile(file);
    });
  }

  @Override public void uploadMapToFirebase(Uri fileUri, String gameId, String mapId) {
    final StorageReference photoRef = storageReference.child(gameId)
        .child(FireBaseUtils.GAME_MAPS)
        .child(gameId)
        .child(mapId)
        .child(fileUri.getLastPathSegment());
    // [END get_child_ref]

    // Upload file to Firebase Storage
    Timber.d("uploadMap:dst:" + photoRef.getPath());

    photoRef.putFile(fileUri).addOnProgressListener(taskSnapshot -> {
      //showProgressNotification(getString(R.string.progress_uploading),
      //    taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount())
    }).addOnSuccessListener(taskSnapshot -> {
      // Upload succeeded
      Timber.d("uploadMap:onSuccess");
    }).addOnFailureListener(exception -> {
      // Upload failed
      Timber.d(exception);
    });
  }
}
      