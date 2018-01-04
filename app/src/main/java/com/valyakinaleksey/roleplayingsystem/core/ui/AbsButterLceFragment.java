package com.valyakinaleksey.roleplayingsystem.core.ui;

import android.view.View;

import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {link AbsLceFragment} with ButterKnife support
 * Move outside core because one can not make lib with butterknife
 */
public abstract class AbsButterLceFragment<C extends HasPresenter, M extends RequestUpdateViewModel, V extends LceView<M>>
        extends AbsLceFragment<C, M, V> {

    protected Unbinder unbinder;

    @Override
    protected void preSetupViews(View view) {
        super.preSetupViews(view);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
