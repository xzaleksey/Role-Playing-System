package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersFragment;
import dagger.Subcomponent;

@Subcomponent(modules = GamesCharactersModule.class) @GameScope
public interface GamesCharactersFragmentComponent extends HasGameCharactersPresenter {
  GamesCharactersPresenter communicationBus();

  void inject(GamesCharactersFragment masterGameEditFragment);
}

