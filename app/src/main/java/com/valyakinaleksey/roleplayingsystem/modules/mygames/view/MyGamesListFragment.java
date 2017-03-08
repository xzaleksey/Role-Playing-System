package com.valyakinaleksey.roleplayingsystem.modules.mygames.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import butterknife.BindString;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.DaggerMyGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListModule;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll.HideFablListener;

public class MyGamesListFragment
    extends AbsButterLceFragment<MyGamesListComponent, GamesListViewModel, MyGamesListView>
    implements MyGamesListView {

  public static final String TAG = MyGamesListFragment.class.getSimpleName();

  @Bind(R.id.recycler_view) RecyclerView recyclerView;
  @BindString(R.string.error_empty_field) String errorEmptyField;
  @Bind(R.id.fab) FloatingActionButton fab;

  public static MyGamesListFragment newInstance() {
    return new MyGamesListFragment();
  }

  @SuppressWarnings("unchecked") @Override protected MyGamesListComponent createComponent() {
    return DaggerMyGamesListComponent.builder()
        .parentFragmentComponent(
            ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
        .myGamesListModule(new MyGamesListModule())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
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
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_games_list;
  }

  @Override public void onGameCreated() {
    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
  }

  @Override public void showCreateGameDialog() {

  }

  @Override public void showPasswordDialog() {

  }


  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  private void setupFabButton() {
    fab.setOnClickListener(v -> getComponent().getPresenter().onFabPressed());
  }
}
