package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;

@PerFragment
public class ParentGamePresenterImpl extends BasePresenter<ParentView, ParentModel> implements ParentPresenter {


    public ParentGamePresenterImpl() {
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
        view.navigate();
    }

    @Override
    public void navigateTo(Fragment fragment, Bundle args) {
        Fragment navFragment = null;
        switch (viewModel.getNavigationId()) {

            case NavigationUtils.GAMES_LIST:
                navFragment = GamesListFragment.newInstance();
                fragment.getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.inner_fragment_container, navFragment)
                        .commit();
                break;
        }
    }
}
