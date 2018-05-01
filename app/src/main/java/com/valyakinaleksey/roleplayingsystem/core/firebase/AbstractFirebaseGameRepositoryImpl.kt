package com.valyakinaleksey.roleplayingsystem.core.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId
import rx.Completable
import rx.Observable
import rx.functions.Func1
import timber.log.Timber

abstract class AbstractFirebaseGameRepositoryImpl<T : HasId>(private val clazz: Class<T>) : FirebaseGameRepository<T> {

    override fun observeData(gameId: String): Observable<MutableMap<String, T>> {
        return RxFirebaseDatabase.observeValueEvent(getDataBaseReference(gameId), getDataFunc()).distinctUntilChanged()
    }

    override fun getData(gameId: String): Observable<MutableMap<String, T>> {
        return RxFirebaseDatabase.observeSingleValueEvent(getDataBaseReference(gameId), getDataFunc())
    }

    private fun getDataFunc(): Func1<DataSnapshot, MutableMap<String, T>> {
        return Func1 {
            val result: MutableMap<String, T> = linkedMapOf()
            if (it.exists()) {
                val dataSnapshots = it.children.reversed()
                for (dataSnapshot in dataSnapshots) {
                    try {
                        val value = dataSnapshot.getValue(clazz)!!
                        value.id = dataSnapshot.key
                        result.put(dataSnapshot.key, value)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
            return@Func1 result
        }
    }

    abstract fun getDataBaseReference(gameId: String): DatabaseReference

    override fun addData(gameId: String, data: T): Observable<T> {
        return FireBaseUtils.observeSetValuePush(getDataBaseReference(gameId), data).map {
            data.id = it
            return@map data
        }
    }

    override fun removeData(gameId: String, data: T): Completable {
        return removeData(gameId, data.id)
    }

    override fun removeData(gameId: String, id: String): Completable {
        return FireBaseUtils.setData(null, getDataBaseReference(gameId).child(id)).toCompletable()
    }

    override fun removeData(gameId: String, ids: List<String>): Completable {
        val list = ids.map { removeData(gameId, it) }
        return Completable.concat(list)
    }

    override fun getFirstElements(gameId: String, limit: Int): Observable<MutableMap<String, T>> {
        return RxFirebaseDatabase.observeSingleValueEvent(getDataBaseReference(gameId).limitToFirst(limit), getDataFunc())
    }
}