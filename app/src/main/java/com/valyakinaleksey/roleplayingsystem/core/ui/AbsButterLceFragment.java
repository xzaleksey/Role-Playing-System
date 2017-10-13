package com.valyakinaleksey.roleplayingsystem.core.ui;

import android.content.Context;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action1;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;

/**
 * {link AbsLceFragment} with ButterKnife support
 * Move outside core because one can not make lib with butterknife
 */
public abstract class AbsButterLceFragment<C extends HasPresenter, M extends RequestUpdateViewModel, V extends LceView<M>>
    extends AbsLceFragment<C, M, V> {

  private Unbinder unbinder;

  @Override protected void preSetupViews(View view) {
    super.preSetupViews(view);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void performAction(Action1<Context> contextAction) {
    contextAction.apply(getActivity());
  }
}
