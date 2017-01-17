package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.google.firebase.database.DataSnapshot;

import rx.Observable;

public interface GameRepository {

    Observable<FirebaseChildEvent> observeGameChangedById(String id);

    Observable<Boolean> observeGameRemovedById(String id);

    Observable<Boolean> deleteGame(String id);
}
      