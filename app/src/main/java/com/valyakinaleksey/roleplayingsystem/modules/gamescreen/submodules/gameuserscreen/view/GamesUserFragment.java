package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.di.DaggerGamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.di.DaggerGamesUserComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.di.GamesUserComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.GamesUserModel;

import javax.inject.Inject;

import butterknife.Bind;

public class GamesUserFragment extends AbsButterLceFragment<GamesUserComponent, GamesUserModel, GamesUserView> implements GamesUserView {

    public static final String TAG = GamesUserFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private MaterialDialog dialog;

    @Inject
    SectionsAdapter sectionsAdapter;

    public static GamesUserFragment newInstance(Bundle arguments) {
        GamesUserFragment gamesDescriptionFragment = new GamesUserFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    protected GamesUserComponent createComponent() {
        return DaggerGamesUserComponent
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

    @Override
    public void updateView() {
        ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
        sectionsAdapter.notifyDataSetChanged();
    }
}
