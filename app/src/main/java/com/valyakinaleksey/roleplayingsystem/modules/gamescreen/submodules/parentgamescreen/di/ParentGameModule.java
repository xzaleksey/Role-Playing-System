package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.DeleteGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.communication.ParentViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.state.ParentGameViewState;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class ParentGameModule {

    private final static String VIEW_STATE_FILE_NAME = ParentGameModule.class.getSimpleName();


    @Provides
    ParentGameViewState provideViewState(ViewStateStorage storage) {
        return new ParentGameViewState(storage);
    }

    @Provides
    @PerFragmentScope
    ParentGamePresenter provideCommunicationBus(@Named(PRESENTER) ParentGamePresenter presenter, ParentGameViewState viewState) {
        return new ParentViewCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragmentScope
    ParentGamePresenter providePresenter(CheckUserJoinedGameInteractor checkUserJoinedGameInteractor, ParentPresenter parentPresenter, ObserveGameInteractor observeGameInteractor, DeleteGameInteractor deleteGameInteractor) {
        return new ParentGamePresenterImpl(checkUserJoinedGameInteractor,observeGameInteractor, parentPresenter, deleteGameInteractor);
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
