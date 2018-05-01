package com.valyakinaleksey.roleplayingsystem.data.repository.game.diceresults;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;

import org.jetbrains.annotations.NotNull;

public class FirebaseDiceResultRepositoryImpl extends AbstractFirebaseGameRepositoryImpl<FirebaseDiceResultCollection> implements FirebaseDiceResultRepository {

    public FirebaseDiceResultRepositoryImpl() {
        super(FirebaseDiceResultCollection.class);
    }

    @NotNull
    @Override
    public DatabaseReference getDataBaseReference(@NonNull String gameId) {
        return FireBaseUtils.getTableReference(FirebaseTable.GAME_DICES_RESULTS).child(gameId);
    }
}
      