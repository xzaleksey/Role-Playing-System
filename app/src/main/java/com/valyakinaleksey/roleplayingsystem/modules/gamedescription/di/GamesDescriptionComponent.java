package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter.GamesDescriptionPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;

import dagger.Component;

@Component(
        dependencies = ParentFragmentComponent.class,
        modules = GamesDescriptionModule.class
)
@PerFragmentScope
public interface GamesDescriptionComponent extends HasPresenter<GamesDescriptionPresenter> {

    void inject(GamesDescriptionFragment gamesListFragment);
}
