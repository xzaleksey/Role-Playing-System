package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.BuildConfig;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRepositoryImpl;
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
    UserRepository provideUserRepository() {
        return new UserRepositoryImpl();
    }


    @Provides
    @Singleton
    SimpleCrypto provideSimpleCrypto() {
        return SimpleCrypto.getDefault(BuildConfig.masterPassword, "salt", new byte[16]);
    }


    @Provides
    @Singleton
    GameRepository provideGameRepository() {
        return new GameRepositoryImpl();
    }

    @Provides
    SectionsAdapter provideSectionsAdapter() {
        return new SectionsAdapter();
    }
}
