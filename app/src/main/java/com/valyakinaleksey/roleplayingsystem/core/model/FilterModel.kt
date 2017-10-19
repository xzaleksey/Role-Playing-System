package com.valyakinaleksey.roleplayingsystem.core.model

import java.io.Serializable

class FilterModel @JvmOverloads constructor(private var query: String = "",
    private var previousQuery: String = query) : Serializable {

  fun setQuery(query: String) {
    previousQuery = this.query
    this.query = query
  }

  fun getQuery(): String {
    return query
  }

  fun isCleared(): Boolean {
    return previousQuery.isNotEmpty() && query.isEmpty()
  }

  fun copy(): FilterModel {
    return FilterModel(query, previousQuery)
  }
}