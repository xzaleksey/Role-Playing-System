package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.IdDateModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.CHARACTERS_IN_USER
import rx.Observable
import rx.functions.Func3

class CharactersRepositoryImpl(
        private val gameId: String,
        private val userRepository: UserRepository,
        private val gameClassesRepository: GameClassesRepository,
        private val gameRacesRepository: GameRacesRepository,
        private val context: Context
) : AbstractFirebaseRepositoryImpl<GameCharacterModel>(GameCharacterModel::class.java), CharactersRepository {

    private val undefined = context.getString(R.string.undefined)

    override fun getDataBaseReference(): DatabaseReference {
        return FireBaseUtils.getTableReference(FireBaseUtils.GAME_CHARACTERS).child(gameId)
    }

    override fun observeData(): Observable<MutableMap<String, GameCharacterModel>> {
        return super.observeData().concatMap { characters ->
            return@concatMap Observable.zip(userRepository.usersMap, gameClassesRepository.getData(),
                    gameRacesRepository.getData(),
                    Func3 { users: Map<String, User>, classes: Map<String, GameClassModel>, races: Map<String, GameRaceModel> ->
                        return@Func3 characters.onEach { characterEntry ->
                            val characterModel = characterEntry.value
                            if (!characterModel.uid.isNullOrBlank()) {
                                characterModel.user = users[characterModel.uid]
                            }
                            if (characterModel.raceId.isNullOrBlank()) {
                                val gameRaceModel = GameRaceModel()
                                gameRaceModel.name = undefined
                                characterModel.gameRaceModel = gameRaceModel
                            } else {
                                characterModel.gameRaceModel = races[characterModel.raceId]
                            }
                            if (characterModel.classId.isNullOrBlank()) {
                                val gameClassModel = GameClassModel()
                                gameClassModel.name = undefined
                                characterModel.gameClassModel = gameClassModel
                            } else {
                                characterModel.gameClassModel = classes[characterModel.classId]
                            }
                        }
                    })
        }
    }

    override fun getMyCharacters(): Observable<MutableMap<String, GameCharacterModel>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return observeData()
                .take(1)
                .map {
                    val myCharacters = linkedMapOf<String, GameCharacterModel>()
                    it.values
                            .filter { it.uid == currentUserId }
                            .forEach { myCharacters.put(it.id, it) }
                    return@map myCharacters
                }
    }

    override fun updateLastPlayedGameCharacters(): Observable<MutableMap<String, GameCharacterModel>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return getMyCharacters()
                .doOnNext {
                    for (gameCharacterModel in it.values) {
                        FireBaseUtils.getTableReference(CHARACTERS_IN_USER)
                                .child(currentUserId)
                                .child(gameId).child(gameCharacterModel.id).child(IdDateModel.DATE_VISITED).setValue(ServerValue.TIMESTAMP)
                    }
                }
    }

}

interface CharactersRepository : FirebaseRepository<GameCharacterModel> {
    fun getMyCharacters(): Observable<MutableMap<String, GameCharacterModel>>
    fun updateLastPlayedGameCharacters(): Observable<MutableMap<String, GameCharacterModel>>
}