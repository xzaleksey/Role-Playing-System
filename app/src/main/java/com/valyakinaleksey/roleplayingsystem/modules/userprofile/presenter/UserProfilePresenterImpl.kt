package com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter

import android.content.Context
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.model.ResponseModel
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.user.CurrentUserRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter.UserProfileGameViewModel
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.domain.UserProfileInteractor
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.UserProfileView
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel
import com.valyakinaleksey.roleplayingsystem.utils.*
import eu.davidea.flexibleadapter.items.IFlexible

class UserProfilePresenterImpl(private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
                               private val validatePasswordInteractor: ValidatePasswordInteractor,
                               private val parentPresenter: ParentPresenter,
                               private val userProfileInteractor: UserProfileInteractor,
                               private val userRepository: UserRepository,
                               private val gameRepository: GameRepository,
                               private val currentUserRepository: CurrentUserRepository) : BasePresenter<UserProfileView, UserProfileViewModel>(), RestorablePresenter<UserProfileViewModel>, UserProfilePresenter {

    override fun initNewViewModel(arguments: Bundle?): UserProfileViewModel {
        val userProfileViewModel = UserProfileViewModel()
        userProfileViewModel.displayName = RpsApp.app().getString(R.string.profile)
        val userId = arguments?.getString(UserProfileViewModel.USER_ID) ?: FireBaseUtils.getCurrentUserId()
        userProfileViewModel.userId = userId
        userProfileViewModel.isCurrentUser = FireBaseUtils.getCurrentUserId() == userId
        return userProfileViewModel
    }

    override fun restoreViewModel(
            viewViewModelMy: UserProfileViewModel) {
        super.restoreViewModel(viewViewModelMy)
        viewViewModelMy.setNeedUpdate(true)
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
        super.getData()
        compositeSubscription.add(userRepository.getUserByUid(FireBaseUtils.getCurrentUserId())
                .compose(RxTransformers.applySchedulers())
                .subscribe(object : DataObserver<User>() {
                    override fun onData(data: User) {
                        viewModel.displayName = data.name
                        viewModel.email = data.email
                        viewModel.avatarUrl = data.photoUrl
                        viewModel.masterGamesCount = data.countOfGamesMastered.toString()
                        viewModel.totalGamesCount = (data.countOfGamesMastered + data.countOfGamesPlayed).toString()
                        view.showContent()
                        view.showLoading()
                        compositeSubscription.add(userProfileInteractor.observeUserProfile(viewModel.userId)
                                .compose(RxTransformers.applySchedulers())
                                .subscribe(object : DataObserver<List<IFlexible<*>>>() {
                                    override fun onData(data: List<IFlexible<*>>) {
                                        viewModel.items = data
                                        view.hideLoading()
                                        view.updateList(data)
                                    }
                                })
                        )
                    }
                }))

    }

    override fun onItemClicked(item: IFlexible<*>): Boolean {
        if (item is UserProfileGameViewModel) {
            val gameModel = gameRepository.getGameModelById(item.id)
            if (gameModel != null) {
                navigateToGameScreen(gameModel)
            }
        }
        return true
    }

    override fun editProfile() {
        view.openDialog(UserProfileGameViewModel.CHANGE_USER_NAME)
    }

    override fun onEditName(name: String) {
        compositeSubscription.add(currentUserRepository.updateDisplayName(name)
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter({ view.showLoading() }, { view.hideLoading() }))
                .subscribe(object : DataObserver<ResponseModel>() {
                    override fun onData(data: ResponseModel) {
                        viewModel.displayName = name
                        view.preFillModel(viewModel)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        view.showError(BaseError(BaseErrorType.SNACK, StringUtils.getStringById(R.string.error_network_connection)))
                    }
                }))
    }

}
