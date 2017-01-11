package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.state.ParentGameViewState;

import javax.inject.Inject;

@PerFragment
public class ParentViewCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<ParentGameModel, ParentView, ParentGamePresenter, ParentGameViewState>
        implements ParentGamePresenter, ParentView {

    @Override
    public void attachView(ParentView view) {
        super.attachView(view);
    }

    @Inject
    public ParentViewCommunicationBus(ParentGamePresenter presenter, ParentGameViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void navigate() {
        getNavigationResolver().resolveNavigation(ParentView::navigate);
    }

    @Override
    public void onGameJoined() {
        presenter.onGameJoined();
    }
}
