package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.DialogProvider;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseDialogFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ViewPagerAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.DaggerParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;

import java.util.ArrayList;

import butterknife.Bind;

public class ParentGameFragment extends AbsButterLceFragment<ParentGameComponent, ParentGameModel, ParentView> implements ParentView, DialogProvider {

    public static final String TAG = ParentGameFragment.class.getSimpleName();
    public static final String DELETE_GAME = "delete_game";

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private Menu menu;

    public static ParentGameFragment newInstance(Bundle arguments) {
        ParentGameFragment gamesDescriptionFragment = new ParentGameFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ParentGameComponent createComponent() {
        return DaggerParentGameComponent
                .builder()
                .parentFragmentComponent(((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        tabLayout = ((TabLayout) getActivity().findViewById(R.id.tabs));
        tabLayout.setupWithViewPager(viewPager);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), new ArrayList<>());
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void onStart() {
        super.onStart();
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        tabLayout.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.parent_game_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        if (data == null || !data.isMaster()) {
            MenuItem item = getDeleteItem(menu);
            item.setVisible(false);
        }
    }

    @Override
    public void showContent() {
        super.showContent();
        preFillModel(data);
        if (data.isMaster()) {
            getDeleteItem(menu).setVisible(true);
        }
        if (viewPager.getAdapter() == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(GameModel.KEY, data.getGameModel());
            if (data.isMaster()) {
                adapter.addFragment(MasterGameEditFragment.newInstance(arguments), getString(R.string.info));
                adapter.addFragment(MasterLogFragment.newInstance(arguments), getString(R.string.log));
            }
            adapter.addFragment(new Fragment(), "TWO");
            adapter.addFragment(new Fragment(), "THREE");
            viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void preFillModel(ParentGameModel data) {
        super.preFillModel(data);
        ((AbsActivity) getActivity()).setToolbarTitle(data.getGameModel().getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                BaseDialogFragment.newInstance(this).show(getFragmentManager(), DELETE_GAME);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected int getContentResId() {
        return R.layout.fragment_game;
    }

    @Override
    public void onDestroyView() {
        tabLayout.setupWithViewPager(null);
        super.onDestroyView();
    }

    @Override
    public void navigate() {
        String navigationTag = data.getNavigationTag();
        ComponentManagerFragment fragment = null;
//        Bundle arguments = new Bundle();
//        arguments.putParcelable(GameModel.KEY, data.getGameModel());
        if (ParentGameModel.USER_SCREEN.equals(navigationTag)) {
            showSnackbarString("user");
        } else if (ParentGameModel.MASTER_SCREEN.equals(navigationTag)) {
            showSnackbarString("master");
        }
    }

    private MenuItem getDeleteItem(Menu menu) {
        return menu.findItem(R.id.delete);
    }

    @Override
    public Dialog getDialog(String tag) {
        switch (tag) {
            case DELETE_GAME:
                return new MaterialDialog.Builder(getContext())
                        .title(R.string.delete_game)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive((dialog, which) -> {
                            getComponent().getPresenter().deleteGame();
                        }).build();
        }
        return null;
    }
}
