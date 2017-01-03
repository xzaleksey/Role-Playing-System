package com.valyakinaleksey.roleplayingsystem.modules.main_screen.presenter;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.main_screen.view.GamesListView;

public interface GamesListPresenter extends Presenter<GamesListView> {
    void login(String email, String password);

    void restoreData();
}