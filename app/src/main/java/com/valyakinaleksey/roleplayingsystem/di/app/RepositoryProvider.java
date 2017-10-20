package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FirebaseMapRepository;

public interface RepositoryProvider {
  GameRepository getGameRepository();

  UserRepository getUserRepository();

  FirebaseMapRepository getFirebaseMapRepository();
}
      