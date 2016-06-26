package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;

/**
 * ViewState view ability to restore itself
 */
public interface SelfRestorableViewState {
    void save();
    void restore();
    void clean();
}