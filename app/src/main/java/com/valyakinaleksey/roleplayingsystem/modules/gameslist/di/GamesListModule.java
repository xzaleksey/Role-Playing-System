package com.valyakinaleksey.roleplayingsystem.modules.gameslist.di;


import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication.GamesListCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.model.interactor.GetListOfGamesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.model.interactor.GetListOfGamesUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class GamesListModule {

    private final static String VIEW_STATE_FILE_NAME = GamesListModule.class.getSimpleName();
    public static final String PRESENTER = "presenter";

    @Provides
    @PerFragment
    GetListOfGamesInteractor provideLoginInteractor(FirebaseAuth firebaseAuth) {
        return new GetListOfGamesUseCase(firebaseAuth);
    }

    @Provides
    GamesListViewState provideViewState(ViewStateStorage storage) {
        return new GamesListViewState(storage);
    }


    @Provides
    @Named(PRESENTER)
    @PerFragment
    GamesListPresenter provideWeatherPresenter(GetListOfGamesInteractor getListOfGamesInteractor, SharedPreferencesHelper sharedPreferencesHelper, Context context) {
        return new GamesListPresenterImpl(getListOfGamesInteractor, sharedPreferencesHelper, context);
    }

    @Provides
    @PerFragment
    GamesListPresenter provideCommunicationBus(@Named(PRESENTER) GamesListPresenter presenter, GamesListViewState viewState) {
        return new GamesListCommunicationBus(presenter, viewState);
    }

    @Provides
    ViewStateStorage provideAuthStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
