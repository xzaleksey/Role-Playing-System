package com.valyakinaleksey.roleplayingsystem.core.proxy;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.AdapterNotifier;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.LceViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.NavigationViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.SelfRestorableViewState;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.ShouldRequestUpdateViewModel;
import java.io.Serializable;

public abstract class AdapterNotifierCommunicationBus<D extends ShouldRequestUpdateViewModel, V extends LceView<D> & AdapterNotifier, P extends Presenter<V>, VS extends LceViewState<D, V> & SelfRestorableViewState & NavigationViewState<V, Serializable>>
    extends SelfRestorableNavigationLceCommunicationBus<D, V, P, VS> implements AdapterNotifier {
  public AdapterNotifierCommunicationBus(P presenter, VS viewState) {
    super(presenter, viewState);
  }

  @Override public void notifyItemInserted(int index) {
    if (view != null) {
      view.notifyItemInserted(index);
    }
  }

  @Override public void notifyItemChanged(int index) {
    if (view != null) {
      view.notifyItemChanged(index);
    }
  }

  @Override public void notifyItemRemoved(int index) {
    if (view != null) {
      view.notifyItemRemoved(index);
    }
  }

  @Override public void notifyItemMoved(int oldIndex, int index) {
    if (view != null) {
      view.notifyItemMoved(oldIndex, index);
    }
  }
}
      