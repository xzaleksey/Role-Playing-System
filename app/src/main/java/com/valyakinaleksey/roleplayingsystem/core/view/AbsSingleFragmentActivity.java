package com.valyakinaleksey.roleplayingsystem.core.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;


public abstract class AbsSingleFragmentActivity extends AbsActivity {

    protected ViewGroup container;

    @Override
    protected void setupViews() {
        super.setupViews();
        container = (ViewGroup) findViewById(R.id.container);
    }

    protected int getLayoutId() {
        return R.layout.parent_fragment_activity;
    }

    protected void setSingleFragment(final Fragment fragment, final String tag) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(container.getId(), fragment, tag);
        transaction.commit();
    }

}
