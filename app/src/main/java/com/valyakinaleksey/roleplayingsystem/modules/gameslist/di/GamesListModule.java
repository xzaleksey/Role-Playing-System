package com.valyakinaleksey.roleplayingsystem.modules.gameslist.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication.GamesListCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class GamesListModule {

    private final static String VIEW_STATE_FILE_NAME = GamesListModule.class.getSimpleName();


    @Provides
    GamesListViewState provideViewState(ViewStateStorage storage) {
        return new GamesListViewState(storage);
    }

    @Provides
    @PerFragmentScope
    GamesListPresenter provideCommunicationBus(@Named(PRESENTER) GamesListPresenter presenter, GamesListViewState viewState) {
        return new GamesListCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragmentScope
    GamesListPresenter providePresenter(CreateNewGameInteractor createNewGameInteractor, UserGetInteractor userGetInteractor, ValidatePasswordInteractor validatePasswordInteractor, CheckUserJoinedGameInteractor checkUserJoinedGameInteractor, ParentPresenter parentPresenter) {
        return new GamesListPresenterImpl(createNewGameInteractor, userGetInteractor, validatePasswordInteractor, checkUserJoinedGameInteractor, parentPresenter);
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
