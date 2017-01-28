package com.valyakinaleksey.roleplayingsystem.data.repository.maps;

import android.graphics.Bitmap;
import com.kbeanie.multipicker.utils.FileUtils;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import id.zelory.compressor.Compressor;
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
      String newDirectory = pathManager.getCachePath()
          .concat(StringUtils.formatWithSlashes(gameId))
          .concat(FireBaseUtils.GAME_MAPS);
      Compressor compressor =
          new Compressor.Builder(RpsApp.app()).setDestinationDirectoryPath(newDirectory).build();
      return compressor.compressToFile(file);
    });
  }
}
      