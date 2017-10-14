package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.GameListAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.GameViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.DaggerGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListModule;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.utils.DialogExtensionsKt;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll.HideFablListener;

public class GamesListFragment
    extends AbsButterLceFragment<GamesListComponent, GamesListViewViewModel, GamesListView>
    implements GamesListView {

  public static final String TAG = GamesListFragment.class.getSimpleName();

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindString(R.string.error_empty_field) String errorEmptyField;
  @BindView(R.id.fab) FloatingActionButton fab;
  @BindView(R.id.tv_games_count) TextView tvGamesGount;

  private GameListAdapter gameListAdapter;
  private MaterialDialog dialog;

  public static GamesListFragment newInstance(Bundle args) {
    return new GamesListFragment();
  }

  @SuppressWarnings("unchecked") @Override protected GamesListComponent createComponent() {
    return DaggerGamesListComponent.builder()
        .parentFragmentComponent(
            ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
        .gamesListModule(new GamesListModule())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    tvGamesGount.setText(StringUtils.getPluralById(R.plurals.games_count, 0));
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);
    setupFabButton();
    recyclerView.addOnScrollListener(new HideFablListener(fab));
    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
        ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void showContent() {
    super.showContent();
    ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
    if (gameListAdapter == null) {
      gameListAdapter =
          new GameListAdapter(GameModel.class, R.layout.games_list_item, GameViewHolder.class,
              data.getQuery(), getComponent().getPresenter());
      gameListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        @Override public void onItemRangeInserted(int positionStart, int itemCount) {
          super.onItemRangeInserted(positionStart, itemCount);
          if (positionStart == 0) {
            getComponent().getPresenter().loadComplete();
          }
        }
      });
    }
    if (recyclerView.getAdapter() == null) {
      recyclerView.setAdapter(gameListAdapter);
    }
    if (data.getCreateGameDialogViewModel() != null && (dialog == null || !dialog.isShowing())) {
      showCreateGameDialog();
    }
    if (data.getPasswordDialogViewModel() != null && (dialog == null || !dialog.isShowing())) {
      showPasswordDialog();
    }
    updateGamesCount();
  }

  @Override public void onDestroy() {
    if (gameListAdapter != null) {
      gameListAdapter.cleanup();
    }
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_games_list;
  }

  @Override public void onGameCreated() {
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
  }

  @Override public void showCreateGameDialog() {
    dialog =
        DialogExtensionsKt.showCreateGameDialog(getContext(), data, getComponent().getPresenter());
  }

  @Override public void showPasswordDialog() {
    dialog =
        DialogExtensionsKt.showPasswordDialog(getContext(), data, getComponent().getPresenter());
  }

  @Override public void updateGamesCount() {
    int gamesCount = data.getGamesCount();
    if (gamesCount > 0) {
      tvGamesGount.setText(StringUtils.getPluralById(R.plurals.games_count, gamesCount));
      tvGamesGount.setVisibility(View.VISIBLE);
    } else {
      tvGamesGount.setVisibility(View.GONE);
    }
  }

  @Override public void onDestroyView() {
    dialog = null;
    super.onDestroyView();
  }

  private void setupFabButton() {
    fab.setOnClickListener(v -> getComponent().getPresenter().onFabPressed());
  }
}
