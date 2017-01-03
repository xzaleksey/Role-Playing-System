package com.valyakinaleksey.roleplayingsystem.modules.auth.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.auth.view.AuthView;
import com.valyakinaleksey.roleplayingsystem.modules.auth.model.AuthViewModel;

/**
 * Alias name for Weather ViewState
 */
public class AuthViewState extends StorageBackedNavigationLceViewStateImpl<AuthViewModel, AuthView.AuthError, AuthView> {

    public AuthViewState(ViewStateStorage storage) {
        super(storage);
    }
}
