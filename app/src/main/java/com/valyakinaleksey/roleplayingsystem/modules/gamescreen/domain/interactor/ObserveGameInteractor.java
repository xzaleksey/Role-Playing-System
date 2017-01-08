package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import rx.Observable;

public interface ObserveGameInteractor {
    Observable<GameModel> observeGameModel(GameModel gameModel);
}
      