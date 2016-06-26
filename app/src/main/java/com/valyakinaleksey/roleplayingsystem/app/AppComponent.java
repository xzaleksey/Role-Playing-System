package com.valyakinaleksey.roleplayingsystem.app;

import android.content.Context;

import com.firebase.client.Firebase;
import com.valyakinaleksey.roleplayingsystem.model.repository.WeatherRepository;
import com.valyakinaleksey.roleplayingsystem.model.repository.di.ApiModule;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Base app component
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    WeatherRepository repository();

    Context context();

    PathManager pathManager();

    Firebase firebase();
}
