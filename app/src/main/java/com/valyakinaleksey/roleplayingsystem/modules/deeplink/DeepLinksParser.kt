package com.valyakinaleksey.roleplayingsystem.modules.deeplink

import android.content.Context
import android.content.Intent
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthActivity
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.ParentActivity
import com.valyakinaleksey.roleplayingsystem.utils.DeepLinksUtils
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen

const val SCREEN = "screen"
const val GAME = "game"

class DeepLinksParserImpl constructor(private val context: Context) : DeepLinksParser {
  private val mapOfSupportedHosts = mutableMapOf(SCREEN to getScreen())

  override fun parse(host: String, segments: List<String>): Intent {
    val intent = if (FireBaseUtils.isLoggedIn()) {
      Intent(context, ParentActivity::class.java)
    } else {
      Intent(context, AuthActivity::class.java)
    }

    return mapOfSupportedHosts[host]?.invoke(intent, segments) ?: intent
  }

  private fun getScreen(): (Intent, List<String>) -> Intent {
    return { intent, segments ->
      if (segments.contains(GAME)) {
        intent.putExtra(
            DeepLinksUtils.DEEPLINK_TYPE_TAG,
            DeepLinksUtils.DEEPLINK_TYPE_SCREEN)
        intent.putExtra(
            DeepLinksUtils.DEEPLINK_SCREEN_TAG, NavigationScreen.GAME_FRAGMENT)
        intent.putExtra(DeepLinksUtils.DEEPLINK_GAME_ID_TAG, segments.last())
      }
      intent
    }
  }
}

interface DeepLinksParser {
  fun parse(host: String, segments: List<String>): Intent
}