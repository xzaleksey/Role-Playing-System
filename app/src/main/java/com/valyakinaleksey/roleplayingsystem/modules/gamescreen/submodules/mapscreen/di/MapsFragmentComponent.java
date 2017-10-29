package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsFragment;
import dagger.Subcomponent;

@Subcomponent(modules = MapsModule.class) @GameScope public interface MapsFragmentComponent
    extends HasMapsPresenter {
  MapsPresenter communicationBus();

  void inject(MapsFragment masterGameEditFragment);
}

