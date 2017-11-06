package com.valyakinaleksey.roleplayingsystem.core.model;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;

public interface DefaultModelProvider {
    GameCharacterModel getDefaultGameCharacter();

    GameClassModel getDefaultClassModel();

    GameRaceModel getDefaultRaceModel();

    User getDefaultUser();
}
