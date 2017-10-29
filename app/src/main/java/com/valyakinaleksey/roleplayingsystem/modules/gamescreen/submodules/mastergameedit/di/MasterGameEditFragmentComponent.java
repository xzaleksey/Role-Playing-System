package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter.MasterGameEditPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import dagger.Subcomponent;

@Subcomponent(modules = MasterGameEditModule.class) @GameScope
public interface MasterGameEditFragmentComponent extends HasGameEditPresenter {
  MasterGameEditPresenter communicationBus();

  void inject(MasterGameEditFragment masterGameEditFragment);
}

