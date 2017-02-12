package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;

import rx.Observable;

public interface ObserveUsersInGameInteractor {
    Observable<UserInGameModel> observeUserJoinedToGame(String id);
}
      