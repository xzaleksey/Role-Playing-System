package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface UserRepository {
    Observable<Map<String, User>> getUsersList();

    Observable<User> getUserByUid(String uid);

    Observable<List<User>> geUserByGameId(String id);


    Observable<FirebaseChildEvent> observeUsersInGame(String id);
}
      