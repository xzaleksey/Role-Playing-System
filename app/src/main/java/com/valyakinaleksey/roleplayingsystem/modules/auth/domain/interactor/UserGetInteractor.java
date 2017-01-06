package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.util.Map;

import rx.Observable;

public interface UserGetInteractor {
    Observable<Map<String, User>> getUsersList();


    Observable<User> getUserByUid(String uid);
}
      