package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import rx.Observable;

public class GameUsecase implements GameInteractor {
  private FinishGameInteractor finishGameInteractor;
  private DeleteGameInteractor deleteGameInteractor;
  private ObserveGameInteractor observeGameInteractor;
  private ObserveUsersInGameInteractor observeUsersInGameInteractor;
  private CheckUserJoinedGameInteractor checkUserJoinedGameInteractor;

  public GameUsecase(FinishGameInteractor finishGameInteractor,
      DeleteGameInteractor deleteGameInteractor, ObserveGameInteractor observeGameInteractor,
      ObserveUsersInGameInteractor observeUsersInGameInteractor,
      CheckUserJoinedGameInteractor checkUserJoinedGameInteractor) {
    this.finishGameInteractor = finishGameInteractor;
    this.deleteGameInteractor = deleteGameInteractor;
    this.observeGameInteractor = observeGameInteractor;
    this.observeUsersInGameInteractor = observeUsersInGameInteractor;
    this.checkUserJoinedGameInteractor = checkUserJoinedGameInteractor;
  }

  @Override public Observable<Boolean> checkUserInGame(String userId, GameModel gameModel) {
    return checkUserJoinedGameInteractor.checkUserInGame(userId, gameModel);
  }

  @Override public Observable<Boolean> deleteGame(GameModel gameModel) {
    return deleteGameInteractor.deleteGame(gameModel);
  }

  @Override public Observable<Void> finishGame(GameModel gameModel) {
    return finishGameInteractor.finishGame(gameModel);
  }

  @Override public Observable<GameModel> observeGameModelChanged(GameModel gameModel) {
    return observeGameInteractor.observeGameModelChanged(gameModel);
  }

  @Override public Observable<Boolean> observeGameModelRemoved(GameModel gameModel) {
    return observeGameInteractor.observeGameModelRemoved(gameModel);
  }

  @Override public Observable<UserInGameModel> observeUserJoinedToGame(String id) {
    return observeUsersInGameInteractor.observeUserJoinedToGame(id);
  }
}
      