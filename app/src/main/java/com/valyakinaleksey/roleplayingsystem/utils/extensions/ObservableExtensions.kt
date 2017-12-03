package com.valyakinaleksey.roleplayingsystem.utils.extensions

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
import com.google.firebase.auth.FirebaseAuth
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGameView
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType
import com.valyakinaleksey.roleplayingsystem.core.view.LceView
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils
import rx.Observable
import rx.Subscription

fun getValidatePasswordSubscription(validatePasswordInteractor: ValidatePasswordInteractor,
    password: String,
    correctPassword: String,
    block: (isValid: Boolean) -> Unit): Subscription {
  return validatePasswordInteractor.isPasswordValid(password, correctPassword)
      .subscribe({ aBoolean ->
        block.invoke(aBoolean)
      }, { Crashlytics.logException(it) })
}

fun <T : RequestUpdateViewModel> checkInternetConnection(view: LceView<T>,
    error: String): Subscription {
  return ReactiveNetwork.observeInternetConnectivity()
      .take(1)
      .filter { aBoolean -> !aBoolean }
      .subscribe { _ ->
        val snack = BaseError(BaseErrorType.SNACK, error)
        view.showError(snack)
      }
}

fun <V : LceView<D>, D : RequestUpdateViewModel> BasePresenter<V, D>.navigateToGameScreen(
    model: GameModel,
    parentPresenter: ParentPresenter,
    checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor): Subscription {
  val bundle = Bundle()
  bundle.putParcelable(GameModel.KEY, model)
  val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
  bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true)

  return getCheckUserInGameObservable(model, currentUserId,
      checkUserJoinedGameInteractor).subscribe({ userInGame ->
    if (userInGame) {
      parentPresenter.navigateToFragment(NavigationScreen.GAME_FRAGMENT, bundle)
    } else {
      parentPresenter.navigateToFragment(NavigationScreen.GAME_DESCRIPTION_FRAGMENT, bundle)
    }
  }, { this.handleThrowable(it) })
}

fun <V : LceView<D>, D : RequestUpdateViewModel> BasePresenter<V, D>.getCheckUserInGameObservable(
    model: GameModel,
    currentUserId: String,
    checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor): Observable<Boolean> {
  return checkUserJoinedGameInteractor.checkUserInGame(currentUserId, model)
      .compose(RxTransformers.applySchedulers())
      .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
}

fun <T : RequestUpdateViewModel, V> BasePresenter<V, T>.createNewGame(gameModel: GameModel,
    view: V,
    createNewGameInteractor: CreateNewGameInteractor): Subscription where V : LceView<T>, V : CreateGameView {
  val subscription = checkInternetConnection(view,
          RpsApp.app().getString(R.string.game_will_be_synched))
  return createNewGameInteractor.createNewGame(gameModel)
      .compose(RxTransformers.applySchedulers())
      .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
      .doOnTerminate { subscription.unsubscribe() }
      .subscribe({ id ->
        view.onGameCreated()
      }, { Crashlytics.logException(it) })
}

fun navigateToGameDescriptionScreen(model: GameModel,
    parentPresenter: ParentPresenter) {
  val bundle = Bundle()
  bundle.putParcelable(GameModel.KEY, model)
  bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true)
  parentPresenter.navigateToFragment(NavigationScreen.GAME_DESCRIPTION_FRAGMENT, bundle)
}