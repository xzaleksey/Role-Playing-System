package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;


import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;

/**
 * Base ViewState implementation for {@link LceView} with view-based navigation and ability to restore itself
 * ~~ means, save / restore logic is internal to the object implementing this interface
 */
public abstract class AbsSelfRestorableNavigationLceViewStateImpl<D extends RequestUpdateViewModel,  V extends LceView<D>, T>
                        extends AbsNavigationLceViewStateImpl<D, V, T>
                        implements SelfRestorableViewState {
  @Override public void setData(D data) {
    super.setData(data);
    save();
  }
}
