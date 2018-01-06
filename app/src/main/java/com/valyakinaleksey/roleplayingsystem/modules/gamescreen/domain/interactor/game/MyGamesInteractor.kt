package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game

import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.ShadowDividerViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel
import com.valyakinaleksey.roleplayingsystem.core.repository.DrawableRepository
import com.valyakinaleksey.roleplayingsystem.core.repository.StringRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.GamesFilterModel
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter.FlexibleGameViewModel
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import rx.functions.Func4

class MyGamesUsecase(private val gamesRepository: GameRepository,
                     private val userRepository: UserRepository,
                     private val stringRepository: StringRepository,
                     private val drawableRepository: DrawableRepository) : MyGamesInteractor {

    override fun getMyGamesObservable(filter: Observable<GamesFilterModel>): Observable<MutableList<IFlexible<*>>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return Observable.combineLatest(filter,
                userRepository.observeUser(currentUserId),
                gamesRepository.getLastGamesModelByUserId(FireBaseUtils.getCurrentUserId(), 100),
                getAllGames(),
                getCombineFunction())
                .onBackpressureLatest()
    }

    private fun getCombineFunction(): Func4<GamesFilterModel, User, MutableMap<String, GameModel>, MutableMap<String, GameModel>, MutableList<IFlexible<*>>> {
        return Func4 { filterModel: FilterModel, user: User, myGames: Map<String, GameModel>, gameModels: Map<String, GameModel> ->
            return@Func4 getFilledModel(filterModel, user, ArrayList(myGames.values), ArrayList(gameModels.values))
        }
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
            val title = stringRepository.getAllGames()
            val currentUserId = FireBaseUtils.getCurrentUserId()
            val subHeaderViewModel = subHeaderViewModel(title)
            result.add(subHeaderViewModel)
            var itemCount = 0
            for ((index, game) in games.withIndex()) {
                if (!game.isFinished) {
                    if (isFiltered(filterModel, game)) {
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

    private fun isFiltered(filterModel: FilterModel, game: GameModel): Boolean {
        return filterModel.isEmpty() || game.masterName.contains(filterModel.getQuery(), true)
                || game.name.contains(filterModel.getQuery(), true)
    }

    private fun fillUser(user: User, result: MutableList<IFlexible<*>>) {

        result.add(subHeaderViewModel(stringRepository.getMyProfile()))
        result.add(FlexibleAvatarWithTwoLineTextModel(user.displayName,
                user.email,
                defaultPhotoProvider(),
                user.photoUrl,
                user.uid,
                true))
    }

    private fun defaultPhotoProvider() = { drawableRepository.getProfileIcon() }

    private fun subHeaderViewModel(title: String): SubHeaderViewModel {
        return SubHeaderViewModel.Builder()
                .title(title)
                .drawBottomDivider(true)
                .drawTopDivider(true)
                .build()
    }

    private fun fillMyGames(myGames: MutableList<GameModel>, result: MutableList<IFlexible<*>>) {
        if (myGames.isNotEmpty()) {
            val currentUserId = FireBaseUtils.getCurrentUserId()
            val lastGames = stringRepository.getMyLastGames()
            result.add(subHeaderViewModel(lastGames))
            for (game in myGames) {
                val model = FlexibleGameViewModel.Builder()
                        .id(game.id)
                        .title(game.name)
                        .description(getSecondaryText(game))
                        .payLoad(lastGames)
                        .showMasterIcon(game.isMaster(currentUserId))
                        .isGameLocked(!game.password.isNullOrBlank())
                        .build()
                result.add(model)
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

    private fun getSecondaryText(gameModel: GameModel): String = "${stringRepository.getMaster()} ${gameModel.masterName}"

    private fun getAllGames(): Observable<MutableMap<String, GameModel>> = gamesRepository.observeData().distinctUntilChanged()
}


interface MyGamesInteractor {
    fun getMyGamesObservable(filter: Observable<GamesFilterModel>): Observable<MutableList<IFlexible<*>>>
}