package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.subjects.PublishSubject;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.FORMAT_SLASHES;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES_IN_USERS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERISTICS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_LOG;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.USERS_IN_GAME;

public class GameRepositoryImpl implements GameRepository {

  public GameRepositoryImpl() {
  }

  @Override public Observable<FirebaseChildEvent> observeGameChangedById(String id) {
    DatabaseReference databaseReference = getChildReference(id);
    return RxFirebaseDatabase.getInstance().observeChildChanged(databaseReference);
  }

  @Override public Observable<Boolean> observeGameRemovedById(String id) {
    return RxFirebaseDatabase.getInstance()
        .observeChildRemoved(getChildReference(id))
        .map(firebaseChildEvent -> true);
  }

  private DatabaseReference getChildReference(String id) {
    return FirebaseDatabase.getInstance().getReference().child(GAMES).child(id);
  }

  @Override public Observable<Boolean> deleteGame(String id) {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    PublishSubject<Boolean> booleanPublishSubject = PublishSubject.create();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childReference = getChildReference(id);
    DatabaseReference usersInGame = reference.child(USERS_IN_GAME).child(id);
    Map<String, Object> childUpdates = new HashMap<>();
    childReference.removeValue();
    RxFirebaseDatabase.getInstance().observeSingleValue(usersInGame).subscribe(dataSnapshot -> {
      for (DataSnapshot data : dataSnapshot.getChildren()) {
        UserInGameModel userInGameModel = data.getValue(UserInGameModel.class);
        childUpdates.put(String.format(FORMAT_SLASHES, GAMES_IN_USERS)
            + userInGameModel.getUid()
            + "/"
            + id, null);
      }
      childUpdates.put(String.format(FORMAT_SLASHES, USERS_IN_GAME) + id, null);
      childUpdates.put(String.format(FORMAT_SLASHES, GAME_CHARACTERISTICS) + id, null);
      childUpdates.put(String.format(FORMAT_SLASHES, GAME_LOG) + id, null);
      databaseReference.updateChildren(childUpdates);
      booleanPublishSubject.onNext(true);
    }, throwable -> {
      booleanPublishSubject.onError(throwable);
      Crashlytics.logException(throwable);
    });

    return booleanPublishSubject;
  }
}
      