package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter;

import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.firebase.MyFireBaseAdapter;
import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_MAPS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.VISIBLE;

public class MapsPresenterImpl extends BasePresenter<MapsView, MapsViewModel>
    implements MapsPresenter {

  private MapsInteractor mapsInteractor;
  private MyFireBaseAdapter.FirebaseArray firebaseArray;

  public MapsPresenterImpl(MapsInteractor mapsInteractor) {
    this.mapsInteractor = mapsInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected MapsViewModel initNewViewModel(Bundle arguments) {
    final MapsViewModel mapsViewModel = new MapsViewModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    mapsViewModel.setGameModel(gameModel);
    mapsViewModel.setMaster(
        gameModel.getMasterId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));
    return mapsViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    viewModel.setMapModels(new ArrayList<>());
    view.setData(viewModel);
    view.showContent();
    view.showLoading();
    compositeSubscription.add(
        FireBaseUtils.checkReferenceExistsAndNotEmpty(getDatabaseQuery(viewModel))
            .compose(RxTransformers.applySchedulers())
            .subscribe(exists -> {
              if (!exists) {
                view.hideLoading();
              }
            }, this::handleThrowable));
    initUpdates();
  }

  @Override public void loadComplete() {
    view.hideLoading();
  }

  @Override public void uploadImage(ChosenImage chosenImage) {
    mapsInteractor.createNewMap(viewModel.getGameModel(), chosenImage)
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(mapModel -> {
          Timber.d("success map create");
        }, throwable -> {
          if (view != null) {
            BaseError snack = BaseError.SNACK;
            snack.setValue(StringUtils.getStringById(R.string.error));
            view.showError(snack);
          }
        });
  }

  @Override public void changeMapVisibility(MapModel mapModel, boolean isChecked) {
    mapsInteractor.changeMapVisibility(mapModel, isChecked);
  }

  @Override public void deleteMap(MapModel mapModel) {
    mapsInteractor.deleteMap(mapModel)
        .compose(RxTransformers.applySchedulers())
        .subscribe(aVoid -> {

        }, this::handleThrowable);
  }

  private Query getDatabaseQuery(MapsViewModel mapsViewModel) {
    if (viewModel.isMaster()) {
      return FirebaseDatabase.getInstance()
          .getReference()
          .child(GAME_MAPS)
          .child(mapsViewModel.getGameModel().getId());
    } else {
      return FirebaseDatabase.getInstance()
          .getReference()
          .child(GAME_MAPS)
          .child(mapsViewModel.getGameModel().getId())
          .orderByChild(VISIBLE)
          .equalTo(true);
    }
  }

  private void initUpdates() {
    if (firebaseArray == null) {
      firebaseArray = new MyFireBaseAdapter.FirebaseArray(getDatabaseQuery(viewModel));
      List<MapModel> mapModels = viewModel.getMapModels();
      firebaseArray.setOnChangedListener(new MyFireBaseAdapter.FirebaseArray.OnChangedListener() {
        @Override public void onChanged(DataEvent.EventType type, int index, int oldIndex) {
          switch (type) {
            case ADDED:
              if (index == 0) {
                view.hideLoading();
              }
              mapModels.add(index, getFilledModel(index));
              view.notifyItemInserted(index);
              break;
            case CHANGED:
              mapModels.set(index, getFilledModel(index));
              view.notifyItemChanged(index);
              break;
            case REMOVED:
              mapModels.remove(index);
              view.notifyItemRemoved(index);
              break;
            case MOVED:
              mapModels.remove(oldIndex);
              mapModels.add(index, getFilledModel(index));
              view.notifyItemMoved(oldIndex, index);
              break;
            default:
              throw new IllegalStateException("Incomplete case statement");
          }
        }

        @Override public void onCancelled(DatabaseError databaseError) {
          handleThrowable(databaseError.toException());
        }
      });
    }
  }

  private MapModel getFilledModel(int index) {
    DataSnapshot item = firebaseArray.getItem(index);
    MapModel value = item.getValue(MapModel.class);
    value.setId(item.getKey());
    value.setGameId(viewModel.getGameModel().getId());
    return value;
  }

  @Override public void onDestroy() {
    if (firebaseArray != null) {
      firebaseArray.cleanup();
    }
    super.onDestroy();
  }
}
