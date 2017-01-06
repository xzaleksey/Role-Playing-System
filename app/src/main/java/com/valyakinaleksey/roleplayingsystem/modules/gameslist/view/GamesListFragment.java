package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.GameListAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.GameViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.DaggerGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;

import butterknife.Bind;

public class GamesListFragment extends AbsButterLceFragment<GamesListComponent, GamesListViewModel, GamesListView> implements GamesListView {

    public static final String TAG = GamesListFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    private GameListAdapter gameListAdapter;

    public static GamesListFragment newInstance() {
        return new GamesListFragment();
    }

    @Override
    protected GamesListComponent createComponent() {
        return DaggerGamesListComponent
                .builder()
                .appComponent(RpsApp.getAppComponent(getActivity()))
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        fab.setOnClickListener(v -> {
            getComponent().getPresenter().createGame(new GameModel("name1", "description1"));
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AbsActivity) getActivity()).setToolbarTitle("test");
    }

    @Override
    public void showContent() {
        super.showContent();
        if (gameListAdapter == null) {
            gameListAdapter = new GameListAdapter(GameModel.class, R.layout.games_list_item, GameViewHolder.class, data.getReference(), getComponent().getPresenter().getValue());
            gameListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (positionStart == 0) {
                        getComponent().getPresenter().loadComplete();
                    }
//                    int chatMessageCount = gameListAdapter.getItemCount();
//                    int lastVisiblePosition =
//                            ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                    if (lastVisiblePosition == -1 ||
//                            (positionStart >= (chatMessageCount - 1) &&
//                                    lastVisiblePosition == (positionStart - 1))) {
//                        recyclerView.scrollToPosition(positionStart);
//                    }
                }
            });
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(gameListAdapter);
        }
    }


    @Override
    public void onDestroy() {
        if (gameListAdapter != null) {
            gameListAdapter.cleanup();
        }
        super.onDestroy();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_games_list;
    }

    @Override
    public void scrollToBottom() {
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
    }
}
