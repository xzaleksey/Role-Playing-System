package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

public interface PasswordPresenter {
    void checkPassword(GameModel model);

    void validatePassword(Context context, String input, GameModel gameModel);
}
