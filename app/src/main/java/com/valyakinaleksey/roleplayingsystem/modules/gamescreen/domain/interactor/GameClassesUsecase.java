package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.android.gms.games.Game;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERISTICS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CLASSES;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;

public class GameClassesUsecase implements GameClassesInteractor {

  @Override public DatabaseReference getDatabaseReference(GameModel gameModel) {
    return FireBaseUtils.getTableReference(GAME_CLASSES).child(gameModel.getId());
  }

  @Override public Observable<List<GameClassModel>> getValuesByGameModel(GameModel gameModel) {
    DatabaseReference reference = getDatabaseReference(gameModel);
    return FireBaseUtils.checkReferenceExists(reference).switchMap(aBoolean -> {
      if (!aBoolean) {
        return Observable.just(new ArrayList<>());
      } else {
        return RxFirebaseDatabase.getInstance().observeSingleValue(reference).map(dataSnapshot -> {
          List<GameClassModel> gameClassModels = new ArrayList<>();
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            gameClassModels.add(snapshot.getValue(GameClassModel.class));
          }
          return gameClassModels;
        });
      }
    });
  }

  @Override public Observable<Boolean> editGameTmodel(GameModel gameModel, GameClassModel model,
      String fieldName, Object o) {
    return Observable.just(gameModel).switchMap(gameModel1 -> {
      DatabaseReference reference = getDatabaseReference(gameModel).child(model.getId());
      reference.child(fieldName).setValue(o);
      return RxFirebaseDatabase.getInstance()
          .observeChildChanged(reference)
          .map(firebaseChildEvent -> true);
    });
  }

  @Override public Observable<String> createGameTModel(GameModel gameModel, GameClassModel model) {
    DatabaseReference reference = getDatabaseReference(gameModel);
    return RxFirebaseDatabase.getInstance()
        .observeSetValuePush(reference, model)
        .doOnNext(s -> reference.child(s).child(ID).setValue(s));
  }

  @Override public Observable<Boolean> deleteTModel(GameModel gameModel, GameClassModel model) {
    DatabaseReference reference = getDatabaseReference(gameModel).child(model.getId());
    reference.removeValue();
    return RxFirebaseDatabase.getInstance()
        .observeChildRemoved(reference)
        .map(firebaseChildEvent -> true);
  }
}
      