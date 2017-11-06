package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseGameRepository
import com.valyakinaleksey.roleplayingsystem.core.model.DefaultModelProvider
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.*
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.CHARACTERS_IN_USER
import rx.Observable
import rx.functions.Func3

class CharactersRepositoryImpl(
        private val userRepository: UserRepository,
        private val classesRepository: GameClassesRepository,
        private val racesRepository: GameRacesRepository,
        private val defaultModelProvider: DefaultModelProvider
) : AbstractFirebaseGameRepositoryImpl<GameCharacterModel>(GameCharacterModel::class.java), CharactersGameRepository {

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
                            mapCharacter(characterModel, users, races, classes)
                        }
                    })
        }
    }

    private fun mapCharacter(characterModel: GameCharacterModel, users: Map<String, User>, races: Map<String, GameRaceModel>, classes: Map<String, GameClassModel>): GameCharacterModel {
        if (!characterModel.uid.isNullOrBlank()) {
            characterModel.user = users[characterModel.uid]
        }
        if (characterModel.raceId.isNullOrBlank()) {
            characterModel.gameRaceModel = defaultModelProvider.defaultRaceModel
        } else {
            characterModel.gameRaceModel = races[characterModel.raceId]
        }
        if (characterModel.classId.isNullOrBlank()) {
            characterModel.gameClassModel = defaultModelProvider.defaultClassModel
        } else {
            characterModel.gameClassModel = classes[characterModel.classId]
        }
        return characterModel
    }

    private fun getCharacter(characterId: String, gameId: String): Observable<GameCharacterModel> {
        return RxFirebaseDatabase.observeSingleValueEvent(FireBaseUtils.getTableReference(FireBaseUtils.GAME_CHARACTERS).child(gameId).child(characterId))
                .concatMap { t ->
                    if (t.exists()) {
                        val gameCharacterModel = t.getValue(GameCharacterModel::class.java)!!
                        return@concatMap Observable.zip(userRepository.usersMap,
                                classesRepository.getData(gameId),
                                racesRepository.getData(gameId),
                                { users: Map<String, User>, classes: Map<String, GameClassModel>, races: Map<String, GameRaceModel> ->
                                    mapCharacter(gameCharacterModel, users, races, classes)
                                })

                    } else {
                        return@concatMap Observable.just(defaultModelProvider.defaultGameCharacter)
                    }
                }
    }

    override fun observeCharactersLastNCharacters(userId: String, count: Int): Observable<MutableMap<String, GameCharacterModel>> {
        val reference = FireBaseUtils.getTableReference(CHARACTERS_IN_USER).child(userId)
        return RxFirebaseDatabase.observeValueEvent(reference).map { t ->
            val charsIds = mutableMapOf<String, IdDateModel>()
            val charsIdsList = mutableListOf<GameIdDateModel>()
            if (t.exists()) {
                for (child in t.children) {
                    val idDateModel = child.getValue(IdDateModel::class.java)!!
                    charsIds.put(child.key, idDateModel)
                }
                val sortedMap = mutableMapOf<Long, String>()
                for ((id, charId) in charsIds) {
                    sortedMap.put(charId.dateVisitedLong, id)
                }
                var valuesCounter = 0
                for (id in sortedMap.values.reversed()) {
                    if (valuesCounter == count) {
                        break
                    }
                    charsIdsList.add(GameIdDateModel(id, charsIds[id]!!))
                    valuesCounter++
                }
            }
            return@map charsIdsList
        }.concatMap {
            val observables = mutableListOf<Observable<GameCharacterModel>>()
            for (gameIdDateModel in it) {
                observables.add(getCharacter(gameIdDateModel.idDateModel.id, gameIdDateModel.id))
            }
            return@concatMap Observable.concat(observables).toList()
        }.map { characters ->
            val result = linkedMapOf<String, GameCharacterModel>()
            for (gameCharacterModel in characters) {
                result.put(gameCharacterModel.id, gameCharacterModel)
            }
            return@map result
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
    fun observeCharactersLastNCharacters(userId: String, count: Int): Observable<MutableMap<String, GameCharacterModel>>
}