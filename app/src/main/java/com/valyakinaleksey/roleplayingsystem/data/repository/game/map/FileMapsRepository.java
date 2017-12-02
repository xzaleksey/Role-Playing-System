package com.valyakinaleksey.roleplayingsystem.data.repository.game.map;

import android.net.Uri;
import rx.Observable;

import java.io.File;

public interface FileMapsRepository {
  String MAP_REPOSOTORY = "FirebaseMapRepository";
  String AVATAR_REPOSOTORY = "FirebaseAvatarRepository";

  Observable<File> createLocalFileCopy(String gameId, String key, File file);

  Observable<Integer> uploadMapToFirebase(Uri fileUri, String gameId, String mapId);

  Observable<Void> deleteMap(String gameId, String mapId, String fileName);
}
      