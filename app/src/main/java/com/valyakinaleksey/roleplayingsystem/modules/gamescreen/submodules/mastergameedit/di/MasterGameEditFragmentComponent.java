package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter.MasterGameEditPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import dagger.Component;

@Component(dependencies = ParentGameComponent.class, modules = MasterGameEditModule.class)
@GameScope public interface MasterGameEditFragmentComponent
    extends GlobalComponent, HasGameEditPresenter {
  MasterGameEditPresenter communicationBus();

  void inject(MasterGameEditFragment masterGameEditFragment);
}

