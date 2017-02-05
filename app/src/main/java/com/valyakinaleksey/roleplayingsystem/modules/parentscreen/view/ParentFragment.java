package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.valyakinaleksey.roleplayingsystem.R;
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
    implements ParentView {

  private GoogleApiClient googleApiClient;

  @Bind(R.id.toolbar) Toolbar toolbar;

  public static final String TAG = ParentFragment.class.getSimpleName();

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
    ((AbsActivity) getActivity()).setupToolbar();
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

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void showContent() {
    super.showContent();
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
}
