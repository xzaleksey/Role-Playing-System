package com.valyakinaleksey.roleplayingsystem.data.repository.game;

import com.google.firebase.database.DataSnapshot;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import rx.Observable;

public interface GameRepository {

    Observable<RxFirebaseChildEvent<DataSnapshot>> observeGameChangedById(String id);

    Observable<Boolean> observeGameRemovedById(String id);

    Observable<Boolean> deleteGame(String id);
}
      