package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.model.DefaultModelProvider;
import com.valyakinaleksey.roleplayingsystem.core.model.DefaultModelProviderImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FileMapsRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FileMapsRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FirebaseFirebaseMapRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FirebaseMapRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.CurrentUserRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.CurrentUserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.*;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Base repo module
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    GameRepository provideGameRepository() {
        return new GameRepositoryImpl();
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository() {
        return new UserRepositoryImpl();
    }

    @Provides
    @Singleton
    FileMapsRepository provideMapsRepository(PathManager pathManager) {
        return new FileMapsRepositoryImpl(pathManager);
    }

    @Provides
    @Singleton
    FirebaseMapRepository mapRepository() {
        return new FirebaseFirebaseMapRepositoryImpl();
    }


    @Provides
    @Singleton
    CharactersGameRepository charactersRepository(
            UserRepository userRepository, GameClassesRepository classesRepo,
            GameRacesRepository racesRepo, DefaultModelProvider defaultModelProvider) {
        return new CharactersRepositoryImpl(userRepository, classesRepo, racesRepo, defaultModelProvider);
    }

    @Provides
    @Singleton
    GameClassesRepository gameClassesRepository() {
        return new ClassesRepositoryImpl();
    }

    @Provides
    @Singleton
    GameRacesRepository gameRacesRepository() {
        return new GameRacesRepositoryImpl();
    }

    @Provides
    @Singleton
    DefaultModelProvider defaultModelProvider(Context context) {
        return new DefaultModelProviderImpl(context);
    }

    @Provides
    @Singleton
    CurrentUserRepository currentUserRepository() {
        return new CurrentUserRepositoryImpl();
    }
}
