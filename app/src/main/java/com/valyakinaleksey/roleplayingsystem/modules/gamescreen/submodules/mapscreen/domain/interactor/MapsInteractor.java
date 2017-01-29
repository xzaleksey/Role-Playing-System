package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import rx.Observable;

public interface MapsInteractor {
  Observable<MapModel> createNewMap(GameModel gameModel, ChosenFile chosenFile);

  void changeMapVisibility(MapModel mapModel, boolean isChecked);
}
      