package com.valyakinaleksey.roleplayingsystem.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.utils.SnackbarHelper;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.di.auth.AuthComponent;
import com.valyakinaleksey.roleplayingsystem.di.auth.DaggerAuthComponent;
import com.valyakinaleksey.roleplayingsystem.model.repository.preferences.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.StringConstants;
import com.valyakinaleksey.roleplayingsystem.utils.ValidationUtils;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.AuthView;
import com.valyakinaleksey.roleplayingsystem.view.model.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;
import rx.Subscription;

public class LoginFragment extends AbsButterLceFragment<AuthComponent, AuthViewModel, AuthView.AuthError, AuthView> implements AuthView {

    public static final String TAG = LoginFragment.class.getSimpleName();
    public static final String RESET_PASSWORD = "resetPassword";
    private Runnable action;
    private List<Subscription> subscriptions = new ArrayList<>();

    @Inject
    AuthPresenter presenter;

    @Bind(R.id.email)
    EditText etEmail;
    @Bind(R.id.email_input_layout)
    TextInputLayout emailInputLayout;
    @Bind(R.id.password)
    EditText etPassword;
    @Bind(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @Bind(R.id.sign_in_button)
    Button signInBtn;
    @Bind(R.id.sign_up_button)
    Button signUpBtn;
    @Bind(R.id.main_container)
    View mainContainer;
    @BindString(R.string.error_invalid_email)
    String errorInvalidEmail;
    @BindString(R.string.error_empty_field)
    String errorEmptyField;
    @BindString(R.string.error_min_symbols)
    String errorMinSymbols;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    protected AuthComponent createComponent() {
        return DaggerAuthComponent
                .builder()
                .appComponent(RpsApp.getAppComponent(getActivity()))
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                signIn();
                handled = true;
            }
            return handled;
        });
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SharedPreferencesHelper.LOGIN, etEmail.getText().toString());
        savedInstanceState.putString(SharedPreferencesHelper.PASSWORD, etPassword.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void loadData() {
        signUpBtn.post(action);
    }

    @Override
    public void showContent() {
        super.showContent();
        etEmail.setText(data.getEmail());
        etEmail.post(() -> {
            etEmail.setSelection(etEmail.getText().length());
        });
        etPassword.setText(data.getPassword());
        initValidation();
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        action = () -> getComponent().getPresenter().login(etEmail.getText().toString(),
                etPassword.getText().toString());
        startRequest();
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
        action = () -> getComponent().getPresenter().register(etEmail.getText().toString(),
                etPassword.getText().toString());
        startRequest();
    }

    @OnClick(R.id.forgot_password)
    public void resetPassword() {
        if (ValidationUtils.isValidEmail(etEmail.getText())) {
            MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                    .title(R.string.reset_password)
                    .content(R.string.reset_password_text)
                    .positiveText(android.R.string.ok)
                    .onPositive((dialog1, which) -> {
                        getComponent().getPresenter().resetPassword(etEmail.getText().toString());
                    })
                    .negativeText(android.R.string.cancel)
                    .show();
        } else {
            SnackbarHelper.show(mainContainer, errorInvalidEmail);
        }
    }

    @Override
    public void showError(AuthError authError) {
        super.showError(authError);
        SnackbarHelper.show(mainContainer, authError.toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        for (Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
    }


    @Override
    public void onDestroyView() {
        updateEmail();
        updatePassword();
        super.onDestroyView();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_login;
    }

    private void initValidation() {
        subscriptions.add(RxTextView.textChanges(etEmail).subscribe(charSequence -> {
            String s = updateEmail();
            if (TextUtils.isEmpty(s)) {
                emailInputLayout.setError(String.format(errorEmptyField, getString(R.string.email)));
            } else if (ValidationUtils.isValidEmail(s)) {
                emailInputLayout.setError(StringConstants.EMPTY_STRING);
            } else {
                emailInputLayout.setError(errorInvalidEmail);
            }
        }));
        subscriptions.add(RxTextView.textChanges(etPassword).subscribe(charSequence -> {
            String s = updatePassword();
            if (TextUtils.isEmpty(s)) {
                passwordInputLayout.setError(String.format(errorEmptyField, getString(R.string.password)));
            } else if (ValidationUtils.isValidLength(s)) {
                passwordInputLayout.setError(StringConstants.EMPTY_STRING);
            } else {
                passwordInputLayout.setError(String.format(errorMinSymbols, getString(R.string.password)));
            }
        }));
    }

    @NonNull
    private String updatePassword() {
        String s = etPassword.getText().toString();
        data.setPassword(s);
        return s;
    }

    @NonNull
    private String updateEmail() {
        String s = etEmail.getText().toString();
        data.setEmail(s);
        return s;
    }

    private void startRequest() {
        if (TextUtils.isEmpty(emailInputLayout.getError()) && TextUtils.isEmpty(passwordInputLayout.getError())) {
            loadData();
        }
    }

    @Override
    public void showSnackBarString(String s) {
        SnackbarHelper.show(mainContainer, s);
    }
}
