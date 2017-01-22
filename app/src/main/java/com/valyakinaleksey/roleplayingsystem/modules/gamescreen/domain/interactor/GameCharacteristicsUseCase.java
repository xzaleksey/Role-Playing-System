package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERISTICS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;

public class GameCharacteristicsUseCase implements GameCharacteristicsInteractor {

  @Override public Observable<List<GameCharacteristicModel>> getCharacteristicsByGameModel(
      GameModel gameModel) {
    DatabaseReference reference = getDatabaseReference(gameModel);
    return FireBaseUtils.checkReferenceExists(reference).switchMap(aBoolean -> {
      if (!aBoolean) {
        return Observable.just(new ArrayList<>());
      } else {
        return RxFirebaseDatabase.getInstance().observeSingleValue(reference).map(dataSnapshot -> {
          List<GameCharacteristicModel> gameCharacteristicModels = new ArrayList<>();
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            gameCharacteristicModels.add(snapshot.getValue(GameCharacteristicModel.class));
          }
          return gameCharacteristicModels;
        });
      }
    });
  }

  @Override public Observable<Boolean> editGameCharacteristic(GameModel gameModel,
      GameCharacteristicModel gameCharacteristicModel, String fieldName, Object o) {
    return Observable.just(gameModel).switchMap(gameModel1 -> {
      DatabaseReference reference =
          getDatabaseReference(gameModel).child(gameCharacteristicModel.getId());
      reference.child(fieldName).setValue(o);
      return RxFirebaseDatabase.getInstance()
          .observeChildChanged(reference)
          .map(firebaseChildEvent -> true);
    });
  }

  @Override public Observable<String> createGameCharacteristic(GameModel gameModel,
      GameCharacteristicModel gameCharacteristicModel) {
    DatabaseReference reference = getDatabaseReference(gameModel);

    return RxFirebaseDatabase.getInstance()
        .observeSetValuePush(reference, gameCharacteristicModel)
        .doOnNext(s -> reference.child(s).child(ID).setValue(s));
  }

  @Override public Observable<Boolean> deleteCharacteristic(GameModel gameModel,
      GameCharacteristicModel characteristicModel) {
    DatabaseReference reference =
        getDatabaseReference(gameModel).child(characteristicModel.getId());
    reference.removeValue();
    return RxFirebaseDatabase.getInstance()
        .observeChildRemoved(reference)
        .map(firebaseChildEvent -> true);
  }

  private DatabaseReference getDatabaseReference(GameModel gameModel) {
    return FireBaseUtils.getTableReference(GAME_CHARACTERISTICS).child(gameModel.getId());
  }
}
      