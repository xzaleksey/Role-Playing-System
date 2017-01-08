package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.google.firebase.database.DataSnapshot;

import rx.Observable;

public interface GameRepository {

    Observable<FirebaseChildEvent> observeGameById(String id);
}
      