package com.valyakinaleksey.roleplayingsystem.di.modules;

import android.content.Context;
import android.preference.PreferenceManager;

import com.valyakinaleksey.data.storage.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.di.App;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @Inject
    SharedPreferencesHelper providesSharedPreferencesHelper(Context context) {
        return new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(context));
    }
}
      