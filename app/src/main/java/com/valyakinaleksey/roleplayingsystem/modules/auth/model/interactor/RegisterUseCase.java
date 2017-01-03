package com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import rx.Observable;

public class RegisterUseCase implements RegisterInteractor {
    private FirebaseAuth firebaseAuth;

    public RegisterUseCase(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }



    @Override
    public Observable<Void> register(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(authResultOnCompleteListener);
        });
    }
}
      