package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.ParentView;

public interface ParentGamePresenter extends Presenter<ParentView>, ChildGameListener {
}