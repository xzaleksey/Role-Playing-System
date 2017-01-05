package com.valyakinaleksey.roleplayingsystem.modules.auth.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.RegisterInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.interactor.ResetPasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.modules.mainscreen.view.MainActivity;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@PerFragment
public class AuthPresenterImpl extends BasePresenter<AuthView, AuthViewModel> implements AuthPresenter, RestorablePresenter<AuthViewModel>, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;

    private LoginInteractor loginInteractor;
    private RegisterInteractor registerInteractor;
    private ResetPasswordInteractor resetPasswordInteractor;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private Context appContext;
    private GoogleApiClient googleApiClient;

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
        OnCompleteListener<AuthResult> authResultOnCompleteListener = getAuthResultOnCompleteListener();
        compositeSubscription.add(loginInteractor
                .loginWithPassword(email, password, authResultOnCompleteListener)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aVoid -> {
                    Timber.d("Got login result");
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
                view.showMessage(appContext.getString(R.string.email_has_been_sent), LceView.SNACK);
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

    @Override
    public void init(FragmentActivity activity) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) { // navigate to main Activity if logged in
            navigateToMainActivity(activity);
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(appContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void googleAuth(FragmentActivity activity) {
        Intent authorizeIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(authorizeIntent, RC_SIGN_IN);
        view.showLoading();
    }


    /**
     * Check google accont was chosen and login to firebase with google account
     *
     * @param activity    AuthActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(FragmentActivity activity, int requestCode, int resultCode, Intent data) {
        view.hideLoading();
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                loginInteractor.loginWithGoogle(account, getAuthResultOnCompleteListener())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                        .subscribe(aVoid -> {

                        }, this::showError);
            } else {
                String statusMessage = result.getStatus().getStatusMessage();
                if (!TextUtils.isEmpty(statusMessage)) {
                    view.showMessage(statusMessage, LceView.SNACK);
                }
            }
        }
    }


    /**
     * Google connection failed
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (view != null) {
            view.showMessage(connectionResult.getErrorMessage(), LceView.SNACK);
        }
    }

    private void showError(Throwable throwable) {
        BaseError general = BaseError.SNACK;
        general.setValue(throwable.getMessage());
        showError(general);
    }

    private void showError(BaseError error) {
        view.showError(error);
    }


    /**
     * Handle AuthResult from firebase
     *
     * @return firebaseListener
     */
    @NonNull
    private OnCompleteListener<AuthResult> getAuthResultOnCompleteListener() {
        return task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                Timber.d(user.toString());
                onAuthSuccess(user);
                viewModel.setFirebaseUser(task.getResult().getUser());
                view.showMessage(appContext.getString(R.string.success), LceView.TOAST);
                view.performAction(context -> navigateToMainActivity((FragmentActivity) context));
            } else {
                showError(task.getException());
            }
        };
    }

    /**
     * Should be in User Repository, TODO create it, when i will work with users table
     *
     * @param user
     */
    private void onAuthSuccess(FirebaseUser user) {
        String username = FireBaseUtils.usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail());
        RpsApp.logUser();
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(FireBaseUtils.USERS).child(userId).setValue(user);
    }

    private void navigateToMainActivity(FragmentActivity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
}
