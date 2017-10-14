package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import dagger.Component;

@Component(dependencies = ParentGameComponent.class, modules = GamesCharactersModule.class)
@GameScope public interface GamesCharactersFragmentComponent
    extends GlobalComponent, HasGameCharactersPresenter {
  GamesCharactersPresenter communicationBus();

  void inject(GamesCharactersFragment masterGameEditFragment);
}

