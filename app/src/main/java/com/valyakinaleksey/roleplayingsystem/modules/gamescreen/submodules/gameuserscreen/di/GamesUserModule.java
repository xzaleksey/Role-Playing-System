package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.communication.GameUserViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.presenter.GamesUserPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.presenter.GamesUserPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.state.GamesUserViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class GamesUserModule {

    private final static String VIEW_STATE_FILE_NAME = GamesUserModule.class.getSimpleName();


    @Provides
    GamesUserViewState provideViewState(ViewStateStorage storage) {
        return new GamesUserViewState(storage);
    }

    @Provides
    @PerFragmentScope
    GamesUserPresenter provideCommunicationBus(@Named(PRESENTER) GamesUserPresenter presenter, GamesUserViewState viewState) {
        return new GameUserViewCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragmentScope
    GamesUserPresenter providePresenter(UserGetInteractor userGetInteractor, ObserveGameInteractor observeGameInteractor, ObserveUsersInGameInteractor observeUsersInGameInteractor) {
        return new GamesUserPresenterImpl(userGetInteractor, observeGameInteractor, observeUsersInGameInteractor);
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
