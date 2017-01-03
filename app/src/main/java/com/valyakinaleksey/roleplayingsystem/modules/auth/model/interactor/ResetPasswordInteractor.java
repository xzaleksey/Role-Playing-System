package com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor;


import com.google.android.gms.tasks.OnCompleteListener;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface ResetPasswordInteractor {

    Observable<Void> resetPassword(String email, OnCompleteListener<Void> authResultOnCompleteListener);

}
