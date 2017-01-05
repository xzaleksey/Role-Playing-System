package com.valyakinaleksey.roleplayingsystem.modules.gameslist.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

import javax.inject.Inject;

@PerFragment
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
}