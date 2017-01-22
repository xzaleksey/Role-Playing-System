package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import rx.Observable;

public class CheckUserJoinedGameInteractorImpl implements CheckUserJoinedGameInteractor {
    @Override
    public Observable<Boolean> checkUserInGame(String userId, GameModel gameModel) {
        if (gameModel.getMasterId().equals(userId)) return Observable.just(true); // master of the game

        return RxFirebaseDatabase.getInstance().observeSingleValue(FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.USERS_IN_GAME).child(gameModel.getId()))
                .map(dataSnapshot -> {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInGameModel gameUserModel = snapshot.getValue(UserInGameModel.class);
                        if (userId.equals(gameUserModel.getUid())) {
                            return true;
                        }
                    }
                    return false;
                });
    }
}
      