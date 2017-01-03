package com.valyakinaleksey.roleplayingsystem.modules.auth.view;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorType;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorTypes;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;

public interface AuthView extends LceView<AuthViewModel, AuthView.AuthError> {

    void showSnackBarString(String s);

    enum AuthError {
        @ErrorType(type = ErrorTypes.ONE_SHOT)
        AUTH_ERROR;
        private String error;

        public void setValue(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return error;
        }
    }
}
