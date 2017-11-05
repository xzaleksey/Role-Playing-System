package com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter

import android.content.Context
import android.os.Bundle
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
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter.UserProfilePresenter
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.UserProfileView
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel
import com.valyakinaleksey.roleplayingsystem.utils.*
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils
import eu.davidea.flexibleadapter.items.IFlexible

class UserProfilePresenterImpl(private val checkUserJoinedGameInteractor: CheckUserJoinedGameInteractor,
                               private val validatePasswordInteractor: ValidatePasswordInteractor,
                               private val parentPresenter: ParentPresenter,
                               private val gameRepository: GameRepository) : BasePresenter<UserProfileView, UserProfileViewModel>(), RestorablePresenter<UserProfileViewModel>, UserProfilePresenter {

    override fun initNewViewModel(arguments: Bundle?): UserProfileViewModel {
        val gamesListViewModel = UserProfileViewModel()
        gamesListViewModel.toolbarTitle = RpsApp.app().getString(R.string.my_games)
        return gamesListViewModel
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
    }

    override fun onItemClicked(item: IFlexible<*>): Boolean {
        if (item is TwoLineWithIdViewModel) {
            val gameModel = gameRepository.getGameModelById(item.id)
            if (gameModel != null) {
                val bundle = Bundle()
                bundle.putParcelable(GameModel.KEY, gameModel)
                bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true)
                parentPresenter.navigateToFragment(NavigationScreen.GAME_FRAGMENT, bundle)
            }
        }
        return true
    }
}
