package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di;

import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.communication.DiceViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor.DiceInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.state.DiceViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class DiceModule extends BaseFragmentModule {

    private final static String VIEW_STATE_FILE_NAME = "DicesModule";

    public DiceModule(String fragmentId) {
        super(fragmentId);
    }

    @Provides
    DiceViewState provideViewState(@Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
        return new DiceViewState(storage);
    }

    @Provides
    @GameScope
    DicePresenter provideCommunicationBus(@Named(PRESENTER) DicePresenter presenter, DiceViewState viewState) {
        return new DiceViewCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @GameScope
    DicePresenter providePresenter(DiceInteractor diceInteractor) {
        return new DicePresenterImpl(diceInteractor);
    }

    @Named(VIEW_STATE_FILE_NAME)
    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
        return new FileViewStateStorage(fullPath);
    }
}
