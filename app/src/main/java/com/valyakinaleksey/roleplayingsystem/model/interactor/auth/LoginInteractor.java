package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface LoginInteractor {

    Observable<Void> get(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener);

}
