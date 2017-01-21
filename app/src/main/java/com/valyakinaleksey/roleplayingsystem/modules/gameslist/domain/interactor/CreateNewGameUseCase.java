package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import java.util.HashMap;
import org.joda.time.DateTime;

import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class CreateNewGameUseCase implements CreateNewGameInteractor {

  private SimpleCrypto simpleCrypto;

  public CreateNewGameUseCase(SimpleCrypto simpleCrypto) {
    this.simpleCrypto = simpleCrypto;
  }

  @Override public Observable<String> createNewGame(GameModel gameModel) {
    return Observable.just(gameModel).switchMap(gameModel1 -> {
      DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
      DatabaseReference games = reference.child(FireBaseUtils.GAMES);
      gameModel.setTempDateCreate(DateTime.now().getMillis());
      FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
      if (currentUser != null) { //should always be true, check auth
        gameModel.setMasterId(currentUser.getUid());
        if (!TextUtils.isEmpty(gameModel.getPassword())) { //user set password, try to encrypt it
          try {
            String password = simpleCrypto.encrypt(gameModel.getPassword());
            gameModel.setPassword(password);
          } catch (Exception e) {
            Crashlytics.logException(e);
          }
        }
        return RxFirebaseDatabase.getInstance()
            .observeSingleValue(reference.child(FireBaseUtils.USERS).child(gameModel.getMasterId()))
            .doOnNext(dataSnapshot -> {
              User user = dataSnapshot.getValue(User.class);
              gameModel.setMasterName(user.getName());
            })
            .switchMap(dataSnapshot -> RxFirebaseDatabase.getInstance()
                .observeSetValuePush(games, gameModel)
                .doOnNext(s -> {
                  DatabaseReference child = games.child(s);
                  HashMap<String, Object> map = new HashMap<>();
                  map.put(TEMP_DATE_CREATE, null);
                  map.put(ID, s);
                  child.updateChildren(map);
                  gameModel.setId(s);
                }));
      } else {
        throw new IllegalStateException("current user can't be null");
      }
    });
  }
}
      