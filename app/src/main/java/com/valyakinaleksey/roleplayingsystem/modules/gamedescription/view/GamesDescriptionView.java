package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view;


import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.GamesDescriptionModel;

public interface GamesDescriptionView extends LceView<GamesDescriptionModel> {

    void updateView();

    void updateView(InfoSection userInfosection, DataEvent dataEvent);
}
