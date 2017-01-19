package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.communication.MasterLogViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter.MasterLogPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter.MasterLogPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.state.MasterLogViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class MasterLogModule {

    private final static String VIEW_STATE_FILE_NAME = MasterLogModule.class.getSimpleName();


    @Provides
    MasterLogViewState provideViewState(ViewStateStorage storage) {
        return new MasterLogViewState(storage);
    }

    @Provides
    @GameScope
    @AutoExpose(MasterLogFragment.class)
    MasterLogPresenter provideCommunicationBus(@Named(PRESENTER) MasterLogPresenter presenter, MasterLogViewState viewState) {
        return new MasterLogViewCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @GameScope
    MasterLogPresenter providePresenter(MasterLogInteractor masterLogInteractor) {
        return new MasterLogPresenterImpl(masterLogInteractor);
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
