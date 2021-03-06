package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView;

public interface ParentPresenter extends Presenter<ParentView> {

    void navigateTo(Fragment fragment, Bundle args);

    void navigateToFragment(int navId, Bundle args);
}