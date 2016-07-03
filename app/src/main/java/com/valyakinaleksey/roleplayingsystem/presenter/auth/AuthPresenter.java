package com.valyakinaleksey.roleplayingsystem.presenter.auth;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.AuthView;

public interface AuthPresenter extends Presenter<AuthView> {
    void login(String email, String password);

    void register(String email, String password);

    void resetPassword(String email);
}