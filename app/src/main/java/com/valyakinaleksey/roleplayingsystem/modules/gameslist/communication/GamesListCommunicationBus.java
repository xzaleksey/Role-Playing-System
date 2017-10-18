package com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication;

import android.content.Context;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

import eu.davidea.flexibleadapter.items.IFlexible;
import javax.inject.Inject;

@PerFragmentScope public class GamesListCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<GamesListViewViewModel, GamesListView, GamesListPresenter, GamesListViewState>
    implements GamesListPresenter, GamesListView {

  @Override public void attachView(GamesListView view) {
    super.attachView(view);
  }

  @Inject
  public GamesListCommunicationBus(GamesListPresenter presenter, GamesListViewState viewState) {
    super(presenter, viewState);
  }

  @Override public void createGame(GameModel gameModel) {
    presenter.createGame(gameModel);
  }

  @Override public void loadComplete() {
    presenter.loadComplete();
  }

  @Override public void onFabPressed() {
    presenter.onFabPressed();
  }

  @Override public void navigateToGameScreen(Context context, GameModel model) {
    presenter.navigateToGameScreen(context, model);
  }

  @Override public boolean onItemClick(IFlexible<?> item) {
    return presenter.onItemClick(item);
  }

  @Override public void checkPassword(Context context, GameModel model) {
    presenter.checkPassword(context, model);
  }

  @Override public void validatePassword(Context context, String s, GameModel gameModel) {
    presenter.validatePassword(context, s, gameModel);
  }

  @Override public void onGameCreated() {
    getNavigationResolver().resolveNavigation(GamesListView::onGameCreated);
  }

  @Override public void showCreateGameDialog() {
    getNavigationResolver().resolveNavigation(GamesListView::showCreateGameDialog);
  }

  @Override public void showPasswordDialog() {
    getNavigationResolver().resolveNavigation(GamesListView::showPasswordDialog);
  }

  @Override public void updateGamesCount() {
    getNavigationResolver().resolveNavigation(GamesListView::updateGamesCount);
  }
}
