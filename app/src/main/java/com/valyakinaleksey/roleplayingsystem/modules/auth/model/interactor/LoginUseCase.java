package com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
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
    public Observable<Void> loginWithPassword(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authResultOnCompleteListener);
        });
    }

    @Override
    public Observable<Void> loginWithGoogle(GoogleSignInAccount googleSignInAccount, OnCompleteListener<AuthResult> authResultOnCompleteListener) {
        return Observable.create(subscriber -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(authResultOnCompleteListener);
        });
    }
}
