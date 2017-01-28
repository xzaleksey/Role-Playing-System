package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.communication;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.state.MapsViewState;
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

  @Override public void updateView() {
    getNavigationResolver().resolveNavigation(MapsView::updateView);
  }

  @Override public void loadComplete() {
    getPresenter().loadComplete();
  }

  @Override public void uploadImage(ChosenImage chosenImage) {
    presenter.uploadImage(chosenImage);
  }
}
