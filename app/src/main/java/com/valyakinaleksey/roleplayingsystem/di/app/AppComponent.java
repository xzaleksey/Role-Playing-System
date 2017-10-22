package com.valyakinaleksey.roleplayingsystem.di.app;

import com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage.MyUploadService;
import com.valyakinaleksey.roleplayingsystem.modules.deeplink.DeepLinkDispatchActivity;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

/**
 * Base app component
 */
@Singleton @Component(modules = {
    AndroidSupportInjectionModule.class, AppModule.class, InteractorModule.class,
    RepositoryModule.class
}) public interface AppComponent extends GlobalComponent {

  void inject(MyUploadService myUploadService);

  void inject(@NotNull DeepLinkDispatchActivity deepLinkDispatchActivity);
}
