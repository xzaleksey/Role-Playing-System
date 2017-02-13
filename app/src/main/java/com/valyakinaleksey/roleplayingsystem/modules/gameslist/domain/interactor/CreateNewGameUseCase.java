package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;

import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES_IN_USERS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class CreateNewGameUseCase implements CreateNewGameInteractor {

  private SimpleCrypto simpleCrypto;
  private UserRepository userRepository;

  public CreateNewGameUseCase(SimpleCrypto simpleCrypto, UserRepository userRepository) {
    this.simpleCrypto = simpleCrypto;
    this.userRepository = userRepository;
  }

  @Override public Observable<String> createNewGame(GameModel gameModel) {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference tableReference = FireBaseUtils.getTableReference(GAMES);
    DatabaseReference push = tableReference.push();
    gameModel.setId(push.getKey());
    String currentUserId = FireBaseUtils.getCurrentUserId();
    return userRepository.getUserByUid(currentUserId).switchMap(user -> {
      gameModel.setTempDateCreate(DateTime.now().getMillis());
      gameModel.setMasterId(currentUserId);
      gameModel.setMasterName(user.getName());
      if (!TextUtils.isEmpty(gameModel.getPassword())) { //user set password, try to encrypt it
        try {
          String password = simpleCrypto.encrypt(gameModel.getPassword());
          gameModel.setPassword(password);
        } catch (Exception e) {
          Crashlytics.logException(e);
        }
      }
      return FireBaseUtils.setData(gameModel, push).doOnNext(dataSnapshot -> {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.GAMES_IN_USERS)
            + currentUserId
            + "/"
            + push.getKey(), gameModel.toMap());
        databaseReference.updateChildren(childUpdates);
      }).map(dataSnapshot -> {
        push.child(FireBaseUtils.TEMP_DATE_CREATE).setValue(null);
        return push.getKey();
      });
    });
  }
}
      