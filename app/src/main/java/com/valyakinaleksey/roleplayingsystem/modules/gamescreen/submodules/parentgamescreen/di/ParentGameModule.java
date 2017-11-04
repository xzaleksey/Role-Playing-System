package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
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
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameInteractor;
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

@Module
public class ParentGameModule extends BaseFragmentModule {

    private final static String VIEW_STATE_FILE_NAME = "ParentGameModule";
    private String gameId;

    public ParentGameModule(String gameId, String fragmentId) {
        super(fragmentId);
        this.gameId = gameId;
    }

    @Provides
    ParentGameViewState provideViewState(@Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
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
    ParentGamePresenter providePresenter(ParentPresenter parentPresenter, GameInteractor gameInteractor, CharactersRepository charactersRepository) {
        return new ParentGamePresenterImpl(parentPresenter, gameInteractor,charactersRepository);
    }

    @Named(VIEW_STATE_FILE_NAME)
    @Provides
    ViewStateStorage provideViewStateStorage(
            PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
        return new FileViewStateStorage(fullPath);
    }

    @Provides
    @PerFragmentScope
    @GameId
    String getGameId() {
        return gameId;
    }

    @Provides
    @PerFragmentScope
    CharactersRepository charactersRepository(
            UserRepository userRepository, GameClassesRepository classesRepo,
            GameRacesRepository racesRepo, Context context) {
        return new CharactersRepositoryImpl(gameId, userRepository, classesRepo, racesRepo, context);
    }

    @Provides
    @PerFragmentScope
    GameClassesRepository gameClassesRepository() {
        return new GameClassesRepositoryImpl(gameId);
    }

    @Provides
    @PerFragmentScope
    GameRacesRepository gameRacesRepository() {
        return new GameRacesRepositoryImpl(gameId);
    }

    @Provides
    @PerFragmentScope
    GameCharactersInteractor provideGameCharactersInteractor(
            CharactersRepository charactersRepository) {
        return new GameCharactersUseCase(charactersRepository);
    }
}
