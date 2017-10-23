package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.characterisitics.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.DeleteGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.FinishGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.LeaveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.MyGamesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.GameListInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;

public interface InteractorProvider {
  JoinGameInteractor getJoinGameInteractor();

  ObserveGameInteractor getObserveGameInteractor();

  ObserveUsersInGameInteractor getObserveUsersInGameInteractor();

  CreateNewGameInteractor getCreateNewGameInteractor();

  UserGetInteractor getUserGetInteractor();

  ValidatePasswordInteractor getValidatePasswordInteractor();

  CheckUserJoinedGameInteractor getCheckUserJoinedGameInteractor();

  EditGameInteractor getEditGameInteractor();

  DeleteGameInteractor getDeleteGameInteractor();

  MasterLogInteractor getMasterLogInteractor();

  GameCharacteristicsInteractor getGameCharacteristicsInteractor();

  GameClassesInteractor getGameClassesInteractor();

  GameRacesInteractor getGameRacesInteractor();

  MapsInteractor getMapsInteractor();

  FinishGameInteractor getFinishGameInteractor();

  GameInteractor getGameInteractor();

  LeaveGameInteractor getLeaveGameInteractor();

  MyGamesInteractor getMygamesInteractor();

  GameListInteractor getGameListInteractor();
}
      