package com.valyakinaleksey.roleplayingsystem.di.app;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Base app component
 */
@Singleton
@Component(modules = {AppModule.class, InteractorModule.class})
public interface AppComponent extends  GlobalComponent {


}
