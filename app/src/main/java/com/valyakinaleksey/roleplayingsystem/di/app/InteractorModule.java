package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.modules.auth.data.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InteractorModule {
    @Provides
    @Singleton
    ObserveGameInteractor provideObserveGameInteractor(GameRepository gameRepository) {
        return new ObserveGameUseCase(gameRepository);
    }

    @Provides
    @Singleton
    ObserveUsersInGameInteractor provideObserveUsersInGameInteractor(UserRepository userRepository) {
        return new ObserveUsersInGameUseCase(userRepository);
    }

    @Provides
    @Singleton
    JoinGameInteractor provideJoinGameInteractor() {
        return new JoinGameUseCase();
    }


    @Provides
    @Singleton
    UserGetInteractor provideUserGetInteractor(UserRepository userRepository) {
        return new UserGetUseCase(userRepository);
    }

    @Provides
    @Singleton
    ValidatePasswordInteractor provideValidatePasswordInteractor(SimpleCrypto simpleCrypto) {
        return new ValidatePasswordInteractorImpl(simpleCrypto);
    }

    @Provides
    @Singleton
    CheckUserJoinedGameInteractor provideCheckUserJoinedGameInteractor() {
        return new CheckUserJoinedGameInteractorImpl();
    }

    @Provides
    @Singleton
    CreateNewGameInteractor provideCreateNewGameInteractor(SimpleCrypto simpleCrypto) {
        return new CreateNewGameUseCase(simpleCrypto);
    }

    @Provides
    @Singleton
    EditGameInteractor provideEditGameInteractor(SimpleCrypto simpleCrypto) {
        return new EditGameUseCase(simpleCrypto);
    }

}
      