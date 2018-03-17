package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceFragment;

import dagger.Subcomponent;

@Subcomponent(modules = DiceModule.class) @GameScope public interface DiceFragmentComponent
    extends HasDicePresenter {
  void inject(DiceFragment copyFragment);
}

