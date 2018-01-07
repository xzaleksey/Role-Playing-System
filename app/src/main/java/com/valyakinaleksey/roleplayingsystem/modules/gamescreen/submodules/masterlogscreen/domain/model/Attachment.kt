package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model

import java.io.Serializable

data class Attachment(val type: Int = IMAGE_LINK, val url: String) : Serializable {

    companion object {
        const val SIMPLE_LINK = 1
        const val IMAGE_LINK = 2
    }
}