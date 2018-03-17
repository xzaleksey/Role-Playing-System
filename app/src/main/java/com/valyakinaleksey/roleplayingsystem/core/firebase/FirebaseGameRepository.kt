package com.valyakinaleksey.roleplayingsystem.core.firebase

import rx.Observable

interface FirebaseGameRepository<T> {

    fun observeData(gameId: String): Observable<MutableMap<String, T>>

    fun getData(gameId: String): Observable<MutableMap<String, T>>

    fun addData(gameId: String, data: T): Observable<T>
}