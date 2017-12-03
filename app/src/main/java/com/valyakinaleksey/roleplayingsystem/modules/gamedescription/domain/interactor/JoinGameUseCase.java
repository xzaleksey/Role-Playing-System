package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameInUserModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

public class JoinGameUseCase implements JoinGameInteractor {
    @Override
    public Observable<Void> joinGame(GameModel gameModel) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        putUserInGame(gameModel, currentUser, uid, childUpdates);
        putGameInUsers(gameModel, uid, childUpdates);
        databaseReference.updateChildren(childUpdates);
        return RxFirebaseDatabase.observeSingleValueEvent(
                databaseReference.child(FireBaseUtils.USERS_IN_GAME).child(gameModel.getId()))
                .switchMap(dataSnapshot -> FireBaseUtils.startTransaction(
                        FireBaseUtils.getTableReference(FireBaseUtils.USERS).child(FireBaseUtils.getCurrentUserId()),
                        User.class,
                        user -> user.setCountOfGamesPlayed(user.getCountOfGamesPlayed() + 1)));
    }

    private void putGameInUsers(GameModel gameModel, String uid, Map<String, Object> childUpdates) {
        GameInUserModel gameInUserModel = new GameInUserModel();
        childUpdates.put(String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.GAMES_IN_USERS)
                + uid
                + "/"
                + gameModel.getId(), gameInUserModel.toMap());
    }

    private void putUserInGame(GameModel gameModel, FirebaseUser currentUser, String uid, Map<String, Object> childUpdates) {
        UserInGameModel userInGameModel =
                new UserInGameModel(uid, FireBaseUtils.usernameFromEmail(currentUser.getEmail()));
        childUpdates.put(String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.USERS_IN_GAME)
                + gameModel.getId()
                + "/"
                + uid, userInGameModel.toMap());
    }
}
      