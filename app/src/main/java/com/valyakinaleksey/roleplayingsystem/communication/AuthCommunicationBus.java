package com.valyakinaleksey.roleplayingsystem.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.AuthView;
import com.valyakinaleksey.roleplayingsystem.view.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.view.model.state.AuthViewState;

import javax.inject.Inject;

@PerFragment
public class AuthCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<AuthViewModel, AuthView.AuthError, AuthView, AuthPresenter, AuthViewState>
        implements AuthPresenter, AuthView {

    @Override
    public void attachView(AuthView view) {
        super.attachView(view);
    }

    @Inject
    public AuthCommunicationBus(AuthPresenter presenter, AuthViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void login(String email, String password) {
        getPresenter().login(email, password);
    }

    @Override
    public void register(String email, String password) {
        getPresenter().register(email, password);
    }

    @Override
    public void resetPassword(String email) {
        getPresenter().resetPassword(email);
    }

    @Override
    public void restoreData() {
        getPresenter().restoreData();
    }

    @Override
    public void showSnackBarString(String s) {
        viewState.showString(s, view);
    }
}