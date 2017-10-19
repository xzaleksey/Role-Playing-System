package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor

import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel
import eu.davidea.flexibleadapter.items.IFlexible

class GameListResult(val items: List<IFlexible<*>>, val filterModel: FilterModel)