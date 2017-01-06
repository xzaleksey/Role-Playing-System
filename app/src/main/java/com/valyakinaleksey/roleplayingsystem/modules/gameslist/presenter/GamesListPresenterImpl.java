package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.GameActivity;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

@PerFragment
public class GamesListPresenterImpl extends BasePresenter<GamesListView, GamesListViewModel> implements GamesListPresenter, RestorablePresenter<GamesListViewModel> {

    private CreateNewGameInteractor createNewGameInteractor;
    private UserGetInteractor userGetInteractor;

    public GamesListPresenterImpl(CreateNewGameInteractor createNewGameInteractor, UserGetInteractor userGetInteractor) {
        this.createNewGameInteractor = createNewGameInteractor;
        this.userGetInteractor = userGetInteractor;
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
        viewModel.setDialogData(createGameDialogViewModel);
        view.showCreateGameDialog();
    }

    @Override
    public void navigateToGameScreen(Context context, GameModel model) {
        context.startActivity(new Intent(context, GameActivity.class));
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
