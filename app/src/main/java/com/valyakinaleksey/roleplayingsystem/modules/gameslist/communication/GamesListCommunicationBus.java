package com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication;


import android.content.Context;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

import javax.inject.Inject;

@PerFragmentScope
public class GamesListCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<GamesListViewModel, GamesListView, GamesListPresenter, GamesListViewState>
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
    public void navigateToGameScreen(Context context, GameModel model) {
        presenter.navigateToGameScreen(context, model);
    }

    @Override
    public void checkPassword(Context context, GameModel model) {
        presenter.checkPassword(context, model);
    }

    @Override
    public void validatePassword(Context context, String s, GameModel gameModel) {
        presenter.validatePassword(context, s, gameModel);
    }

    @Override
    public UserGetInteractor getValue() {
        return presenter.getValue();
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
}
