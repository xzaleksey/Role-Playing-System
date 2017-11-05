package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.view.ParentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.di.GamesDescriptionComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.di.GamesDescriptionModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameModule;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListModule;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListModule;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragment;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileComponent;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileModule;
import dagger.Subcomponent;

@Subcomponent(modules = ParentFragmentModule.class)
@ParentScope
public interface ParentFragmentComponent extends HasParentPresenter {
    void inject(ParentFragment parentFragment);

    ParentPresenter communicationBus();

    ParentGameComponent getParentGameComponent(ParentGameModule parentGameModule);

    GamesListComponent getGamesListComponent(GamesListModule gamesListModule);

    MyGamesListComponent getMyGamesListComponent(MyGamesListModule myGamesListModule);

    GamesDescriptionComponent getGamesDescriptionComponent(GamesDescriptionModule gamesDescriptionModule);

    UserProfileComponent getUserProfileComponent(UserProfileModule userProfileModule);
}

