package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import rx.Observable;

public interface JoinGameInteractor {
    Observable<Boolean> joinGame(GameModel gameModel);
}
      