package com.valyakinaleksey.roleplayingsystem.di.inject;


import com.valyakinaleksey.roleplayingsystem.di.base.AbstractInjectableFragment;
import com.valyakinaleksey.roleplayingsystem.di.components.FragmentComponent;
import com.valyakinaleksey.roleplayingsystem.di.modules.FragmentModule;

public class InjectableFragment extends AbstractInjectableFragment<FragmentComponent> {

    @Override
    protected FragmentComponent createComponent() {
        return InjectHelper.getActivityComponent(getActivity()).plusPerFragmentComponent(new FragmentModule(this));
    }
}
