package com.valyakinaleksey.roleplayingsystem.modules.mygames.communication;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.MyGamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.state.MyGamesListViewState;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.List;
import javax.inject.Inject;

@PerFragmentScope public class MyGamesListCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<MyGamesListViewViewModel, MyGamesListView, MyGamesListPresenter, MyGamesListViewState>
    implements MyGamesListPresenter, MyGamesListView {

  @Override public void attachView(MyGamesListView view) {
    super.attachView(view);
  }

  @Inject public MyGamesListCommunicationBus(MyGamesListPresenter presenter,
      MyGamesListViewState viewState) {
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

  @Override public boolean onItemClicked(IFlexible<?> item) {
    return presenter.onItemClicked(item);
  }

  @Override public void checkPassword(Context context, GameModel model) {
    presenter.checkPassword(context, model);
  }

  @Override public void validatePassword(Context context, String s, GameModel gameModel) {
    presenter.validatePassword(context, s, gameModel);
  }

  @Override public void onGameCreated() {
    getNavigationResolver().resolveNavigation(MyGamesListView::onGameCreated);
  }

  @Override public void showCreateGameDialog() {
    getNavigationResolver().resolveNavigation(MyGamesListView::showCreateGameDialog);
  }

  @Override public void showPasswordDialog() {
    getNavigationResolver().resolveNavigation(MyGamesListView::showPasswordDialog);
  }

  @Override public void updateList(List<IFlexible> iFlexibles) {
    getNavigationResolver().resolveNavigation((view) -> view.updateList(iFlexibles));
  }
}
