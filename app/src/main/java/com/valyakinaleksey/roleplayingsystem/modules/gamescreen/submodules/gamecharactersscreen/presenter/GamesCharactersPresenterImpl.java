package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

public class GamesCharactersPresenterImpl
    extends BasePresenter<GamesCharactersView, GamesCharactersViewModel>
    implements GamesCharactersPresenter {

  public GamesCharactersPresenterImpl() {
  }

  @SuppressWarnings("unchecked") @Override
  protected GamesCharactersViewModel initNewViewModel(Bundle arguments) {
    final GamesCharactersViewModel gamesCharactersViewModel = new GamesCharactersViewModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    gamesCharactersViewModel.setToolbarTitle(gameModel.getName());
    gamesCharactersViewModel.setGameModel(gameModel);

    return gamesCharactersViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {

  }
}
