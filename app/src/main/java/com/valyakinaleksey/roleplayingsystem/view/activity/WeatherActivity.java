package com.valyakinaleksey.roleplayingsystem.view.activity;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;
import com.valyakinaleksey.roleplayingsystem.view.fragment.WeatherFragment;


public class WeatherActivity extends AbsSingleFragmentActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setSingleFragment(WeatherFragment.newInstance(), WeatherFragment.TAG);
        }
    }

    @Override
    protected void setupToolbarImpl() {
        setToolbarTitle("The Weather");
    }

}
