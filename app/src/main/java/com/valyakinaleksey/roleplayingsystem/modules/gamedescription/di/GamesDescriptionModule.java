package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.communication.GameDescriptionViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter.GamesDescriptionPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter.GamesDescriptionPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.state.GamesDescriptionViewState;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class GamesDescriptionModule {

  private final static String VIEW_STATE_FILE_NAME = GamesDescriptionModule.class.getSimpleName();

  @Provides GamesDescriptionViewState provideViewState(ViewStateStorage storage) {
    return new GamesDescriptionViewState(storage);
  }

  @Provides @PerFragmentScope GamesDescriptionPresenter provideCommunicationBus(
      @Named(PRESENTER) GamesDescriptionPresenter presenter, GamesDescriptionViewState viewState) {
    return new GameDescriptionViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @PerFragmentScope GamesDescriptionPresenter providePresenter(
      UserGetInteractor userGetInteractor, JoinGameInteractor joinGameInteractor,
      ObserveGameInteractor observeGameInteractor,
      ObserveUsersInGameInteractor observeUsersInGameInteractor, ParentPresenter parentPresenter,
      GameCharacteristicsInteractor gameCharInteractor) {
    return new GamesDescriptionPresenterImpl(userGetInteractor, joinGameInteractor,
        observeGameInteractor, observeUsersInGameInteractor, parentPresenter, gameCharInteractor);
  }

  @Provides ViewStateStorage provideViewStateStorage(PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
    return new FileViewStateStorage(fullPath);
  }
}
