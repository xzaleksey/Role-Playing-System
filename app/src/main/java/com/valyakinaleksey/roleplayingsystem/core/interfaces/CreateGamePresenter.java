package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

public interface CreateGamePresenter extends PasswordPresenter {
  void createGame(GameModel gameModel);
}
