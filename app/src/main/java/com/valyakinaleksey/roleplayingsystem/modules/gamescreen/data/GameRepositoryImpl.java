package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import rx.Observable;

public class GameRepositoryImpl implements GameRepository {

    @Override
    public Observable<FirebaseChildEvent> observeGameById(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAMES)
                .child(id);
        return RxFirebaseDatabase.getInstance()
                .observeChildChanged(databaseReference);
    }
}
      