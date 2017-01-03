package com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import rx.Observable;

/**
 * Use case: login firebase
 */
public interface LoginInteractor {

    Observable<Void> loginWithPassword(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener);

    Observable<Void> loginWithGoogle(GoogleSignInAccount googleSignInAccount, OnCompleteListener<AuthResult> authResultOnCompleteListener);


}
