package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentModule;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;

import butterknife.BindString;

public class ParentFragment extends AbsButterLceFragment<ParentFragmentComponent, ParentModel, ParentView>
        implements ParentView {

    public static final String TAG = ParentFragment.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    @BindString(R.string.connecting)
    protected String connectionString;

    public static ParentFragment newInstance(Bundle arguments) {
        ParentFragment gamesDescriptionFragment = new ParentFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    protected com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent createComponent(
            String fragmentId) {
        return RpsApp.getAppComponent().getParentFragmentComponent(new ParentFragmentModule(fragmentId));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), connectionResult -> {
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void showContent() {
        super.showContent();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_parent_container;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void getNavigationFragment(Bundle args) {
        getComponent().getPresenter().navigateTo(this, args);
    }

    @Override
    public Fragment getCurrentFragment() {
        return this;
    }

    private AbsActivity getAbsActivity() {
        return (AbsActivity) getActivity();
    }

    public void handleNewIntent(Intent intent) {
        getComponent().getPresenter().tryOpenDeepLink(intent.getExtras());
    }
}
