package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.Bind;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.core.view.customview.AnimatedTitlesLayout;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di.GamesCharactersModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di.HasGameCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.RecyclerViewUtils;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll.HideFablListener;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;

@AutoComponent(dependencies = { ParentGameComponent.class },
    modules = GamesCharactersModule.class,
    superinterfaces = { GlobalComponent.class, HasGameCharactersPresenter.class }) @GameScope
@AutoInjector public class GamesCharactersFragment extends
    AbsButterLceFragment<GamesCharactersFragmentComponent, GamesCharactersViewModel, GamesCharactersView>
    implements GamesCharactersView {

  public static final String TAG = GamesCharactersFragment.class.getSimpleName();

  @Bind(R.id.recycler_view) RecyclerView recyclerView;
  @Bind(R.id.fab) FloatingActionButton fab;
  @Bind(R.id.title_switcher) AnimatedTitlesLayout titleLayout;

  FlexibleAdapter<IFlexible> flexibleAdapter;
  private HideFablListener listener;

  public static GamesCharactersFragment newInstance(Bundle arguments) {
    GamesCharactersFragment gamesDescriptionFragment = new GamesCharactersFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked")
  protected GamesCharactersFragmentComponent createComponent() {
    return DaggerGamesCharactersFragmentComponent.builder()
        .parentGameComponent(
            ((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment()).getComponent())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    fab.setOnClickListener(v -> getComponent().getPresenter().createCharacter());
    listener = new HideFablListener(fab);
    if (data != null) {
      updateNavTabs(data);
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
    if (titleLayout.getTitleModels().isEmpty()) {
      updateNavTabs(data);
    }
    if (recyclerView.getAdapter() == null) {
      flexibleAdapter = new FlexibleAdapter<>(new ArrayList<>());
      recyclerView.setAdapter(flexibleAdapter);
    }
    updateView();
    if (data.hasCharacter()) { // if has character - no creation
      recyclerView.removeOnScrollListener(listener);
      fab.hide();
    } else {
      recyclerView.clearOnScrollListeners();
      recyclerView.addOnScrollListener(listener);
      recyclerView.post(() -> RecyclerViewUtils.checkFabShow(recyclerView, fab));
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_game_characters;
  }

  @Override public void updateView() {
    flexibleAdapter.updateDataSet(data.getiFlexibles(), true);
  }

  @Override public void preFillModel(GamesCharactersViewModel data) {
    updateNavTabs(data);
  }

  private void updateNavTabs(GamesCharactersViewModel data) {
    titleLayout.updateModels(data.getTitleModels(), data.getNavigationTab());
  }
}
