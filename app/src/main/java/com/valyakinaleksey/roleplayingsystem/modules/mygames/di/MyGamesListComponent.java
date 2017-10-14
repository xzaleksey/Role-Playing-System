package com.valyakinaleksey.roleplayingsystem.modules.mygames.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.MyGamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import dagger.Component;

/**
 * Component for Auth screen
 */
@Component(
        dependencies = {ParentFragmentComponent.class},
        modules = MyGamesListModule.class
)
@PerFragmentScope
public interface MyGamesListComponent extends HasPresenter<MyGamesListPresenter> {

    void inject(MyGamesListFragment gamesListFragment);
}
