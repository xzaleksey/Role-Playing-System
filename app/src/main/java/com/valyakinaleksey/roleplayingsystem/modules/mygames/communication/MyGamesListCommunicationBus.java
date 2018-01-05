package com.valyakinaleksey.roleplayingsystem.modules.mygames.communication;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.MyGamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.state.MyGamesListViewState;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;

@PerFragmentScope
public class MyGamesListCommunicationBus extends
        SelfRestorableNavigationLceCommunicationBus<MyGamesListViewViewModel, MyGamesListView, MyGamesListPresenter, MyGamesListViewState>
        implements MyGamesListPresenter, MyGamesListView {

    @Override
    public void attachView(MyGamesListView view) {
        super.attachView(view);
    }

    @Inject
    public MyGamesListCommunicationBus(MyGamesListPresenter presenter,
                                       MyGamesListViewState viewState) {
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
    public boolean onItemClicked(IFlexible<?> item) {
        return presenter.onItemClicked(item);
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
        getNavigationResolver().resolveNavigation(MyGamesListView::onGameCreated);
    }

    @Override
    public void showCreateGameDialog() {
        getNavigationResolver().resolveNavigation(MyGamesListView::showCreateGameDialog);
    }

    @Override
    public void showPasswordDialog() {
        getNavigationResolver().resolveNavigation(MyGamesListView::showPasswordDialog);
    }

    @Override
    public void updateList(List<IFlexible> iFlexibles) {
        getNavigationResolver().resolveNavigation((view) -> view.updateList(iFlexibles));
    }
}
