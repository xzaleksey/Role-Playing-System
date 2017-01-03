package com.valyakinaleksey.roleplayingsystem.modules.auth.communication;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.auth.presenter.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.state.AuthViewState;

import javax.inject.Inject;

@PerFragment
public class AuthCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<AuthViewModel, AuthView, AuthPresenter, AuthViewState>
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
    public void initGoogleAuth(FragmentActivity activity) {
        presenter.initGoogleAuth(activity);
    }

    @Override
    public void googleAuth(FragmentActivity activity) {
        presenter.googleAuth(activity);
    }

    @Override
    public void onActivityResult(FragmentActivity activity, int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(activity, requestCode, resultCode, data);
    }

    @Override
    public void showSnackBarString(String s) {
        getNavigationResolver().resolveNavigation(authView -> authView.showSnackBarString(s));
    }
}
