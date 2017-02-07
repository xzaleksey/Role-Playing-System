package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.communication.GameCharactersViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.state.GamesCharactersViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class GamesCharactersModule {

  private final static String VIEW_STATE_FILE_NAME = GamesCharactersModule.class.getSimpleName();

  @Provides GamesCharactersViewState provideViewState(ViewStateStorage storage) {
    return new GamesCharactersViewState(storage);
  }

  @Provides @GameScope GamesCharactersPresenter provideCommunicationBus(
      @Named(PRESENTER) GamesCharactersPresenter presenter, GamesCharactersViewState viewState) {
    return new GameCharactersViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @GameScope GamesCharactersPresenter providePresenter() {
    return new GamesCharactersPresenterImpl();
  }

  @Provides ViewStateStorage provideViewStateStorage(PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
    return new FileViewStateStorage(fullPath);
  }
}
