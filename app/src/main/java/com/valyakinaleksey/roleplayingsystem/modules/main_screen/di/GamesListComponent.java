package com.valyakinaleksey.roleplayingsystem.modules.main_screen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.view.GamesListFragment;

import dagger.Component;

/**
 * Component for Auth screen
 */
@Component(
        dependencies = AppComponent.class,
        modules = GamesListModule.class
)
@PerFragment
public interface GamesListComponent extends HasPresenter<GamesListPresenter> {

    void inject(GamesListFragment gamesListFragment);
}
