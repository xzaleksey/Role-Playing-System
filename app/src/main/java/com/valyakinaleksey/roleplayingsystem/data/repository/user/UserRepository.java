package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface UserRepository {
    Observable<Map<String, User>> getUsersMap();

    Observable<User> observeUser(String uid);

    Observable<User> getUserByUid(String uid);

    Observable<List<User>> getUserByGameId(String id);


    Observable<RxFirebaseChildEvent<DataSnapshot>> observeUsersInGame(String id);

    Observable<User> getUserByUidFromServer(String uid);
}
      