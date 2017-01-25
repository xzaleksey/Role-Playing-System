package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERISTICS;

public class GameCharacteristicsUseCase extends BaseGameTEditInteractorImpl<GameCharacteristicModel> implements GameCharacteristicsInteractor {

  public GameCharacteristicsUseCase() {
    super(GameCharacteristicModel.class);
  }

  @Override public DatabaseReference getDatabaseReference(GameModel gameModel) {
    return FireBaseUtils.getTableReference(GAME_CHARACTERISTICS).child(gameModel.getId());
  }
}
      