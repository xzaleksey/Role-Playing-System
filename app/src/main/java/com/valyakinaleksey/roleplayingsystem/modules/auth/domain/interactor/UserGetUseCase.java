package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import java.util.List;
import java.util.Map;
import rx.Observable;

public class UserGetUseCase implements UserGetInteractor {

  private UserRepository userRepository;

  public UserGetUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override public Observable<Map<String, User>> getUsersList() {
    return userRepository.getUsersMap();
  }

  @Override public Observable<User> getUserByUid(String uid) {
    return userRepository.getUserByUid(uid);
  }

  @Override public Observable<User> getUserByUidFromServer(String uid) {
    return userRepository.getUserByUidFromServer(uid);
  }

  @Override public Observable<List<User>> getUsersByGameId(String id) {
    return userRepository.getUserByGameId(id);
  }
}
      