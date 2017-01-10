package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.state.ParentGameViewState;

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
}
