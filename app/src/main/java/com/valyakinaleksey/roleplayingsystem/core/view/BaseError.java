package com.valyakinaleksey.roleplayingsystem.core.view;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorType;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorTypes;

public enum BaseError {
    NO_CONNECTION,
    NO_DATA,
    @ErrorType(type = ErrorTypes.ONE_SHOT)
    SNACK,
    @ErrorType(type = ErrorTypes.ONE_SHOT)
    TOAST;

    protected String error;

    public void setValue(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return error;
    }
}
