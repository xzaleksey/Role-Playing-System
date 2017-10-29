package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.CopyFragment;
import dagger.Subcomponent;

@Subcomponent(modules = CopyModule.class) @GameScope public interface CopyFragmentComponent
    extends HasCopyPresenter {
  void inject(CopyFragment copyFragment);
}

