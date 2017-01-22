package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import rx.Observable;

public interface DeleteGameInteractor {
    Observable<Boolean> deleteGame(GameModel gameModel);
}
      