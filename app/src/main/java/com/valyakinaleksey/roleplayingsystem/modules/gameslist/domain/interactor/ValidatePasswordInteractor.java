package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import rx.Observable;

public interface ValidatePasswordInteractor {
    Observable<Boolean> isPasswordValid(String inputPassword, String encryptedPassword);
}
      