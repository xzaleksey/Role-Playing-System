package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.communication.MasterViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter.MasterPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter.MasterPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.state.MasterViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class MasterModule {

    private final static String VIEW_STATE_FILE_NAME = MasterModule.class.getSimpleName();


    @Provides
    MasterViewState provideViewState(ViewStateStorage storage) {
        return new MasterViewState(storage);
    }

    @Provides
    @PerFragment
    MasterPresenter provideCommunicationBus(@Named(PRESENTER) MasterPresenter presenter, MasterViewState viewState) {
        return new MasterViewCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragment
    MasterPresenter providePresenter(UserGetInteractor userGetInteractor, ObserveGameInteractor observeGameInteractor, ObserveUsersInGameInteractor observeUsersInGameInteractor) {
        return new MasterPresenterImpl(userGetInteractor, observeGameInteractor, observeUsersInGameInteractor);
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
