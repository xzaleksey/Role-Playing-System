package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import rx.Observable;

public interface CurrentUserRepository {

    Observable<Boolean> updateDisplayNameAndEmail(String displayName, String email);

}
      