package com.valyakinaleksey.roleplayingsystem.core.rx

import rx.Subscriber
import timber.log.Timber

abstract class DataObserver<T> : Subscriber<T>() {

  override fun onCompleted() {

  }

  override fun onError(e: Throwable) {
    Timber.e(e, "! got error")
  }

  override fun onNext(data: T) {
    onData(data)
  }

  abstract fun onData(data: T)
}
