package com.valyakinaleksey.roleplayingsystem.di.base;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Helper methods related to dependency injection.
 */
public final class InjectHelper {

    private InjectHelper() {
    }

    /**
     * Returns component provided by given Activity.
     *
     * @param componentType Class of component
     * @param activity      Activity providing the component
     * @param <C>           Type of component
     * @return Component instance
     */
    @SuppressWarnings("unchecked")
    public static <C> C getComponent(@NonNull final Class<C> componentType,
                                     @NonNull final Activity activity) {

        return componentType.cast(((HasComponent<C>) activity).getComponent());
    }
}
