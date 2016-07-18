package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public Observable<FirebaseUser> get(String email, String password) {
        return Observable.create(subscriber -> {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    logger.d(task.getResult().getUser().toString());
                    subscriber.onNext(task.getResult().getUser());
                } else {
                    logger.d(task.getException());
                }
            });
        });
    }
}
