package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.maps.MapsRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.maps.MapsRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Base repo module
 */
@Module public class RepositoryModule {

  @Provides @Singleton GameRepository provideGameRepository() {
    return new GameRepositoryImpl();
  }

  @Provides @Singleton UserRepository provideUserRepository() {
    return new UserRepositoryImpl();
  }

  @Provides @Singleton MapsRepository provideMapsRepository(PathManager pathManager) {
    return new MapsRepositoryImpl(pathManager);
  }
}
