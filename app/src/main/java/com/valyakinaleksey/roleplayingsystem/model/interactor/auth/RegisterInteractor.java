package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.google.firebase.auth.AuthResult;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface RegisterInteractor {

    Observable<AuthResult> register(String email, String password);

}
