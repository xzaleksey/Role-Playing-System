package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.ParentGameModel;

public class ParentGameViewState extends StorageBackedNavigationLceViewStateImpl<ParentGameModel, ParentView> {

    public ParentGameViewState(ViewStateStorage storage) {
        super(storage);
    }
}
