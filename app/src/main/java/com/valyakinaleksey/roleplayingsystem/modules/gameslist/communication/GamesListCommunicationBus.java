package com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state.GamesListViewState;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;

@PerFragmentScope
public class GamesListCommunicationBus extends
        SelfRestorableNavigationLceCommunicationBus<GamesListViewModel, GamesListView, GamesListPresenter, GamesListViewState>
        implements GamesListPresenter, GamesListView {

    @Override
    public void attachView(GamesListView view) {
        super.attachView(view);
    }

    @Inject
    public GamesListCommunicationBus(GamesListPresenter presenter, GamesListViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void createGame(GameModel gameModel) {
        presenter.createGame(gameModel);
    }

    @Override
    public void loadComplete() {
        presenter.loadComplete();
    }

    @Override
    public void onFabPressed() {
        presenter.onFabPressed();
    }

    @Override
    public void navigateToGameScreen(GameModel model) {
        presenter.navigateToGameScreen(model);
    }

    @Override
    public boolean onItemClick(IFlexible<?> item) {
        return presenter.onItemClick(item);
    }

    @Override
    public void onSearchQueryChanged(String queryText) {
        presenter.onSearchQueryChanged(queryText);
    }

    @Override
    public void checkPassword(GameModel model) {
        presenter.checkPassword(model);
    }

    @Override
    public void validatePassword(String s, GameModel gameModel) {
        presenter.validatePassword(s, gameModel);
    }

    @Override
    public void onGameCreated() {
        getNavigationResolver().resolveNavigation(GamesListView::onGameCreated);
    }

    @Override
    public void showCreateGameDialog() {
        getNavigationResolver().resolveNavigation(GamesListView::showCreateGameDialog);
    }

    @Override
    public void showPasswordDialog() {
        getNavigationResolver().resolveNavigation(GamesListView::showPasswordDialog);
    }

    @Override
    public void updateGamesCount() {
        getNavigationResolver().resolveNavigation(GamesListView::updateGamesCount);
    }

    @Override
    public void scrollToTop() {
        getNavigationResolver().resolveNavigation(GamesListView::scrollToTop);
    }
}
