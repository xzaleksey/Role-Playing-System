package com.valyakinaleksey.roleplayingsystem.core.model

import java.io.Serializable

class FilterModel @JvmOverloads constructor(var query: String = "") : Serializable {
  fun copy(): FilterModel {
    return FilterModel(query)
  }
}