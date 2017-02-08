package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;

public abstract class BaseGameTEditInteractorImpl<T extends HasId>
    implements GameTEditInteractor<T> {
  private Class<T> tClass;

  public BaseGameTEditInteractorImpl(Class<T> tClass) {
    this.tClass = tClass;
  }

  @Override public Observable<List<T>> getValuesByGameModel(GameModel gameModel) {
    DatabaseReference reference = getDatabaseReference(gameModel);
    return FireBaseUtils.checkReferenceExistsAndNotEmpty(reference).switchMap(aBoolean -> {
      if (!aBoolean) {
        return Observable.just(new ArrayList<>());
      } else {
        return RxFirebaseDatabase.getInstance().observeSingleValue(reference).map(dataSnapshot -> {
          List<T> models = new ArrayList<>();
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            models.add(snapshot.getValue(tClass));
          }
          return models;
        });
      }
    });
  }

  @Override
  public Observable<Boolean> editGameTmodel(GameModel gameModel, T model, String fieldName,
      Object o) {
    return Observable.just(gameModel).switchMap(gameModel1 -> {
      DatabaseReference reference = getDatabaseReference(gameModel).child(model.getId());
      reference.child(fieldName).setValue(o);
      return RxFirebaseDatabase.getInstance()
          .observeChildChanged(reference)
          .map(firebaseChildEvent -> true);
    });
  }

  @Override public Observable<String> createGameTModel(GameModel gameModel, T model) {
    DatabaseReference reference = getDatabaseReference(gameModel);
    DatabaseReference push = reference.push();
    model.setId(push.getKey());
    push.setValue(model);
    return RxFirebaseDatabase.getInstance().observeSingleValue(push).map(DataSnapshot::getKey);
  }

  @Override public Observable<Boolean> deleteTModel(GameModel gameModel, T model) {
    DatabaseReference reference = getDatabaseReference(gameModel).child(model.getId());
    reference.removeValue();
    return RxFirebaseDatabase.getInstance()
        .observeChildRemoved(reference)
        .map(firebaseChildEvent -> true);
  }

  @Override public Observable<RxFirebaseChildEvent<T>> observeChildren(GameModel gameModel) {
    return com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeChildEvent(
        getDatabaseReference(gameModel), tClass);
  }

  @Override public Observable<T> getSingleChild(GameModel gameModel, String id) {
    return com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeSingleValueEvent(
        getDatabaseReference(gameModel).child(id), tClass);
  }
}
      