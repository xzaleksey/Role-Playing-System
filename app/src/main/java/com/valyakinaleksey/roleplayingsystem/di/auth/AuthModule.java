package com.valyakinaleksey.roleplayingsystem.di.auth;


import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.communication.AuthCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.model.interactor.auth.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.model.interactor.auth.LoginUseCase;
import com.valyakinaleksey.roleplayingsystem.model.repository.preferences.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.view.model.state.AuthViewState;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {

    private final static String VIEW_STATE_FILE_NAME = AuthModule.class.getSimpleName();

    @Provides
    @PerFragment
    LoginInteractor provideLoginInteractor(FirebaseAuth firebaseAuth) {
        return new LoginUseCase(firebaseAuth);
    }

    @Provides
    @PerFragment
    @Named("presenter")
    AuthPresenter provideAuthPresenter(LoginInteractor loginInteractor, SharedPreferencesHelper sharedPreferencesHelper) {
        return new AuthPresenterImpl(loginInteractor, sharedPreferencesHelper);
    }

    @Provides
    AuthViewState provideViewState(ViewStateStorage storage) {
        return new AuthViewState(storage);
    }

    @Provides
    @PerFragment
    AuthPresenter provideCommunicationBus(@Named("presenter") AuthPresenter presenter, AuthViewState viewState) {
        return new AuthCommunicationBus(presenter, viewState);
    }

    @Provides
    ViewStateStorage provideAuthStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
