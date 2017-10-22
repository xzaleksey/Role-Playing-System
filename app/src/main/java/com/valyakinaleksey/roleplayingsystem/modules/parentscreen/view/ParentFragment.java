package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindString;
import butterknife.BindView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnToolbarChangedListener;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.*;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.DaggerParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.DrawerInfoModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;
import java.util.ArrayList;
import java.util.List;

public class ParentFragment extends
    AbsButterLceFragment<com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent, ParentModel, ParentView>
    implements ParentView, OnToolbarChangedListener {

  public static final String TAG = ParentFragment.class.getSimpleName();
  private List<DrawerInfoModel> drawerItems;
  private Drawer drawer;
  private GoogleApiClient googleApiClient;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_progress_bar) ProgressBar progressBar;
  @BindString(R.string.connecting) protected String connectionString;

  public static ParentFragment newInstance(Bundle arguments) {
    ParentFragment gamesDescriptionFragment = new ParentFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override
  protected com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent createComponent() {
    return DaggerParentFragmentComponent.builder()
        .appComponent(RpsApp.getAppComponent())
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
    initProgressBar();
    initDrawerItems();
    initDrawer();
  }

  private void initProgressBar() {
    progressBar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(getActivity(), R.color.md_white_1000),
            PorterDuff.Mode.MULTIPLY);
  }

  private void initDrawerItems() {
    List<DrawerInfoModel> result = new ArrayList<>();
    result.add(new DrawerInfoModel(getString(R.string.my_games), NavigationUtils.MY_GAMES));
    result.add(new DrawerInfoModel(getString(R.string.list_of_games), NavigationUtils.GAMES_LIST));
    result.add(new DrawerInfoModel(getString(R.string.settings), NavigationUtils.SETTINGS));
    drawerItems = result;
  }

  private void initDrawer() {
    DrawerBuilder drawerBuilder = new DrawerBuilder().withActivity(getActivity())
        .withToolbar(((AbsActivity) getActivity()).getToolbar())
        .withOnDrawerItemClickListener((view1, position, drawerItem) -> {
          getComponent().getPresenter().navigateToFragment(drawerItems.get(position).getNavId());
          drawer.closeDrawer();
          return true;
        });

    for (DrawerInfoModel drawerInfoModel : drawerItems) {
      drawerBuilder.addDrawerItems(getDrawerItem(drawerInfoModel));
    }
    drawer = drawerBuilder.build();
  }

  private IDrawerItem getDrawerItem(DrawerInfoModel drawerInfoModel) {
    return new PrimaryDrawerItem().withName(drawerInfoModel.getName());
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

  public void handleNewIntent(Intent intent) {
    getComponent().getPresenter().tryOpenDeepLink(intent.getExtras());
  }
}
