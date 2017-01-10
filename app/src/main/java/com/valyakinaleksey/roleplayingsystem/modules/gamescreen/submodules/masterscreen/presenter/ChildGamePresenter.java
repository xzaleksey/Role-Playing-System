package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter;

import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.View;

public interface ChildGamePresenter<V extends View> extends Presenter<V> {
    void setParentPresenter(ChildGameListener parentPresenter);
}
      