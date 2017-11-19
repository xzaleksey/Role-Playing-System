package com.valyakinaleksey.roleplayingsystem.modules.userprofile.domain

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.ShadowDividerViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersGameRepository
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter.UserProfileGameViewModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import rx.functions.Func2

class UserProfileInteractorImpl(
        private val charactersGameRepository: CharactersGameRepository,
        private val gameRepository: GameRepository,
        private val context: Context
) : UserProfileInteractor {

    private val lastItemsCount = 2

    override fun observeUserProfile(userId: String): Observable<List<IFlexible<*>>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return Observable.combineLatest(gameRepository.getLastGamesModelByUserId(userId, lastItemsCount),
                charactersGameRepository.observeCharactersLastCharacters(userId, lastItemsCount),
                Func2 { gameModels, characters ->
                    val result = mutableListOf<IFlexible<*>>()
                    if (!gameModels.isEmpty()) {
                        result.add(SubHeaderViewModel(getHeader(StringUtils.getStringById(R.string.games)), true))
                        gameModels.values.forEach { gameModel ->
                            result.add(UserProfileGameViewModel(gameModel.id,
                                    gameModel.name,
                                    "${StringUtils.getStringById(R.string.master)} ${gameModel.masterName}",
                                    StringUtils.areEqual(currentUserId, gameModel.masterId)
                            ))
                        }
                        result.add(ShadowDividerViewModel(result.lastIndex))
                    }
                    if (characters.isNotEmpty()) {
                        result.add(SubHeaderViewModel(getHeader(StringUtils.getStringById(R.string.characters)), true))
                        characters.values.forEach { value ->
                            result.add(FlexibleAvatarWithTwoLineTextModel(value.name,
                                    value.gameClassModel.name + ", " + value.gameRaceModel.name,
                                    { ContextCompat.getDrawable(context, R.drawable.mage) },
                                    value.photoUrl,
                                    value.uid
                            ))
                        }
                        result.add(ShadowDividerViewModel(result.lastIndex))

                    }
                    return@Func2 result
                })
    }

    private fun getHeader(headerText: String): CharSequence {
        val spannableString = SpannableString(headerText)
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), 0, headerText.length, 0)
        return spannableString
    }
}


interface UserProfileInteractor {
    fun observeUserProfile(userId: String): Observable<List<IFlexible<*>>>
}