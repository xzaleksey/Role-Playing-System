package com.valyakinaleksey.roleplayingsystem.modules.auth.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseUser;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.LoginInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.RegisterInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.ResetPasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.ParentActivity;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@PerFragmentScope
public class AuthPresenterImpl extends BasePresenter<AuthView, AuthViewModel>
        implements AuthPresenter, RestorablePresenter<AuthViewModel>,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;

    private LoginInteractor loginInteractor;
    private RegisterInteractor registerInteractor;
    private ResetPasswordInteractor resetPasswordInteractor;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private Context appContext;
    private UserGetInteractor userGetInteractor;
    private GoogleApiClient googleApiClient;

    @Inject
    public AuthPresenterImpl(LoginInteractor loginInteractor, RegisterInteractor registerInteractor,
                             ResetPasswordInteractor resetPasswordInteractor,
                             SharedPreferencesHelper sharedPreferencesHelper, Context appContext,
                             UserGetInteractor userGetInteractor) {
        this.loginInteractor = loginInteractor;
        this.registerInteractor = registerInteractor;
        this.resetPasswordInteractor = resetPasswordInteractor;
        this.sharedPreferencesHelper = sharedPreferencesHelper;
        this.appContext = appContext;
        this.userGetInteractor = userGetInteractor;
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
        compositeSubscription.add(
                loginInteractor.loginWithPassword(email, password, authResultOnCompleteListener)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                        .subscribe(aVoid -> {
                            Timber.d("Got login result");
                        }, this::showError));
    }

    @Override
    public void register(String email, String password) {
        compositeSubscription.add(
                registerInteractor.register(email, password, getAuthResultOnCompleteListener())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                        .subscribe(aVoid -> {

                        }, this::showError));
    }

    @Override
    public void resetPassword(String email) {
        compositeSubscription.add(resetPasswordInteractor.resetPassword(email, task -> {
            if (task.isSuccessful()) {
                view.showMessage(appContext.getString(R.string.email_has_been_sent), LceView.SNACK);
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
        if (FirebaseAuth.getInstance().getCurrentUser()
                != null) { // getNavigationFragment to main Activity if logged in
            navigateToMainActivity(activity);
            return;
        }
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                        appContext.getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(activity).enableAutoManage(activity, this)
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
     * @param activity AuthActivity
     */
    @Override
    public void onActivityResult(FragmentActivity activity, int requestCode, int resultCode,
                                 Intent data) {
        view.hideLoading();
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                compositeSubscription.add(loginInteractor.loginWithGoogle(account, getAuthResultOnCompleteListener())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                        .subscribe(aVoid -> {

                        }, this::showError));
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
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (view != null) {
            view.showMessage(connectionResult.getErrorMessage(), LceView.SNACK);
        }
    }

    private void showError(Throwable throwable) {
        BaseError general = new BaseError(BaseErrorType.SNACK, throwable.getMessage());
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
        return this::handleTaskResult;
    }

    private void handleTaskResult(Task<AuthResult> task) {
        view.hideLoading();
        if (task.isSuccessful()) {
            FirebaseUser user = task.getResult().getUser();
            Timber.d(user.toString());
            compositeSubscription.add(onAuthSuccess(user).subscribe(user1 -> {
                view.showMessage(appContext.getString(R.string.success), LceView.TOAST);
                view.performAction(context -> navigateToMainActivity((FragmentActivity) context));
            }, Timber::d));
        } else {
            showError(task.getException());
        }
    }

    /**
     * Should be in User Repository, TODO create it, when i will work with users table
     */
    private Observable<User> onAuthSuccess(FirebaseUser user) {
        String username = FireBaseUtils.usernameFromEmail(user.getEmail());
        RpsApp.logUser();
        return writeOrUpdateUser(user.getUid(), username, user.getEmail());
    }

    private Observable<User> writeOrUpdateUser(String userId, String name, String email) {
        return userGetInteractor.getUserByUidFromServer(userId).map(oldUser -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (oldUser == null) {
                User newUser = new User(userId, name, email);
                List<? extends UserInfo> providerData =
                        currentUser.getProviderData();
                for (UserInfo userInfo : providerData) {
                    Uri photoUrl = userInfo.getPhotoUrl();
                    if (photoUrl != null && !TextUtils.isEmpty(photoUrl.toString())) {
                        newUser.setPhotoUrl(photoUrl.toString());
                        updateProfilePhoto(currentUser, photoUrl);
                        break;
                    }
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child(FirebaseTable.USERS).child(userId).setValue(newUser);
                return newUser;
            } else {
                HashMap<String, Object> map = new HashMap<>();
                Uri photoUrl = currentUser.getPhotoUrl();
                String displayName = currentUser.getDisplayName();
                if (photoUrl != null) {
                    map.put(User.FIELD_PHOTO_URL, photoUrl.toString());
                }
                if (displayName != null) {
                    map.put(User.FIELD_DISPLAY_NAME, currentUser.getDisplayName());
                }
                if (displayName != null) {
                    map.put(User.FIELD_DISPLAY_NAME, currentUser.getDisplayName());
                }
                if (!StringUtils.areEqual(oldUser.getEmail(), email)) {
                    map.put(User.FIELD_EMAIL, email);
                }
                if (!map.isEmpty()) {
                    FireBaseUtils.getTableReference(FirebaseTable.USERS).child(userId).updateChildren(map);
                }
            }
            return oldUser;
        });
    }

    private void updateProfilePhoto(FirebaseUser currentUser, Uri photoUrl) {
        compositeSubscription.add(RxFirebaseUser.updateProfile(currentUser, new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUrl).build())
                .compose(RxTransformers.applyIoSchedulers())
                .subscribe(new DataObserver<Void>() {
                    @Override
                    public void onData(Void data) {

                    }
                }));
    }

    private void navigateToMainActivity(FragmentActivity activity) {
        Intent intent = new Intent(activity, ParentActivity.class);
        Intent authActivityIntent = activity.getIntent();
        if (authActivityIntent != null && authActivityIntent.getExtras() != null) {
            intent.putExtras(authActivityIntent.getExtras());
        }
        activity.startActivity(intent);
        activity.finish();
    }
}
