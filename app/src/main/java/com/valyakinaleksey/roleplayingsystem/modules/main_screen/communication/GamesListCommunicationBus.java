package com.valyakinaleksey.roleplayingsystem.modules.main_screen.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.model.state.GamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.view.GamesListView;

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
    public void login(String email, String password) {
        getPresenter().login(email, password);
    }

    @Override
    public void restoreData() {
        getPresenter().restoreData();
    }
}
