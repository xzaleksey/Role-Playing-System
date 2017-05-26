package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.di.DaggerGamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.di.GamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;
import javax.inject.Inject;
import timber.log.Timber;

public class GamesDescriptionFragment extends
    AbsButterLceFragment<GamesDescriptionComponent, GamesDescriptionModel, GamesDescriptionView>
    implements GamesDescriptionView {

  public static final String TAG = GamesDescriptionFragment.class.getSimpleName();

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.join_game_btn) Button btnJoinGame;

  @Inject SectionsAdapter sectionsAdapter;

  public static GamesDescriptionFragment newInstance(Bundle arguments) {
    GamesDescriptionFragment gamesDescriptionFragment = new GamesDescriptionFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked") protected GamesDescriptionComponent createComponent() {
    return DaggerGamesDescriptionComponent.builder()
        .parentFragmentComponent(
            ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    btnJoinGame.setOnClickListener(v -> {
      getComponent().getPresenter().joinGame();
    });
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void showContent() {
    super.showContent();
    Timber.d(data.toString());
    sectionsAdapter.update(data.getInfoSections());
    recyclerView.setAdapter(sectionsAdapter);
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_game_description;
  }

  @Override public void updateView() {
    ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
    sectionsAdapter.notifyDataSetChanged();
  }

  @Override public void preFillModel(GamesDescriptionModel data) {
    ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
  }

  @Override public void updateView(InfoSection userInfosection, DataEvent dataEvent) {
    if (sectionsAdapter != null) {
      sectionsAdapter.updateSection(userInfosection, dataEvent);
    }
  }
}
