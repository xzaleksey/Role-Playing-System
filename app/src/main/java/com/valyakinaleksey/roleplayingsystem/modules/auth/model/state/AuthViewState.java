package com.valyakinaleksey.roleplayingsystem.modules.auth.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;

/**
 * Alias name for Weather ViewState
 */
public class AuthViewState extends StorageBackedNavigationLceViewStateImpl<AuthViewModel, AuthView.AuthError, AuthView> {
    private String s;

    @Override
    public void apply(AuthView view) {
        super.apply(view);
        switch (currentState) {
            case STATE_SHOW_SNACKBAR_STRING:
                view.showSnackBarString(s);
                currentState = STATE_UNINITIALIZED;
                break;
        }
    }

    public void showString(String s, AuthView view) {
        this.s = s;
        currentState = STATE_SHOW_SNACKBAR_STRING;
        apply(view);
    }

    public AuthViewState(ViewStateStorage storage) {
        super(storage);
    }
}
