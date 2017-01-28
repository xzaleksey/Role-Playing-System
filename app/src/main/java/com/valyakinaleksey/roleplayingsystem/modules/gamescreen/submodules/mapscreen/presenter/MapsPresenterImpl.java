package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter;

import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_LOG;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_MAPS;

public class MapsPresenterImpl extends BasePresenter<MapsView, MapsViewModel>
    implements MapsPresenter {

  private MapsInteractor mapsInteractor;

  public MapsPresenterImpl(MapsInteractor mapsInteractor) {
    this.mapsInteractor = mapsInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected MapsViewModel initNewViewModel(Bundle arguments) {
    final MapsViewModel mapsViewModel = new MapsViewModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    mapsViewModel.setGameModel(gameModel);
    setDatabaseReference(mapsViewModel);
    return mapsViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    view.setData(viewModel);
    view.showContent();
    view.showLoading();
    compositeSubscription.add(FireBaseUtils.checkReferenceExists(
        FireBaseUtils.getTableReference(GAME_LOG).child(viewModel.getGameModel().getId()))
        .compose(RxTransformers.applySchedulers())
        .subscribe(exists -> {
          if (!exists) {
            view.hideLoading();
          }
        }, Crashlytics::logException));
  }

  @Override public void loadComplete() {
    view.hideLoading();
  }

  @Override public void restoreViewModel(MapsViewModel viewModel) {
    super.restoreViewModel(viewModel);
    setDatabaseReference(viewModel);
  }

  private void setDatabaseReference(MapsViewModel mapsViewModel) {
    mapsViewModel.setDatabaseReference(FirebaseDatabase.getInstance()
        .getReference()
        .child(GAME_MAPS)
        .child(mapsViewModel.getGameModel().getId())
        .orderByChild(FireBaseUtils.DATE_CREATE)
        .getRef());
  }
}
