package com.valyakinaleksey.roleplayingsystem.view.model;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.view.WeatherView;

/**
 * Alias name for Weather ViewState
 */
public class WeatherViewState extends StorageBackedNavigationLceViewStateImpl<WeatherViewModel, WeatherView.WeatherError, WeatherView> {

    public WeatherViewState(ViewStateStorage storage) {
        super(storage);
    }
}
