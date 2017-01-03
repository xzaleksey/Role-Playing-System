package com.valyakinaleksey.roleplayingsystem.modules.auth.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.RegisterInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.ResetPasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@PerFragment
public class AuthPresenterImpl extends BasePresenter<AuthView, AuthViewModel> implements AuthPresenter, RestorablePresenter<AuthViewModel> {

    private LoginInteractor loginInteractor;
    private RegisterInteractor registerInteractor;
    private ResetPasswordInteractor resetPasswordInteractor;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private Context appContext;

    @Inject
    public AuthPresenterImpl(LoginInteractor loginInteractor, RegisterInteractor registerInteractor, ResetPasswordInteractor resetPasswordInteractor, SharedPreferencesHelper sharedPreferencesHelper, Context appContext) {
        this.loginInteractor = loginInteractor;
        this.registerInteractor = registerInteractor;
        this.resetPasswordInteractor = resetPasswordInteractor;
        this.sharedPreferencesHelper = sharedPreferencesHelper;
        this.appContext = appContext;
    }

    @Override
    protected AuthViewModel initNewViewModel(Bundle arguments) {
        if (viewModel == null) {
            viewModel = new AuthViewModel();
            viewModel.setEmail(sharedPreferencesHelper.getLogin());
            viewModel.setPassword(sharedPreferencesHelper.getPassword());
        }
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        updateUi(viewModel);
    }

    @Override
    public void getData() {
        updateUi(viewModel);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveModel(viewModel.getEmail(), viewModel.getPassword());
    }

    private void updateUi(AuthViewModel model) {
        view.setData(model);
        view.showContent();
    }

    @Override
    public void login(String email, String password) {
        compositeSubscription.add(loginInteractor
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
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aVoid -> {

                }, this::showError));
    }

    @Override
    public void register(String email, String password) {
        compositeSubscription.add(registerInteractor.register(email, password,
                task -> {
                    if (task.isSuccessful()) {
                        Timber.d(task.getResult().toString());
                    } else {
                        showError(task.getException());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aVoid -> {

                }, this::showError));
    }

    @Override
    public void resetPassword(String email) {
        resetPasswordInteractor.resetPassword(email, task -> {
            if (task.isSuccessful()) {
                view.showSnackBarString(appContext.getString(R.string.email_has_been_sent));
            } else {
                showError(task.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
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

    private void showError(Throwable throwable) {
        BaseError general = BaseError.SNACK;
        general.setValue(throwable.getMessage());
        showError(general);
    }

    private void showError(BaseError error) {
        view.showError(error);
    }
}
