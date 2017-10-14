package com.valyakinaleksey.roleplayingsystem.modules.mygames.view;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGameView;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.List;

public interface MyGamesListView extends LceView<MyGamesListViewViewModel>, CreateGameView {
  void updateList(List<IFlexible> iFlexibles);
}
