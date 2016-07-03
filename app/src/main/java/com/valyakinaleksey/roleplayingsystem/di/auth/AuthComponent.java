package com.valyakinaleksey.roleplayingsystem.di.auth;


import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.view.fragment.LoginFragment;

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
