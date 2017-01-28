package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.valyakinaleksey.roleplayingsystem.data.repository.maps.MapsRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.io.File;
import rx.Observable;

public class MapUseCase implements MapsInteractor {
  private MapsRepository mapsRepository;

  public MapUseCase(MapsRepository mapsRepository) {
    this.mapsRepository = mapsRepository;
  }

  private DatabaseReference getReference(GameModel gameModel) {
    return FirebaseDatabase.getInstance()
        .getReference()
        .child(FireBaseUtils.GAME_MAPS)
        .child(gameModel.getId());
  }

  @Override public Observable<MapModel> createNewMap(GameModel gameModel, ChosenFile chosenFile) {
    return mapsRepository.createLocalFileCopy(gameModel.getId(),
        new File(chosenFile.getOriginalPath())).map(file -> {
      return new MapModel();
    });
  }
}
      