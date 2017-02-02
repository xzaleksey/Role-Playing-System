package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.DialogProvider;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseDialogFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ViewPagerAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.DaggerParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;

import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;
import java.util.ArrayList;

import butterknife.Bind;

import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_MAPS_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_MASTER_EDIT_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_MASTER_LOG_FRAGMENT;

public class ParentGameFragment
    extends AbsButterLceFragment<ParentGameComponent, ParentGameModel, ParentView>
    implements ParentView, DialogProvider {

  public static final String TAG = ParentGameFragment.class.getSimpleName();
  public static final String DELETE_GAME = "delete_game";

  @Bind(R.id.viewpager) ViewPager viewPager;

  private TabLayout tabLayout;
  private ViewPagerAdapter adapter;
  private Menu menu;

  public static ParentGameFragment newInstance(Bundle arguments) {
    ParentGameFragment gamesDescriptionFragment = new ParentGameFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked") protected ParentGameComponent createComponent() {
    return DaggerParentGameComponent.builder()
        .parentFragmentComponent(
            ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
    adapter = new ViewPagerAdapter(getChildFragmentManager(), new ArrayList<>(), getArguments());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    tabLayout = ((TabLayout) getActivity().findViewById(R.id.tabs));
    tabLayout.setupWithViewPager(viewPager);
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        KeyboardUtils.hideKeyboard(getActivity());
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.parent_game_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
    this.menu = menu;
    MenuItem item = getDeleteItem(menu);
    if (data == null || !data.isMaster()) {
      item.setVisible(false);
    } else if (data != null && data.isMaster()) {
      item.setVisible(true);
    }
  }

  @Override public void showContent() {
    super.showContent();
    preFillModel(data);
    if (data.isMaster() && menu != null) {
      getDeleteItem(menu).setVisible(true);
    }
    if (viewPager.getAdapter() == null) {
      Bundle arguments = new Bundle();
      arguments.putParcelable(GameModel.KEY, data.getGameModel());
      if (data.isMaster()) {
        adapter.addFragment(GAME_MASTER_EDIT_FRAGMENT, getString(R.string.info));
        adapter.addFragment(GAME_MASTER_LOG_FRAGMENT, getString(R.string.log));
      }
      adapter.addFragment(GAME_MAPS_FRAGMENT, getString(R.string.maps));
      viewPager.setAdapter(adapter);
    }
  }

  @Override public void preFillModel(ParentGameModel data) {
    super.preFillModel(data);
    ((AbsActivity) getActivity()).setToolbarTitle(data.getGameModel().getName());
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.delete:
        BaseDialogFragment.newInstance(this).show(getFragmentManager(), DELETE_GAME);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_game;
  }

  @Override public void onDestroyView() {
    tabLayout.setupWithViewPager(null);
    super.onDestroyView();
  }

  @Override public void navigate() {

  }

  @Override public void onStart() {
    super.onStart();
    tabLayout.setVisibility(View.VISIBLE);
  }

  @Override public void onStop() {
    tabLayout.setVisibility(View.GONE);
    super.onStop();
  }

  private MenuItem getDeleteItem(Menu menu) {
    return menu.findItem(R.id.delete);
  }

  @Override public Dialog getDialog(String tag) {
    switch (tag) {
      case DELETE_GAME:
        return new MaterialDialog.Builder(getContext()).title(R.string.delete_game)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive((dialog, which) -> getComponent().getPresenter().deleteGame())
            .build();
    }
    return null;
  }
}
