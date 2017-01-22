package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import rx.Observable;

public interface CheckUserJoinedGameInteractor {
    Observable<Boolean> checkUserInGame(String userId, GameModel gameModel);
}
      