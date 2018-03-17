package com.valyakinaleksey.roleplayingsystem.data.repository.game.dices;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;

import org.jetbrains.annotations.NotNull;

public class FirebaseDiceRepositoryImpl extends AbstractFirebaseGameRepositoryImpl<FirebaseDiceCollection> implements FirebaseDiceCollectionRepository {

    public FirebaseDiceRepositoryImpl() {
        super(FirebaseDiceCollection.class);
    }

    @NotNull
    @Override
    public DatabaseReference getDataBaseReference(@NonNull String gameId) {
        return FireBaseUtils.getTableReference(FirebaseTable.GAME_DICES).child(gameId);
    }

}
      