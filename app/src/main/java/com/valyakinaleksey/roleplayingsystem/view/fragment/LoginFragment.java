package com.valyakinaleksey.roleplayingsystem.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.onemanparty.lib.dialog.delegate.ConfirmDialogFragmentDelegate;
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
import com.valyakinaleksey.roleplayingsystem.view.data.CautionDialogData;
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

    ConfirmDialogFragmentDelegate<CautionDialogData> mCautionDialogDelegate;

    private ConfirmDialogFragmentDelegate.OnConfirmWithDataDialogListener<CautionDialogData> listener = new ConfirmDialogFragmentDelegate.OnConfirmWithDataDialogListener<CautionDialogData>() {
        @Override
        public void onOkDialog(DialogFragment dialogFragment, CautionDialogData data) {
//            ViewPagerActivity.start(getActivity(), data);
        }

        @Override
        public void onCancelDialog(DialogFragment dialogFragment, CautionDialogData data) {
//            getComponent().getPresenter().someInsaneActionClicked();
        }
    };

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
        mCautionDialogDelegate = new ConfirmDialogFragmentDelegate<>("caution_dialog", listener, R.string.weather_dialog_title, R.string.weather_dialog_subtitle, android.R.string.ok, android.R.string.cancel);
        mCautionDialogDelegate.onCreate(savedInstanceState, getActivity());
        getComponent().inject(this);
        getComponent().getPresenter().restoreData();
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCautionDialogDelegate.onResume();
    }

    private void initValidation() {
        subscriptions.add(RxTextView.textChanges(etEmail).subscribe(charSequence -> {
            String s = etEmail.getText().toString();
            data.setEmail(s);
            if (TextUtils.isEmpty(s)) {
                emailInputLayout.setError(errorEmptyField);
            } else if (ValidationUtils.isValidEmail(s)) {
                emailInputLayout.setError(StringConstants.EMPTY_STRING);
            } else {
                emailInputLayout.setError(errorInvalidEmail);
            }
        }));
        subscriptions.add(RxTextView.textChanges(etPassword).subscribe(charSequence -> {
            String s = etPassword.getText().toString();
            data.setPassword(s);
            if (TextUtils.isEmpty(s)) {
                passwordInputLayout.setError(errorEmptyField);
            } else if (ValidationUtils.isValidLength(s)) {
                passwordInputLayout.setError(StringConstants.EMPTY_STRING);
            } else {
                passwordInputLayout.setError(errorMinSymbols);
            }
        }));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SharedPreferencesHelper.LOGIN, etEmail.getText().toString());
        savedInstanceState.putString(SharedPreferencesHelper.PASSWORD, etPassword.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
        mCautionDialogDelegate.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void loadData() {
        signUpBtn.post(action);
    }

    @Override
    public void showContent() {
        super.showContent();
        initValidation();
        etEmail.setText(data.getEmail());
        etPassword.setText(data.getPassword());
    }

    @Override
    public void showCautionDialog(CautionDialogData data) {
        mCautionDialogDelegate.showDialog(data);
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        action = () -> getComponent().getPresenter().login(etEmail.getText().toString(),
                etPassword.getText().toString());
        startRequest();
    }

    private void startRequest() {
        if (TextUtils.isEmpty(emailInputLayout.getError()) && TextUtils.isEmpty(passwordInputLayout.getError())) {
            loadData();
        }
    }

    @OnClick(R.id.sign_up_button)
    public void signUp() {
        action = () -> getComponent().getPresenter().register(etEmail.getText().toString(),
                etPassword.getText().toString());
        startRequest();
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
}
