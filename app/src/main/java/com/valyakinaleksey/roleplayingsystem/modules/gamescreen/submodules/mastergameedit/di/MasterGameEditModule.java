package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.communication.MasterGameEditViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter.MasterGameEditPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter.MasterGameEditPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.state.MasterGameEditViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class MasterGameEditModule {

  private final static String VIEW_STATE_FILE_NAME = MasterGameEditModule.class.getSimpleName();

  @Provides MasterGameEditViewState provideViewState(ViewStateStorage storage) {
    return new MasterGameEditViewState(storage);
  }

  @Provides @GameScope @AutoExpose(MasterGameEditFragment.class)
  MasterGameEditPresenter provideCommunicationBus(
      @Named(PRESENTER) MasterGameEditPresenter presenter, MasterGameEditViewState viewState) {
    return new MasterGameEditViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @GameScope MasterGameEditPresenter providePresenter(
      EditGameInteractor editGameInteractor, GameCharacteristicsInteractor gameCharInteractor,
      GameClassesInteractor gameClassesInteractor,
      GameRacesInteractor gameRacesInteractorInteractor) {
    return new MasterGameEditPresenterImpl(editGameInteractor, gameCharInteractor,
        gameClassesInteractor, gameRacesInteractorInteractor);
  }

  @Provides ViewStateStorage provideViewStateStorage(PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
    return new FileViewStateStorage(fullPath);
  }
}
