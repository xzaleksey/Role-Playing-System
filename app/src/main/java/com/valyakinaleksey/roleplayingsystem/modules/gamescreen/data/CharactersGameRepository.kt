package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseGameRepository
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

class CharactersGameRepositoryImpl(
        private val userRepository: UserRepository,
        private val classesRepository: GameClassesRepository,
        private val racesRepository: GameRacesRepository,
        private val context: Context
) : AbstractFirebaseGameRepositoryImpl<GameCharacterModel>(GameCharacterModel::class.java), CharactersGameRepository {

    private val undefined = context.getString(R.string.undefined)

    override fun getDataBaseReference(gameId: String): DatabaseReference {
        return FireBaseUtils.getTableReference(FireBaseUtils.GAME_CHARACTERS).child(gameId)
    }

    override fun observeData(gameId: String): Observable<MutableMap<String, GameCharacterModel>> {
        return super.observeData(gameId).concatMap { characters ->
            return@concatMap Observable.zip(userRepository.usersMap, classesRepository.getData(gameId),
                    racesRepository.getData(gameId),
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

    override fun getMyCharacters(gameId: String): Observable<MutableMap<String, GameCharacterModel>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return getData(gameId)
                .map {
                    val myCharacters = linkedMapOf<String, GameCharacterModel>()
                    it.values
                            .asSequence()
                            .filter { it.uid == currentUserId }
                            .forEach { myCharacters.put(it.id, it) }
                    return@map myCharacters
                }
    }

    override fun updateLastPlayedGameCharacters(gameId: String): Observable<MutableMap<String, GameCharacterModel>> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        return getMyCharacters(gameId)
                .doOnNext {
                    for (gameCharacterModel in it.values) {
                        FireBaseUtils.getTableReference(CHARACTERS_IN_USER)
                                .child(currentUserId)
                                .child(gameId).child(IdDateModel.DATE_VISITED)
                                .setValue(ServerValue.TIMESTAMP)
                    }
                }
    }

}

interface CharactersGameRepository : FirebaseGameRepository<GameCharacterModel> {
    fun getMyCharacters(gameId: String): Observable<MutableMap<String, GameCharacterModel>>
    fun updateLastPlayedGameCharacters(gameId: String): Observable<MutableMap<String, GameCharacterModel>>
}