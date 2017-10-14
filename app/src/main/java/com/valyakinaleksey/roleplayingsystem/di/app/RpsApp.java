package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.BuildConfig;
import com.valyakinaleksey.roleplayingsystem.R;
import io.fabric.sdk.android.Fabric;
import net.danlew.android.joda.JodaTimeAndroid;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RpsApp extends MultiDexApplication {

    private AppComponent component;

    private static RpsApp instance;
    private static Typeface font;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        instance = this;
        font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        ButterKnife.setDebug(true);
    }

    public static Typeface getFont() {
        return font;
    }

    public static AppComponent getAppComponent(Context context) {
        RpsApp app = (RpsApp) context.getApplicationContext();
        if (app.component == null) {
            app.component = DaggerAppComponent.builder()
                    .appModule(app.getApplicationModule())
                    .interactorModule(new InteractorModule())
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

    public static void logUser() {
        // You can call any combination of these three methods
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Crashlytics.setUserIdentifier(currentUser.getUid());
            Crashlytics.setUserEmail(currentUser.getEmail());
        }
    }
}
