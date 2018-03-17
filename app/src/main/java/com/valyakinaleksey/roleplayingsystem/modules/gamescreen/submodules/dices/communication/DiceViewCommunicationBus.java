package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.communication;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.state.DiceViewState;

public class DiceViewCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<DiceViewModel, DiceView, DicePresenter, DiceViewState>
    implements DicePresenter, DiceView {

  public DiceViewCommunicationBus(DicePresenter presenter, DiceViewState viewState) {
    super(presenter, viewState);
  }
}
