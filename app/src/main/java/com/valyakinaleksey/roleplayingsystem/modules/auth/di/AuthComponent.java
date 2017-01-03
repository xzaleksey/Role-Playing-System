package com.valyakinaleksey.roleplayingsystem.modules.auth.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.modules.auth.presenter.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.LoginFragment;

import dagger.Component;

/**
 * Component for Auth screen
 */
@Component(
        dependencies = AppComponent.class,
        modules = AuthModule.class
)
@PerFragment
public interface AuthComponent extends HasPresenter<AuthPresenter> {

    void inject(LoginFragment loginFragment);
}
