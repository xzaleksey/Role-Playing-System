package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.google.android.gms.tasks.OnCompleteListener;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface ResetPasswordInteractor {

    Observable<Void> resetPassword(String email, OnCompleteListener<Void> authResultOnCompleteListener);

}
