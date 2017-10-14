package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter.MasterLogPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import dagger.Component;

@Component(
    dependencies = ParentGameComponent.class,
    modules = MasterLogModule.class
)
@GameScope
public interface MasterLogFragmentComponent extends GlobalComponent, HasMasterLogPresenter {
  MasterLogPresenter communicationBus();

  void inject(MasterLogFragment masterLogFragment);
}

