package com.valyakinaleksey.roleplayingsystem.data.repository.maps;

import android.net.Uri;
import java.io.File;
import rx.Observable;

public interface MapsRepository {
  String MAP_REPOSOTORY = "MapRepository";

  Observable<File> createLocalFileCopy(String gameId, String key, File file);

  Observable<Integer> uploadMapToFirebase(Uri fileUri, String gameId, String mapId);

  Observable<Void> deleteMap(String gameId, String mapId, String fileName);
}
      