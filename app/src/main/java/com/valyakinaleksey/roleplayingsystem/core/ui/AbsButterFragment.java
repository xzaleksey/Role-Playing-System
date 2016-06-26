package com.valyakinaleksey.roleplayingsystem.core.ui;

import android.view.View;

import com.valyakinaleksey.roleplayingsystem.core.view.AbsFragment;

import butterknife.ButterKnife;

/**
 * {link AbsFragment} with ButterKnife support
 * Move outside core because one can not make lib with butterknife
 */
public abstract class AbsButterFragment extends AbsFragment {

    @Override
    protected void preSetupViews(final View view) {
        super.preSetupViews(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
