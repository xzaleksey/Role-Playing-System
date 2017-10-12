package com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView;

public interface MyGamesListPresenter extends Presenter<MyGamesListView> {
    void createGame(GameModel gameModel);

    void loadComplete();

    void onFabPressed();

    void navigateToGameScreen(Context context, GameModel model);

    void checkPassword(Context context, GameModel model);

    void validatePassword(Context context, String s, GameModel gameModel);
}