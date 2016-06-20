package com.valyakinaleksey.roleplayingsystem.di;

import android.app.Application;

import com.firebase.client.Firebase;
import com.valyakinaleksey.roleplayingsystem.di.components.AccountComponent;
import com.valyakinaleksey.roleplayingsystem.di.components.ActivityComponent;
import com.valyakinaleksey.roleplayingsystem.di.components.ApplicationComponent;
import com.valyakinaleksey.roleplayingsystem.di.modules.AccountModule;
import com.valyakinaleksey.roleplayingsystem.di.modules.ActivityModule;
import com.valyakinaleksey.roleplayingsystem.di.modules.ApplicationModule;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Map;

import hugo.weaving.DebugLog;

public class App extends Application {
    private static Map<String, Object> components = new LinkedHashMap<>();

    private ApplicationComponent applicationComponent;
    private AccountComponent accountComponent;

    public static void saveComponent(String key, Object o) {
        components.put(key, o);
    }

    public static Object restoreComponent(String key) {
        return components.get(key);
    }

    public static void removeComponent(String key) {
        components.remove(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    private void initEventBus() {
        EventBus.builder().logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false).installDefaultEventBus();
    }

    public ApplicationComponent component() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    @DebugLog
    public AccountComponent plusPerAccountComponent() {
        if (accountComponent == null) {
            // start lifecycle of chatComponent
            accountComponent = component().plusPerAccountComponent(
                    new AccountModule());
        }
        return accountComponent;
    }

    public ActivityComponent plusPerActivityComponent(ActivityModule activityModule) {
        return plusPerAccountComponent().plusPerActivityComponent(activityModule);
    }
}
      