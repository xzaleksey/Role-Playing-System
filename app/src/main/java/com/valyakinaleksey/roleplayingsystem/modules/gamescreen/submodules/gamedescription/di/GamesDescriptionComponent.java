package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter.GamesDescriptionPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionFragment;

import dagger.Component;

@Component(
        dependencies = AppComponent.class,
        modules = GamesDescriptionModule.class
)
@PerFragment
public interface GamesDescriptionComponent extends HasPresenter<GamesDescriptionPresenter> {

    void inject(GamesDescriptionFragment gamesListFragment);
}