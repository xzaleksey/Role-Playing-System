package com.valyakinaleksey.roleplayingsystem.di.components;

import com.valyakinaleksey.roleplayingsystem.di.base.PerAccount;
import com.valyakinaleksey.roleplayingsystem.di.modules.AccountModule;
import com.valyakinaleksey.roleplayingsystem.di.modules.ActivityModule;

import dagger.Subcomponent;

/**
 * PerAccountComponent
 */
@Subcomponent(modules = {
        AccountModule.class,
})
@PerAccount
public interface AccountComponent {


    ActivityComponent plusPerActivityComponent(ActivityModule activityModule);
    // ADAPTERS

    // ACTIVITIES

    // PRESENTERS


    // View


    // NAVIGATORS


    //Buses

    //ViewModel


    // ModelMappers


}
