package com.valyakinaleksey.roleplayingsystem.utils;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import rx.Observable;

public class FireBaseUtils {
  //Fields
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String TEMP_DATE_CREATE = "tempDateCreate";
  public static final String DATE_CREATE = "dateCreate";
  public static final String FIELD_GAME_MASTER_NAME = "masterName";
  public static final String TIMESTAMP = "timestamp";

  //Tables
  public static final String USERS = "users";
  public static final String USERS_IN_GAME = "users_in_game";
  public static final String GAMES_IN_USERS = "games_in_user";
  public static final String GAMES = "games";
  public static final String GAME_LOG = "game_log";

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

  public static Observable<Boolean> checkReferenceExists(DatabaseReference databaseReference) {
    return RxFirebaseDatabase.getInstance()
        .observeSingleValue(databaseReference)
        .map(DataSnapshot::exists);
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
}
      