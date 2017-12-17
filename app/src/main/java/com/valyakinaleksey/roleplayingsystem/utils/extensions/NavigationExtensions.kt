package com.valyakinaleksey.roleplayingsystem.utils.extensions

import android.os.Bundle
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils

fun ParentPresenter.navigateToGame(gameModel: GameModel) {
    val bundle = Bundle()
    bundle.putParcelable(GameModel.KEY, gameModel)
    bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true)
    navigateToFragment(NavigationScreen.GAME_FRAGMENT, bundle)
}
