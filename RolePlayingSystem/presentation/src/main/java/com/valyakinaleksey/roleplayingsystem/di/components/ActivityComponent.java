package com.valyakinaleksey.roleplayingsystem.di.components;

import com.valyakinaleksey.roleplayingsystem.LoginActivity;
import com.valyakinaleksey.roleplayingsystem.di.base.PerActivity;
import com.valyakinaleksey.roleplayingsystem.di.modules.ActivityModule;
import com.valyakinaleksey.roleplayingsystem.di.modules.FragmentModule;

import dagger.Subcomponent;

@Subcomponent(modules = {
        ActivityModule.class
})

@PerActivity
public interface ActivityComponent {

    FragmentComponent plusPerFragmentComponent(FragmentModule fragmentModule);

    void inject(LoginActivity loginActivity);
}
