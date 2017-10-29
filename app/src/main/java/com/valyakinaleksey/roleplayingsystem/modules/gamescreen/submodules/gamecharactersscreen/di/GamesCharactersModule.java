package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.communication.GameCharactersViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.state.GamesCharactersViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class GamesCharactersModule extends BaseFragmentModule {

  private final static String VIEW_STATE_FILE_NAME = "GamesCharactersModule";

  public GamesCharactersModule(String fragmentId) {
    super(fragmentId);
  }

  @Provides GamesCharactersViewState provideViewState(
      @Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
    return new GamesCharactersViewState(storage);
  }

  @Provides @GameScope GamesCharactersPresenter provideCommunicationBus(
      @Named(PRESENTER) GamesCharactersPresenter presenter, GamesCharactersViewState viewState) {
    return new GameCharactersViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @GameScope GamesCharactersPresenter providePresenter(
      GameCharactersInteractor gameCharactersInteractor) {
    return new GamesCharactersPresenterImpl(gameCharactersInteractor);
  }

  @Named(VIEW_STATE_FILE_NAME) @Provides ViewStateStorage provideViewStateStorage(
      PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
    return new FileViewStateStorage(fullPath);
  }
}
