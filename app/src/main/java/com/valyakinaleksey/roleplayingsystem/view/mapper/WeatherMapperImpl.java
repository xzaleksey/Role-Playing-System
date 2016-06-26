package com.valyakinaleksey.roleplayingsystem.view.mapper;


import com.valyakinaleksey.roleplayingsystem.model.domain.WeatherResponse;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewModel;

public class WeatherMapperImpl implements WeatherMapper {

    private static final double KELVIN_CONST = 273.15;

    public WeatherViewModel map(WeatherResponse weather) {
        WeatherViewModel result = new WeatherViewModel();
        int celsiusTemp = toCelsius((int) weather.getMain().getTemp());
        result.setTemperature(celsiusTemp);
        return result;
    }

    private int toCelsius(int temp) {
        return (int) (temp - KELVIN_CONST);
    }
}
