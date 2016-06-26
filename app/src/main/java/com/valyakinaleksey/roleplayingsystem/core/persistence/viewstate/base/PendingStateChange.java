package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;

/**
 * Pending view-based navigation
 */
public interface PendingStateChange<V> {
    void apply(V view);
}
