package com.valyakinaleksey.roleplayingsystem.data.repository.maps;

import com.kbeanie.multipicker.utils.FileUtils;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import rx.Observable;

public class MapsRepositoryImpl implements MapsRepository {

  private PathManager pathManager;

  public MapsRepositoryImpl(PathManager pathManager) {
    this.pathManager = pathManager;
  }

  @Override public Observable<File> createLocalFileCopy(String gameId, File file) {
    return Observable.just(file).compose(RxTransformers.applyIoSchedulers()).map(file1 -> {
      File newFile = new File(pathManager.getCachePath()
          .concat(StringUtils.formatWithSlashes(gameId))
          .concat(FireBaseUtils.GAME_MAPS)
          .concat("/")
          .concat(file.getName()));
      try {
        FileUtils.copyFile(file, newFile);
      } catch (IOException e) {
        Observable.error(e);
      }
      return newFile;
    });
  }
}
      