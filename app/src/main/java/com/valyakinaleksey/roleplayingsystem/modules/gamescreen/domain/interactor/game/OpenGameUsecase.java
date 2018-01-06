package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;

import rx.Observable;

public class OpenGameUsecase implements OpenGameInteractor {
    @Override
    public Observable<Boolean> openGame(GameModel gameModel) {
        return FireBaseUtils.setData(false, FireBaseUtils.getTableReference(FirebaseTable.GAMES)
                .child(gameModel.getId())
                .child(GameModel.FIELD_FINISHED))
                .switchMap(aVoid -> FireBaseUtils.startTransaction(
                        FireBaseUtils.getTableReference(FirebaseTable.USERS).child(gameModel.getMasterId()),
                        User.class,
                        user -> user.setCountOfGamesMastered(user.getCountOfGamesMastered() - 1)))
                .map(aVoid -> true);
    }
}
