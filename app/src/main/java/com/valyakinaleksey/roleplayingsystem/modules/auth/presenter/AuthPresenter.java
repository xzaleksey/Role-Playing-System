package com.valyakinaleksey.roleplayingsystem.modules.auth.presenter;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;

public interface AuthPresenter extends Presenter<AuthView> {
    void login(String email, String password);

    void register(String email, String password);

    void resetPassword(String email);

    void restoreData();

    void init(FragmentActivity activity);

    void googleAuth(FragmentActivity activity);

    void onActivityResult(FragmentActivity activity, int requestCode, int resultCode, Intent data);
}