package com.valyakinaleksey.roleplayingsystem.model.repository;


import com.valyakinaleksey.roleplayingsystem.model.domain.WeatherResponse;

import rx.Observable;


/**
 * Interface for weather repository
 */
public interface WeatherRepository {

	Observable<WeatherResponse> getWeather(String city);

}
