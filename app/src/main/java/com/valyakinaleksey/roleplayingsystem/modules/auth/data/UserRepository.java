package com.valyakinaleksey.roleplayingsystem.modules.auth.data;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface UserRepository {
    Observable<Map<String, User>> getUsersList();

    Observable<User> getUserByUid(String uid);

    Observable<List<User>> geUserByGameId(String id);
}
      