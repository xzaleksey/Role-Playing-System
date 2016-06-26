package com.valyakinaleksey.roleplayingsystem.model.repository;


import com.valyakinaleksey.roleplayingsystem.BuildConfig;
import com.valyakinaleksey.roleplayingsystem.model.domain.WeatherResponse;

import rx.Observable;

/**
 * Implementation of {@link WeatherRepository} that fetch data from Open Weather Api
 */
public class WeatherRetrofitRepository
		implements WeatherRepository {

	Api api;

	public WeatherRetrofitRepository(Api api) {
		this.api = api;
	}

	@Override
	public Observable<WeatherResponse> getWeather(String city) {
		return api.get(BuildConfig.WEATHER_API_KEY, city);
	}
}
