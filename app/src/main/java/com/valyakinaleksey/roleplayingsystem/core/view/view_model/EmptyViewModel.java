package com.valyakinaleksey.roleplayingsystem.core.view.view_model;

/**
 * Interface to determine empty state for view model
 */
public interface EmptyViewModel {
    default boolean isEmpty() {return false;}
}
