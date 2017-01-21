package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.util.HashMap;
import org.joda.time.DateTime;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class MasterLogUseCase implements MasterLogInteractor {

  @Override public Observable<MasterLogMessage> sendMessage(GameModel gameModel,
      MasterLogMessage masterLogMessage) {
    DatabaseReference reference = getReference(gameModel);
    masterLogMessage.setDateCreate(DateTime.now().getMillis());
    return RxFirebaseDatabase.getInstance()
        .observeSetValuePush(reference, masterLogMessage)
        .map(s -> {
          DatabaseReference child = reference.child(s);
          HashMap<String, Object> map = new HashMap<>();
          map.put(TEMP_DATE_CREATE, null);
          map.put(ID, s);
          child.updateChildren(map);
          masterLogMessage.setId(s);
          return masterLogMessage;
        });
  }

  private DatabaseReference getReference(GameModel gameModel) {
    return FirebaseDatabase.getInstance()
        .getReference()
        .child(FireBaseUtils.GAME_LOG)
        .child(gameModel.getId());
  }
}
      