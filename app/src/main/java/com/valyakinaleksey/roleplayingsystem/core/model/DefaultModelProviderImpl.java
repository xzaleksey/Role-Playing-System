package com.valyakinaleksey.roleplayingsystem.core.model;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;

import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.UNDEFINED;

public class DefaultModelProviderImpl implements DefaultModelProvider {

    private Context context;

    public DefaultModelProviderImpl(Context context) {
        this.context = context;
    }

    @Override
    public GameCharacterModel getDefaultGameCharacter() {
        GameCharacterModel gameCharacterModel = new GameCharacterModel();
        gameCharacterModel.setId(UNDEFINED);
        gameCharacterModel.gameClassModel = getDefaultClassModel();
        gameCharacterModel.gameRaceModel = getDefaultRaceModel();
        String currentUserId = FireBaseUtils.getCurrentUserId();
        gameCharacterModel.setUid(currentUserId);
        gameCharacterModel.user = getDefaultUser();
        return gameCharacterModel;
    }

    @Override
    public GameClassModel getDefaultClassModel() {
        GameClassModel gameClassModel = new GameClassModel();
        gameClassModel.setName(context.getString(R.string.undefined));
        gameClassModel.setDescription(context.getString(R.string.undefined));
        gameClassModel.setId(UNDEFINED);
        return gameClassModel;
    }

    @Override
    public GameRaceModel getDefaultRaceModel() {
        GameRaceModel gameRaceModel = new GameRaceModel();
        gameRaceModel.setName(context.getString(R.string.undefined));
        gameRaceModel.setDescription(context.getString(R.string.undefined));
        gameRaceModel.setId(UNDEFINED);
        return gameRaceModel;
    }

    @Override
    public User getDefaultUser() {
        String currentUserId = FireBaseUtils.getCurrentUserId();
        return new User(currentUserId, UNDEFINED, UNDEFINED);
    }
}
