package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.presenter.GamesUserPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.GamesUserModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.state.GamesUserViewState;

import javax.inject.Inject;

@PerFragment
public class GameUserViewCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<GamesUserModel, GamesUserView, GamesUserPresenter, GamesUserViewState>
        implements GamesUserPresenter, GamesUserView {

    @Override
    public void attachView(GamesUserView view) {
        super.attachView(view);
    }

    @Inject
    public GameUserViewCommunicationBus(GamesUserPresenter presenter, GamesUserViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public UserGetInteractor getValue() {
        return presenter.getValue();
    }


    @Override
    public void updateView() {
        getNavigationResolver().resolveNavigation(GamesUserView::updateView);
    }
}
