package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.parcelable;

import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.NavigationViewState;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.ViewNavigationResolver;


/**
 * Implementation of helper class to abstract away details of view-based navigation with Parcelable data
 */
public class ParcelableViewNavigationResolver<V> extends ViewNavigationResolver<V, Parcelable> {

    public ParcelableViewNavigationResolver(NavigationViewState<V, Parcelable> viewState) {
        super(viewState);
    }
}
