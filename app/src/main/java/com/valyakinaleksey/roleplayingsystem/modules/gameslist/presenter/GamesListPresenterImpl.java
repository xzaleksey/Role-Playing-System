package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;

import android.content.Context;
import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.model.interactor.GetListOfGamesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@PerFragment
public class GamesListPresenterImpl implements GamesListPresenter, RestorablePresenter<GamesListViewModel> {

    private CompositeSubscription mSubscriptions;
    private GamesListView mView;
    private GetListOfGamesInteractor getListOfGamesInteractor;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private GamesListViewModel viewModel;
    private Context context;
    private Runnable mShowLoading = () -> {
        mView.showLoading();
    };
    private Runnable mHideLoading = () -> {
        mView.hideLoading();
    };

    @Inject
    public GamesListPresenterImpl(GetListOfGamesInteractor getListOfGamesInteractor, SharedPreferencesHelper sharedPreferencesHelper, Context context) {
        this.getListOfGamesInteractor = getListOfGamesInteractor;
        this.sharedPreferencesHelper = sharedPreferencesHelper;
        this.context = context;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            restoreData();
        }
    }

    @Override
    public void getData() {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        String login = bundle.getString(SharedPreferencesHelper.LOGIN, "");
        String password = bundle.getString(SharedPreferencesHelper.PASSWORD, "");
        saveModel(login, password);
    }

    private void saveModel(String login, String password) {
        sharedPreferencesHelper.saveLogin(login);
        sharedPreferencesHelper.savePassword(password);
    }

    // todo abstract away attach / detach of view
    @Override
    public void attachView(GamesListView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onDestroy() {
        saveModel(viewModel.getEmail(), viewModel.getPassword());
        if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
    }

    private void updateUi(GamesListViewModel model) {
        mView.setData(model);
        mView.showContent();
    }

    @Override
    public void restoreViewModel(GamesListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void login(String email, String password) {
        mSubscriptions.add(getListOfGamesInteractor
                .get(email, password, task -> {
                    if (task.isSuccessful()) {
                        Timber.d(task.getResult().getUser().toString());
                        viewModel.setFirebaseUser(task.getResult().getUser());
                        updateUi(viewModel);
                    } else {
                        showError(task.getException());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformers.applyOpBeforeAndAfter(mShowLoading, mHideLoading))
                .subscribe(aVoid -> {

                }, this::showError));
    }

    private void showError(Throwable throwable) {

    }

    @Override
    public void restoreData() {
        if (viewModel == null) {
            viewModel = new GamesListViewModel();
            viewModel.setEmail(sharedPreferencesHelper.getLogin());
            viewModel.setPassword(sharedPreferencesHelper.getPassword());
        }
        updateUi(viewModel);
    }

}
