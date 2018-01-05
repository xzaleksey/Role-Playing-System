package com.valyakinaleksey.roleplayingsystem.modules.userprofile.domain

import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.ShadowDividerViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.repository.DrawableRepository
import com.valyakinaleksey.roleplayingsystem.core.repository.StringRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersGameRepository
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter.FlexibleGameViewModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import rx.functions.Func2

class UserProfileInteractorImpl(
        private val charactersGameRepository: CharactersGameRepository,
        private val gameRepository: GameRepository,
        private val drawableRepository: DrawableRepository,
        private val stringRepository: StringRepository
) : UserProfileInteractor {

    private val lastItemsCount = 2

    override fun observeUserProfile(userId: String): Observable<List<IFlexible<*>>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return Observable.combineLatest(gameRepository.getLastGamesModelByUserId(userId, lastItemsCount),
                charactersGameRepository.observeCharactersLastCharacters(userId, lastItemsCount),
                Func2 { gameModels, characters ->
                    val result = mutableListOf<IFlexible<*>>()
                    if (!gameModels.isEmpty()) {
                        result.add(getSubHeaderViewModel(stringRepository.getGames()))
                        gameModels.values.forEach { gameModel ->
                            result.add(FlexibleGameViewModel(gameModel.id,
                                    gameModel.name,
                                    "${stringRepository.getMaster()} ${gameModel.masterName}",
                                    StringUtils.areEqual(currentUserId, gameModel.masterId)
                            ))
                        }
                        result.add(ShadowDividerViewModel(result.lastIndex))
                    }
                    if (characters.isNotEmpty()) {
                        result.add(getSubHeaderViewModel(stringRepository.getCharacters()))
                        characters.values.forEach { value ->
                            result.add(FlexibleAvatarWithTwoLineTextModel(value.name,
                                    value.gameClassModel.name + ", " + value.gameRaceModel.name,
                                    { drawableRepository.getMageIcon() },
                                    value.photoUrl,
                                    value.uid
                            ))
                        }
                        result.add(ShadowDividerViewModel(result.lastIndex))

                    }
                    return@Func2 result
                })
    }

    private fun getSubHeaderViewModel(text: String): SubHeaderViewModel = SubHeaderViewModel(text, true)

}


interface UserProfileInteractor {
    fun observeUserProfile(userId: String): Observable<List<IFlexible<*>>>
}