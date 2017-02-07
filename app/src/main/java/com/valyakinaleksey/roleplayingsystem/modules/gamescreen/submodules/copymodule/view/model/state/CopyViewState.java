package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.CopyView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.CopyViewModel;

public class CopyViewState extends StorageBackedNavigationLceViewStateImpl<CopyViewModel, CopyView> {

    public CopyViewState(ViewStateStorage storage) {
        super(storage);
    }
}
