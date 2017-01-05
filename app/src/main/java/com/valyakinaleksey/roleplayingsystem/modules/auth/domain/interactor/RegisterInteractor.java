package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface RegisterInteractor {

    Observable<Void> register(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener);

}
