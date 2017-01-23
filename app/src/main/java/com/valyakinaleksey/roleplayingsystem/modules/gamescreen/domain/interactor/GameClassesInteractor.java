package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.util.List;
import rx.Observable;

public interface GameClassesInteractor {
  Observable<List<GameClassModel>> getClassesByGameModel(GameModel gameModel);

  Observable<Boolean> editGameClass(GameModel gameModel, GameClassModel gameClassModel,
      String fieldName, Object o);

  Observable<String> createGameClass(GameModel gameModel, GameClassModel gameClassModel);

  Observable<Boolean> deleteClass(GameModel gameModel, GameClassModel characteristicModel);
}
      