package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import rx.Observable;

public interface EditGameInteractor {

    Observable<Object> saveField(GameModel gameModel, String fieldName, Object o);


}
      