package com.valyakinaleksey.roleplayingsystem.di.app;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;
import com.valyakinaleksey.roleplayingsystem.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RpsApp extends Application {

    private AppComponent component;

    private static RpsApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Firebase.setAndroidContext(this);
        instance = this;
    }

    public static AppComponent getAppComponent(Context context) {
        RpsApp app = (RpsApp) context.getApplicationContext();
        if (app.component == null) {
            app.component = DaggerAppComponent.builder()
                    .appModule(app.getApplicationModule())
                    .build();
        }
        return app.component;
    }

    public static RpsApp app() {
        return instance;
    }

    protected AppModule getApplicationModule() {
        return new AppModule(this);
    }

}
