package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter.MasterLogPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.state.MasterLogViewState;

import javax.inject.Inject;

@PerFragmentScope
public class MasterLogViewCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<MasterLogModel, MasterLogView, MasterLogPresenter, MasterLogViewState>
        implements MasterLogPresenter, MasterLogView {

    @Override
    public void attachView(MasterLogView view) {
        super.attachView(view);
    }

    @Inject
    public MasterLogViewCommunicationBus(MasterLogPresenter presenter, MasterLogViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void updateView() {
        getNavigationResolver().resolveNavigation(MasterLogView::updateView);
    }

    @Override
    public void loadComplete() {
        getPresenter().loadComplete();
    }

    @Override
    public void sendMessage(String s) {
        presenter.sendMessage(s);
    }
}
