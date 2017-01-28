package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.valyakinaleksey.roleplayingsystem.data.repository.maps.MapsRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.io.File;
import org.joda.time.DateTime;
import rx.Observable;

public class MapUseCase implements MapsInteractor {
  private MapsRepository mapsRepository;

  public MapUseCase(MapsRepository mapsRepository) {
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
    return mapsRepository.createLocalFileCopy(gameModel.getId(), push.getKey(),
        new File(chosenFile.getOriginalPath())).switchMap(file -> {
      String fileName = file.getName();
      MapModel mapModel = new MapModel(fileName);
      mapModel.setTempDateCreate(DateTime.now().getMillis());
      mapModel.setFileName(fileName);
      push.setValue(mapModel);
      return RxFirebaseDatabase.getInstance().observeSingleValue(push).map(dataSnapshot -> {
        push.child(FireBaseUtils.TEMP_DATE_CREATE).setValue(null);
        return mapModel;
      });
    });
  }
}
      