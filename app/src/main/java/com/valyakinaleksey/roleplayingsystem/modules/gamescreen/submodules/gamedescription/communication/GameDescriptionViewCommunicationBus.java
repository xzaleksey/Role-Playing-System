package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter.GamesDescriptionPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.state.GamesDescriptionViewState;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ChildGameListener;

import javax.inject.Inject;

@PerFragment
public class GameDescriptionViewCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<GamesDescriptionModel, GamesDescriptionView, GamesDescriptionPresenter, GamesDescriptionViewState>
        implements GamesDescriptionPresenter, GamesDescriptionView {

    @Override
    public void attachView(GamesDescriptionView view) {
        super.attachView(view);
    }

    @Inject
    public GameDescriptionViewCommunicationBus(GamesDescriptionPresenter presenter, GamesDescriptionViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public UserGetInteractor getValue() {
        return presenter.getValue();
    }

    @Override
    public void joinGame() {
        presenter.joinGame();
    }

    @Override
    public void updateView() {
        getNavigationResolver().resolveNavigation(GamesDescriptionView::updateView);
    }

    @Override
    public void setParentPresenter(ChildGameListener parentPresenter) {
        presenter.setParentPresenter(parentPresenter);
    }
}
