package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogView;

public interface MasterLogPresenter extends Presenter<MasterLogView> {

    void sendMessage(String s);
}