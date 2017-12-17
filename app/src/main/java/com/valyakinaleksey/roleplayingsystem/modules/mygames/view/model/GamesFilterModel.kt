package com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model

import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel

class GamesFilterModel(private val showAllMyGames: Boolean = false) : FilterModel() {

    constructor(gamesFilterModel: GamesFilterModel) : this(gamesFilterModel.showAllMyGames) {
        this.setQuery(gamesFilterModel.getQuery())
        this.previousQuery = gamesFilterModel.previousQuery
    }
}