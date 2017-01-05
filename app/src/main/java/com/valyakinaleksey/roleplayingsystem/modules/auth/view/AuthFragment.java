package com.valyakinaleksey.roleplayingsystem.modules.auth.view;

import android.content.Intent;
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
import com.valyakinaleksey.roleplayingsystem.modules.auth.di.AuthComponent;
import com.valyakinaleksey.roleplayingsystem.modules.auth.di.DaggerAuthComponent;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.model.AuthViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.StringConstants;
import com.valyakinaleksey.roleplayingsystem.utils.ValidationUtils;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

public class AuthFragment extends AbsButterLceFragment<AuthComponent, AuthViewModel, AuthView> implements AuthView {

    public static final String TAG = AuthFragment.class.getSimpleName();

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

    public static AuthFragment newInstance() {
        return new AuthFragment();
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
        getComponent().getPresenter().init(getActivity());
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        emailInputLayout.setHint(getString(R.string.email));
        passwordInputLayout.setHint(getString(R.string.password));
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
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SharedPreferencesHelper.LOGIN, etEmail.getText().toString());
        savedInstanceState.putString(SharedPreferencesHelper.PASSWORD, etPassword.getText().toString());
    }

    @Override
    public void loadData() {
        signUpBtn.post(action);
    }

    @Override
    public void showContent() {
        super.showContent();
        etEmail.setText(data.getEmail());
        etEmail.setSelection(etEmail.getText().length());
        etPassword.setText(data.getPassword());
        initSubscriptions();
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
    public void onPause() {
        compositeSubscription.unsubscribe();
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        if (data != null) {
            updateEmail();
            updatePassword();
        }
        super.onDestroyView();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initSubscriptions() {
        compositeSubscription.add(RxTextView.textChanges(etEmail)
                .skip(1)
                .subscribe(charSequence -> {
                    String s = updateEmail();
                    if (TextUtils.isEmpty(s)) {
                        emailInputLayout.setError(String.format(errorEmptyField, getString(R.string.email)));
                    } else if (ValidationUtils.isValidEmail(s)) {
                        emailInputLayout.setError(StringConstants.EMPTY_STRING);
                    } else {
                        emailInputLayout.setError(errorInvalidEmail);
                    }
                }));
        compositeSubscription.add(RxTextView.textChanges(etPassword)
                .skip(1)
                .subscribe(charSequence -> {
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


    @OnClick(R.id.auth_button)
    public void googleAuth() {
        getComponent().getPresenter().googleAuth(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getComponent().getPresenter().onActivityResult(getActivity(), requestCode, resultCode, data);
    }
}
