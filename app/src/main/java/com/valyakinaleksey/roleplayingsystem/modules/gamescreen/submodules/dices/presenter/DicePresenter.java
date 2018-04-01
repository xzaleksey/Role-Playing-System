package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView;

public interface DicePresenter extends Presenter<DiceView> {
    void saveCurrentDices();
}