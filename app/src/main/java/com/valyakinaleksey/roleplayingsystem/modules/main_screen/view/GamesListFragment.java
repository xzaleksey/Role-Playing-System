package com.valyakinaleksey.roleplayingsystem.modules.main_screen.view;

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
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.di.DaggerAuthComponent;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.di.DaggerGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.di.GamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.presenter.GamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.StringConstants;
import com.valyakinaleksey.roleplayingsystem.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;
import rx.Subscription;

public class GamesListFragment extends AbsButterLceFragment<GamesListComponent, GamesListViewModel, GamesListView> implements GamesListView {

    public static final String TAG = GamesListFragment.class.getSimpleName();

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

    public static GamesListFragment newInstance() {
        return new GamesListFragment();
    }

    @Override
    protected GamesListComponent createComponent() {
        return DaggerGamesListComponent
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
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbsActivity) getActivity()).setToolbarTitle("test");
    }

    @Override
    public void showContent() {
        super.showContent();
    }


    @Override
    protected int getContentResId() {
        return R.layout.fragment_login;
    }
}
