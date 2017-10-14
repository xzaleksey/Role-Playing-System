package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import dagger.Component;

@Component(dependencies = ParentGameComponent.class, modules = MapsModule.class)
@GameScope public interface MapsFragmentComponent
    extends GlobalComponent, HasMapsPresenter {
  MapsPresenter communicationBus();

  void inject(MapsFragment masterGameEditFragment);
}

