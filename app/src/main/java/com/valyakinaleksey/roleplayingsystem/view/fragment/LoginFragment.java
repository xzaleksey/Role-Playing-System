package com.valyakinaleksey.roleplayingsystem.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.onemanparty.lib.dialog.delegate.ConfirmDialogFragmentDelegate;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.di.auth.AuthComponent;
import com.valyakinaleksey.roleplayingsystem.di.auth.DaggerAuthComponent;
import com.valyakinaleksey.roleplayingsystem.presenter.auth.AuthPresenter;
import com.valyakinaleksey.roleplayingsystem.view.data.CautionDialogData;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.WeatherView;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewModel;

import javax.inject.Inject;

import butterknife.Bind;

public class LoginFragment extends AbsButterLceFragment<AuthComponent, WeatherViewModel, WeatherView.WeatherError, WeatherView> implements WeatherView {

    public static final String TAG = LoginFragment.class.getSimpleName();

    @Inject
    AuthPresenter presenter;

    @Bind(R.id.email)
    AutoCompleteTextView atEmail;
    @Bind(R.id.til_password)
    TextInputLayout tilPassword;
    @Bind(R.id.password)
    EditText etPassword;
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
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        etPassword.setTypeface(atEmail.getTypeface());
        tilPassword.setTypeface(atEmail.getTypeface());
    }

    @Override
    public void onResume() {
        super.onResume();
        mCautionDialogDelegate.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCautionDialogDelegate.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void loadData() {
//        getComponent().getPresenter().restoreData;
    }

    @Override
    public void showContent() {
        super.showContent();
    }

    @Override
    public void showCautionDialog(CautionDialogData data) {
        mCautionDialogDelegate.showDialog(data);
    }

}
