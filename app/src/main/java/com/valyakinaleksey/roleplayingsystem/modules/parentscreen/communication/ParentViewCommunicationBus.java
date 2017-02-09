package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.communication;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.state.ParentGameViewState;

import javax.inject.Inject;

@PerFragmentScope public class ParentViewCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<ParentModel, ParentView, ParentPresenter, ParentGameViewState>
    implements ParentPresenter, ParentView {

  @Override public void attachView(ParentView view) {
    super.attachView(view);
  }

  @Inject
  public ParentViewCommunicationBus(ParentPresenter presenter, ParentGameViewState viewState) {
    super(presenter, viewState);
  }

  @Override public void navigateTo(Fragment fragment, Bundle args) {
    presenter.navigateTo(fragment, args);
  }

  @Override public void navigateToFragment(int navId, Bundle args) {
    presenter.navigateToFragment(navId, args);
  }

  @Override public void navigateBack() {
    presenter.navigateBack();
  }

  @Override public void showToolbarLoading(boolean visible) {
    getNavigationResolver().resolveNavigation(parentView -> parentView.showToolbarLoading(visible));
  }

  @Override public void getNavigationFragment(Bundle args) {
    getNavigationResolver().resolveNavigation(parentView -> parentView.getNavigationFragment(args));
  }
}
