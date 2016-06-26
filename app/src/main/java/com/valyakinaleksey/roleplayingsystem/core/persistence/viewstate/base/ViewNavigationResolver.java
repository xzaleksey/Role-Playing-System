package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;


import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action1;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action2;

/**
 * Helper class to abstract away details of view-based navigation
 */
public abstract class ViewNavigationResolver<V, T> {

    private final NavigationViewState<V, T> viewState;
    private V view;

    public ViewNavigationResolver(NavigationViewState<V, T> viewState) {
        this.viewState = viewState;
    }

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public <S extends T> void resolveNavigation(Action2<V, S> op, S data) {
        if (view == null) {
            viewState.addToPending(op, data);
        } else {
            op.invoke(view, data);
        }
    }

    public void resolveNavigation(Action1<V> op) {
        if (view == null) {
            viewState.addToPending(op);
        } else {
            op.apply(view);
        }
    }

}
