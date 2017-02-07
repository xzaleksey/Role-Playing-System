package com.valyakinaleksey.roleplayingsystem.utils;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.kelvinapps.rxfirebase.RxHandler;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;

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
    return RxFirebaseDatabase.getInstance()
        .observeSingleValue(getTableReference(tableName))
        .map(DataSnapshot::exists);
  }

  public static Observable<Boolean> checkReferenceExistsAndNotEmpty(Query query) {
    return RxFirebaseDatabase.getInstance()
        .observeSingleValue(query)
        .map(dataSnapshot -> dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0);
  }

  public static DatabaseReference getTableReference(String name) {
    return getDatabaseReference().child(name);
  }

  public static Observable<Boolean> getConnectionObservable() {

    return RxFirebaseDatabase.getInstance()
        .observeValueEvent(getDatabaseReference().child(CHECK_CONNECTION_REFERENCE))
        .map(dataSnapshot -> dataSnapshot.getValue(Boolean.class));
  }

  public static Observable<Boolean> getConnectionObservableWithTimeInterval() {

    return getConnectionObservable().switchMap(
        aBoolean -> Observable.timer(aBoolean ? 0 : 5, TimeUnit.SECONDS).map(aLong -> aBoolean));
  }

  public static Observable<Void> deleteValue(DatabaseReference databaseReference) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
        RxHandler.assignOnTask(subscriber, databaseReference.removeValue());
      }
    });
  }

  public static Observable<Void> setValue(Object o, DatabaseReference databaseReference) {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
        RxHandler.assignOnTask(subscriber, databaseReference.setValue(o));
      }
    });
  }
}
      