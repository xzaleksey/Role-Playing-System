package com.valyakinaleksey.roleplayingsystem.modules.auth.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.auth.presenter.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthFragment;

import dagger.Component;

/**
 * Component for Auth screen
 */
@Component(
        dependencies = AppComponent.class,
        modules = AuthModule.class
)
@PerFragmentScope
public interface AuthComponent extends HasPresenter<AuthPresenter> {

    void inject(AuthFragment authFragment);
}
