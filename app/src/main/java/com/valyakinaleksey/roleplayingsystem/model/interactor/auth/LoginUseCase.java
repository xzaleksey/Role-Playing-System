package com.valyakinaleksey.roleplayingsystem.model.interactor.auth;


import com.firebase.client.AuthData;
import com.google.firebase.auth.FirebaseAuth;
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
    public Observable<AuthData> get(String email, String password) {

//        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                task.getResult().getUser().
//            }
//        });
        return null;
    }
}
