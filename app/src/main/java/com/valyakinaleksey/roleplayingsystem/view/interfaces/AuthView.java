package com.valyakinaleksey.roleplayingsystem.view.interfaces;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorType;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorTypes;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.view.data.CautionDialogData;
import com.valyakinaleksey.roleplayingsystem.view.model.AuthViewModel;

public interface AuthView extends LceView<AuthViewModel, AuthView.AuthError> {

    void showCautionDialog(CautionDialogData data);

    enum AuthError {
        @ErrorType(type = ErrorTypes.ONE_SHOT)
        LOGIN;
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
