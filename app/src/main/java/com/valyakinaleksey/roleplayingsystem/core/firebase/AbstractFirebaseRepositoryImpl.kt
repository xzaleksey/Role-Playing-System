package com.valyakinaleksey.roleplayingsystem.core.firebase

import com.google.firebase.database.DatabaseReference
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId
import com.valyakinaleksey.roleplayingsystem.utils.makeList
import rx.Observable
import rx.functions.Func1
import timber.log.Timber

abstract class AbstractFirebaseRepositoryImpl<T : HasId>(
    private val clazz: Class<T>) : FirebaseRepository<T> {

  override fun observeData(): Observable<MutableMap<String, T>> {

    return RxFirebaseDatabase.observeValueEvent(getDataBaseReference(), Func1 {
      val result: MutableMap<String, T> = linkedMapOf()

      if (it.exists()) {
        val dataSnapshots = it.children.makeList()
        dataSnapshots.indices.reversed()
            .asSequence()
            .map { dataSnapshots[it].getValue(clazz)!! }
            .forEach {
              try {
                result.put(it.id, it)
              } catch (e: Exception) {
                Timber.e(e)
              }
            }
      }
      return@Func1 result
    })
  }

  abstract fun getDataBaseReference(): DatabaseReference
}