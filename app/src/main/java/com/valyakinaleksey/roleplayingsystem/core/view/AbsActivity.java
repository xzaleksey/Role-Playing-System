package com.valyakinaleksey.roleplayingsystem.core.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.firebase.listener.AuthStateListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class AbsActivity extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected FirebaseAuth.AuthStateListener mAuthListener = new AuthStateListener(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        setupViews();
    }

    protected abstract void setupViews();

    public void setContentView() {
        super.setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        firebaseAuth.removeAuthStateListener(mAuthListener);
        super.onStop();
    }

}
