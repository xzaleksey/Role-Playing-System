package com.valyakinaleksey.roleplayingsystem.view.activity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;
import com.valyakinaleksey.roleplayingsystem.view.fragment.LoginFragment;

public class LoginActivityUpdated extends AbsSingleFragmentActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            logger.d("user is null");
        } else {
            logger.d(currentUser.toString());
        }
        if (savedInstanceState == null) {
            setSingleFragment(LoginFragment.newInstance(), LoginFragment.TAG);
        }
    }


    @Override
    protected void setupToolbar() {

    }

    @Override
    protected void setupToolbarImpl() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.single_fragment_activity_no_toolbar;
    }
}
      