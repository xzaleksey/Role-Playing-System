package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;

public interface RepositoryProvider {
  GameRepository getGameRepository();
}
      