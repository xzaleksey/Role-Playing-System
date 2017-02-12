package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class UserGetUseCase implements UserGetInteractor {

  private UserRepository userRepository;

  public UserGetUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override public Observable<Map<String, User>> getUsersList() {
    return userRepository.getUsersList();
  }

  @Override public Observable<User> getUserByUid(String uid) {
    return userRepository.getUserByUid(uid);
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

  @Override public Observable<List<User>> getUsersByGameId(String id) {
    return userRepository.getUserByGameId(id);
  }
}
      