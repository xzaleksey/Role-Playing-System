package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel;

public class DiceViewState extends StorageBackedNavigationLceViewStateImpl<DiceViewModel, DiceView> {

    public DiceViewState(ViewStateStorage storage) {
        super(storage);
    }
}
