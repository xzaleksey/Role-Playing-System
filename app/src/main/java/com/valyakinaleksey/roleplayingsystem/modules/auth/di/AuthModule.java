package com.valyakinaleksey.roleplayingsystem.modules.auth.di;


import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.modules.auth.communication.AuthCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.LoginUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.RegisterInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.RegisterUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.ResetPasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.ResetPasswordUseCase;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.modules.auth.presenter.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.presenter.AuthPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.model.state.AuthViewState;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class AuthModule {

    private final static String VIEW_STATE_FILE_NAME = AuthModule.class.getSimpleName();

    @Provides
    @PerFragmentScope
    LoginInteractor provideLoginInteractor(FirebaseAuth firebaseAuth) {
        return new LoginUseCase(firebaseAuth);
    }

    @Provides
    @PerFragmentScope
    RegisterInteractor provideRegisterInteractor(FirebaseAuth firebaseAuth) {
        return new RegisterUseCase(firebaseAuth);
    }

    @Provides
    @PerFragmentScope
    ResetPasswordInteractor provideResetPasswordInteractor(FirebaseAuth firebaseAuth) {
        return new ResetPasswordUseCase(firebaseAuth);
    }

    @Provides
    AuthViewState provideViewState(ViewStateStorage storage) {
        return new AuthViewState(storage);
    }


    @Provides
    @Named(PRESENTER)
    @PerFragmentScope
    AuthPresenter provideAuthPresenter(LoginInteractor loginInteractor, RegisterInteractor registerInteractor, ResetPasswordInteractor resetPasswordInteractor, SharedPreferencesHelper sharedPreferencesHelper, Context context) {
        return new AuthPresenterImpl(loginInteractor, registerInteractor, resetPasswordInteractor, sharedPreferencesHelper, context);
    }

    @Provides
    @PerFragmentScope
    AuthPresenter provideCommunicationBus(@Named(PRESENTER) AuthPresenter presenter, AuthViewState viewState) {
        return new AuthCommunicationBus(presenter, viewState);
    }

    @Provides
    ViewStateStorage provideAuthStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
