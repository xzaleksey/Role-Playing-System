package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

public interface PasswordPresenter {
    void checkPassword(GameModel model);

    void validatePassword(String input, GameModel gameModel);
}
