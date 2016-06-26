package com.valyakinaleksey.roleplayingsystem.view.mapper;


import com.valyakinaleksey.roleplayingsystem.model.domain.WeatherResponse;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewModel;

public interface WeatherMapper {
    WeatherViewModel map(WeatherResponse weather);
}
