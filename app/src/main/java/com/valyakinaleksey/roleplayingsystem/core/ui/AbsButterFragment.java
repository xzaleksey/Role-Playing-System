package com.valyakinaleksey.roleplayingsystem.core.ui;

import android.view.View;

import butterknife.Unbinder;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsFragment;

import butterknife.ButterKnife;

/**
 * {link AbsFragment} with ButterKnife support
 * Move outside core because one can not make lib with butterknife
 */
public abstract class AbsButterFragment extends AbsFragment {

  private Unbinder unbinder;

  @Override protected void preSetupViews(final View view) {
    super.preSetupViews(view);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
