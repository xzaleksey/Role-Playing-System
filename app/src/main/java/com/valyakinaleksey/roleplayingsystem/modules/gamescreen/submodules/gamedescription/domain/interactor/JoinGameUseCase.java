package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class JoinGameUseCase implements JoinGameInteractor {
    @Override
    public Observable<Boolean> joinGame(GameModel gameModel) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        UserInGameModel userInGameModel = new UserInGameModel(uid, FireBaseUtils.usernameFromEmail(currentUser.getEmail()));
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.USERS_IN_GAME) + gameModel.getId() + "/" + uid, userInGameModel.toMap());
        childUpdates.put(String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.GAMES_IN_USERS) + uid + "/" + gameModel.getId(), gameModel.toMap());
        databaseReference.updateChildren(childUpdates);
        return RxFirebaseDatabase.getInstance()
                .observeSingleValue(databaseReference.child(FireBaseUtils.USERS_IN_GAME)
                        .child(gameModel.getId()))
                .map(dataSnapshot -> true);
    }
}
      