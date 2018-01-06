package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.abstractions.BaseGameTEditInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;

import static com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable.GAME_RACES;

public class GameRacesUsecase extends BaseGameTEditInteractorImpl<GameRaceModel>
    implements GameRacesInteractor {
  public GameRacesUsecase() {
    super(GameRaceModel.class);
  }

  @Override public DatabaseReference getDatabaseReference(GameModel gameModel) {
    return FireBaseUtils.getTableReference(GAME_RACES).child(gameModel.getId());
  }
}
      