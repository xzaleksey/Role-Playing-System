package com.valyakinaleksey.roleplayingsystem.view.di;


import com.valyakinaleksey.roleplayingsystem.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.presenter.WeatherPresenter;
import com.valyakinaleksey.roleplayingsystem.view.fragment.WeatherFragment;

import dagger.Component;

/**
 * Component for weather screen
 */
@Component(
        dependencies = AppComponent.class,
        modules = WeatherModule.class
)
@PerFragment
public interface WeatherComponent extends HasPresenter<WeatherPresenter> {
    void inject(WeatherFragment fragment);
}
