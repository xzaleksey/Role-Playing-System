package com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter

import android.content.Context
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel
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
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.GamesFilterModel
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter.FlexibleGameViewModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.extensions.*
import eu.davidea.flexibleadapter.items.IFlexible
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

class MyGamesListPresenterImpl(private val createNewGameInteractor: CreateNewGameInteractor,
                               private val validatePasswordInteractor: ValidatePasswordInteractor,
                               private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
                               private val parentPresenter: ParentPresenter,
                               private val myGamesInteractor: MyGamesInteractor,
                               private val gameRepository: GameRepository) : BasePresenter<MyGamesListView, MyGamesListViewViewModel>(), MyGamesListPresenter, RestorablePresenter<MyGamesListViewViewModel> {

    private val filterSubject: BehaviorSubject<GamesFilterModel> = BehaviorSubject.create()

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

    override fun navigateToGameScreen(model: GameModel) {
        if (model.isMaster(FireBaseUtils.getCurrentUserId())) {
            parentPresenter.navigateToGame(gameModel = model)
            return
        }
        compositeSubscription.add(navigateToGameScreen(model, parentPresenter, checkUserJoinedGameInteractor))
    }

    override fun checkPassword(model: GameModel) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        compositeSubscription.add(getCheckUserInGameObservable(model, currentUserId, checkUserJoinedGameInteractor)
                .subscribe({ userInGame ->
                    if (userInGame!!) {
                        parentPresenter.navigateToGame(gameModel = model)
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
        super.getData()
        compositeSubscription.add(
                myGamesInteractor.getMyGamesObservable(filterSubject)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ data ->
                            this.viewModel.items = data
                            view.showContent()
                        }, { Crashlytics.logException(it) }))
    }

    override fun onItemClicked(item: IFlexible<*>): Boolean {
        when (item) {
            is FlexibleGameViewModel -> {
                val gameModel = gameRepository.getGameModelById(item.id)
                if (gameModel != null) {
                    if (gameModel.password.isNullOrBlank()) {
                        navigateToGameScreen(gameModel)
                    } else {
                        checkPassword(gameModel)
                    }
                }
            }
            is FlexibleAvatarWithTwoLineTextModel -> parentPresenter.navigateToMyProfile()
        }
        return true
    }

    override fun onSearchQueryChanged(queryText: String) {
        viewModel.filterModel.setQuery(queryText)
        filterSubject.onNext(GamesFilterModel(viewModel.filterModel))
    }
}
