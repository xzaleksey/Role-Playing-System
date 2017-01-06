package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view;


import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;

public interface GamesListView extends LceView<GamesListViewModel> {

    void scrollToBottom();
}
