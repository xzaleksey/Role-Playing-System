package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;

public class ParentGameViewState extends StorageBackedNavigationLceViewStateImpl<ParentModel, ParentView> {

    public ParentGameViewState(ViewStateStorage storage) {
        super(storage);
    }
}
