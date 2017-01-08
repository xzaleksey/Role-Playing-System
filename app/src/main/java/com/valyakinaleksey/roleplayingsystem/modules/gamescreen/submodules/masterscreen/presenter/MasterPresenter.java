package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter;


import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.MasterView;

public interface MasterPresenter extends Presenter<MasterView>, Provider<UserGetInteractor> {
}