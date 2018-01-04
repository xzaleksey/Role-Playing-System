package com.valyakinaleksey.roleplayingsystem.core.repository

import android.graphics.drawable.Drawable
import com.valyakinaleksey.roleplayingsystem.R

class DrawableRepositoryImpl(private val resourcesProvider: ResourcesProvider) : DrawableRepository {
    override fun getProfileIcon(): Drawable? = resourcesProvider.getDrawable(R.drawable.profile_icon)

}

interface DrawableRepository {
    fun getProfileIcon(): Drawable?
}
