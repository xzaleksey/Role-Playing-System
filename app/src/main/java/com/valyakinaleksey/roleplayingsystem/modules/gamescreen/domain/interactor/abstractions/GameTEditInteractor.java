package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.abstractions;

import com.google.firebase.database.DatabaseReference;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.util.List;
import rx.Observable;

public interface GameTEditInteractor<T> {

  Observable<List<T>> getValuesByGameModel(GameModel gameModel);

  Observable<Boolean> editGameTmodel(GameModel gameModel, T model, String fieldName, Object o);

  Observable<String> createGameTModel(GameModel gameModel, T model);

  Observable<Boolean> deleteTModel(GameModel gameModel, T model);

  DatabaseReference getDatabaseReference(GameModel gameModel);

  Observable<RxFirebaseChildEvent<T>> observeChildren(GameModel gameModel);

  Observable<T> getSingleChild(GameModel gameModel, String id);
}
      