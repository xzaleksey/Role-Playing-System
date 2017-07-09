package com.valyakinaleksey.roleplayingsystem.core.model

import rx.subjects.PublishSubject
import java.lang.IllegalStateException

class ObservableModelHolder() {
  private val subject = PublishSubject.create <Pair<String, Any?>>().toSerialized()
  private val observableModels: MutableMap<String, ObservableModel<*>> = HashMap()

  constructor(values: Map<String, *>) : this() {
    for ((name, value) in observableModels) {
      addModel(name, value)
    }
  }

  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : Any?> getModel(modelName: String): ObservableModel<T> {
    val observableModel = `access$observableModels`[modelName] ?: throw NoSuchElementException(
        "no property exists for this modelName")
    if (observableModel.getValue() !is T) {
      throw IllegalStateException("property has different type ${observableModel.getValue()}")
    }
    return observableModel as ObservableModel<T>
  }

  fun <T : Any> addModel(modelName: String, value: T) {
    checkPropertyExists(modelName)
    observableModels.put(modelName,
        ObservableModel.create(modelName,
            value, subject))
  }

  fun <T : Any?> addNullableModel(modelName: String, value: T, clazz: Class<T>) {
    checkPropertyExists(modelName)
    observableModels.put(modelName,
        ObservableModel.createNullable(
            modelName, value, subject, clazz))
  }

  inline fun <reified T : Any?> getValue(modelName: String): T? {
    return getModel<T>(modelName).getValue()
  }

  inline fun <reified T : Any?> setValue(modelName: String, value: T) {
    getModel<T>(modelName).updateValue(value)
  }

  private fun checkPropertyExists(modelName: String) {
    if (observableModels.containsKey(modelName)) {
      throw IllegalArgumentException("property already exists")
    }
  }

  fun dispose() {
    for ((_, value) in observableModels) {
      value.dispose()
    }
  }

  @PublishedApi
  internal val `access$observableModels`: MutableMap<String, ObservableModel<*>>
    get() = observableModels
}
