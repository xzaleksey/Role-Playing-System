package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import rx.Observable;

public interface GameCharactersInteractor {

  Observable<RxFirebaseChildEvent<GameCharacterModel>> observeGameCharacters(GameModel gameModel);
}
      