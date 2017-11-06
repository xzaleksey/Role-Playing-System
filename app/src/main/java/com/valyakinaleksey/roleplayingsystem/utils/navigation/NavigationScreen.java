package com.valyakinaleksey.roleplayingsystem.utils.navigation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {
        NavigationScreen.GAMES_LIST,
        NavigationScreen.GAME_FRAGMENT,
        NavigationScreen.GAME_DESCRIPTION_FRAGMENT,
        NavigationScreen.GAME_MASTER_EDIT_FRAGMENT,
        NavigationScreen.GAME_MASTER_LOG_FRAGMENT,
        NavigationScreen.GAME_MAPS_FRAGMENT,
        NavigationScreen.GAME_CHARACTERS_FRAGMENT,
        NavigationScreen.IMAGE_FRAGMENT,
        NavigationScreen.MY_GAMES,
        NavigationScreen.PROFILE,
        NavigationScreen.BACK})

public @interface NavigationScreen {
    int GAMES_LIST = 1;
    int GAME_FRAGMENT = 2;
    int GAME_DESCRIPTION_FRAGMENT = 3;
    int GAME_MASTER_EDIT_FRAGMENT = 4;
    int GAME_MASTER_LOG_FRAGMENT = 5;
    int GAME_MAPS_FRAGMENT = 6;
    int GAME_CHARACTERS_FRAGMENT = 7;
    int IMAGE_FRAGMENT = 8;
    int MY_GAMES = 9;
    int PROFILE = 10;
    int BACK = -1;
}
