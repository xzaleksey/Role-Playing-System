package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragment;

public class ParentActivity extends AbsSingleFragmentActivity {

  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      initNavigate();
    }
  }

  @Override protected int getLayoutId() {
    return R.layout.parent_fragment_activity;
  }

  @Override public void onBackPressed() {
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
    if (fragment != null && fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
      // Get the fragment fragment manager - and pop the backstack
      FragmentManager childFragmentManager = fragment.getChildFragmentManager();
      childFragmentManager.popBackStack();
      childFragmentManager.executePendingTransactions();
    }
    // Else, nothing in the direct fragment back stack
    else {
      // Let super handle the back press
      super.onBackPressed();
    }
  }

  @Override protected void fillToolbarItems() {

  }

  private void initNavigate() {
    Bundle extras = getIntent().getExtras();
    Fragment fragment = ParentFragment.newInstance(extras);
    setSingleFragment(fragment, ParentFragment.TAG);
  }
}
      