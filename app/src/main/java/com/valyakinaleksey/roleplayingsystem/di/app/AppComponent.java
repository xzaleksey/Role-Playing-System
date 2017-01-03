package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Base app component
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context context();

    PathManager pathManager();


    FirebaseAuth firebaseAuth();

    SharedPreferencesHelper sharedPrefencesPreferencesHelper();
}
