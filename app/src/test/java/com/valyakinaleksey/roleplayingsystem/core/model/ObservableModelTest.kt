package com.valyakinaleksey.roleplayingsystem.core.model

import com.rules.SchedulersRule
import org.junit.Test

import org.junit.Rule
import rx.observers.TestSubscriber
import rx.schedulers.TestScheduler
import rx.subjects.PublishSubject

class ObservableModelTest {
  private val testScheduler = TestScheduler()
  @JvmField
  @Rule var schedulersRule = SchedulersRule(testScheduler)

  @Test fun testObserveModel() {
    val subject: PublishSubject<Pair<String, Any?>> = PublishSubject.create()
    val testSubscriber = TestSubscriber<Int>()
    val observableModel = ObservableModel.create("x", 5, subject)
    observableModel.observeModel(testSubscriber)
    observableModel.updateValue(10)
    testScheduler.triggerActions()
    testSubscriber.assertValues(5, 10)
    testSubscriber.assertNoTerminalEvent()
  }
}