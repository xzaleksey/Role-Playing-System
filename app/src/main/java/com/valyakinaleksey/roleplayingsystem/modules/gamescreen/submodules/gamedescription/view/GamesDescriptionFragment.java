package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.di.DaggerGamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.di.GamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;

import javax.inject.Inject;

import butterknife.Bind;

public class GamesDescriptionFragment extends AbsButterLceFragment<GamesDescriptionComponent, GamesDescriptionModel, GamesDescriptionView> implements GamesDescriptionView {

    public static final String TAG = GamesDescriptionFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private MaterialDialog dialog;

    @Inject
    SectionsAdapter sectionsAdapter;

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
        sectionsAdapter.update(data.getInfoSections());
        recyclerView.setAdapter(sectionsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected int getContentResId() {
        return R.layout.fragment_game_description;
    }

    @Override
    public void onDestroyView() {
        dialog = null;
        super.onDestroyView();
    }

}