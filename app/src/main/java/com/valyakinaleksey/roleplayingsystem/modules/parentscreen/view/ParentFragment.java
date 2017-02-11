package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.BindString;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnToolbarChangedListener;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.ParentScope;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.HasParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentModule;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;

import autodagger.AutoComponent;
import autodagger.AutoInjector;

@AutoComponent(dependencies = { AppComponent.class },
    modules = ParentModule.class,
    superinterfaces = { HasParentPresenter.class, GlobalComponent.class }) @ParentScope
@AutoInjector public class ParentFragment
    extends AbsButterLceFragment<ParentFragmentComponent, ParentModel, ParentView>
    implements ParentView, OnToolbarChangedListener {

  public static final String TAG = ParentFragment.class.getSimpleName();

  private GoogleApiClient googleApiClient;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.toolbar_progress_bar) ProgressBar progressBar;

  @BindString(R.string.connecting) protected String connectionString;

  public static ParentFragment newInstance(Bundle arguments) {
    ParentFragment gamesDescriptionFragment = new ParentFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override protected ParentFragmentComponent createComponent() {
    return DaggerParentFragmentComponent.builder()
        .appComponent(RpsApp.getAppComponent(getActivity()))
        .parentModule(new ParentModule())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
    setHasOptionsMenu(true);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            this.getString(R.string.default_web_client_id)).requestEmail().build();
    googleApiClient = new GoogleApiClient.Builder(getContext()).enableAutoManage(getActivity(),
        connectionResult -> {

        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    AbsActivity activity = (AbsActivity) getActivity();
    activity.setupToolbar();
    activity.addOnToolBarChangedListener(this);
    progressBar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_white_1000),
            PorterDuff.Mode.MULTIPLY);
    //Drawer result = new DrawerBuilder()
    //    .withActivity(getActivity())
    //    .withToolbar(((AbsActivity) getActivity()).getToolbar())
    //    .addDrawerItems(
    //        new PrimaryDrawerItem().withName(R.string.list_of_games),
    //        new SecondaryDrawerItem().withName(R.string.settings)
    //    )
    //    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
    //      @Override
    //      public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
    //        // do something with the clicked item :D
    //        return true;
    //      }
    //    })
    //    .build();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.main_menu, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sign_out_menu:
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onStart() {
    super.onStart();
    googleApiClient.connect();
  }

  @Override public void onStop() {
    super.onStop();
    googleApiClient.disconnect();
  }

  @Override public void updateToolbar() {
    boolean visible = data.isDisconnected();
    progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    ActionBar supportActionBar = ((AbsActivity) getActivity()).getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.setTitle(visible ? connectionString : data.getToolbarTitle());
    }
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void showContent() {
    super.showContent();
    updateToolbar();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_parent_container;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void getNavigationFragment(Bundle args) {
    getComponent().getPresenter().navigateTo(this, args);
  }

  @Override public void onTitleChanged(String title) {
    data.setToolbarTitle(title);
    updateToolbar();
  }
}
