package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.GamesDescriptionModel;

public class GamesDescriptionViewState extends StorageBackedNavigationLceViewStateImpl<GamesDescriptionModel, GamesDescriptionView> {

    public GamesDescriptionViewState(ViewStateStorage storage) {
        super(storage);
    }
}
