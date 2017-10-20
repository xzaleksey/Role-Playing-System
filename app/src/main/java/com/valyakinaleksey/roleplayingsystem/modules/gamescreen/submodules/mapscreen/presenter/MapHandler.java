package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;

public interface MapHandler {
  void deleteMap(MapModel mapModel);

  void changeMapVisibility(MapModel mapModel, boolean isChecked);
}
