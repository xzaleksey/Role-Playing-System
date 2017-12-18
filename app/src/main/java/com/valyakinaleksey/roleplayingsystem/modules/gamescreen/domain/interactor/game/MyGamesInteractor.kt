package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game

import android.support.v7.content.res.AppCompatResources
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.ShadowDividerViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.GamesFilterModel
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter.FlexibleGameViewModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import rx.functions.Func4

class MyGamesUsecase(private val gamesRepository: GameRepository,
                     private val userRepository: UserRepository) : MyGamesInteractor {

    override fun getMyGamesObservable(filter: Observable<GamesFilterModel>): Observable<MutableList<IFlexible<*>>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return Observable.combineLatest(filter, userRepository.observeUser(currentUserId),
                gamesRepository.getLastGamesModelByUserId(FireBaseUtils.getCurrentUserId(), 100),
                getAllGames(),
                Func4 { filterModel: FilterModel, user: User, myGames: Map<String, GameModel>, gameModels: Map<String, GameModel> ->
                    return@Func4 getFilledModel(filterModel, user, ArrayList(myGames.values), ArrayList(gameModels.values))
                }).onBackpressureLatest()
    }

    private fun getFilledModel(
            filerModel: FilterModel,
            user: User,
            myGames: MutableList<GameModel>,
            values: MutableList<GameModel>): MutableList<IFlexible<*>> {
        val result = mutableListOf<IFlexible<*>>()
        if (filerModel.getQuery().isBlank()) {
            fillUser(user, result)
            fillMyGames(myGames, result)
        }
        fillAllGames(filerModel, values, result)
        return result
    }

    private fun fillAllGames(filterModel: FilterModel, games: MutableList<GameModel>, result: MutableList<IFlexible<*>>) {
        if (games.isNotEmpty()) {
            val title = StringUtils.getStringById(R.string.games)
            val currentUserId = FireBaseUtils.getCurrentUserId()
            val subHeaderViewModel = subHeaderViewModel(title)
            result.add(subHeaderViewModel)
            var itemCount = 0
            for ((index, game) in games.withIndex()) {
                if (!game.isFinished) {
                    if (filterModel.isEmpty() || game.masterName.contains(filterModel.getQuery(), true) || game.name.contains(filterModel.getQuery(), true)) {
                        itemCount++
                        val model = FlexibleGameViewModel.Builder()
                                .id(game.id)
                                .title(game.name)
                                .description(getSecondaryText(game))
                                .payLoad(title)
                                .showMasterIcon(game.isMaster(currentUserId))
                                .isGameLocked(!game.password.isNullOrBlank())
                                .build()
                        result.add(model)
                        addDivider(index, games, result)
                    }
                }
            }
            subHeaderViewModel.setTitle("$title ($itemCount)")
        }
    }

    private fun fillUser(user: User, result: MutableList<IFlexible<*>>) {
        result.add(subHeaderViewModel(StringUtils.getStringById(R.string.my_profile)))
        result.add(FlexibleAvatarWithTwoLineTextModel(user.displayName,
                user.email,
                {
                    return@FlexibleAvatarWithTwoLineTextModel AppCompatResources.getDrawable(RpsApp.app(), R.drawable.profile_icon)
                },
                user.photoUrl,
                user.uid,
                true))
        result.add(ShadowDividerViewModel(result.lastIndex))
    }

    private fun subHeaderViewModel(title: String) =
            SubHeaderViewModel(title, true)

    private fun fillMyGames(
            myGames: MutableList<GameModel>,
            result: MutableList<IFlexible<*>>) {
        if (myGames.isNotEmpty()) {
            val currentUserId = FireBaseUtils.getCurrentUserId()
            val lastGames = StringUtils.getStringById(R.string.my_last_games)
            result.add(subHeaderViewModel(lastGames))
            for ((index, game) in myGames.withIndex()) {
                val model = FlexibleGameViewModel.Builder()
                        .id(game.id)
                        .title(game.name)
                        .description(getSecondaryText(game))
                        .payLoad(lastGames)
                        .showMasterIcon(game.isMaster(currentUserId))
                        .isGameLocked(!game.password.isNullOrBlank())
                        .build()
                result.add(model)
                addDivider(index, myGames, result)
            }
        }
    }

    private fun addDivider(index: Int,
                           games: MutableList<GameModel>,
                           result: MutableList<IFlexible<*>>) {
        if (index == games.lastIndex) {
            result.add(ShadowDividerViewModel(result.lastIndex))
        }
    }

    private fun getSecondaryText(gameModel: GameModel): String {
        return "${StringUtils.getStringById(R.string.master)} ${gameModel.masterName}"
    }

    private fun getAllGames(): Observable<MutableMap<String, GameModel>> =
            gamesRepository.observeData().distinctUntilChanged()
}


interface MyGamesInteractor {
    fun getMyGamesObservable(filter: Observable<GamesFilterModel>): Observable<MutableList<IFlexible<*>>>
}