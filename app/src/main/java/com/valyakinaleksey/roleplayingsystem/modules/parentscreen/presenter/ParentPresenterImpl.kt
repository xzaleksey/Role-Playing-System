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
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListFragment
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.UserProfileFragment
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel
import com.valyakinaleksey.roleplayingsystem.modules.photo.view.ImageFragment
import com.valyakinaleksey.roleplayingsystem.utils.*
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils.ADD_BACK_STACK
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils.POP_BACK_STACK
import rx.Subscription

class ParentPresenterImpl(
        private val gameRepository: GameRepository,
        private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor
) : BasePresenter<ParentView, ParentModel>(), ParentPresenter {

    private val navigationHandler: MutableMap<Int, (parentFragment: Fragment, bundle: Bundle?) -> Unit> = HashMap()

    init {
        navigationHandler.put(NavigationScreen.GAMES_LIST, navigateToFragment<GamesListFragment>())
        navigationHandler.put(NavigationScreen.MY_GAMES, navigateToFragment<MyGamesListFragment>())
        navigationHandler.put(NavigationScreen.GAME_FRAGMENT, navigateToFragment<ParentGameFragment>())
        navigationHandler.put(NavigationScreen.GAME_DESCRIPTION_FRAGMENT, navigateToFragment<GamesDescriptionFragment>())
        navigationHandler.put(NavigationScreen.IMAGE_FRAGMENT, navigateToFragment<ImageFragment>())
        navigationHandler.put(NavigationScreen.PROFILE, navigateToFragment<UserProfileFragment>())
        navigationHandler.put(NavigationScreen.BACK, { _, _ -> })
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
                .setAllowOptimization(true)
                .replace(R.id.parent_fragment_container, navFragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun initNewViewModel(arguments: Bundle?): ParentModel {
        val parentModel = ParentModel()
        parentModel.navigationId = NavigationScreen.MY_GAMES
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
                .compose(
                        RxTransformers.applySchedulers()).subscribe(
                object : DataObserver<GameModel>() {
                    override fun onData(data: GameModel) {
                        compositeSubscription.add(
                                navigateToGameScreen(data, this@ParentPresenterImpl, checkUserJoinedGameInteractor))
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
        val fragment = parentFragment.childFragmentManager.findFragmentById(
                R.id.parent_fragment_container)
        val arguments = args ?: Bundle()
        if (fragment is ParentGameFragment) {
            arguments.putBoolean(ADD_BACK_STACK, true)
        }
        handlePopBackStack(arguments, parentFragment)
        navigationHandler[viewModel.navigationId]!!.invoke(parentFragment, arguments)
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
            val childFragmentManager = parentFragment.childFragmentManager
            val fragment = childFragmentManager.findFragmentById(R.id.parent_fragment_container)
            if (fragment != null) {
                childFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit()
            }
            childFragmentManager.popBackStack()
            parentFragment.childFragmentManager.executePendingTransactions()
        }
    }

    override fun navigateBack() {
        val args = Bundle()
        args.putBoolean(POP_BACK_STACK, true)
        navigateToFragment(NavigationScreen.BACK, args)
    }
}
