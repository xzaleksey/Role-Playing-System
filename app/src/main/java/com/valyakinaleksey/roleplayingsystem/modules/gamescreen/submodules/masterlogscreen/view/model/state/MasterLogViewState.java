package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;

public class MasterLogViewState extends StorageBackedNavigationLceViewStateImpl<MasterLogModel, MasterLogView> {

    public MasterLogViewState(ViewStateStorage storage) {
        super(storage);
    }
}
