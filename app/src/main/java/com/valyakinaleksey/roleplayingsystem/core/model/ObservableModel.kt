package com.valyakinaleksey.roleplayingsystem.core.model

import rx.Subscriber
import rx.Subscription
import rx.subjects.Subject
import rx.subscriptions.CompositeSubscription

class ObservableModel<T> {
  var modelName: String
  private var value: T

  private val subject: Subject<Pair<String, Any?>, Pair<String, Any?>>
  private val compositeSubscription: CompositeSubscription = CompositeSubscription()

  private constructor(modelName: String, value: T,
      subject: Subject<Pair<String, Any?>, Pair<String, Any?>>) {
    this.modelName = modelName
    this.value = value
    this.subject = subject
  }

  private constructor(modelName: String, value: T,
      subject: Subject<Pair<String, Any?>, Pair<String, Any?>>, clazz: Class<T>) : this(modelName,
      value, subject)

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
    fun <T : Any> create(modelName: String, value: T,
        subject: Subject<Pair<String, Any?>, Pair<String, Any?>>): ObservableModel<T> {
      return ObservableModel(modelName, value,
          subject)
    }

    @JvmStatic
    fun <T : Any?> createNullable(modelName: String, value: T,
        subject: Subject<Pair<String, Any?>, Pair<String, Any?>>,
        clazz: Class<T>): ObservableModel<T> {
      return ObservableModel(modelName, value,
          subject, clazz)
    }
  }
}