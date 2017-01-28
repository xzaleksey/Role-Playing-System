package com.valyakinaleksey.roleplayingsystem.data.repository.maps;

import java.io.File;
import rx.Observable;

public interface MapsRepository {

  Observable<File> createLocalFileCopy(String gameId, File file);
}
      