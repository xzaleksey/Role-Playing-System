package com.valyakinaleksey.roleplayingsystem.core.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.valyakinaleksey.roleplayingsystem.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class AbsActivity extends AppCompatActivity {

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
}
