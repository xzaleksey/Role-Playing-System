package com.valyakinaleksey.roleplayingsystem.core.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.firebase.listener.AuthStateListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class AbsActivity extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected Logger logger = LoggerManager.getLogger();
    protected FirebaseAuth.AuthStateListener mAuthListener = new AuthStateListener(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        setupViews();
    }

    public void setContentView() {
        super.setContentView(getLayoutId());
    }

    protected void setupViews() {
        setupToolbar();
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbarImpl();
    }

    protected void setToolbarTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(title);
    }

    protected abstract void setupToolbarImpl(); // todo refactor, use builder pattern

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
