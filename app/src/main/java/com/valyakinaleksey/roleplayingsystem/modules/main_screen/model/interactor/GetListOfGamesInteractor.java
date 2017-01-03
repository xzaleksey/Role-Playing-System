package com.valyakinaleksey.roleplayingsystem.modules.main_screen.model.interactor;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import rx.Observable;

/**
 * Use case: get list of games
 */
public interface GetListOfGamesInteractor {

    Observable<Void> get(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener);

}
