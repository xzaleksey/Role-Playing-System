package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListModule;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import com.valyakinaleksey.roleplayingsystem.utils.extensions.DialogExtensionsKt;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll.HideFablListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class GamesListFragment
    extends AbsButterLceFragment<GamesListComponent, GamesListViewModel, GamesListView>
    implements GamesListView {

  public static final String TAG = GamesListFragment.class.getSimpleName();
  public static final int DEBOUNCE_TIMEOUT = 200;
  public static final int THROTTLE_INTERVAL = 100;

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindString(R.string.error_empty_field) String errorEmptyField;
  @BindView(R.id.fab) FloatingActionButton fab;
  @BindView(R.id.tv_games_count) TextView tvGamesGount;

  private FlexibleAdapter<IFlexible<?>> flexibleAdapter;
  private MaterialDialog dialog;
  private SearchView searchView;
  private MenuItem searchViewItem;

  public static GamesListFragment newInstance(Bundle args) {
    return new GamesListFragment();
  }

  @SuppressWarnings("unchecked") @Override protected GamesListComponent createComponent(
      String fragmentId) {
    return ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent()
        .getGamesListComponent(new GamesListModule(fragmentId));
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
    setHasOptionsMenu(true);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.games_list_menu, menu);
    searchViewItem = menu.findItem(R.id.action_search);
    searchViewItem.setShowAsAction(
        MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
    initSearchView();
    searchViewItem.setActionView(searchView);
  }

  @Override public void onStart() {
    super.onStart();
    searchView.post(() -> {
      if (searchView != null) {
        if (searchViewItem != null && data != null) {
          String query = data.getFilterModel().getQuery();
          if (!StringUtils.isEmpty(query)) {
            searchViewItem.expandActionView();
            searchView.setQuery(query, false);
          }
        }
        compositeSubscription.add(RxSearchView.queryTextChangeEvents(searchView)
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .throttleLast(THROTTLE_INTERVAL, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(searchViewQueryTextEvent -> {
              Timber.d("new query " + searchViewQueryTextEvent.queryText().toString());
              getComponent().getPresenter()
                  .onSearchQueryChanged(searchViewQueryTextEvent.queryText().toString());
            }));
      }
    });
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    tvGamesGount.setText(StringUtils.getPluralById(R.plurals.games_count, 0));
    setupFabButton();
    initSearchView();
    flexibleAdapter =
        new FlexibleAdapter<>(data == null ? Collections.emptyList() : data.getItems());
    flexibleAdapter.mItemClickListener = position -> getComponent().getPresenter().onItemClick(flexibleAdapter.getItem(position));
    recyclerView.addOnScrollListener(new HideFablListener(fab));
    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
        ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
    recyclerView.setAdapter(flexibleAdapter);
  }

  private void initSearchView() {
    if (searchView != null) {
      return;
    }
    searchView = new SearchView(
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void showContent() {
    super.showContent();
    restoreDialogs();
    updateGamesCount();
    List<IFlexible<?>> items = data.getItems();
    flexibleAdapter.updateDataSet(items, true);
  }

  private void restoreDialogs() {
    if (data.getCreateGameDialogViewModel() != null && (dialog == null || !dialog.isShowing())) {
      showCreateGameDialog();
    }
    if (data.getPasswordDialogViewModel() != null && (dialog == null || !dialog.isShowing())) {
      showPasswordDialog();
    }
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

  @Override public void scrollToTop() {
    recyclerView.postDelayed(() -> {
      if (recyclerView != null) {
        recyclerView.getLayoutManager().scrollToPosition(0);
      }
    }, 50);
  }

  @Override public void onDestroyView() {
    dialog = null;
    searchView = null;
    super.onDestroyView();
  }

  private void setupFabButton() {
    fab.setOnClickListener(v -> getComponent().getPresenter().onFabPressed());
  }
}
