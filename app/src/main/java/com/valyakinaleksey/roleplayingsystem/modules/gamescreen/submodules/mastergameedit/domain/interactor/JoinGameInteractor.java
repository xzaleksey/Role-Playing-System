package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import rx.Observable;

public interface JoinGameInteractor {
    Observable<Boolean> joinGame(GameModel gameModel);
}
      