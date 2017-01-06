package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.util.Map;

import rx.Observable;

public class UserGetUseCase implements UserGetInteractor {

    private UserRepository userRepository;

    public UserGetUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Observable<Map<String, User>> getUsersList() {
        return userRepository.getUsersList();
    }

    @Override
    public Observable<User> getUserByUid(String uid) {
        return userRepository.getUserByUid(uid);
    }
}
      