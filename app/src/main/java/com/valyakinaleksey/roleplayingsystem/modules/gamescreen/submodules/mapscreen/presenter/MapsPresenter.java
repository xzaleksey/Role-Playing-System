package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter;


import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;

public interface MapsPresenter extends Presenter<MapsView> {
    void loadComplete();

    void uploadImage(ChosenImage chosenImage);

    void changeMapVisibility(MapModel mapModel, boolean isChecked);

    void deleteMap(MapModel mapModel);

    void openImage(String path, String fileName);
}