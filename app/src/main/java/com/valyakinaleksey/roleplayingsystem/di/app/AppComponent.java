package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage.MyUploadService;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Base app component
 */
@Singleton @Component(modules = { AppModule.class, InteractorModule.class, RepositoryModule.class })
public interface AppComponent extends GlobalComponent {

  void inject(MyUploadService myUploadService);
}
