package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.firebase.client.AuthData;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface LoginInteractor {

    Observable<AuthData> get(String email, String password);

}
