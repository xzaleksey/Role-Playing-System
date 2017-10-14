package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;


public class GamesListViewState extends StorageBackedNavigationLceViewStateImpl<GamesListViewViewModel, GamesListView> {

    public GamesListViewState(ViewStateStorage storage) {
        super(storage);
    }
}
