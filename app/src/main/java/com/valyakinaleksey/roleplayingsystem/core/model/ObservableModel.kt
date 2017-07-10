package com.valyakinaleksey.roleplayingsystem.core.model

import rx.Subscriber
import rx.Subscription
import rx.subjects.Subject
import rx.subscriptions.CompositeSubscription

class ObservableModel<T> private constructor(var modelName: String,
    private var value: T,
    private var subject: Subject<Pair<String, Any?>, Pair<String, Any?>>,
    val clazz: Class<*>,
    val nullable: Boolean) {

  private val compositeSubscription: CompositeSubscription = CompositeSubscription()

  fun getValue(): T {
    return value
  }

  fun updateValue(value: T) {
    this.value = value
    subject.onNext(Pair(modelName, value))
  }

  @Suppress("UNCHECKED_CAST")
  fun observeModel(subscriber: Subscriber<T>): Subscription {
    val subscription = subject
        .filter { it.first == modelName }
        .map { it.second as T }
        .subscribe(subscriber)
    subscriber.onNext(getValue())
    compositeSubscription.add(subscription)
    return subscription
  }

  fun dispose() {
    compositeSubscription.clear()
  }

  companion object {
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(modelName: String, value: T,
        subject: Subject<Pair<String, Any?>, Pair<String, Any?>>): ObservableModel<T> {
      return ObservableModel(modelName, value,
          subject, value::class.java as Class<T>, false)
    }

    @JvmStatic
    fun <T : R?, R>  createNullable(modelName: String, value: R,
        subject: Subject<Pair<String, Any?>, Pair<String, Any?>>,
        clazz: Class<*>): ObservableModel<R> {
      return ObservableModel(modelName, value,
          subject, clazz, true)
    }
  }
}