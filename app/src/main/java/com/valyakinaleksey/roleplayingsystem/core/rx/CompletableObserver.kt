package com.valyakinaleksey.roleplayingsystem.core.rx

import rx.CompletableSubscriber
import timber.log.Timber

abstract class CompletableObserver : CompletableSubscriber {

    override fun onError(e: Throwable?) {
        Timber.e(e)
    }
}