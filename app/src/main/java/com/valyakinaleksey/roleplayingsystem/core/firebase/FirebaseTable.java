package com.valyakinaleksey.roleplayingsystem.core.firebase;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({
        FirebaseTable.USERS,
        FirebaseTable.USERS_IN_GAME,
        FirebaseTable.GAMES_IN_USERS,
        FirebaseTable.GAMES,
        FirebaseTable.GAME_LOG,
        FirebaseTable.GAME_MAPS,
        FirebaseTable.GAME_CHARACTERISTICS,
        FirebaseTable.GAME_CLASSES,
        FirebaseTable.GAME_RACES,
        FirebaseTable.CHARACTERS_IN_USER,
        FirebaseTable.GAME_CHARACTERS
})
public @interface FirebaseTable {
    String USERS = "users";
    String USERS_IN_GAME = "users_in_game";
    String GAMES_IN_USERS = "games_in_user";
    String CHARACTERS_IN_USER = "characters_in_user";
    String GAMES = "games";
    String GAME_LOG = "game_log";
    String GAME_MAPS = "game_maps";
    String GAME_CHARACTERISTICS = "game_characteristics";
    String GAME_CLASSES = "game_classes";
    String GAME_RACES = "game_races";
    String GAME_CHARACTERS = "game_characters";
}
