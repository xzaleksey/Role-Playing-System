package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;

import com.valyakinaleksey.roleplayingsystem.core.model.DefaultModelProvider;
import com.valyakinaleksey.roleplayingsystem.core.model.DefaultModelProviderImpl;
import com.valyakinaleksey.roleplayingsystem.core.repository.ColorRepository;
import com.valyakinaleksey.roleplayingsystem.core.repository.ColorRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.repository.DrawableRepository;
import com.valyakinaleksey.roleplayingsystem.core.repository.DrawableRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.repository.FirebaseInfoRepository;
import com.valyakinaleksey.roleplayingsystem.core.repository.FirebaseInfoRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.repository.ResourcesProvider;
import com.valyakinaleksey.roleplayingsystem.core.repository.ResourcesProviderImpl;
import com.valyakinaleksey.roleplayingsystem.core.repository.StringRepository;
import com.valyakinaleksey.roleplayingsystem.core.repository.StringRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.log.LogRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.log.LogRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FileMapsRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FileMapsRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FirebaseFirebaseMapRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.map.FirebaseMapRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.AvatarUploadRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.AvatarUploadRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.CurrentUserRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.CurrentUserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersGameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.ClassesRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameClassesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRacesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRacesRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    CurrentUserRepository currentUserRepository(GameRepository gameRepository, AvatarUploadRepository avatarUploadRepository) {
        return new CurrentUserRepositoryImpl(gameRepository, avatarUploadRepository);
    }

    @Provides
    @Singleton
    AvatarUploadRepository avatarUploadRepository(PathManager pathManager) {
        return new AvatarUploadRepositoryImpl(pathManager);
    }

    @Provides
    @Singleton
    ColorRepository colorRepository(ResourcesProvider resourcesProvider) {
        return new ColorRepositoryImpl(resourcesProvider);
    }


    @Provides
    @Singleton
    StringRepository stringRepository(ResourcesProvider resourcesProvider) {
        return new StringRepositoryImpl(resourcesProvider);
    }

    @Provides
    @Singleton
    DrawableRepository drawableRepository(ResourcesProvider resourcesProvider) {
        return new DrawableRepositoryImpl(resourcesProvider);
    }

    @Provides
    @Singleton
    ResourcesProvider resourcesProvider(Context context) {
        return new ResourcesProviderImpl(context);
    }

    @Provides
    @Singleton
    FirebaseInfoRepository firebaseInfoRepository() {
        return new FirebaseInfoRepositoryImpl();
    }


    @Provides
    @Singleton
    LogRepository logRepository() {
        return new LogRepositoryImpl();
    }

}
