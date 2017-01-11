package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.communication.ParentViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentGamePresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.state.ParentGameViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class ParentModule {

    private final static String VIEW_STATE_FILE_NAME = ParentModule.class.getSimpleName();


    @Provides
    ParentGameViewState provideViewState(ViewStateStorage storage) {
        return new ParentGameViewState(storage);
    }

    @Provides
    @PerFragment
    ParentPresenter provideCommunicationBus(@Named(PRESENTER) ParentPresenter presenter, ParentGameViewState viewState) {
        return new ParentViewCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragment
    ParentPresenter providePresenter() {
        return new ParentGamePresenterImpl();
    }


    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }
}
