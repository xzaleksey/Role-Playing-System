package com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel;

public class MyGamesListViewState extends StorageBackedNavigationLceViewStateImpl<MyGamesListViewViewModel, MyGamesListView> {

    public MyGamesListViewState(ViewStateStorage storage) {
        super(storage);
    }
}
