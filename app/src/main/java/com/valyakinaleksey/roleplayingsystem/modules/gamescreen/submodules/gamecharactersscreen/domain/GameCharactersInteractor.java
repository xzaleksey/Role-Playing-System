package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameTEditInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import rx.Observable;

public interface GameCharactersInteractor extends GameTEditInteractor<GameCharacterModel> {

  Observable<AbstractGameCharacterListItem> getAbstractGameCharacterListItem(GameModel gameModel,
      GameCharacterModel gameCharacterModel);
}
      