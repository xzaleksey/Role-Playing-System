package com.valyakinaleksey.roleplayingsystem.di.inject;


import com.valyakinaleksey.roleplayingsystem.di.App;
import com.valyakinaleksey.roleplayingsystem.di.base.AbstractInjectableActivity;
import com.valyakinaleksey.roleplayingsystem.di.components.ActivityComponent;
import com.valyakinaleksey.roleplayingsystem.di.modules.ActivityModule;

public class InjectableActivity extends AbstractInjectableActivity<ActivityComponent> {

    @Override
    protected ActivityComponent createComponent() {
        return ((App) getApplication()).plusPerActivityComponent(new ActivityModule());
    }
}
