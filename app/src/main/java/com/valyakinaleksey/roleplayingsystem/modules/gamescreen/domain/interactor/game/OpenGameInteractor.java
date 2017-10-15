package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import rx.Observable;

public interface OpenGameInteractor {
  Observable<Boolean> openGame(GameModel gameModel);
}
      