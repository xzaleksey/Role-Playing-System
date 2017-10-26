package com.valyakinaleksey.roleplayingsystem.core.view

import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import java.io.Serializable

class BaseError @JvmOverloads constructor(val baseError: BaseErrorType,
    val text: String = StringUtils.EMPTY_STRING) : Serializable {

  override fun toString(): String {
    return text
  }
}