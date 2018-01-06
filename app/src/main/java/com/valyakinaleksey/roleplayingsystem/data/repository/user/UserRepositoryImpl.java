package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.FirebaseTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeChildEvent;

public class UserRepositoryImpl implements UserRepository {
    private ConcurrentHashMap<String, User> stringUserConcurrentHashMap;
    private Subscription subscription = Subscriptions.unsubscribed();

    public UserRepositoryImpl() {
        stringUserConcurrentHashMap = new ConcurrentHashMap<>();

        initSubscription();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                subscription.unsubscribe();
            } else {
                initSubscription();
            }
        });
    }

    private void initSubscription() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = observeChildEvent(
                FirebaseDatabase.getInstance().getReference().child(FirebaseTable.USERS)).compose(
                RxTransformers.applyIoSchedulers()).subscribe(firebaseChildEvent -> {
            User value = firebaseChildEvent.getValue().getValue(User.class);
            switch (firebaseChildEvent.getEventType()) {
                case ADDED:
                case CHANGED:
                    stringUserConcurrentHashMap.put(value.getUid(), value);
                    break;
                case REMOVED:
                    stringUserConcurrentHashMap.remove(value.getUid());
                    break;
            }
        }, throwable -> {
            Timber.d(throwable);
            Crashlytics.logException(throwable);
        });
    }

    @Override
    public Observable<Map<String, User>> getUsersMap() {
        return Observable.defer(() -> Observable.just(stringUserConcurrentHashMap)
                .concatMap(stringUserConcurrentHashMap1 -> {
                    if (stringUserConcurrentHashMap1.isEmpty()) {
                        return getUsersMapFromServer();
                    } else {
                        return Observable.just(stringUserConcurrentHashMap1);
                    }
                }));
    }

    @Override
    public Observable<User> observeUser(String uid) {
        return RxFirebaseDatabase.observeValueEvent(FireBaseUtils.getTableReference(FirebaseTable.USERS).child(uid), User.class);
    }

    public Observable<Map<String, User>> getUsersMapFromServer() {
        DatabaseReference child = FireBaseUtils.getTableReference(FirebaseTable.USERS);
        return FireBaseUtils.checkReferenceExistsAndNotEmpty(child).switchMap(aBoolean -> {
            if (aBoolean) {
                return com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeSingleValueEvent(child);
            } else {
                return Observable.just(null);
            }
        }).map(dataSnapshot -> {
            HashMap<String, User> stringUserHashMap = new HashMap<String, User>();
            if (dataSnapshot == null) {
                return stringUserHashMap;
            }
            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for (DataSnapshot snapshot : children) {
                User value = snapshot.getValue(User.class);
                stringUserHashMap.put(value.getUid(), value);
            }
            return stringUserHashMap;
        });
    }

    @Override
    public Observable<User> getUserByUid(String uid) {
        return Observable.defer(() -> Observable.just(stringUserConcurrentHashMap.get(uid)))
                .concatMap(user -> {
                    if (user == null) {
                        return getUserByUidFromServer(uid);
                    } else {
                        return Observable.just(user);
                    }
                });
    }

    @Override
    public Observable<List<User>> getUserByGameId(String id) {
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference().child(FirebaseTable.GAMES_IN_USERS).child(id);
        return com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeSingleValueEvent(databaseReference)
                .map(dataSnapshot -> {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        users.add(stringUserConcurrentHashMap.get(snapshot.getKey()));
                    }
                    return users;
                });
    }

    @Override
    public Observable<RxFirebaseChildEvent<DataSnapshot>> observeUsersInGame(String id) {
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference().child(FirebaseTable.USERS_IN_GAME).child(id);
        return com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeChildEvent(databaseReference);
    }

    @Override
    public Observable<User> getUserByUidFromServer(String uid) {
        DatabaseReference child = FireBaseUtils.getTableReference(FirebaseTable.USERS).child(uid);
        return FireBaseUtils.checkReferenceExistsAndNotEmpty(child).switchMap(aBoolean -> {
            if (aBoolean) {
                return com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeSingleValueEvent(child,
                        User.class);
            } else {
                return Observable.just(null);
            }
        });
    }
}
      