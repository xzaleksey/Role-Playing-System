package com.valyakinaleksey.roleplayingsystem.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.view.interfaces.AuthView;
import com.valyakinaleksey.roleplayingsystem.view.model.AuthViewModel;

/**
 * Alias name for Weather ViewState
 */
public class AuthViewState extends StorageBackedNavigationLceViewStateImpl<AuthViewModel, AuthView.AuthError, AuthView> {

    public AuthViewState(ViewStateStorage storage) {
        super(storage);
    }
}
