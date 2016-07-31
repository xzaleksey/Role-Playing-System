package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.valyakinaleksey.roleplayingsystem.core.model.Interactor;

import rx.Observable;

/**
 * Implementation of {@link LoginInteractor}
 */
public class LoginUseCase extends Interactor implements LoginInteractor {
    private final Logger logger = LoggerManager.getLogger();
    private FirebaseAuth firebaseAuth;

    public LoginUseCase(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<Void> get(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authResultOnCompleteListener);
        });
    }
}
