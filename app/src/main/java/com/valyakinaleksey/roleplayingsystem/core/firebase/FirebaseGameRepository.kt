package com.valyakinaleksey.roleplayingsystem.core.firebase

import rx.Completable
import rx.Observable

interface FirebaseGameRepository<T> {

    fun observeData(gameId: String): Observable<MutableMap<String, T>>

    fun getData(gameId: String): Observable<MutableMap<String, T>>

    fun addData(gameId: String, data: T): Observable<T>

    fun removeData(gameId: String, data: T): Completable

    fun getFirstElements(gameId: String, limit: Int): Observable<MutableMap<String, T>>
    fun removeData(gameId: String, id: String): Completable
    fun removeData(gameId: String, ids: List<String>): Completable
}