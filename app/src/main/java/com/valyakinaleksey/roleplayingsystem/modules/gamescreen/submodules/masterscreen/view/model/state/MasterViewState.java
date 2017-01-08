package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.MasterView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.MasterModel;

public class MasterViewState extends StorageBackedNavigationLceViewStateImpl<MasterModel, MasterView> {

    public MasterViewState(ViewStateStorage storage) {
        super(storage);
    }
}
