package com.valyakinaleksey.roleplayingsystem.modules.auth.data;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;

public class UserRepositoryImpl implements UserRepository {
    private ConcurrentHashMap<String, User> stringUserConcurrentHashMap;

    public UserRepositoryImpl() {
        stringUserConcurrentHashMap = new ConcurrentHashMap<>();
        RxFirebaseDatabase.getInstance()
                .observeValueEvent(FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.USERS))
                .compose(RxTransformers.applyIoSchedulers())
                .subscribe(dataSnapshot -> {
                    HashMap<String, User> stringUserHashMap = new HashMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User value = snapshot.getValue(User.class);
                        stringUserHashMap.put(snapshot.getKey(), value);
                    }
                    stringUserConcurrentHashMap = new ConcurrentHashMap<>(stringUserHashMap);
                }, Crashlytics::logException);
    }

    @Override
    public Observable<Map<String, User>> getUsersList() {
        return Observable.defer(() -> Observable.just(stringUserConcurrentHashMap));
    }

    @Override
    public Observable<User> getUserByUid(String uid) {
        return Observable.defer(() -> Observable.just(stringUserConcurrentHashMap.get(uid)));
    }

    @Override
    public Observable<List<User>> geUserByGameId(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.USERS_IN_GAME)
                .child(id);
        return RxFirebaseDatabase.getInstance().observeSingleValue(databaseReference)
                .map(dataSnapshot -> {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInGameModel value = snapshot.getValue(UserInGameModel.class);
                        users.add(stringUserConcurrentHashMap.get(value.getUid()));
                    }
                    return users;
                });
    }

    @Override
    public Observable<FirebaseChildEvent> observeUsersInGame(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.USERS_IN_GAME)
                .child(id);
        return RxFirebaseDatabase.getInstance()
                .observeChildEvent(databaseReference);
    }
}
      