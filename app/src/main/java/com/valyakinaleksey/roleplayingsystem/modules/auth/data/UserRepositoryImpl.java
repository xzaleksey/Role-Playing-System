package com.valyakinaleksey.roleplayingsystem.modules.auth.data;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.util.HashMap;
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
}
      