package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.crashlytics.android.Crashlytics;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.GameActivity;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.util.concurrent.TimeUnit;

@PerFragment
public class GamesListPresenterImpl extends BasePresenter<GamesListView, GamesListViewModel> implements GamesListPresenter, RestorablePresenter<GamesListViewModel> {

    private CreateNewGameInteractor createNewGameInteractor;
    private UserGetInteractor userGetInteractor;
    private ValidatePasswordInteractor validatePasswordInteractor;
    private CheckUserJoinedGameInteractor checkUserJoinedGameInteractor;

    public GamesListPresenterImpl(CreateNewGameInteractor createNewGameInteractor, UserGetInteractor userGetInteractor, ValidatePasswordInteractor validatePasswordInteractor, CheckUserJoinedGameInteractor checkUserJoinedGameInteractor) {
        this.createNewGameInteractor = createNewGameInteractor;
        this.userGetInteractor = userGetInteractor;
        this.validatePasswordInteractor = validatePasswordInteractor;
        this.checkUserJoinedGameInteractor = checkUserJoinedGameInteractor;
    }

    @Override
    protected GamesListViewModel initNewViewModel(Bundle arguments) {
        GamesListViewModel gamesListViewModel = new GamesListViewModel();
        gamesListViewModel.setToolbarTitle(RpsApp.app().getString(R.string.list_of_games));
        setDatabaseReference(gamesListViewModel);
        return gamesListViewModel;
    }

    @Override
    public void restoreViewModel(GamesListViewModel viewModel) {
        super.restoreViewModel(viewModel);
        setDatabaseReference(viewModel);
    }

    @Override
    public void createGame(GameModel gameModel) {
        compositeSubscription.add(createNewGameInteractor.createNewGame(gameModel)
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(s -> {
                    view.onGameCreated();
                }, Crashlytics::logException));

        ReactiveNetwork.observeInternetConnectivity()
                .take(1)
                .filter(aBoolean -> !aBoolean)
                .subscribe(aBoolean -> {
                    BaseError snack = BaseError.SNACK;
                    snack.setValue(RpsApp.app().getString(R.string.game_will_be_synched));
                    view.showError(snack);
                });
    }

    @Override
    public void loadComplete() {
        view.hideLoading();
    }

    @Override
    public void onFabPressed() {
        CreateGameDialogViewModel createGameDialogViewModel = new CreateGameDialogViewModel();
        createGameDialogViewModel.setTitle(RpsApp.app().getString(R.string.create_game));
        createGameDialogViewModel.setGameModel(new GameModel());
        viewModel.setCreateGameDialogData(createGameDialogViewModel);
        view.showCreateGameDialog();
    }

    @Override
    public void navigateToGameScreen(Context context, GameModel model) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(GameModel.KEY, (Parcelable) model);
        context.startActivity(intent);
    }

    @Override
    public void checkPassword(Context context, GameModel model) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        compositeSubscription.add(
                checkUserJoinedGameInteractor.checkUserInGame(currentUserId, model)
                        .timeout(1000, TimeUnit.MILLISECONDS)
                        .compose(RxTransformers.applySchedulers())
                        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                        .subscribe(userInGame -> {
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

    @Override
    public void validatePassword(Context context, String s, GameModel gameModel) {
        compositeSubscription.add(validatePasswordInteractor.isPasswordValid(s, gameModel.getPassword())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        navigateToGameScreen(context, gameModel);
                    } else {
                        BaseError snack = BaseError.SNACK;
                        snack.setValue(RpsApp.app().getString(R.string.error_incorrect_password));
                        view.showError(snack);
                    }
                }, Crashlytics::logException));
    }

    @Override
    public void getData() {
        view.setData(viewModel);
        view.showContent();
        view.showLoading();
        compositeSubscription.add(ReactiveNetwork.observeInternetConnectivity()
                .compose(RxTransformers.applySchedulers())
                .take(1)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        view.showError(BaseError.NO_CONNECTION);
                        view.hideLoading();
                    }
                }));
    }

    private void setDatabaseReference(GamesListViewModel gamesListViewModel) {
        gamesListViewModel.setReference(FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAMES));
    }

    @Override
    public UserGetInteractor getValue() {
        return userGetInteractor;
    }
}
