package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.di.DaggerGamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.di.GamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;

import butterknife.Bind;
import butterknife.BindString;

public class GamesDescriptionFragment extends AbsButterLceFragment<GamesDescriptionComponent, GamesDescriptionModel, GamesDescriptionView> implements GamesDescriptionView {

    public static final String TAG = GamesDescriptionFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindString(R.string.error_empty_field)
    String errorEmptyField;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private MaterialDialog dialog;


    public static GamesDescriptionFragment newInstance(Bundle arguments) {
        GamesDescriptionFragment gamesDescriptionFragment = new GamesDescriptionFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    protected GamesDescriptionComponent createComponent() {
        return DaggerGamesDescriptionComponent
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
        setupFabButton();
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
    }

    @Override
    public void showContent() {
        super.showContent();
        ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected int getContentResId() {
        return R.layout.fragment_games_list;
    }

    @Override
    public void onDestroyView() {
        dialog = null;
        super.onDestroyView();
    }

    private void setupFabButton() {
        fab.setOnClickListener(v -> {
            getComponent().getPresenter().onFabPressed();
        });
    }
}
