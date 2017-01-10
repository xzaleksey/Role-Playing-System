package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import android.support.annotation.NonNull;

public interface HasComponent<C> {

    /**
     * @return Component provided by this class.
     */
    @NonNull
    C getComponent();
}
