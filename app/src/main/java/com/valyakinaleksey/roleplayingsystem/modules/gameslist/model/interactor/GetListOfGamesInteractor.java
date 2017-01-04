package com.valyakinaleksey.roleplayingsystem.modules.gameslist.model.interactor;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import rx.Observable;

/**
 * Use case: loginWithPassword list of games
 */
public interface GetListOfGamesInteractor {

    Observable<Void> get(String email, String password, OnCompleteListener<AuthResult> authResultOnCompleteListener);

}
