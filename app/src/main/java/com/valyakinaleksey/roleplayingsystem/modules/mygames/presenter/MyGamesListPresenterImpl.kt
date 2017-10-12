package com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter

import android.content.Context
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.GamesListViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.utils.*

import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES_IN_USERS

class MyGamesListPresenterImpl(private val createNewGameInteractor: CreateNewGameInteractor,
    private val userGetInteractor: UserGetInteractor,
    private val validatePasswordInteractor: ValidatePasswordInteractor,
    private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
    private val parentPresenter: ParentPresenter) : BasePresenter<MyGamesListView, GamesListViewModel>(), MyGamesListPresenter, RestorablePresenter<GamesListViewModel> {

  private val getMyGamesQuery: Query
    get() = FirebaseDatabase.getInstance()
        .reference
        .child(GAMES_IN_USERS)
        .child(FireBaseUtils.getCurrentUserId())

  override fun initNewViewModel(arguments: Bundle?): GamesListViewModel {
    val gamesListViewModel = GamesListViewModel()
    gamesListViewModel.toolbarTitle = RpsApp.app().getString(R.string.my_games)
    return gamesListViewModel
  }

  override fun restoreViewModel(viewModel: GamesListViewModel) {
    super.restoreViewModel(viewModel)
    viewModel.setNeedUpdate(true)
  }

  override fun createGame(gameModel: GameModel) {
    createNewGame(gameModel, view, createNewGameInteractor)
  }

  override fun loadComplete() {
    view.hideLoading()
  }

  override fun onFabPressed() {
    view.showCreateGameDialog()
  }

  override fun navigateToGameScreen(context: Context, model: GameModel) {
    compositeSubscription.add(
        navigateToGameScreen(model, parentPresenter, checkUserJoinedGameInteractor))
  }

  override fun checkPassword(context: Context, model: GameModel) {
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    compositeSubscription.add(
        getCheckUserInGameObservable(model, currentUserId, checkUserJoinedGameInteractor)
            .subscribe(
                { userInGame ->
                  if (userInGame!!) {
                    navigateToGameScreen(context, model)
                  } else {
                    view.showPasswordDialog()
                  }
                }, { this.handleThrowable(it) }))
  }

  override fun validatePassword(context: Context, userInput: String, gameModel: GameModel) {
    compositeSubscription.add(getValidatePasswordSubscription(validatePasswordInteractor,
        userInput,
        gameModel.password,
        { isValid ->
          if (isValid) {
            navigateToGameDescriptionScreen(gameModel, parentPresenter)
          } else {
            val snack = BaseError.SNACK
            snack.setValue(RpsApp.app().getString(R.string.error_incorrect_password))
            view.showError(snack)
          }
        }))
  }

  override fun getData() {
    compositeSubscription.add(
        FireBaseUtils.checkReferenceExistsAndNotEmpty(FireBaseUtils.getTableReference(GAMES))
            .doOnSubscribe {
              view.setData(viewModel)
              viewModel.setNeedUpdate(false)
              view.showContent()
              view.showLoading()
            }
            .subscribe({ exists ->
              if (!exists) {
                view.hideLoading()
              }
            }, { Crashlytics.logException(it) }))
  }
}
