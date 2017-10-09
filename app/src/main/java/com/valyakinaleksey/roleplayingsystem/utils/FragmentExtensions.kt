package com.valyakinaleksey.roleplayingsystem.utils

import android.os.Bundle
import android.support.v4.app.Fragment

inline fun <reified T : Fragment> createFragment(args: Bundle?): T {
  val fragment = T::class.java.newInstance()
  fragment.arguments = args
  return fragment
}