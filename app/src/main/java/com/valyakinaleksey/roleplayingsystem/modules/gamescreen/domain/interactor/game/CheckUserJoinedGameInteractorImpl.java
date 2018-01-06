package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;

import rx.Observable;

public class CheckUserJoinedGameInteractorImpl implements CheckUserJoinedGameInteractor {
    @Override
    public Observable<Boolean> checkUserInGame(String userId, GameModel gameModel) {
        if (gameModel.getMasterId().equals(userId)) return Observable.just(true); // master of the game

      return RxFirebaseDatabase.observeSingleValueEvent(FirebaseDatabase.getInstance().getReference().child(FirebaseTable.USERS_IN_GAME).child(gameModel.getId()))
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
      