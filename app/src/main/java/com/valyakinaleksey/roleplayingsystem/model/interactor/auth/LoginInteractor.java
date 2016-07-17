package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.google.firebase.auth.FirebaseUser;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface LoginInteractor {

    Observable<FirebaseUser> get(String email, String password);

}
