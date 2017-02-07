package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.presenter;

import android.os.Bundle;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.CopyView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.CopyViewModel;

public class CopyPresenterImpl
    extends BasePresenter<CopyView, CopyViewModel>
    implements CopyPresenter {

  public CopyPresenterImpl() {
  }

  @SuppressWarnings("unchecked") @Override
  protected CopyViewModel initNewViewModel(Bundle arguments) {
    final CopyViewModel copyViewModel = new CopyViewModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    copyViewModel.setToolbarTitle(gameModel.getName());
    copyViewModel.setGameModel(gameModel);

    return copyViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {

  }
}
