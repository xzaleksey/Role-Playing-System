package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view;

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
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.di.DaggerMasterComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.di.MasterComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model.MasterModel;

import javax.inject.Inject;

import butterknife.Bind;

public class MasterFragment extends AbsButterLceFragment<MasterComponent, MasterModel, MasterView> implements MasterView {

    public static final String TAG = MasterFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private MaterialDialog dialog;

    @Inject
    SectionsAdapter sectionsAdapter;

    public static MasterFragment newInstance(Bundle arguments) {
        MasterFragment gamesDescriptionFragment = new MasterFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    protected MasterComponent createComponent() {
        return DaggerMasterComponent
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

    @Override
    public void updateView() {
        ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
        sectionsAdapter.notifyDataSetChanged();
    }
}
