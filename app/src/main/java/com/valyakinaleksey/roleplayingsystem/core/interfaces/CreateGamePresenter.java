package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

public interface CreateGamePresenter {
  void createGame(GameModel gameModel);

  void checkPassword(GameModel model);

  void validatePassword(Context context, String input, GameModel gameModel);
}
