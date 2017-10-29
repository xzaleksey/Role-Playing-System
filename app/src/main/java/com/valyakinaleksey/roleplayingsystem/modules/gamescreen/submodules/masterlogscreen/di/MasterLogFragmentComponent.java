package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter.MasterLogPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import dagger.Subcomponent;

@Subcomponent(modules = MasterLogModule.class) @GameScope
public interface MasterLogFragmentComponent extends HasMasterLogPresenter {
  MasterLogPresenter communicationBus();

  void inject(MasterLogFragment masterLogFragment);
}

