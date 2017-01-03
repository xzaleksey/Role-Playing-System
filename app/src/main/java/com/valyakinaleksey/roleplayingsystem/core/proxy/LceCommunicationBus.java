package com.valyakinaleksey.roleplayingsystem.core.proxy;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.LceViewState;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

/**
 * Base classes for all communication buses for {@link LceView}
 * Handles ViewState restore, tracking ViewState for loading - content - error related operations
 */
public class LceCommunicationBus<D extends EmptyViewModel, V extends LceView<D>, P extends Presenter<V>, VS extends LceViewState<D, V>>
        extends CommunicationBus<V, P>
        implements LceView<D> {

    protected VS viewState;

    public LceCommunicationBus(P presenter, VS viewState) {
        super(presenter);
        this.viewState = viewState;
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        viewState.apply(view);
    }

    @Override
    public void getData() {
        presenter.getData();
    }

    @Override
    public void showLoading() {
        viewState.setStateShowLoading();
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        viewState.setStateHideLoading();
        if (view != null) {
            view.hideLoading();
        }
    }

    @Override
    public void setData(D data) {
        viewState.setData(data);
        if (view != null) {
            view.setData(data);
        }
    }

    @Override
    public void showContent() {
        viewState.setStateShowContent();
        if (view != null) {
            view.showContent();
        }
    }

    @Override
    public void showError(BaseError error) {
        boolean isViewAttached = view != null;
        viewState.setStateShowError(error, isViewAttached);
        if (isViewAttached) {
            view.showError(error);
        }
    }

    @Override
    public void loadData() {
        if (view != null) {
            view.loadData();
        }
    }
}
