package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import rx.Observable;

public interface FinishGameInteractor {
  Observable<Void> finishGame(GameModel gameModel);
}
      