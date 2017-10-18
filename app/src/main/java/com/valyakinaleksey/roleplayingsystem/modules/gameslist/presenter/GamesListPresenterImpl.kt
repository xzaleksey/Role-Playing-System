package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter

import android.content.Context
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
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
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.createNewGame
import com.valyakinaleksey.roleplayingsystem.utils.navigateToGameDescriptionScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigateToGameScreen
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable

@PerFragmentScope
class GamesListPresenterImpl(private val createNewGameInteractor: CreateNewGameInteractor,
    private val gamesListInteractor: GameListInteractor,
    private val validatePasswordInteractor: ValidatePasswordInteractor,
    private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
    private val parentPresenter: ParentPresenter) : BasePresenter<GamesListView, GamesListViewViewModel>(), GamesListPresenter, RestorablePresenter<GamesListViewViewModel> {

  override fun initNewViewModel(arguments: Bundle): GamesListViewViewModel {
    val gamesListViewModel = GamesListViewViewModel()
    gamesListViewModel.toolbarTitle = RpsApp.app().getString(R.string.list_of_games)
    return gamesListViewModel
  }

  override fun restoreViewModel(viewModel: GamesListViewViewModel) {
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

  override fun navigateToGameScreen(context: Context, model: GameModel) {
    compositeSubscription.add(
        navigateToGameScreen(model, parentPresenter, checkUserJoinedGameInteractor))
  }

  override fun checkPassword(context: Context, model: GameModel) {
    val currentUserId = FireBaseUtils.getCurrentUserId()
    compositeSubscription.add(
        getCheckUserInGameObservable(model, currentUserId).subscribe({ userInGame: Boolean ->
          if (userInGame) {
            navigateToGameScreen(context, model)
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
            val snack = BaseError.SNACK
            snack.setValue(RpsApp.app().getString(R.string.error_incorrect_password))
            view.showError(snack)
          }
        }, { Crashlytics.logException(it) }))
  }

  override fun getData() {
    view.setData(viewModel)
    viewModel.setNeedUpdate(false)
    view.showContent()
    view.showLoading()
    compositeSubscription.add(
        FireBaseUtils.checkReferenceExistsAndNotEmpty(FireBaseUtils.getTableReference(GAMES))
            .compose(RxTransformers.applySchedulers())
            .subscribe({ exists ->
              if (!exists) {
                view.hideLoading()
              }
            }, { Crashlytics.logException(it) }))

    compositeSubscription.add(
        gamesListInteractor.observeGameViewModels()
            .compose(RxTransformers.applySchedulers())
            .subscribe({ items ->
              view.hideLoading()
              viewModel.items = items
              viewModel.gamesCount = items.size
              view.showContent()
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
      val bundle = Bundle()
      bundle.putParcelable(GameModel.KEY, item.gameModel)
      bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true)
      parentPresenter.navigateToFragment(NavigationUtils.GAME_FRAGMENT, bundle)
    }
    return true
  }
}
