package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.DialogProvider;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.utils.SerializableTuple;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseDialogFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ViewPagerAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.DaggerParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;
import java.util.ArrayList;

public class ParentGameFragment
    extends AbsButterLceFragment<ParentGameComponent, ParentGameModel, ParentView>
    implements ParentView, DialogProvider {
  public static final String TAG = ParentGameFragment.class.getSimpleName();
  public static final String DELETE_GAME = "delete_game";
  public static final String FINISH_GAME = "finish_game";
  public static final String OPEN_GAME = "open_game";
  public static final String LEAVE_GAME = "leave_game";
  public static final int FINISH_GAME_ID = 1;
  public static final int OPEN_GAME_ID = 2;

  @BindView(R.id.viewpager) ViewPager viewPager;

  private TabLayout tabLayout;
  private ViewPagerAdapter adapter;
  private String gameId;

  public static ParentGameFragment newInstance(Bundle arguments) {
    ParentGameFragment gamesDescriptionFragment = new ParentGameFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked") protected ParentGameComponent createComponent() {
    return DaggerParentGameComponent.builder()
        .parentGameModule(new ParentGameModule(gameId))
        .parentFragmentComponent(
            ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    Bundle arguments = savedInstanceState == null ? getArguments() : savedInstanceState;
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    gameId = gameModel.getId();
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    ArrayList<SerializableTuple<Integer, String>> fragmentTitlePairs;
    if (data == null) {
      fragmentTitlePairs = new ArrayList<>();
    } else {
      fragmentTitlePairs = data.getFragmentsInfo();
    }
    adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentTitlePairs, getArguments());
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
    if (data != null) {
      if (data.isMaster()) {
        if (data.getGameModel().isFinished()) {
          menu.add(Menu.NONE, OPEN_GAME_ID, Menu.NONE, R.string.open_game);
        } else {
          menu.add(Menu.NONE, FINISH_GAME_ID, Menu.NONE, R.string.finish_game);
        }
        inflater.inflate(R.menu.parent_game_menu_master, menu);
      } else {
        inflater.inflate(R.menu.parent_game_menu_player, menu);
      }
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public void showContent() {
    super.showContent();
    preFillModel(data);
    if (!data.getFragmentsInfo().isEmpty()) {
      tabLayout.setVisibility(View.VISIBLE);
    }
    invalidateOptionsMenu();
    if (viewPager.getAdapter() == null) {
      if (data.isFirstNavigation() || adapter.getCount() == 0) {
        adapter.clear();
        for (SerializableTuple<Integer, String> integerStringSerializableTuple : data.getFragmentsInfo()) {
          adapter.addFragment(integerStringSerializableTuple);
        }
      }
      viewPager.setAdapter(adapter);
    }
  }

  @Override public void invalidateOptionsMenu() {
    getActivity().invalidateOptionsMenu();
  }

  @Override public void preFillModel(ParentGameModel data) {
    super.preFillModel(data);
    ((AbsActivity) getActivity()).setToolbarTitle(data.getGameModel().getName());
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_delete:
        BaseDialogFragment.newInstance(this).show(getFragmentManager(), DELETE_GAME);
        return true;
      case FINISH_GAME_ID:
        BaseDialogFragment.newInstance(this).show(getFragmentManager(), FINISH_GAME);
        return true;
      case OPEN_GAME_ID:
        getComponent().getPresenter().openGame();
        return true;
      case R.id.action_leave_game:
        BaseDialogFragment.newInstance(this).show(getFragmentManager(), LEAVE_GAME);
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
    tabLayout.setVisibility(View.GONE);
    super.onDestroyView();
  }

  @Override public Dialog getDialog(String tag) {
    switch (tag) {
      case DELETE_GAME:
        return new MaterialDialog.Builder(getContext()).title(R.string.delete_game)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive((dialog, which) -> getComponent().getPresenter().deleteGame())
            .build();
      case FINISH_GAME:
        return new MaterialDialog.Builder(getContext()).title(R.string.finish_game)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive((dialog, which) -> getComponent().getPresenter().finishGame())
            .build();
      case LEAVE_GAME:
        return new MaterialDialog.Builder(getContext()).title(R.string.leave_game)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onPositive((dialog, which) -> getComponent().getPresenter().leaveGame())
            .build();
    }
    return null;
  }
}
