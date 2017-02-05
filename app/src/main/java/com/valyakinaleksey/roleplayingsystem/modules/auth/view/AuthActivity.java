package com.valyakinaleksey.roleplayingsystem.modules.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;

import timber.log.Timber;

public class AuthActivity extends AbsSingleFragmentActivity {
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser == null) {
      Timber.d("user is null");
    } else {
      Timber.d(currentUser.toString());
    }
    if (savedInstanceState == null) {
      setSingleFragment(AuthFragment.newInstance(), AuthFragment.TAG);
    }
  }

  @Override public void setupToolbar() { // empty for remove toolbar

  }

  @Override protected int getLayoutId() {
    return R.layout.single_fragment_activity_no_toolbar;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode,
        data); // fix for start activity for result from activity (in AuthPresenter)
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
    fragment.onActivityResult(requestCode, resultCode, data);
  }
}
      