package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;

public class MapsViewState extends StorageBackedNavigationLceViewStateImpl<MapsViewModel, MapsView> {

    public MapsViewState(ViewStateStorage storage) {
        super(storage);
    }
}
