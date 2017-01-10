package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.ParentGameFragment;

import dagger.Component;

@Component(
        dependencies = AppComponent.class,
        modules = MasterModule.class
)
@PerFragment
public interface MasterComponent extends HasPresenter<ParentGamePresenter> {

    void inject(ParentGameFragment gamesListFragment);
}
