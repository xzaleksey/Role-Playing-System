package com.valyakinaleksey.roleplayingsystem.core.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId
import rx.Observable
import rx.functions.Func1
import timber.log.Timber

abstract class AbstractFirebaseRepositoryImpl<T : HasId>(
        private val clazz: Class<T>) : FirebaseRepository<T> {

    override fun observeData(): Observable<MutableMap<String, T>> {
        return RxFirebaseDatabase.observeValueEvent(getDataBaseReference(), getDataFunc())
    }

    override fun getData(): Observable<MutableMap<String, T>> {
        return RxFirebaseDatabase.observeSingleValueEvent(getDataBaseReference(), getDataFunc())
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

    abstract fun getDataBaseReference(): DatabaseReference
}