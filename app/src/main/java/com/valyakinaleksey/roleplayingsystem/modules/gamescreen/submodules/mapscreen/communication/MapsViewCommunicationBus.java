package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.communication;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.state.MapsViewState;
import eu.davidea.flexibleadapter.items.IFlexible;
import javax.inject.Inject;

@PerFragmentScope public class MapsViewCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<MapsViewModel, MapsView, MapsPresenter, MapsViewState>
    implements MapsPresenter, MapsView {

  @Override public void attachView(MapsView view) {
    super.attachView(view);
  }

  @Inject public MapsViewCommunicationBus(MapsPresenter presenter, MapsViewState viewState) {
    super(presenter, viewState);
  }

  @Override public void loadComplete() {
    getPresenter().loadComplete();
  }

  @Override public void uploadImage(ChosenImage chosenImage) {
    presenter.uploadImage(chosenImage);
  }

  @Override public void changeMapVisibility(MapModel mapModel, boolean isChecked) {
    presenter.changeMapVisibility(mapModel, isChecked);
  }

  @Override public void deleteMap(MapModel mapModel) {
    presenter.deleteMap(mapModel);
  }

  @Override public void openImage(String path, String fileName) {
    presenter.openImage(path, fileName);
  }

  @Override public boolean onItemClick(IFlexible<?> item) {
   return presenter.onItemClick(item);
  }
}
