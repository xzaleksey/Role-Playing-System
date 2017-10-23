package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.qualifiers.GameId;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameClassesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameClassesRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRacesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRacesRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.communication.ParentViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.state.ParentGameViewState;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class ParentGameModule {

  private String gameId;

  public ParentGameModule(String gameId) {
    this.gameId = gameId;
  }

  private final static String VIEW_STATE_FILE_NAME = ParentGameModule.class.getSimpleName();

  @Provides ParentGameViewState provideViewState(ViewStateStorage storage) {
    return new ParentGameViewState(storage);
  }

  @Provides @PerFragmentScope ParentGamePresenter provideCommunicationBus(
      @Named(PRESENTER) ParentGamePresenter presenter, ParentGameViewState viewState) {
    return new ParentViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @PerFragmentScope ParentGamePresenter providePresenter(
      ParentPresenter parentPresenter, GameInteractor gameInteractor) {
    return new ParentGamePresenterImpl(parentPresenter, gameInteractor);
  }

  @Provides ViewStateStorage provideViewStateStorage(PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
    return new FileViewStateStorage(fullPath);
  }

  @Provides @PerFragmentScope @GameId String getGameId() {
    return gameId;
  }

  @Provides @PerFragmentScope CharactersRepository charactersRepository() {
    return new CharactersRepositoryImpl(gameId);
  }

  @Provides @PerFragmentScope GameClassesRepository gameClassesRepository() {
    return new GameClassesRepositoryImpl(gameId);
  }

  @Provides @PerFragmentScope GameRacesRepository gameRacesRepository() {
    return new GameRacesRepositoryImpl(gameId);
  }

  @Provides @PerFragmentScope GameCharactersInteractor provideGameCharactersInteractor(
      GameClassesInteractor gameClassesInteractor, GameRacesInteractor gameRacesInteractor,
      UserRepository userRepository, CharactersRepository charactersRepository) {
    return new GameCharactersUseCase(gameClassesInteractor, gameRacesInteractor, userRepository,
        charactersRepository);
  }
}
