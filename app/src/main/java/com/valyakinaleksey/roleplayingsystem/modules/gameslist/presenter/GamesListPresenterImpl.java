package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import timber.log.Timber;

@PerFragment
public class GamesListPresenterImpl extends BasePresenter<GamesListView, GamesListViewModel> implements GamesListPresenter, RestorablePresenter<GamesListViewModel> {

    private CreateNewGameInteractor createNewGameInteractor;

    public GamesListPresenterImpl(CreateNewGameInteractor createNewGameInteractor) {
        this.createNewGameInteractor = createNewGameInteractor;
    }

    @Override
    protected GamesListViewModel initNewViewModel(Bundle arguments) {
        GamesListViewModel gamesListViewModel = new GamesListViewModel();
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
        createNewGameInteractor.createNewGame(gameModel)
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(s -> {
                    Timber.d("success");
                }, throwable -> {
                    Crashlytics.logException(throwable);
                });
    }

    @Override
    public void getData() {
        view.setData(viewModel);
        view.showContent();
    }

    private void setDatabaseReference(GamesListViewModel gamesListViewModel) {
        gamesListViewModel.setReference(FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAMES));
    }
}
