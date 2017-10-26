package com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter

import android.content.Context
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.flexible.TwoLineWithIdViewModel
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.MyGamesInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.createNewGame
import com.valyakinaleksey.roleplayingsystem.utils.getCheckUserInGameObservable
import com.valyakinaleksey.roleplayingsystem.utils.getValidatePasswordSubscription
import com.valyakinaleksey.roleplayingsystem.utils.navigateToGameDescriptionScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigateToGameScreen
import eu.davidea.flexibleadapter.items.IFlexible
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MyGamesListPresenterImpl(private val createNewGameInteractor: CreateNewGameInteractor,
    private val validatePasswordInteractor: ValidatePasswordInteractor,
    private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
    private val parentPresenter: ParentPresenter,
    private val myGamesInteractor: MyGamesInteractor,
    private val gameRepository: GameRepository) : BasePresenter<MyGamesListView, MyGamesListViewViewModel>(), MyGamesListPresenter, RestorablePresenter<MyGamesListViewViewModel> {

  override fun initNewViewModel(arguments: Bundle?): MyGamesListViewViewModel {
    val gamesListViewModel = MyGamesListViewViewModel()
    gamesListViewModel.toolbarTitle = RpsApp.app().getString(R.string.my_games)
    return gamesListViewModel
  }

  override fun restoreViewModel(
      viewModelMy: MyGamesListViewViewModel) {
    super.restoreViewModel(viewModelMy)
    viewModelMy.setNeedUpdate(true)
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
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    compositeSubscription.add(
        getCheckUserInGameObservable(model, currentUserId, checkUserJoinedGameInteractor)
            .subscribe(
                { userInGame ->
                  if (userInGame!!) {
                    navigateToGameScreen(model)
                  } else {
                    val passwordDialogViewModel = PasswordDialogViewModel()
                    passwordDialogViewModel.title = StringUtils.getStringById(R.string.create_game)
                    passwordDialogViewModel.gameModel = model
                    viewModel.passwordDialogViewModel = passwordDialogViewModel
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
            val snack = BaseError(BaseErrorType.SNACK,
                RpsApp.app().getString(R.string.error_incorrect_password))
            view.showError(snack)
          }
        }))
  }

  override fun getData() {
    view.setData(viewModel)
    viewModel.setNeedUpdate(false)
    view.showContent()
    view.showLoading()
    compositeSubscription.add(
        myGamesInteractor.getMyGamesObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
              this.viewModel.items = data
              view.hideLoading()
              view.updateList(this.viewModel.items)
            }, { Crashlytics.logException(it) }))
  }

  override fun onItemClicked(item: IFlexible<*>): Boolean {
    if (item is TwoLineWithIdViewModel) {
      val gameModel = gameRepository.getGameModelById(item.id)
      if (gameModel != null) {
        val bundle = Bundle()
        bundle.putParcelable(GameModel.KEY, gameModel)
        bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true)
        parentPresenter.navigateToFragment(NavigationUtils.GAME_FRAGMENT, bundle)
      }
    }
    return true
  }
}
