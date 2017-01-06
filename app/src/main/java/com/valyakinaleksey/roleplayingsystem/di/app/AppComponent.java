package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

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

    CreateNewGameInteractor getCreateNewGameInteractor();

    UserGetInteractor getUserGetInteractor();

    SimpleCrypto getSimpleCrypto();

}
