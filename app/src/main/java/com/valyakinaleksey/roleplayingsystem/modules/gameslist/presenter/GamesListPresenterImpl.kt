package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter

import android.content.Context
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType.SNACK
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.model.GameListItemViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.GameListInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.extensions.createNewGame
import com.valyakinaleksey.roleplayingsystem.utils.extensions.navigateToGameDescriptionScreen
import com.valyakinaleksey.roleplayingsystem.utils.extensions.navigateToGameScreen
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import rx.subjects.BehaviorSubject

@PerFragmentScope
class GamesListPresenterImpl(private val createNewGameInteractor: CreateNewGameInteractor,
                             private val gamesListInteractor: GameListInteractor,
                             private val validatePasswordInteractor: ValidatePasswordInteractor,
                             private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
                             private val parentPresenter: ParentPresenter) : BasePresenter<GamesListView, GamesListViewModel>(), GamesListPresenter, RestorablePresenter<GamesListViewModel> {

    private val filterSubject: BehaviorSubject<FilterModel> = BehaviorSubject.create()

    override fun initNewViewModel(arguments: Bundle?): GamesListViewModel {
        val gamesListViewModel = GamesListViewModel()
        gamesListViewModel.toolbarTitle = RpsApp.app().getString(R.string.list_of_games)
        return gamesListViewModel
    }

    override fun restoreViewModel(viewModel: GamesListViewModel) {
        super.restoreViewModel(viewModel)
        viewModel.setNeedUpdate(true)
    }

    override fun createGame(gameModel: GameModel) {
        compositeSubscription.add(createNewGame(gameModel, view, createNewGameInteractor))
    }

    override fun loadComplete() {
        view.hideLoading()
    }

    override fun onFabPressed() {
        val createGameDialogViewModel = CreateGameDialogViewModel()
        createGameDialogViewModel.title = RpsApp.app().getString(R.string.create_game)
        createGameDialogViewModel.gameModel = GameModel(StringUtils.EMPTY_STRING,
                StringUtils.EMPTY_STRING)
        viewModel.createGameDialogViewModel = createGameDialogViewModel
        view.showCreateGameDialog()
    }

    override fun navigateToGameScreen(
            model: GameModel) {
        compositeSubscription.add(
                navigateToGameScreen(model, parentPresenter, checkUserJoinedGameInteractor))
    }

    override fun checkPassword(
            model: GameModel) {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        compositeSubscription.add(
                getCheckUserInGameObservable(model, currentUserId).subscribe({ userInGame: Boolean ->
                    if (userInGame) {
                        navigateToGameScreen(model)
                    } else {
                        val passwordDialogViewModel = PasswordDialogViewModel()
                        passwordDialogViewModel.title = RpsApp.app().getString(R.string.create_game)
                        passwordDialogViewModel.gameModel = model
                        viewModel.passwordDialogViewModel = passwordDialogViewModel
                        view.showPasswordDialog()
                    }
                }, { this.handleThrowable(it) }))
    }

    override fun validatePassword(context: Context, s: String, gameModel: GameModel) {
        compositeSubscription.add(validatePasswordInteractor.isPasswordValid(s, gameModel.password)
                .subscribe({ isValid ->
                    if (isValid) {
                        navigateToGameDescriptionScreen(gameModel,
                                parentPresenter)
                    } else {
                        val snack = BaseError(SNACK, RpsApp.app().getString(R.string.error_incorrect_password))
                        view.showError(snack)
                    }
                }, { Crashlytics.logException(it) }))
    }

    override fun getData() {
        super.getData()
        filterSubject.onNext(viewModel.filterModel)
        compositeSubscription.add(
                gamesListInteractor.observeGameViewModelsWithFilter(filterSubject)
                        .compose(RxTransformers.applySchedulers())
                        .subscribe({ gameListResult ->
                            val items = gameListResult.items
                            viewModel.items = items
                            viewModel.gamesCount = items.size
                            view.showContent()
                            if (gameListResult.filterModel.isCleared()) {
                                view.scrollToTop()
                            }
                        }, { Crashlytics.logException(it) }))

    }

    private fun getCheckUserInGameObservable(model: GameModel,
                                             currentUserId: String): Observable<Boolean> {
        return checkUserJoinedGameInteractor.checkUserInGame(currentUserId, model)
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
    }

    override fun onItemClick(item: IFlexible<*>?): Boolean {
        if (item is GameListItemViewModel) {
            val gameModel = item.gameModel
            if (StringUtils.isEmpty(gameModel.password)) {
                navigateToGameScreen(gameModel, parentPresenter, checkUserJoinedGameInteractor)
            } else {
                checkPassword(gameModel)
            }
        }
        return true
    }

    override fun onSearchQueryChanged(queryText: String) {
        viewModel.filterModel.setQuery(queryText)
        filterSubject.onNext(FilterModel(viewModel.filterModel))
    }

}
