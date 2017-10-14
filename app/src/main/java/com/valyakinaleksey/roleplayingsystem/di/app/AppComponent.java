package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage.MyUploadService;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

/**
 * Base app component
 */
@Singleton @Component(modules = {
    AndroidSupportInjectionModule.class, AppModule.class, InteractorModule.class,
    RepositoryModule.class
}) public interface AppComponent extends GlobalComponent {

  void inject(MyUploadService myUploadService);
}
