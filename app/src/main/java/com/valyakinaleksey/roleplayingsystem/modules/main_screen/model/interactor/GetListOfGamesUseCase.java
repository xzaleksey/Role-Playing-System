package com.valyakinaleksey.roleplayingsystem.modules.main_screen.model.interactor;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.model.Interactor;

import rx.Observable;

/**
 * Implementation of {@link GetListOfGamesInteractor}
 */
public class GetListOfGamesUseCase extends Interactor implements GetListOfGamesInteractor {
    private FirebaseAuth firebaseAuth;

    public GetListOfGamesUseCase(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<Void> get(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authResultOnCompleteListener);
        });
    }
}
