package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

/**
 * Alias name for Weather ViewState
 */
public class GamesListViewState extends StorageBackedNavigationLceViewStateImpl<GamesListViewModel, GamesListView> {

    public GamesListViewState(ViewStateStorage storage) {
        super(storage);
    }
}
