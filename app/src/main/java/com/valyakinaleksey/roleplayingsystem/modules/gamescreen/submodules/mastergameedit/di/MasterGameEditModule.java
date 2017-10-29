package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di;

import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.characterisitics.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
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

@Module public class MasterGameEditModule extends BaseFragmentModule {

  private final static String VIEW_STATE_FILE_NAME = "MasterGameEditModule";

  public MasterGameEditModule(String fragmentId) {
    super(fragmentId);
  }

  @Provides MasterGameEditViewState provideViewState(
      @Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
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

  @Named(VIEW_STATE_FILE_NAME) @Provides ViewStateStorage provideViewStateStorage(
      PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
    return new FileViewStateStorage(fullPath);
  }
}
