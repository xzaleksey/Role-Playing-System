package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERISTICS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.getTableReference;

public class GameCharactersUseCase implements GameCharactersInteractor {

  @Override public Observable<RxFirebaseChildEvent<GameCharacterModel>> observeGameCharacters(
      GameModel gameModel) {
    return RxFirebaseDatabase.observeChildEvent(getTableReference(GAME_CHARACTERISTICS),
        GameCharacterModel.class);
  }


}
      