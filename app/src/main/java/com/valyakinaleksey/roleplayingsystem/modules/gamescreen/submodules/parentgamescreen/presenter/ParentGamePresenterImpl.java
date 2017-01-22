package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.DeleteGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;

public class ParentGamePresenterImpl extends BasePresenter<ParentView, ParentGameModel>
    implements ParentGamePresenter {

  private CheckUserJoinedGameInteractor checkUserJoinedGameInteractor;
  private ObserveGameInteractor observeGameInteractor;
  private ParentPresenter parentPresenter;
  private DeleteGameInteractor deleteGameInteractor;

  public ParentGamePresenterImpl(CheckUserJoinedGameInteractor checkUserJoinedGameInteractor,
      ObserveGameInteractor observeGameInteractor, ParentPresenter parentPresenter,
      DeleteGameInteractor deleteGameInteractor) {
    this.checkUserJoinedGameInteractor = checkUserJoinedGameInteractor;
    this.observeGameInteractor = observeGameInteractor;
    this.parentPresenter = parentPresenter;
    this.deleteGameInteractor = deleteGameInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected ParentGameModel initNewViewModel(Bundle arguments) {
    final ParentGameModel parentGameModel = new ParentGameModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    parentGameModel.setGameModel(gameModel);
    parentGameModel.setMaster(
        gameModel.getMasterId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));
    return parentGameModel;
  }

  @Override public void restoreViewModel(ParentGameModel viewModel) {
    super.restoreViewModel(viewModel);
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    view.setData(viewModel);
    view.preFillModel(viewModel);
    view.showLoading();
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    GameModel gameModel = viewModel.getGameModel();
    compositeSubscription.add(
        checkUserJoinedGameInteractor.checkUserInGame(currentUserId, gameModel)
            .compose(RxTransformers.applySchedulers())
            .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
            .subscribe(aBoolean -> {
              if (viewModel.isMaster()) {
                viewModel.setNavigationTag(ParentGameModel.MASTER_SCREEN);
              } else {
                if (!aBoolean) {
                  Bundle bundle = new Bundle();
                  bundle.putParcelable(GameModel.KEY, viewModel.getGameModel());
                  parentPresenter.navigateToFragment(NavigationUtils.GAME_DESCRIPTION_FRAGMENT,
                      bundle);
                } else {
                  viewModel.setNavigationTag(ParentGameModel.USER_SCREEN);
                }
              }
              view.setData(viewModel);
              viewModel.setFirstNavigation(false);
              view.showContent();
              view.navigate();
            }));

    compositeSubscription.add(
        observeGameInteractor.observeGameModelChanged(gameModel).subscribe(gameModel1 -> {
          viewModel.setGameModel(gameModel1);
          view.preFillModel(viewModel);
        }, Crashlytics::logException));
    compositeSubscription.add(
        observeGameInteractor.observeGameModelRemoved(gameModel).subscribe(gameModel1 -> {
          parentPresenter.navigateBack();
        }, Crashlytics::logException));
    compositeSubscription.add(FireBaseUtils.getConnectionObservableWithTimeInterval()
        .compose(RxTransformers.applySchedulers())
        .subscribe(aBoolean -> {
          if (!aBoolean) {
            view.hideLoading();
            view.showError(BaseError.NO_CONNECTION);
          }
        }));
  }

  @Override public void deleteGame() {
    compositeSubscription.add(deleteGameInteractor.
        deleteGame(viewModel.getGameModel())
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(aBoolean -> {

        }, Crashlytics::logException));
  }
}
