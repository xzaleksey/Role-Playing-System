package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import rx.Observable;

public interface CreateNewGameInteractor {
    Observable<String> createNewGame(GameModel gameModel);
}
      