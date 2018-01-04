package com.valyakinaleksey.roleplayingsystem.core.repository

import com.valyakinaleksey.roleplayingsystem.R

class StringRepositoryImpl(private val resourcesProvider: ResourcesProvider) : StringRepository {
    override fun getMaster(): String = resourcesProvider.getString(R.string.master)
    override fun getMyLastGames(): String = resourcesProvider.getString(R.string.my_last_games)
    override fun getAllGames(): String = resourcesProvider.getString(R.string.all_games)
    override fun getMyProfile(): String = resourcesProvider.getString(R.string.my_profile)
}

interface StringRepository {
    fun getAllGames(): String
    fun getMyProfile(): String
    fun getMyLastGames(): String
    fun getMaster(): String
}
