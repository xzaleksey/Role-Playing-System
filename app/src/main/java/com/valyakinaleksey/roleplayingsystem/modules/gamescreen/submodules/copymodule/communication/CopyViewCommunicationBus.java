package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.communication;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.presenter.CopyPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.CopyView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.CopyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.state.CopyViewState;
import javax.inject.Inject;

public class CopyViewCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<CopyViewModel, CopyView, CopyPresenter, CopyViewState>
    implements CopyPresenter, CopyView {

  public CopyViewCommunicationBus(CopyPresenter presenter, CopyViewState viewState) {
    super(presenter, viewState);
  }
}
