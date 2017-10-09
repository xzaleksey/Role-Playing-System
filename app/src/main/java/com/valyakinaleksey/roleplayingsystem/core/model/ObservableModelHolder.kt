package com.valyakinaleksey.roleplayingsystem.core.model

import rx.subjects.PublishSubject
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput
import java.io.Serializable
import java.util.LinkedHashMap
import kotlin.NoSuchElementException

class ObservableModelHolder() : Externalizable {

  private val subject = PublishSubject.create <Pair<String, Any?>>().toSerialized()
  private val observableModels: MutableMap<String, ObservableModel<*>> = LinkedHashMap()

  constructor(values: Map<String, *>) : this() {
    for ((name, value) in observableModels) {
      addModel(name, value)
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : Any?> getModel(modelName: String): ObservableModel<T> {
    val observableModel = observableModels[modelName] ?: throw NoSuchElementException(
        "no property exists for this modelName")
    return observableModel as ObservableModel<T>
  }

  fun <T : Any> addModel(modelName: String, value: T) {
    checkPropertyExists(modelName)
    observableModels.put(modelName,
        ObservableModel.create(modelName,
            value, subject))
  }

  fun <T : Any?> addNullableModel(modelName: String, value: T?, clazz: Class<T>) {
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

  override fun readExternal(input: ObjectInput) {
    var key: String? = input.readUTF()
    while (key != null) {
      val clazz = input.readObject() as Class<*>
      val value = input.readObject() as Any?
      val nullable = input.readBoolean()
      val observableModel: ObservableModel<*>
      if (nullable) {
        observableModel = ObservableModel.createNullable(key, value, subject, clazz)
      } else {
        observableModel = ObservableModel.create(key, value!!, subject)
      }
      observableModels.put(key, observableModel)
      if (input.available() > 0) {
        key = input.readUTF()
      } else {
        key = null
      }
    }
  }

  override fun writeExternal(output: ObjectOutput) {
    for ((key, observableModel) in observableModels) {
      if (isSerializable(observableModel.clazz)) {
        output.writeUTF(key)
        output.writeObject(observableModel.clazz)
        output.writeObject(observableModel.getValue())
        output.writeBoolean(observableModel.nullable)
      }
    }
  }

  fun isSerializable(clazz: Class<*>): Boolean {
    clazz.interfaces?.let { interfaces ->
      if (interfaces.contains(Serializable::class.java)) {
        return true
      }
      for (classInterface in interfaces) {
        if (isSerializable(classInterface))
          return true
      }
    }
    if (clazz.superclass != null && isSerializable(clazz.superclass)) {
      return true
    }

    return false
  }

  companion object {
    @JvmStatic
    private val serialVersionUID = 1L
  }
}