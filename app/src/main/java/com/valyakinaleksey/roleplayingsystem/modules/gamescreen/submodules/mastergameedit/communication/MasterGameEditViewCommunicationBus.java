package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter.MasterGameEditPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.state.MasterGameEditViewState;

import javax.inject.Inject;

@PerFragmentScope
public class MasterGameEditViewCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<MasterGameEditModel, MasterGameEditView, MasterGameEditPresenter, MasterGameEditViewState>
        implements MasterGameEditPresenter, MasterGameEditView {

    @Override
    public void attachView(MasterGameEditView view) {
        super.attachView(view);
    }

    @Inject
    public MasterGameEditViewCommunicationBus(MasterGameEditPresenter presenter, MasterGameEditViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void updateView() {
        getNavigationResolver().resolveNavigation(MasterGameEditView::updateView);
    }
}
