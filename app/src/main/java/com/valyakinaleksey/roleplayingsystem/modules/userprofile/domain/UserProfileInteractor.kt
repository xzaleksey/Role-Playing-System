package com.valyakinaleksey.roleplayingsystem.modules.userprofile.domain

import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.interfaces.MaterialDrawableProviderImpl
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersGameRepository
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable

class UserProfileInteractorImpl(private val charactersGameRepository: CharactersGameRepository) : UserProfileInteractor {

    override fun observeUserProfile(userId: String): Observable<List<IFlexible<*>>> {
        return charactersGameRepository.observeCharactersLastNCharacters(userId, 2)
                .take(1)
                .concatMap { characters ->
                    val result = mutableListOf<IFlexible<*>>()
                    if (characters.isNotEmpty()) {
                        result.add(SubHeaderViewModel(StringUtils.getStringById(R.string.characters)))
                        for ((_, value) in characters) {
                            result.add(FlexibleAvatarWithTwoLineTextModel(value.name,
                                    value.gameClassModel.name + ", " + value.gameRaceModel.name,
                                    MaterialDrawableProviderImpl(value.user.name, value.uid),
                                    value.photoUrl,
                                    value.uid
                            ))
                        }
                    }

                    return@concatMap Observable.just(result)
                }
    }
}


interface UserProfileInteractor {
    fun observeUserProfile(userId: String): Observable<List<IFlexible<*>>>
}