package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter.MasterPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.MasterView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.MasterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.state.MasterViewState;

import javax.inject.Inject;

@PerFragment
public class MasterViewCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<MasterModel, MasterView, MasterPresenter, MasterViewState>
        implements MasterPresenter, MasterView {

    @Override
    public void attachView(MasterView view) {
        super.attachView(view);
    }

    @Inject
    public MasterViewCommunicationBus(MasterPresenter presenter, MasterViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public UserGetInteractor getValue() {
        return presenter.getValue();
    }

    @Override
    public void updateView() {
        getNavigationResolver().resolveNavigation(MasterView::updateView);
    }
}
