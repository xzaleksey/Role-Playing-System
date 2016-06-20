package com.valyakinaleksey.roleplayingsystem.di.components;

import com.valyakinaleksey.data.storage.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.di.modules.AccountModule;
import com.valyakinaleksey.roleplayingsystem.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import rx.subscriptions.CompositeSubscription;

@Component(modules = {
        ApplicationModule.class
})
@Singleton
public interface ApplicationComponent {
    AccountComponent plusPerAccountComponent(AccountModule accountModule);


    SharedPreferencesHelper getSharedPreferencesHelper();

    CompositeSubscription getCompositeSubscription();
}
