package com.valyakinaleksey.roleplayingsystem.core.firebase

import rx.Observable

interface FirebaseRepository<T> {

  fun observeData(): Observable<MutableMap<String, T>>
}