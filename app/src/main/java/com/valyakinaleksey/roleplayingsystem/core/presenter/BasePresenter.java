package com.valyakinaleksey.roleplayingsystem.core.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.core.exceptions.NetworkConnectionException;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AccessFirebaseException;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;

import java.util.concurrent.TimeoutException;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public abstract class BasePresenter<V extends LceView<DATA>, DATA extends RequestUpdateViewModel>
    implements Presenter<V>, RestorablePresenter<DATA> {

  protected V view;
  public Runnable showLoading = () -> view.showLoading();
  public Runnable hideLoading = () -> view.hideLoading();
  protected CompositeSubscription compositeSubscription;
  protected DATA viewModel;

  public BasePresenter() {
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void attachView(V view) {
    this.view = view;
  }

  @Override public void detachView() {
    this.view = null;
  }

  @Override public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
    if (viewModel == null) {
      viewModel = initNewViewModel(arguments);
    } else {
      restoreViewModelWithSavedInstanceState(savedInstanceState);
    }
  }

  protected abstract DATA initNewViewModel(Bundle arguments);

  protected void restoreViewModelWithSavedInstanceState(Bundle savedInstanceState) {

  }

  @Override public void onSaveInstanceState(Bundle bundle) {

  }

  @Override public void onDestroy() {
    compositeSubscription.unsubscribe();
    detachView();
  }

  public boolean handleThrowable(Throwable throwable) {
    if (throwable instanceof NetworkConnectionException || throwable instanceof TimeoutException) {
      view.showError(BaseError.NO_CONNECTION);
      return true;
    } else if (throwable instanceof AccessFirebaseException) {
      BaseError toast = BaseError.TOAST;
      toast.setValue(throwable.getMessage());
      view.showError(toast);
    } else {
      Crashlytics.logException(throwable);
    }
    Timber.d(this.getClass().getSimpleName());
    Timber.d(throwable);
    return false;
  }

  @Override public void restoreViewModel(DATA viewModel) {
    this.viewModel = viewModel;
  }
}
      