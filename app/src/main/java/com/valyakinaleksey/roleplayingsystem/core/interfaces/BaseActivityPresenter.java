package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.View;

public abstract class BaseActivityPresenter<V extends View> implements Presenter<V> {
    @Override
    public void attachView(V view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void getData() {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }
}
      