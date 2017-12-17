package com.valyakinaleksey.roleplayingsystem.core.model

import java.io.Serializable

open class FilterModel @JvmOverloads constructor(private var query: String = "",
                                                 protected var previousQuery: String = query) : Serializable {

    constructor(filterModel: FilterModel) : this(filterModel.getQuery(), filterModel.previousQuery)

    fun setQuery(query: String) {
        previousQuery = this.query
        this.query = query
    }

    fun getQuery(): String = query

    fun isEmpty(): Boolean = query.isBlank()

    fun isCleared(): Boolean = previousQuery.isNotEmpty() && query.isEmpty()

}