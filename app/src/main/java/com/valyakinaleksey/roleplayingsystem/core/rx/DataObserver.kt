package com.valyakinaleksey.roleplayingsystem.core.rx

import com.crashlytics.android.Crashlytics
import rx.Subscriber
import timber.log.Timber

abstract class DataObserver<T> : Subscriber<T>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        Timber.e(e, "! got error")
        Crashlytics.logException(e)
    }

    override fun onNext(data: T) {
        onData(data)
    }

    abstract fun onData(data: T)
}
