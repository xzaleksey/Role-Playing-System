package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.valyakinaleksey.roleplayingsystem.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RpsApp extends MultiDexApplication {

    private AppComponent component;

    private static RpsApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
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
