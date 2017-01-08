package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.BuildConfig;
import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.domain.interactor.JoinGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Base app module
 */
@Module
public class AppModule {

    private final RpsApp mApp;

    public AppModule(RpsApp app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApp;
    }


    @Provides
    @Singleton
    PathManager providePathManager(Context context) {
        return new PathManager(context);
    }


    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPreferencesHelper(SharedPreferences sharedPreferences) {
        return new SharedPreferencesHelper(sharedPreferences);
    }


    @Provides
    @Singleton
    CreateNewGameInteractor provideCreateNewGameInteractor(SimpleCrypto simpleCrypto) {
        return new CreateNewGameUseCase(simpleCrypto);
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository() {
        return new UserRepositoryImpl();
    }

    @Provides
    @Singleton
    UserGetInteractor provideUserGetInteractor(UserRepository userRepository) {
        return new UserGetUseCase(userRepository);
    }

    @Provides
    @Singleton
    ValidatePasswordInteractor provideValidatePasswordInteractor(SimpleCrypto simpleCrypto) {
        return new ValidatePasswordInteractorImpl(simpleCrypto);
    }

    @Provides
    @Singleton
    CheckUserJoinedGameInteractor provideCheckUserJoinedGameInteractor() {
        return new CheckUserJoinedGameInteractorImpl();
    }

    @Provides
    @Singleton
    SimpleCrypto provideSimpleCrypto() {
        return SimpleCrypto.getDefault(BuildConfig.masterPassword, "salt", new byte[16]);
    }

    @Provides
    @Singleton
    JoinGameInteractor provideJoinGameInteractor() {
        return new JoinGameUseCase();
    }

    @Provides
    @Singleton
    GameRepository provideGameRepository() {
        return new GameRepositoryImpl();
    }

    @Provides
    @Singleton
    ObserveGameInteractor provideObserveGameInteractor(GameRepository gameRepository) {
        return new ObserveGameUseCase(gameRepository);
    }

}
