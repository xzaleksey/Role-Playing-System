package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.DeleteGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;

public interface UseCaseProvider {
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

  GameCharactersInteractor getGameCharactersInteractor();

  MapsInteractor getMapsInteractor();
}
      