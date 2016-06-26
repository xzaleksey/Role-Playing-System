package com.valyakinaleksey.roleplayingsystem.app;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

public class RpsApp extends Application {

	private AppComponent component;

	private static RpsApp instance;
	@Override
	public void onCreate() {
		super.onCreate();
		Firebase.setAndroidContext(this);
		instance = this;
	}

	public static AppComponent getAppComponent(Context context) {
		RpsApp app = (RpsApp)context.getApplicationContext();
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
