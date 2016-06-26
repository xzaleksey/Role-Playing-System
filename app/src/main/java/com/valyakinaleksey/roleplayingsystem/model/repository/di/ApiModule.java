package com.valyakinaleksey.roleplayingsystem.model.repository.di;


import com.valyakinaleksey.roleplayingsystem.BuildConfig;
import com.valyakinaleksey.roleplayingsystem.model.repository.Api;
import com.valyakinaleksey.roleplayingsystem.model.repository.WeatherRepository;
import com.valyakinaleksey.roleplayingsystem.model.repository.WeatherRetrofitRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Module with Open Weather Api dependencies
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    public Api provideApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.WEATHER_API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    public WeatherRepository provideWeatherRepository(Api api) {
        WeatherRepository repo = new WeatherRetrofitRepository(api);
        return repo;
    }
}
