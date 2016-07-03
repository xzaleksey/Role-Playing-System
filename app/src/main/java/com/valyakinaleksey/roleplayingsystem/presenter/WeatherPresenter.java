package com.valyakinaleksey.roleplayingsystem.presenter;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.WeatherView;

public interface WeatherPresenter extends Presenter<WeatherView> {
    void loadWeather();

    void someInsaneActionClicked();
}