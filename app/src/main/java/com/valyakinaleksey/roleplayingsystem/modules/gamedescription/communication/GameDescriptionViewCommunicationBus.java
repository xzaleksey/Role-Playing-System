package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.communication;

import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter.GamesDescriptionPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.state.GamesDescriptionViewState;

import javax.inject.Inject;

@PerFragmentScope public class GameDescriptionViewCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<GamesDescriptionModel, GamesDescriptionView, GamesDescriptionPresenter, GamesDescriptionViewState>
    implements GamesDescriptionPresenter, GamesDescriptionView {

  @Override public void attachView(GamesDescriptionView view) {
    super.attachView(view);
  }

  @Inject public GameDescriptionViewCommunicationBus(GamesDescriptionPresenter presenter,
      GamesDescriptionViewState viewState) {
    super(presenter, viewState);
  }

  @Override public UserGetInteractor getValue() {
    return presenter.getValue();
  }

  @Override public void joinGame() {
    presenter.joinGame();
  }

  @Override public void updateView() {
    getNavigationResolver().resolveNavigation(GamesDescriptionView::updateView);
  }

  @Override public void updateView(InfoSection userInfosection, DataEvent dataEvent) {
    if (view != null) {
      view.updateView(userInfosection, dataEvent);
    }
  }
}
