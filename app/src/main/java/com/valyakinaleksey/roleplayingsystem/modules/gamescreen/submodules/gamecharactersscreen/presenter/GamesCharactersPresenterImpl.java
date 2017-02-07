package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERISTICS;

public class GamesCharactersPresenterImpl
    extends BasePresenter<GamesCharactersView, GamesCharactersViewModel>
    implements GamesCharactersPresenter {
  private GameCharactersInteractor gameCharactersInteractor;

  public GamesCharactersPresenterImpl(GameCharactersInteractor gameCharactersInteractor) {
    this.gameCharactersInteractor = gameCharactersInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected GamesCharactersViewModel initNewViewModel(Bundle arguments) {
    final GamesCharactersViewModel gamesCharactersViewModel = new GamesCharactersViewModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    gamesCharactersViewModel.setGameModel(gameModel);

    return gamesCharactersViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    view.setData(viewModel);
    view.showLoading();
    compositeSubscription.add(FireBaseUtils.checkReferenceExistsAndNotEmpty(
        FireBaseUtils.getTableReference(GAME_CHARACTERISTICS)
            .child(viewModel.getGameModel().getId())).subscribe(aBoolean -> {
      if (!aBoolean) {
        view.hideLoading();
      }
    }, this::handleThrowable));

    compositeSubscription.add(
        gameCharactersInteractor.observeGameCharacters(viewModel.getGameModel())
            .subscribe(gameCharacterModelRxFirebaseChildEvent -> {

            }, this::handleThrowable));
  }
}
