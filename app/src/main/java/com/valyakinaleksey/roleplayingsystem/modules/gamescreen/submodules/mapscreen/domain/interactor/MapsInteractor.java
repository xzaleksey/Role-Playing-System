package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapHandler;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsListFlexibleModel;
import rx.Observable;

public interface MapsInteractor {
  Observable<MapModel> createNewMap(GameModel gameModel, ChosenFile chosenFile);

  void changeMapVisibility(MapModel mapModel, boolean isChecked);

  Observable<Void> deleteMap(MapModel mapModel);

  Observable<MapsListFlexibleModel> observeMaps(GameModel gameModel, MapHandler mapHandler);
}
      