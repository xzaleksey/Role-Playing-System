package com.valyakinaleksey.roleplayingsystem.core.view;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorType;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorTypes;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

public enum BaseError {
  @ErrorType(type = ErrorTypes.ONE_SHOT_OR_DEFAULT)
  NO_CONNECTION, NO_DATA, @ErrorType(type = ErrorTypes.ONE_SHOT)
  SNACK, @ErrorType(type = ErrorTypes.ONE_SHOT)
  TOAST;

  BaseError() {
  }

  protected String error = StringUtils.EMPTY_STRING;

  public void setValue(String error) {
    this.error = error;
  }

  @Override public String toString() {
    return error;
  }
}
