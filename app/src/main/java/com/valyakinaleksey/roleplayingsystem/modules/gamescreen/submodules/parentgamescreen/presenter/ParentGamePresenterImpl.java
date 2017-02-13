package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter;

import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.utils.SerializableTuple;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;
import java.util.ArrayList;

import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_CHARACTERS_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_MAPS_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_MASTER_EDIT_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_MASTER_LOG_FRAGMENT;

public class ParentGamePresenterImpl extends BasePresenter<ParentView, ParentGameModel>
    implements ParentGamePresenter {

  private ParentPresenter parentPresenter;
  private GameInteractor gameInteractor;

  public ParentGamePresenterImpl(ParentPresenter parentPresenter, GameInteractor gameInteractor) {
    this.parentPresenter = parentPresenter;
    this.gameInteractor = gameInteractor;
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
    initSubscriptions(viewModel.getGameModel());
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    view.setData(viewModel);
    view.preFillModel(viewModel);
    view.showLoading();
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    GameModel gameModel = viewModel.getGameModel();
    compositeSubscription.add(gameInteractor.checkUserInGame(currentUserId, gameModel)
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(aBoolean -> {
          ArrayList<SerializableTuple<Integer, String>> fragmentsInfo =
              viewModel.getFragmentsInfo();

          if (aBoolean) {
            if (viewModel.isMaster()) {
              viewModel.setNavigationTag(ParentGameModel.MASTER_SCREEN);
              fragmentsInfo.add(new SerializableTuple<>(GAME_MASTER_EDIT_FRAGMENT,
                  RpsApp.app().getString(R.string.info)));
              fragmentsInfo.add(new SerializableTuple<>(GAME_MASTER_LOG_FRAGMENT,
                  RpsApp.app().getString(R.string.log)));
            } else { // user

            }
            fragmentsInfo.add(new SerializableTuple<>(GAME_CHARACTERS_FRAGMENT,
                RpsApp.app().getString(R.string.characters)));
            fragmentsInfo.add(
                new SerializableTuple<>(GAME_MAPS_FRAGMENT, RpsApp.app().getString(R.string.maps)));
          } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(GameModel.KEY, viewModel.getGameModel());
            parentPresenter.navigateToFragment(NavigationUtils.GAME_DESCRIPTION_FRAGMENT, bundle);
          }
          view.setData(viewModel);
          view.showContent();
          viewModel.setFirstNavigation(false);
        }));

    initSubscriptions(gameModel);
  }

  private void initSubscriptions(GameModel gameModel) {
    compositeSubscription.add(
        gameInteractor.observeGameModelChanged(gameModel).subscribe(gameModel1 -> {
          viewModel.setGameModel(gameModel1);
          view.preFillModel(viewModel);
        }, Crashlytics::logException));
    compositeSubscription.add(
        gameInteractor.observeGameModelRemoved(gameModel).subscribe(gameModel1 -> {
          parentPresenter.navigateBack();
        }, Crashlytics::logException));
  }

  @Override public void deleteGame() {
    compositeSubscription.add(gameInteractor.
        deleteGame(viewModel.getGameModel())
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(aBoolean -> {

        }, Crashlytics::logException));
  }

  @Override public void finishGame() {
    compositeSubscription.add(gameInteractor.
        finishGame(viewModel.getGameModel())
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(aVoid -> {
          parentPresenter.navigateBack();
        }, Crashlytics::logException));
  }

  @Override public void leaveGame() {
    compositeSubscription.add(gameInteractor.
        leaveGame(viewModel.getGameModel())
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(aVoid -> {
          parentPresenter.navigateBack();
        }, Crashlytics::logException));
  }
}
