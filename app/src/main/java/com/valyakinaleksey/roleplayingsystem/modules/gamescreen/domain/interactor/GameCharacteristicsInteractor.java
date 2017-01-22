package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.util.List;
import rx.Observable;

public interface GameCharacteristicsInteractor {
  Observable<List<GameCharacteristicModel>> getCharacteristicsByGameModel(GameModel gameModel);

  Observable<Boolean> editGameCharacteristic(GameModel gameModel,
      GameCharacteristicModel gameCharacteristicModel, String fieldName, Object o);

  Observable<String> createGameCharacteristic(GameModel gameModel,
      GameCharacteristicModel gameCharacteristicModel);

  Observable<Boolean> deleteCharacteristic(GameModel gameModel,
      GameCharacteristicModel characteristicModel);
}
      