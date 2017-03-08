package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

public class UserRepositoryImpl implements UserRepository {
  private ConcurrentHashMap<String, User> stringUserConcurrentHashMap;
  private Subscription subscription;

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
    subscription = RxFirebaseDatabase.getInstance()
        .observeChildEvent(FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.USERS))
        .compose(RxTransformers.applyIoSchedulers())
        .subscribe(firebaseChildEvent -> {
          User value = firebaseChildEvent.getDataSnapshot().getValue(User.class);
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

  @Override public Observable<Map<String, User>> getUsersMap() {
    return Observable.defer(() -> Observable.just(stringUserConcurrentHashMap)
        .concatMap(stringUserConcurrentHashMap1 -> {
          if (stringUserConcurrentHashMap1.isEmpty()) {
            return getUsersMapFromServer();
          } else {
            return Observable.just(stringUserConcurrentHashMap1);
          }
        }));
  }

  public Observable<Map<String, User>> getUsersMapFromServer() {
    DatabaseReference child = FireBaseUtils.getTableReference(FireBaseUtils.USERS);
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

  @Override public Observable<User> getUserByUid(String uid) {
    return Observable.defer(() -> Observable.just(stringUserConcurrentHashMap.get(uid)))
        .concatMap(user -> {
          if (user == null) {
            return getUserByUidFromServer(uid);
          } else {
            return Observable.just(user
            );
          }
        });
  }

  @Override public Observable<List<User>> getUserByGameId(String id) {
    DatabaseReference databaseReference =
        FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAMES_IN_USERS).child(id);
    return RxFirebaseDatabase.getInstance()
        .observeSingleValue(databaseReference)
        .map(dataSnapshot -> {
          List<User> users = new ArrayList<>();
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            UserInGameModel value = snapshot.getValue(UserInGameModel.class);
            users.add(stringUserConcurrentHashMap.get(value.getUid()));
          }
          return users;
        });
  }

  @Override public Observable<FirebaseChildEvent> observeUsersInGame(String id) {
    DatabaseReference databaseReference =
        FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.USERS_IN_GAME).child(id);
    return RxFirebaseDatabase.getInstance().observeChildEvent(databaseReference);
  }

  @Override public Observable<User> getUserByUidFromServer(String uid) {
    DatabaseReference child = FireBaseUtils.getTableReference(FireBaseUtils.USERS).child(uid);
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
      