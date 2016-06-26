package com.valyakinaleksey.roleplayingsystem.view.di;


import com.valyakinaleksey.roleplayingsystem.communication.WeatherCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.model.interactor.GetWeatherInMoscowInteractor;
import com.valyakinaleksey.roleplayingsystem.model.interactor.GetWeatherInMoscowUseCase;
import com.valyakinaleksey.roleplayingsystem.model.repository.WeatherRepository;
import com.valyakinaleksey.roleplayingsystem.presenter.WeatherPresenter;
import com.valyakinaleksey.roleplayingsystem.presenter.WeatherPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.view.mapper.WeatherMapper;
import com.valyakinaleksey.roleplayingsystem.view.mapper.WeatherMapperImpl;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewState;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherModule {

    private final static String VIEW_STATE_FILE_NAME = WeatherModule.class.getSimpleName();

    @Provides
    @PerFragment
    GetWeatherInMoscowInteractor provideGetWeatherInterator(WeatherRepository repo) {
        return new GetWeatherInMoscowUseCase(repo);
    }

    @Provides
    @PerFragment
    WeatherMapper provideWeatherMapper() {
        return new WeatherMapperImpl();
    }

    @Provides
    ViewStateStorage provideViewStateStorage(PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
        return new FileViewStateStorage(fullPath);
    }

    @Provides
    WeatherViewState provideViewState(ViewStateStorage storage) {
        return new WeatherViewState(storage);
    }

    @Provides
    @PerFragment
    WeatherPresenter provideCommunicationBus(@Named("presenter") WeatherPresenter presenter, WeatherViewState viewState) {
        return new WeatherCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named("presenter")
    @PerFragment
    WeatherPresenter provideWeatherPresenter(GetWeatherInMoscowInteractor getWeather, WeatherMapper mapper) {
        return new WeatherPresenterImpl(getWeather, mapper);
    }
}
