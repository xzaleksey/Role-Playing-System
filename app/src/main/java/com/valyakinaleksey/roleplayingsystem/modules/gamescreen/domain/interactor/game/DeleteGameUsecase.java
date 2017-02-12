package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import rx.Observable;

public class DeleteGameUsecase implements DeleteGameInteractor {

    private GameRepository gameRepository;

    public DeleteGameUsecase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Observable<Boolean> deleteGame(GameModel gameModel) {
        return gameRepository.deleteGame(gameModel.getId());
    }
}
      