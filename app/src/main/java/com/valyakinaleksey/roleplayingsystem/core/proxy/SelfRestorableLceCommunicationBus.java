package com.valyakinaleksey.roleplayingsystem.core.proxy;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.LceViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.SelfRestorableViewState;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;

/**
 * Base class for all communication buses for {@link LceView} with ability to save / restore
 * ViewState automatically
 */
public abstract class
SelfRestorableLceCommunicationBus<D extends RequestUpdateViewModel, V extends LceView<D>, P extends Presenter<V>, VS extends LceViewState<D, V> & SelfRestorableViewState>
    extends LceCommunicationBus<D, V, P, VS> {

  public SelfRestorableLceCommunicationBus(P presenter, VS viewState) {
    super(presenter, viewState);
  }

  @Override public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      viewState.restore();
      D data = viewState.getData();
      if (data != null) {
        data.setRestored(true);
      }
      restorePresenterViewModelIfNeeded();
    } else {
      viewState.clean();
    }
    super.onCreate(arguments, savedInstanceState);
  }

  @SuppressWarnings("unchecked") private void restorePresenterViewModelIfNeeded() {
    if (presenter instanceof RestorablePresenter) {
      ((RestorablePresenter<D>) presenter).restoreViewModel(viewState.getData());
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    viewState.clean();
  }
}
