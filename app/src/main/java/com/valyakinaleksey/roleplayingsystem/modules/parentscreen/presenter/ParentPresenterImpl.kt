package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter

import android.os.Bundle
import android.support.v4.app.Fragment
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionFragment
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModelInfo
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListFragment
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel
import com.valyakinaleksey.roleplayingsystem.modules.photo.view.ImageFragment
import com.valyakinaleksey.roleplayingsystem.utils.DeepLinksUtils
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.ADD_BACK_STACK
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.BACK
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAMES_LIST
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_DESCRIPTION_FRAGMENT
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.GAME_FRAGMENT
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.IMAGE_FRAGMENT
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.MY_GAMES
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.POP_BACK_STACK
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils.SETTINGS
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.createFragment
import rx.Subscription

class ParentPresenterImpl(
    private val gameRepository: GameRepository,
    private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor
) : BasePresenter<ParentView, ParentModel>(), ParentPresenter {

  private val navigationHandler: MutableMap<Int, (parentFragment: Fragment, bundle: Bundle?) -> Unit> = HashMap()

  init {
    navigationHandler.put(GAMES_LIST, navigateToFragment<GamesListFragment>())
    navigationHandler.put(MY_GAMES, navigateToFragment<MyGamesListFragment>())
    navigationHandler.put(GAME_FRAGMENT, navigateToFragment<ParentGameFragment>())
    navigationHandler.put(GAME_DESCRIPTION_FRAGMENT, navigateToFragment<GamesDescriptionFragment>())
    navigationHandler.put(IMAGE_FRAGMENT, navigateToFragment<ImageFragment>())
    navigationHandler.put(SETTINGS, { _, _ -> })
    navigationHandler.put(BACK, { _, _ -> })
  }

  private inline fun <reified T : Fragment> navigateToFragment(): (Fragment, Bundle?) -> Unit {
    return { parentFragment, bundle ->
      navigateToFragment(parentFragment, createFragment<T>(bundle),
          bundle?.getBoolean(ADD_BACK_STACK) ?: false)
    }
  }

  private fun navigateToFragment(fragment: Fragment, navFragment: Fragment,
      addToBackStack: Boolean) {
    val transaction = fragment.childFragmentManager
        .beginTransaction()
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        .replace(R.id.parent_fragment_container, navFragment)
    if (addToBackStack) {
      transaction.addToBackStack(navFragment.javaClass.simpleName)
    }
    transaction.commit()
  }

  override fun initNewViewModel(arguments: Bundle?): ParentModel {
    val parentModel = ParentModel()
    parentModel.navigationId = MY_GAMES
    tryOpenDeepLink(arguments)
    parentModel.toolbarTitle = StringUtils.getStringById(R.string.app_name)
    return parentModel
  }

  override fun tryOpenDeepLink(arguments: Bundle?) {
    if (arguments != null) {
      if (DeepLinksUtils.DEEPLINK_TYPE_SCREEN == arguments[DeepLinksUtils.DEEPLINK_TYPE_TAG]) {
        if (arguments.containsKey(
            DeepLinksUtils.DEEPLINK_SCREEN_TAG)) {
          if (arguments.containsKey(DeepLinksUtils.DEEPLINK_GAME_ID_TAG)) {
            compositeSubscription.add(getGameScreenSubscription(arguments))
          }
        }
      }
    }
  }

  private fun getGameScreenSubscription(arguments: Bundle): Subscription? {
    return gameRepository.getGameModelObservableById(
        arguments.getString(DeepLinksUtils.DEEPLINK_GAME_ID_TAG))
        .flatMap { gameModel ->
          return@flatMap checkUserJoinedGameInteractor.checkUserInGame(
              FireBaseUtils.getCurrentUserId(), gameModel).map { isInGame ->
            GameModelInfo(gameModel, isInGame)
          }
        }
        .compose(
            RxTransformers.applySchedulers()).subscribe(
        object : DataObserver<GameModelInfo>() {
          override fun onData(data: GameModelInfo) {
            val bundle = Bundle()
            bundle.putParcelable(GameModel.KEY, data.gameModel)
            bundle.putBoolean(ADD_BACK_STACK, true)
            if (data.isUserInGame) {
              navigateToFragment(GAME_FRAGMENT, bundle)
            } else {
              navigateToFragment(GAME_DESCRIPTION_FRAGMENT, bundle)
            }
          }
        })
  }

  override fun getData() {
    if (viewModel.isUpdatedRequired) {
      viewModel.isFirstNavigation = false
      view.getNavigationFragment(null)
      view.setData(viewModel)
      view.showContent()
    }
    compositeSubscription.add(FireBaseUtils.getConnectionObservableWithTimeInterval()
        .compose(RxTransformers.applySchedulers())
        .subscribe { connected ->
          viewModel.isDisconnected = !connected
          view.updateToolbar()
        })
  }

  override fun navigateTo(parentFragment: Fragment, args: Bundle?) {
    handlePopBackStack(args, parentFragment)
    navigationHandler[viewModel.navigationId]!!.invoke(parentFragment, args)
  }

  override fun navigateToFragment(navId: Int, args: Bundle?) {
    viewModel.navigationId = navId
    view.getNavigationFragment(args)
  }

  override fun navigateToFragment(navId: Int) {
    navigateToFragment(navId, null)
  }


  private fun handlePopBackStack(args: Bundle?, parentFragment: Fragment) {
    if (args != null && args.getBoolean(POP_BACK_STACK, false)) {
      parentFragment.childFragmentManager.popBackStackImmediate()
    }
  }

  override fun navigateBack() {
    val args = Bundle()
    args.putBoolean(POP_BACK_STACK, true)
    navigateToFragment(NavigationUtils.BACK, args)
  }
}
