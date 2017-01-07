package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import rx.Observable;

public class ValidatePasswordInteractorImpl implements ValidatePasswordInteractor {
    private SimpleCrypto simpleCrypto;

    public ValidatePasswordInteractorImpl(SimpleCrypto simpleCrypto) {
        this.simpleCrypto = simpleCrypto;
    }

    @Override
    public Observable<Boolean> isPasswordValid(String inputPassword, String encryptedPassword) {
        try {
            return Observable.just(inputPassword.equals(simpleCrypto.decrypt(encryptedPassword)));
        } catch (Exception e) {
            Crashlytics.logException(e);
            return Observable.just(false);
        }
    }
}
      