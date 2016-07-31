package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

import rx.Observable;

public class ResetPasswordUseCase implements ResetPasswordInteractor {

    private FirebaseAuth firebaseAuth;

    public ResetPasswordUseCase(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Observable<Void> resetPassword(String email, OnCompleteListener<Void> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(authResultOnCompleteListener);
        });
    }
}
      