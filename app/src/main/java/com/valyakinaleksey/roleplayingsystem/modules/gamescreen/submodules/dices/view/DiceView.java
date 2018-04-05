package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view;


import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel;

public interface DiceView extends LceView<DiceViewModel> {

    void setThrowBtnEnabled(boolean b);

    void setSaveDicesEnabled(boolean b);

    void updateDiceCollections(boolean animate);
}
