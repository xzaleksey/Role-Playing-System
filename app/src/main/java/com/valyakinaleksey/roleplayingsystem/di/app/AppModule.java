package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.BuildConfig;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.StringConstants;

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
    Firebase provideFirebase() {
        return new Firebase(String.format(StringConstants.FIREBASE_URL_FORMAT, BuildConfig.FIREBASE_NAME));
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
