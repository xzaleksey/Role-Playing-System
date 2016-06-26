package com.valyakinaleksey.roleplayingsystem.model.interactor;


import com.valyakinaleksey.roleplayingsystem.core.model.Interactor;
import com.valyakinaleksey.roleplayingsystem.model.domain.WeatherResponse;
import com.valyakinaleksey.roleplayingsystem.model.repository.WeatherRepository;

import rx.Observable;

/**
 * Implementation of {@link GetWeatherInMoscowInteractor}
 */
public class GetWeatherInMoscowUseCase extends Interactor
								implements GetWeatherInMoscowInteractor {

	private final WeatherRepository mRepository;

	public GetWeatherInMoscowUseCase(WeatherRepository repository) {
		mRepository = repository;
	}

	@Override
	public Observable<WeatherResponse> get() {
		return mRepository.getWeather("Moscow");
	}
}
