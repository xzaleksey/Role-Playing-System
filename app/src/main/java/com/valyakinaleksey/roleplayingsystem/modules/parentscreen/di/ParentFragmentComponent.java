package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.ParentScope;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragment;
import dagger.Component;

@Component(
    dependencies = AppComponent.class,
    modules = ParentModule.class
)
@ParentScope
public interface ParentFragmentComponent extends HasParentPresenter, GlobalComponent {
  void inject(ParentFragment parentFragment);

  ParentPresenter communicationBus();
}

