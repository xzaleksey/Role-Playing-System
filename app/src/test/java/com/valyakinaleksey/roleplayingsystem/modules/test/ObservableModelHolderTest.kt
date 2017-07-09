package com.valyakinaleksey.roleplayingsystem.modules.test

import com.rules.SchedulersRule
import com.valyakinaleksey.roleplayingsystem.core.model.ObservableModelHolder
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber
import rx.schedulers.TestScheduler

class ObservableModelHolderTest {
  private val testScheduler = TestScheduler()
  @JvmField
  @Rule var schedulersRule = SchedulersRule(testScheduler)
  lateinit var observableModelHolder: ObservableModelHolder

  @Before
  fun init() {
    observableModelHolder = ObservableModelHolder()
  }

  @Test
  fun testAddModel() {
    observableModelHolder.addModel("x", 5)
    val model = observableModelHolder.getModel<Int?>("x")
    Assert.assertEquals(5, model.getValue())
    model.updateValue(3)
  }

  @Test
  fun testAddNullableModel() {
    observableModelHolder.addNullableModel("x", 5, Int::class.java)
    val model = observableModelHolder.getModel<Int?>("x")
    Assert.assertEquals(5, model.getValue())
    model.updateValue(null)
    Assert.assertNull(model.getValue())
  }

  @Test
  fun testObservableModelObserve() {
    observableModelHolder.addModel("x", 5)
    val testSubscriber = TestSubscriber<Int>()
    val model = observableModelHolder.getModel<Int>("x")
    model.observeModel(testSubscriber)
    Assert.assertEquals(5, model.getValue())
    model.updateValue(3)
    testScheduler.triggerActions()
    testSubscriber.assertValues(5, 3)
  }
}