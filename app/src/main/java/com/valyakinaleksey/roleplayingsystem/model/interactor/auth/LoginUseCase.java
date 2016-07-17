package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.core.model.Interactor;

import rx.Observable;

/**
 * Implementation of {@link LoginInteractor}
 */
public class LoginUseCase extends Interactor implements LoginInteractor {

    private FirebaseAuth firebaseAuth;

    public LoginUseCase(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<FirebaseUser> get(String email, String password) {
        return Observable.create(subscriber -> {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        subscriber.onNext(task.getResult().getUser());
                    }
                }
            });
        });
    }
}
