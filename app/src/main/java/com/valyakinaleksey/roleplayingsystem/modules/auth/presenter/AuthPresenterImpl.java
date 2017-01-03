package com.valyakinaleksey.roleplayingsystem.modules.auth.presenter;

import android.content.Context;
import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.RegisterInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.ResetPasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@PerFragment
public class AuthPresenterImpl implements AuthPresenter, RestorablePresenter<AuthViewModel> {

    private CompositeSubscription mSubscriptions;
    private AuthView mView;
    private LoginInteractor loginInteractor;
    private RegisterInteractor registerInteractor;
    private ResetPasswordInteractor resetPasswordInteractor;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private AuthViewModel viewModel;
    private Context context;
    private Runnable mShowLoading = () -> {
        mView.showLoading();
    };
    private Runnable mHideLoading = () -> {
        mView.hideLoading();
    };

    @Inject
    public AuthPresenterImpl(LoginInteractor loginInteractor, RegisterInteractor registerInteractor, ResetPasswordInteractor resetPasswordInteractor, SharedPreferencesHelper sharedPreferencesHelper, Context context) {
        this.loginInteractor = loginInteractor;
        this.registerInteractor = registerInteractor;
        this.resetPasswordInteractor = resetPasswordInteractor;
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
    public void attachView(AuthView view) {
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
        AuthView.AuthError general = AuthView.AuthError.AUTH_ERROR;
        general.setValue(throwable.getMessage());
        showError(general);
    }

    @Override
    public void register(String email, String password) {
        mSubscriptions.add(registerInteractor.register(email, password,
                task -> {
                    if (task.isSuccessful()) {
                        Timber.d(task.getResult().toString());
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

    @Override
    public void resetPassword(String email) {
        resetPasswordInteractor.resetPassword(email, task -> {
            if (task.isSuccessful()) {
                mView.showSnackBarString(context.getString(R.string.email_has_been_sent));
            } else {
                showError(task.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformers.applyOpBeforeAndAfter(mShowLoading, mHideLoading))
                .subscribe(aVoid -> {

                }, this::showError);
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
