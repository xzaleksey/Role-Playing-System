package com.valyakinaleksey.roleplayingsystem.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.view.data.CautionDialogData;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.AuthView;
import com.valyakinaleksey.roleplayingsystem.view.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.view.model.state.AuthViewState;

import javax.inject.Inject;

@PerFragment
public class AuthCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<AuthViewModel, AuthView.AuthError, AuthView, AuthPresenter, AuthViewState>
        implements AuthPresenter, AuthView {

    @Inject
    public AuthCommunicationBus(AuthPresenter presenter, AuthViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void showCautionDialog(CautionDialogData data) {
//        getNavigationResolver().resolveNavigation(WeatherView::showCautionDialog, data);
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
}
