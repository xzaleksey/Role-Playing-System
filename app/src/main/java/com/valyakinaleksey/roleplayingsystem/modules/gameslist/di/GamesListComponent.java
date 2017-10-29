package com.valyakinaleksey.roleplayingsystem.modules.gameslist.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment;
import dagger.Subcomponent;

@Subcomponent(modules = GamesListModule.class) @PerFragmentScope public interface GamesListComponent
    extends HasPresenter<GamesListPresenter> {

  void inject(GamesListFragment gamesListFragment);
}
