package com.valyakinaleksey.roleplayingsystem.modules.gameslist.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication.GamesListCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class GamesListModule {

    private final static String VIEW_STATE_FILE_NAME = GamesListModule.class.getSimpleName();
    public static final String PRESENTER = "presenter";


    @Provides
    GamesListViewState provideViewState(ViewStateStorage storage) {
        return new GamesListViewState(storage);
    }

    @Provides
    @PerFragment
    GamesListPresenter provideCommunicationBus(@Named(PRESENTER) GamesListPresenter presenter, GamesListViewState viewState) {
        return new GamesListCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragment
    GamesListPresenter providePresenter(CreateNewGameInteractor createNewGameInteractor, UserGetInteractor userGetInteractor) {
        return new GamesListPresenterImpl(createNewGameInteractor, userGetInteractor);
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
