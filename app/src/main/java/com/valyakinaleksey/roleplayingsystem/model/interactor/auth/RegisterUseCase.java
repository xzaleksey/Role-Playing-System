package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import rx.Observable;

public class RegisterUseCase implements RegisterInteractor {
    private FirebaseAuth firebaseAuth;
    private final Logger logger = LoggerManager.getLogger();

    public RegisterUseCase(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }



    @Override
    public Observable<AuthResult> register(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(authResultOnCompleteListener);
        });


    }
}
      