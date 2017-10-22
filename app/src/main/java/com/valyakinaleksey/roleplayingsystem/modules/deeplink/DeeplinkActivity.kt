package com.valyakinaleksey.roleplayingsystem.modules.deeplink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import javax.inject.Inject

class DeepLinkDispatchActivity : AppCompatActivity() {

  @Inject lateinit var deepLinkParser: DeepLinksParser

  override fun onCreate(savedInstanceState: Bundle?) {
    RpsApp.getAppComponent().inject(this)
    super.onCreate(savedInstanceState)
    onNewIntent(intent)
  }

  override fun onNewIntent(intent: Intent?) {
    if (intent?.data != null) {
      parseIntentData(intent.data!!)
    } else {
      finish()
    }
  }

  private fun parseIntentData(data: Uri) {
    startActivity(deepLinkParser.parse(data.host, data.pathSegments))
    finish()
  }
}