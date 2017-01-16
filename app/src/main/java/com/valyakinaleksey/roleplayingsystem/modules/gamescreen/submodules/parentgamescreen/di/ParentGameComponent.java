package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;

import dagger.Component;

@Component(
        dependencies = ParentFragmentComponent.class,
        modules = ParentGameModule.class
)
@PerFragmentScope
public interface ParentGameComponent extends HasPresenter<ParentGamePresenter>, GlobalComponent {

    void inject(ParentGameFragment gamesListFragment);
}
