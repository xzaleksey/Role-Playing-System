package com.valyakinaleksey.roleplayingsystem.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.kelvinapps.rxfirebase.RxHandler;
import com.kelvinapps.rxfirebase.exceptions.RxFirebaseDataException;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Executor;
import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;

import static com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeChildEvent;

public class FireBaseUtils {
  public static final int IN_PROGRESS = 0;
  public static final int ERROR = -1;
  public static final int SUCCESS = 1;

  //Fields
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String TEMP_DATE_CREATE = "tempDateCreate";
  public static final String DATE_CREATE = "dateCreate";
  public static final String FIELD_GAME_MASTER_NAME = "masterName";
  public static final String FIELD_NAME = "name";
  public static final String FIELD_DESCRIPTION = "description";
  public static final String TIMESTAMP = "timestamp";
  public static final String ALL = "all";
  public static final String STATUS = "status";
  public static final String VISIBLE = "visible";

  //Tables
  public static final String USERS = "users";
  public static final String USERS_IN_GAME = "users_in_game";
  public static final String GAMES_IN_USERS = "games_in_user";
  public static final String CHARACTERS_IN_USER = "characters_in_user";
  public static final String GAMES = "games";
  public static final String GAME_LOG = "game_log";
  public static final String GAME_MAPS = "game_maps";
  public static final String GAME_CHARACTERISTICS = "game_characteristics";
  public static final String GAME_CLASSES = "game_classes";
  public static final String GAME_RACES = "game_races";
  public static final String GAME_CHARACTERS = "game_characters";

  //formats
  public static final String FORMAT_SLASHES = "/%s/";

  //urls
  private static final String CHECK_CONNECTION_REFERENCE = ".info/connected";

  public static String usernameFromEmail(String email) {
    if (email.contains("@")) {
      return email.split("@")[0];
    } else {
      return email;
    }
  }

  public static DatabaseReference getDatabaseReference() {
    return FirebaseDatabase.getInstance().getReference();
  }

  public static Observable<Boolean> checkTableReferenceExists(String tableName) {
    return RxFirebaseDatabase.
        observeSingleValueEvent(getTableReference(tableName)).map(DataSnapshot::exists);
  }

  public static Observable<Boolean> checkReferenceExistsAndNotEmpty(Query query) {
    return RxFirebaseDatabase.
        observeSingleValueEvent(query)
        .map(dataSnapshot -> dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0);
  }

  public static DatabaseReference getTableReference(String name) {
    return getDatabaseReference().child(name);
  }

  public static Observable<Boolean> getConnectionObservable() {
    return RxFirebaseDatabase.
        observeValueEvent(getDatabaseReference().child(CHECK_CONNECTION_REFERENCE))
        .map(dataSnapshot -> dataSnapshot.getValue(Boolean.class));
  }

  public static Observable<Boolean> getConnectionObservableWithTimeInterval() {

    return getConnectionObservable().switchMap(
        aBoolean -> Observable.timer(aBoolean ? 0 : 5, TimeUnit.SECONDS).map(aLong -> aBoolean));
  }

  public static String getCurrentUserId() {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser == null) {
      return StringUtils.EMPTY_STRING;
    }
    return currentUser.getUid();
  }

  public static boolean isLoggedIn() {
    return !getCurrentUserId().isEmpty();
  }

  public static Observable<Void> deleteValue(DatabaseReference databaseReference) {
    return Observable.create(
        subscriber -> RxHandler.assignOnTask(subscriber, databaseReference.removeValue()));
  }

  public static Observable<Void> setData(Object o, DatabaseReference databaseReference) {
    return Observable.create(
        subscriber -> RxHandler.assignOnTask(subscriber, databaseReference.setValue(o)));
  }

  public static Observable<Void> startTransaction(DatabaseReference databaseReference,
      Executor<Transaction.Result, MutableData> transactionExecutor) {
    return Observable.create(subscriber -> {
      databaseReference.runTransaction(new Transaction.Handler() {
        @Override public Transaction.Result doTransaction(MutableData mutableData) {
          return transactionExecutor.execute(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
          if (!subscriber.isUnsubscribed()) {
            if (databaseError == null) {
              subscriber.onNext(null);
              subscriber.onCompleted();
            } else {
              subscriber.onError(databaseError.toException());
            }
          }
        }
      });
    });
  }

  public static Observable<RxFirebaseChildEvent<DataSnapshot>> observeChildAdded(
      final Query firebaseRef) {
    return RxFirebaseDatabase.observeChildEvent(firebaseRef)
        .filter(filterChildEvent(RxFirebaseChildEvent.EventType.ADDED));
  }

  public static Observable<RxFirebaseChildEvent<DataSnapshot>> observeChildChanged(
      final Query firebaseRef) {
    return observeChildEvent(firebaseRef).filter(
        filterChildEvent(RxFirebaseChildEvent.EventType.CHANGED));
  }

  public static Observable<RxFirebaseChildEvent<DataSnapshot>> observeChildRemoved(
      final Query firebaseRef) {
    return observeChildEvent(firebaseRef).filter(
        filterChildEvent(RxFirebaseChildEvent.EventType.REMOVED));
  }

  public static Observable<RxFirebaseChildEvent<DataSnapshot>> observeChildMoved(
      final Query firebaseRef) {
    return observeChildEvent(firebaseRef).filter(
        filterChildEvent(RxFirebaseChildEvent.EventType.MOVED));
  }

  private static <T> Func1<RxFirebaseChildEvent<T>, Boolean> filterChildEvent(
      final RxFirebaseChildEvent.EventType type) {
    return firebaseChildEvent -> firebaseChildEvent.getEventType() == type;
  }

  public static Observable<String> observeSetValuePush(final DatabaseReference firebaseRef,
      final Object object) {
    return Observable.create(subscriber -> {
      final DatabaseReference ref = firebaseRef.push();
      ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
          subscriber.onNext(ref.getKey());
          subscriber.onCompleted();
        }

        @Override public void onCancelled(DatabaseError error) {
          subscriber.onError(new RxFirebaseDataException(error));
        }
      });
      ref.setValue(object);
    });
  }
}
      