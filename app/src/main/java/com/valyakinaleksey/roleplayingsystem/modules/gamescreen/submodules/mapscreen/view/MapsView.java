package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.AdapterNotifier;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;

public interface MapsView extends LceView<MapsViewModel>, AdapterNotifier {

  void updateView();
}
