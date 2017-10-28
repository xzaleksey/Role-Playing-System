package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
      ((ParentFragment) fragment).getComponent().getPresenter().navigateBack();
      return;
    }
    super.onBackPressed();
  }

  @Override protected void fillToolbarItems() {

  }

  private void initNavigate() {
    Bundle extras = getIntent().getExtras();
    Fragment fragment = ParentFragment.newInstance(extras);
    setSingleFragment(fragment, ParentFragment.TAG);
  }

  @Override protected void onNewIntent(Intent intent) {
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
    if (fragment != null && intent != null) {
      ((ParentFragment) fragment).handleNewIntent(intent);
    }
  }
}
      