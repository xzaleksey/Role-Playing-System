package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;
import com.valyakinaleksey.roleplayingsystem.modules.photo.view.ImageFragment;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.BACK;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAMES_LIST;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_DESCRIPTION_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.IMAGE_FRAGMENT;
import static com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.POP_BACKSTACK;

public class ParentPresenterImpl extends BasePresenter<ParentView, ParentModel>
    implements ParentPresenter {

  public ParentPresenterImpl() {
  }

  @SuppressWarnings("unchecked") @Override
  protected ParentModel initNewViewModel(Bundle arguments) {
    final ParentModel parentModel = new ParentModel();
    parentModel.setNavigationTag(GAMES_LIST);
    parentModel.setToolbarTitle(StringUtils.getStringById(R.string.app_name));
    return parentModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    if (viewModel.isUpdatedRequired()) {
      viewModel.setFirstNavigation(false);
      view.getNavigationFragment(null);
      view.setData(viewModel);
      view.showContent();
    }
    compositeSubscription.add(FireBaseUtils.getConnectionObservableWithTimeInterval()
        .compose(RxTransformers.applySchedulers())
        .subscribe(connected -> {
          viewModel.setDisconnected(!connected);
          view.updateToolbar();
        }));
  }

  @Override public void navigateTo(Fragment fragment, Bundle args) {
    Fragment navFragment;
    if (args != null && args.getBoolean(POP_BACKSTACK, false)) {
      fragment.getChildFragmentManager().popBackStackImmediate();
    }
    switch (viewModel.getNavigationId()) {
      case GAMES_LIST:
        navFragment = GamesListFragment.newInstance();
        navigate(fragment, navFragment, false);
        break;
      case GAME_FRAGMENT:
        navFragment = ParentGameFragment.newInstance(args);
        navigate(fragment, navFragment, true);
        break;
      case GAME_DESCRIPTION_FRAGMENT:
        navFragment = GamesDescriptionFragment.newInstance(args);
        navigate(fragment, navFragment, true);
        break;
      case IMAGE_FRAGMENT:
        navFragment = ImageFragment.newInstance(args);
        navigate(fragment, navFragment, true);
        break;
      case BACK:
        break;
    }
  }

  private void navigate(Fragment fragment, Fragment navFragment, boolean addToBackStack) {
    FragmentTransaction transaction = fragment.getChildFragmentManager()
        .beginTransaction()
        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
            android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        .replace(R.id.parent_fragment_container, navFragment);
    if (addToBackStack) {
      transaction.addToBackStack(navFragment.getClass().getSimpleName());
    }
    transaction.commit();
  }

  @Override public void navigateToFragment(int navId, Bundle args) {
    viewModel.setNavigationTag(navId);
    view.getNavigationFragment(args);
  }

  @Override public void navigateBack() {
    Bundle args = new Bundle();
    args.putBoolean(POP_BACKSTACK, true);
    navigateToFragment(NavigationUtils.BACK, args);
  }
}
