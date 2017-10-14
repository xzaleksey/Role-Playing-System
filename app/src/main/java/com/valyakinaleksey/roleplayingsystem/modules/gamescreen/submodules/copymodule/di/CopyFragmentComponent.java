package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di;

import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.CopyFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import dagger.Component;

@Component(
    dependencies = ParentGameComponent.class,
    modules = CopyModule.class
)
@GameScope
public interface CopyFragmentComponent extends GlobalComponent, HasCopyPresenter {
  void inject(CopyFragment copyFragment);
}

