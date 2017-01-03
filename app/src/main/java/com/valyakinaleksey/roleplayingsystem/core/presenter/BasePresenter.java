package com.valyakinaleksey.roleplayingsystem.core.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<V extends LceView<DATA>, DATA extends EmptyViewModel> implements Presenter<V>, RestorablePresenter<DATA> {

    protected V view;
    protected Runnable showLoading = () -> view.showLoading();
    protected Runnable hideLoading = () -> view.hideLoading();
    protected CompositeSubscription compositeSubscription;
    protected DATA viewModel;

    public BasePresenter() {
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        if (viewModel == null) {
            viewModel = initNewViewModel(arguments);
        } else {
            restoreViewModelWithSavedInstanceState(savedInstanceState);
        }
    }

    protected abstract DATA initNewViewModel(Bundle arguments);

    protected void restoreViewModelWithSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onDestroy() {
        compositeSubscription.unsubscribe();
        detachView();
    }

    @Override
    public void restoreViewModel(DATA viewModel) {
        this.viewModel = viewModel;
    }
}
      