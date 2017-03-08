package com.valyakinaleksey.roleplayingsystem.core.proxy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.LceViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.NavigationViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.SelfRestorableViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.SerializableViewNavigationResolver;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action1;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.ShouldRequestUpdateViewModel;

import java.io.Serializable;

/**
 * Base class for all communication buses for {@link LceView} with view-based navigation(i.e. showing dialog, transition to another Activity etc.)
 * And also it inherits the ability to save / restore ViewState(but with pending view state navigations this time) automatically
 */
public class SelfRestorableNavigationLceCommunicationBus<D extends ShouldRequestUpdateViewModel,
        V extends LceView<D>,
        P extends Presenter<V>,
        VS extends LceViewState<D, V> & SelfRestorableViewState & NavigationViewState<V, Serializable>>
        extends SelfRestorableLceCommunicationBus<D, V, P, VS> {

    private SerializableViewNavigationResolver<V> navigationResolver;

    public SelfRestorableNavigationLceCommunicationBus(P presenter, VS viewState) {
        super(presenter, viewState);
    }

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        navigationResolver = new SerializableViewNavigationResolver<>(viewState);
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        navigationResolver.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        navigationResolver.detachView();
    }

    public SerializableViewNavigationResolver<V> getNavigationResolver() {
        return navigationResolver;
    }

    @Override
    public void performAction(Action1<Context> contextAction) {
        getNavigationResolver().resolveNavigation(authView -> authView.performAction(contextAction));
    }

    @Override
    public void showMessage(CharSequence message, @MessageType int type) {
        getNavigationResolver().resolveNavigation(v -> v.showMessage(message, type));
    }

    @Override
    public void preFillModel(D data) {
        getNavigationResolver().resolveNavigation(v -> v.preFillModel(data));
    }
}
