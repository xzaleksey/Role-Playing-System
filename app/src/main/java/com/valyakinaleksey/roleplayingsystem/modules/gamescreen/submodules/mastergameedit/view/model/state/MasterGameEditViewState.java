package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;

public class MasterGameEditViewState extends StorageBackedNavigationLceViewStateImpl<MasterGameEditModel, MasterGameEditView> {

    public MasterGameEditViewState(ViewStateStorage storage) {
        super(storage);
    }
}
