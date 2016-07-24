package com.valyakinaleksey.roleplayingsystem.core.firebase.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.valyakinaleksey.roleplayingsystem.view.activity.LoginActivityUpdated;

import java.lang.ref.WeakReference;

public class AuthStateListener implements FirebaseAuth.AuthStateListener {
    private final Logger logger = LoggerManager.getLogger();
    private final WeakReference<Activity> activityWeakReference;

    public AuthStateListener(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            logger.d("onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            logger.d("onAuthStateChanged:signed_out");
            Activity activity = activityWeakReference.get();
            if (activity != null && !(activity instanceof LoginActivityUpdated)) {
                Intent intent = new Intent(activity, LoginActivityUpdated.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        }
    }
}
      