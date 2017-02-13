package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

public interface GameInteractor
    extends CheckUserJoinedGameInteractor, DeleteGameInteractor, FinishGameInteractor,
    ObserveGameInteractor, ObserveUsersInGameInteractor, LeaveGameInteractor {


}
      