package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.maps.MapsRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetUseCase;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.DeleteGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.DeleteGameUsecase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.EditGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.characterisitics.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.characterisitics.GameCharacteristicsUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesUsecase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.FinishGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.FinishGameUsecase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameUsecase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.LeaveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.LeaveGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesUsecase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveUsersInGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module public class InteractorModule {
  @Provides @Singleton ObserveGameInteractor provideObserveGameInteractor(
      GameRepository gameRepository) {
    return new ObserveGameUseCase(gameRepository);
  }

  @Provides @Singleton ObserveUsersInGameInteractor provideObserveUsersInGameInteractor(
      UserRepository userRepository) {
    return new ObserveUsersInGameUseCase(userRepository);
  }

  @Provides @Singleton JoinGameInteractor provideJoinGameInteractor() {
    return new JoinGameUseCase();
  }

  @Provides @Singleton UserGetInteractor provideUserGetInteractor(UserRepository userRepository) {
    return new UserGetUseCase(userRepository);
  }

  @Provides @Singleton ValidatePasswordInteractor provideValidatePasswordInteractor(
      SimpleCrypto simpleCrypto) {
    return new ValidatePasswordInteractorImpl(simpleCrypto);
  }

  @Provides @Singleton CheckUserJoinedGameInteractor provideCheckUserJoinedGameInteractor() {
    return new CheckUserJoinedGameInteractorImpl();
  }

  @Provides @Singleton CreateNewGameInteractor provideCreateNewGameInteractor(
      SimpleCrypto simpleCrypto, UserRepository userRepo) {
    return new CreateNewGameUseCase(simpleCrypto, userRepo);
  }

  @Provides @Singleton EditGameInteractor provideEditGameInteractor(SimpleCrypto simpleCrypto) {
    return new EditGameUseCase(simpleCrypto);
  }

  @Provides @Singleton DeleteGameInteractor provideDeleteGameInteractor(
      GameRepository gameRepository) {
    return new DeleteGameUsecase(gameRepository);
  }

  @Provides @Singleton MasterLogInteractor provideMasterLogInteractor() {
    return new MasterLogUseCase();
  }

  @Provides @Singleton GameCharacteristicsInteractor provideGameCharacteristicsInteractor() {
    return new GameCharacteristicsUseCase();
  }

  @Provides @Singleton GameClassesInteractor provideGameClassesInteractor() {
    return new GameClassesUsecase();
  }

  @Provides @Singleton GameRacesInteractor provideGameRacesInteractor() {
    return new GameRacesUsecase();
  }

  @Provides @Singleton MapsInteractor provideMapsInteractor(MapsRepository mapsRepository) {
    return new MapUseCase(mapsRepository);
  }

  @Provides @Singleton GameCharactersInteractor provideGameCharactersInteractor(
      GameClassesInteractor gameClassesInteractor, GameRacesInteractor gameRacesInteractor,
      UserRepository userRepository) {
    return new GameCharactersUseCase(gameClassesInteractor, gameRacesInteractor, userRepository);
  }

  @Provides @Singleton FinishGameInteractor provideFinishGameInteractor() {
    return new FinishGameUsecase();
  }

  @Provides @Singleton GameInteractor provideGameInteractor(
      FinishGameInteractor finishGameInteractor, DeleteGameInteractor deleteGameInteractor,
      ObserveGameInteractor observeGameInteractor,
      ObserveUsersInGameInteractor observeUsersInGameInteractor,
      CheckUserJoinedGameInteractor checkUserJoinedGameInteractor,
      LeaveGameInteractor leaveGameInteractor) {
    return new GameUsecase(finishGameInteractor, deleteGameInteractor, observeGameInteractor,
        observeUsersInGameInteractor, checkUserJoinedGameInteractor, leaveGameInteractor);
  }

  @Provides @Singleton LeaveGameInteractor provideLeaveGameInteractor() {
    return new LeaveGameUseCase();
  }
}
      