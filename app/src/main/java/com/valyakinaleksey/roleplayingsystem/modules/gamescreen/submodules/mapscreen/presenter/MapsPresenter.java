package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;
import eu.davidea.flexibleadapter.items.IFlexible;

public interface MapsPresenter extends Presenter<MapsView>, MapHandler {
  void loadComplete();

  void uploadImage(ChosenImage chosenImage);

  void openImage(String path, String fileName);

  boolean onItemClick(IFlexible<?> item);
}