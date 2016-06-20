package com.valyakinaleksey.roleplayingsystem.di.inject;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.valyakinaleksey.roleplayingsystem.di.components.ActivityComponent;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public final class InjectHelper {

    private InjectHelper() {
    }

    @NonNull
    public static ActivityComponent getActivityComponent(@NonNull final Activity activity) {
        checkNotNull(activity);
        return com.valyakinaleksey.roleplayingsystem.di.base.InjectHelper.getComponent(ActivityComponent.class,
                activity);
    }
}
