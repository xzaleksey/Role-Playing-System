package com.valyakinaleksey.roleplayingsystem.communication;


import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.presenter.WeatherPresenter;
import com.valyakinaleksey.roleplayingsystem.view.CautionDialogData;
import com.valyakinaleksey.roleplayingsystem.view.WeatherView;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewModel;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewState;

import javax.inject.Inject;

@PerFragment
public class WeatherCommunicationBus
        extends SelfRestorableNavigationLceCommunicationBus<WeatherViewModel, WeatherView.WeatherError, WeatherView, WeatherPresenter, WeatherViewState>
        implements WeatherPresenter, WeatherView {

    @Inject
    public WeatherCommunicationBus(WeatherPresenter presenter, WeatherViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void showCautionDialog(CautionDialogData data) {
        getNavigationResolver().resolveNavigation(WeatherView::showCautionDialog, data);
    }

    // presenter
    @Override
    public void loadWeather() {
        getPresenter().loadWeather();
    }

    @Override
    public void someInsaneActionClicked() {
        getPresenter().someInsaneActionClicked();
    }
}
