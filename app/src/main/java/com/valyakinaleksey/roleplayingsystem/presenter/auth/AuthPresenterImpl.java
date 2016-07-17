package com.valyakinaleksey.roleplayingsystem.presenter.auth;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.model.interactor.auth.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.model.repository.preferences.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.AuthView;
import com.valyakinaleksey.roleplayingsystem.view.model.AuthViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@PerFragment
public class AuthPresenterImpl implements AuthPresenter, RestorablePresenter<AuthViewModel> {

    private CompositeSubscription mSubscriptions;
    private AuthView mView;
    private LoginInteractor loginInteractor;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private AuthViewModel viewModel;

    private Runnable mShowLoading = () -> {
        mView.showLoading();
    };
    private Runnable mHideLoading = () -> {
        mView.hideLoading();
    };

    @Inject
    public AuthPresenterImpl(LoginInteractor loginInteractor, SharedPreferencesHelper sharedPreferencesHelper) {
        this.loginInteractor = loginInteractor;
        this.sharedPreferencesHelper = sharedPreferencesHelper;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mView.loadData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        String login = bundle.getString(SharedPreferencesHelper.LOGIN, "");
        String password = bundle.getString(SharedPreferencesHelper.PASSWORD, "");
        viewModel.setEmail(login);
        viewModel.setPassword(password);
        sharedPreferencesHelper.saveLogin(login);
        sharedPreferencesHelper.savePassword(bundle.getString(SharedPreferencesHelper.PASSWORD, ""));
    }

    // todo abstract away attach / detach of view
    @Override
    public void attachView(AuthView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
    }

    private void updateUi(AuthViewModel model) {
        mView.setData(model);
        mView.showContent();
    }

    @Override
    public void restoreViewModel(AuthViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void login(String email, String password) {
        mSubscriptions.add(loginInteractor
                .get(email, password)
                .subscribeOn(Schedulers.io())               // inject for testing
                .observeOn(AndroidSchedulers.mainThread())  // inject for testing
                .compose(RxTransformers.applyOpBeforeAndAfter(mShowLoading, mHideLoading))
                .subscribe(firebaseUser -> {
                    updateUi(viewModel);
                }, throwable -> {
                    showError(AuthView.AuthError.GENERAL);
                }));
    }

    @Override
    public void register(String email, String password) {

    }

    @Override
    public void resetPassword(String email) {

    }

    @Override
    public void restoreData() {
        if (viewModel == null) {
            viewModel = new AuthViewModel();
            viewModel.setEmail(sharedPreferencesHelper.getLogin());
            viewModel.setPassword(sharedPreferencesHelper.getPassword());
        }
        updateUi(viewModel);
    }

    private void showError(AuthView.AuthError error) {
        mView.showError(error);
    }
}
