package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.util.HashMap;
import org.joda.time.DateTime;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class MapUseCase implements MapsInteractor {

  @Override public Observable<MapModel> sendMessage(GameModel gameModel, MapModel mapModel) {
    DatabaseReference reference = getReference(gameModel);
    mapModel.setDateCreate(DateTime.now().getMillis());
    return RxFirebaseDatabase.getInstance().observeSetValuePush(reference, mapModel).map(s -> {
      DatabaseReference child = reference.child(s);
      HashMap<String, Object> map = new HashMap<>();
      map.put(TEMP_DATE_CREATE, null);
      map.put(ID, s);
      child.updateChildren(map);
      mapModel.setId(s);
      return mapModel;
    });
  }

  private DatabaseReference getReference(GameModel gameModel) {
    return FirebaseDatabase.getInstance()
        .getReference()
        .child(FireBaseUtils.GAME_MAPS)
        .child(gameModel.getId());
  }
}
      