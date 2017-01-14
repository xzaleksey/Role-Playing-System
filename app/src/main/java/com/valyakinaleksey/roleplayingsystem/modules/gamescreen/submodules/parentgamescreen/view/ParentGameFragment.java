package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.utils.Tuple;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ViewPagerAdapter;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.DaggerMasterComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.MasterComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ChildGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;

import java.util.ArrayList;

import butterknife.Bind;

public class ParentGameFragment extends AbsButterLceFragment<MasterComponent, ParentGameModel, ParentView> implements ParentView {

    public static final String TAG = ParentGameFragment.class.getSimpleName();

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private TabLayout tabLayout;

    public static ParentGameFragment newInstance(Bundle arguments) {
        ParentGameFragment gamesDescriptionFragment = new ParentGameFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MasterComponent createComponent() {
        return DaggerMasterComponent
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
    public void setupViews(View view) {
        super.setupViews(view);
        tabLayout = ((TabLayout) getActivity().findViewById(R.id.tabs));
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), new ArrayList<>());
        adapter.addFragment(new Fragment(), "ONE");
        adapter.addFragment(new Fragment(), "TWO");
        adapter.addFragment(new Fragment(), "THREE");
        viewPager.setAdapter(adapter);
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
    public void showContent() {
        super.showContent();
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
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.inner_fragment_container, fragment)
//                .commit();
    }
}
