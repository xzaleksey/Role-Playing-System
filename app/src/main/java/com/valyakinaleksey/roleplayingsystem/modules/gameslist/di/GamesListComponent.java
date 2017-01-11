package com.valyakinaleksey.roleplayingsystem.modules.gameslist.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;

import dagger.Component;

/**
 * Component for Auth screen
 */
@Component(
        dependencies = {ParentFragmentComponent.class},
        modules = GamesListModule.class
)
@PerFragment
public interface GamesListComponent extends HasPresenter<GamesListPresenter> {

    void inject(GamesListFragment gamesListFragment);
}
