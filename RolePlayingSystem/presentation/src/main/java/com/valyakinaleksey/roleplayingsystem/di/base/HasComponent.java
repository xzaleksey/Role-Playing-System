package com.valyakinaleksey.roleplayingsystem.di.base;

import android.support.annotation.NonNull;

/**
 * Interfaces for classes providing a component.
 */
public interface HasComponent<C> {

    /**
     * @return Component provided by this class.
     */
    @NonNull
    C getComponent();
}
