package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor;

import android.content.Intent;
import android.net.Uri;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage.MyUploadService;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FileMapsRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FirebaseMapRepository;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter.MapFlexibleViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapHandler;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsListFlexibleModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import rx.Observable;

public class MapUseCase implements MapsInteractor {
  private FileMapsRepository fileMapsRepository;
  private FirebaseMapRepository mapsRepository;

  public MapUseCase(FileMapsRepository fileMapsRepository, FirebaseMapRepository mapsRepository) {
    this.fileMapsRepository = fileMapsRepository;
    this.mapsRepository = mapsRepository;
  }

  private DatabaseReference getReference(String gameModelId) {
    return FirebaseDatabase.getInstance()
        .getReference()
        .child(FireBaseUtils.GAME_MAPS)
        .child(gameModelId);
  }

  @Override public Observable<MapModel> createNewMap(GameModel gameModel, ChosenFile chosenFile) {
    DatabaseReference fileReference = getReference(gameModel.getId());
    DatabaseReference push = fileReference.push();
    String key = push.getKey();
    return fileMapsRepository.
        createLocalFileCopy(gameModel.getId(), key, new File(chosenFile.getOriginalPath()))
        .switchMap(file -> {
          String fileName = file.getName();
          MapModel mapModel = new MapModel(fileName);
          mapModel.setTempDateCreate(DateTime.now().getMillis());
          mapModel.setFileName(fileName);
          return FireBaseUtils.setData(mapModel, push).map(aVoid -> {
            push.child(FireBaseUtils.TEMP_DATE_CREATE).setValue(null);
            Intent intent = new Intent(RpsApp.app(), MyUploadService.class);
            intent.setAction(MyUploadService.ACTION_UPLOAD);
            intent.putExtra(MyUploadService.REPOSITORY_TYPE, FileMapsRepository.MAP_REPOSOTORY);
            intent.putExtra(MyUploadService.EXTRA_FILE_URI, Uri.fromFile(file));
            intent.putExtra(FireBaseUtils.ID, gameModel.getId());
            intent.putExtra(MapModel.MAP_MODEL_ID, key);
            RpsApp.app().startService(intent);
            return mapModel;
          });
        });
  }

  @Override public void changeMapVisibility(MapModel mapModel, boolean isVisible) {
    getReference(mapModel.getGameId()).child(mapModel.getId())
        .child(FireBaseUtils.VISIBLE)
        .setValue(isVisible);
  }

  @Override public Observable<Void> deleteMap(MapModel mapModel) {
    return Observable.just(getReference(mapModel.getGameId()))
        .switchMap(databaseReference -> {
          DatabaseReference mapReference = databaseReference.child(mapModel.getId());
          return FireBaseUtils.deleteValue(mapReference);
        })
        .switchMap(r -> fileMapsRepository.deleteMap(mapModel.getGameId(), mapModel.getId(),
            mapModel.getFileName()));
  }

  @Override
  public Observable<MapsListFlexibleModel> observeMaps(GameModel gameModel, MapHandler mapHandler) {
    boolean isMaster = gameModel.getMasterId().equals(FireBaseUtils.getCurrentUserId());
    return mapsRepository.observeMaps(gameModel.getId()).map(stringMapModelMap -> {
      List<IFlexible<?>> result = new ArrayList<>();
      for (MapModel mapModel : stringMapModelMap.values()) {
        if (isMaster || mapModel.isVisible()) {
          result.add(new MapFlexibleViewModel(mapModel, isMaster, mapHandler));
        }
      }
      return new MapsListFlexibleModel(result);
    });
  }
}
      