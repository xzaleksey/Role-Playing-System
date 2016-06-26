package com.valyakinaleksey.roleplayingsystem.model.interactor;


import com.valyakinaleksey.roleplayingsystem.model.domain.WeatherResponse;

import rx.Observable;

/**
 * Use case: get weather in Moscow
 */
public interface GetWeatherInMoscowInteractor {

	Observable<WeatherResponse> get();

}
