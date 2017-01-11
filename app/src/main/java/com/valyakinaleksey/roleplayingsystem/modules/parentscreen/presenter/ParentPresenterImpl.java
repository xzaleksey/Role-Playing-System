package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;

public class ParentPresenterImpl extends BasePresenter<ParentView, ParentModel> implements ParentPresenter {


    public ParentPresenterImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParentModel initNewViewModel(Bundle arguments) {
        final ParentModel parentModel = new ParentModel();
        parentModel.setNavigationTag(NavigationUtils.GAMES_LIST);
        return parentModel;
    }

    @Override
    public void restoreViewModel(ParentModel viewModel) {
        super.restoreViewModel(viewModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        viewModel.setFirstNavigation(false);
        view.getNavigationFragment(null);
    }

    @Override
    public void navigateTo(Fragment fragment, Bundle args) {
        Fragment navFragment = null;
        switch (viewModel.getNavigationId()) {
            case NavigationUtils.GAMES_LIST:
                navFragment = GamesListFragment.newInstance();
                fragment.getChildFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                        .replace(R.id.parent_fragment_container, navFragment)
                        .commit();
                break;
            case NavigationUtils.GAME_FRAGMENT:
                navFragment = ParentGameFragment.newInstance(args);
                fragment.getChildFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
                        .replace(R.id.parent_fragment_container, navFragment)
                        .addToBackStack(ParentGameFragment.TAG)
                        .commit();
                break;
        }
    }

    @Override
    public void navigateToFragment(int navId, Bundle args) {
        viewModel.setNavigationTag(navId);
        view.getNavigationFragment(args);
    }
}
