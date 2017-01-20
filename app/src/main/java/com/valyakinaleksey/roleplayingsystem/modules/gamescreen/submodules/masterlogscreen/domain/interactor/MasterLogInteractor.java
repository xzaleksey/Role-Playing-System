package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import rx.Observable;

public interface MasterLogInteractor {
    Observable<MasterLogMessage> sendMessage(GameModel gameModel, MasterLogMessage masterLogMessage);

    Observable<Boolean> checkLogExists(GameModel gameModel);
}
      