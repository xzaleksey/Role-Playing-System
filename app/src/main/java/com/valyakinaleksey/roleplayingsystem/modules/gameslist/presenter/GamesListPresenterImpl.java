package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;

import android.content.Context;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES;

@PerFragmentScope public class GamesListPresenterImpl
    extends BasePresenter<GamesListView, GamesListViewModel>
    implements GamesListPresenter, RestorablePresenter<GamesListViewModel> {

  private CreateNewGameInteractor createNewGameInteractor;
  private UserGetInteractor userGetInteractor;
  private ValidatePasswordInteractor validatePasswordInteractor;
  private CheckUserJoinedGameInteractor checkUserJoinedGameInteractor;
  private ParentPresenter parentPresenter;

  public GamesListPresenterImpl(CreateNewGameInteractor createNewGameInteractor,
      UserGetInteractor userGetInteractor, ValidatePasswordInteractor validatePasswordInteractor,
      CheckUserJoinedGameInteractor checkUserJoinedGameInteractor,
      ParentPresenter parentPresenter) {
    this.createNewGameInteractor = createNewGameInteractor;
    this.userGetInteractor = userGetInteractor;
    this.validatePasswordInteractor = validatePasswordInteractor;
    this.checkUserJoinedGameInteractor = checkUserJoinedGameInteractor;
    this.parentPresenter = parentPresenter;
  }

  @Override protected GamesListViewModel initNewViewModel(Bundle arguments) {
    GamesListViewModel gamesListViewModel = new GamesListViewModel();
    gamesListViewModel.setToolbarTitle(RpsApp.app().getString(R.string.list_of_games));
    setDatabaseReference(gamesListViewModel);
    return gamesListViewModel;
  }

  @Override public void restoreViewModel(GamesListViewModel viewModel) {
    super.restoreViewModel(viewModel);
    viewModel.setEmpty(true);
    setDatabaseReference(viewModel);
  }

  @Override public void createGame(GameModel gameModel) {
    Subscription subscription = ReactiveNetwork.observeInternetConnectivity()
        .take(1)
        .filter(aBoolean -> !aBoolean)
        .subscribe(aBoolean -> {
          BaseError snack = BaseError.SNACK;
          snack.setValue(RpsApp.app().getString(R.string.game_will_be_synched));
          view.showError(snack);
        });
    compositeSubscription.add(subscription);

    compositeSubscription.add(createNewGameInteractor.createNewGame(gameModel)
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(s -> {
          subscription.unsubscribe();
          view.onGameCreated();
        }, Crashlytics::logException));
  }

  @Override public void loadComplete() {
    view.hideLoading();
  }

  @Override public void onFabPressed() {
    CreateGameDialogViewModel createGameDialogViewModel = new CreateGameDialogViewModel();
    createGameDialogViewModel.setTitle(RpsApp.app().getString(R.string.create_game));
    createGameDialogViewModel.setGameModel(new GameModel("", ""));
    viewModel.setCreateGameDialogData(createGameDialogViewModel);
    view.showCreateGameDialog();
  }

  public void navigateToGameDescriptionScreen(GameModel model) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(GameModel.KEY, model);
    parentPresenter.navigateToFragment(NavigationUtils.GAME_DESCRIPTION_FRAGMENT, bundle);
  }

  @Override public void navigateToGameScreen(Context context, GameModel model) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(GameModel.KEY, model);
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    compositeSubscription.add(
        getCheckUserInGameObservable(model, currentUserId).subscribe(userInGame -> {
          if (userInGame) {
            parentPresenter.navigateToFragment(NavigationUtils.GAME_FRAGMENT, bundle);
          } else {
            parentPresenter.navigateToFragment(NavigationUtils.GAME_DESCRIPTION_FRAGMENT, bundle);
          }
        }, this::handleThrowable));
  }

  @Override public void checkPassword(Context context, GameModel model) {
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    compositeSubscription.add(
        getCheckUserInGameObservable(model, currentUserId).subscribe(userInGame -> {
          if (userInGame) {
            navigateToGameScreen(context, model);
          } else {
            PasswordDialogViewModel passwordDialogViewModel = new PasswordDialogViewModel();
            passwordDialogViewModel.setTitle(RpsApp.app().getString(R.string.create_game));
            passwordDialogViewModel.setGameModel(model);
            viewModel.setPasswordDialogViewModel(passwordDialogViewModel);
            view.showPasswordDialog();
          }
        }, this::handleThrowable));
  }

  @Override public void validatePassword(Context context, String s, GameModel gameModel) {
    compositeSubscription.add(validatePasswordInteractor.isPasswordValid(s, gameModel.getPassword())
        .subscribe(aBoolean -> {
          if (aBoolean) {
            navigateToGameDescriptionScreen(gameModel);
          } else {
            BaseError snack = BaseError.SNACK;
            snack.setValue(RpsApp.app().getString(R.string.error_incorrect_password));
            view.showError(snack);
          }
        }, Crashlytics::logException));
  }

  @Override public void getData() {
    view.setData(viewModel);
    viewModel.setEmpty(false);
    view.showContent();
    view.showLoading();
    compositeSubscription.add(
        FireBaseUtils.checkReferenceExists(FireBaseUtils.getTableReference(GAMES))
            .subscribe(exists -> {
              if (!exists) {
                view.hideLoading();
              }
            }, Crashlytics::logException));

    compositeSubscription.add(FireBaseUtils.getConnectionObservableWithTimeInterval()
        .compose(RxTransformers.applySchedulers())
        .subscribe(aBoolean -> {
          if (!aBoolean) {
            view.showError(BaseError.NO_CONNECTION);
          } else {
            view.showContent();
          }
        }));
    compositeSubscription.add(RxFirebaseDatabase.getInstance()
        .observeValueEvent(FireBaseUtils.getTableReference(FireBaseUtils.GAMES))
        .map(DataSnapshot::getChildrenCount)
        .subscribe(aLong -> {
          viewModel.setGamesCount(aLong.intValue());
          view.updateGamesCount();
        }, Crashlytics::logException));
  }

  private void setDatabaseReference(GamesListViewModel gamesListViewModel) {
    gamesListViewModel.setReference(FirebaseDatabase.getInstance().getReference().child(GAMES));
  }

  @Override public UserGetInteractor getValue() {
    return userGetInteractor;
  }

  private Observable<Boolean> getCheckUserInGameObservable(GameModel model, String currentUserId) {
    return checkUserJoinedGameInteractor.checkUserInGame(currentUserId, model)
        .timeout(5000, TimeUnit.MILLISECONDS)
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading));
  }
}
