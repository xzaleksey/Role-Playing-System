package com.valyakinaleksey.roleplayingsystem.modules.main_screen.view;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;

import timber.log.Timber;

public class MainActivity extends AbsSingleFragmentActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Timber.d("user is null");
        } else {
            Timber.d(currentUser.toString());
        }
        if (savedInstanceState == null) {
            setSingleFragment(GamesListFragment.newInstance(), GamesListFragment.TAG);
        }
    }

    @Override
    protected void fillToolbarItems() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.single_fragment_activity;
    }
}
      