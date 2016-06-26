package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;

/**
 * Saving / restoration view state by providing an object responsible for these actions
 */
public interface RestorableViewState<S> {

    void save(S out);

    RestorableViewState restore(S in);

}
