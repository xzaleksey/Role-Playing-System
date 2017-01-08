package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.GamesUserModel;

public class GamesUserViewState extends StorageBackedNavigationLceViewStateImpl<GamesUserModel, GamesUserView> {

    public GamesUserViewState(ViewStateStorage storage) {
        super(storage);
    }
}
