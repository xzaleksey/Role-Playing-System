package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.presenter.GamesUserPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserFragment;

import dagger.Component;

@Component(
        dependencies = AppComponent.class,
        modules = GamesUserModule.class
)
@PerFragmentScope
public interface GamesUserComponent extends HasPresenter<GamesUserPresenter> {

    void inject(GamesUserFragment gamesListFragment);
}
