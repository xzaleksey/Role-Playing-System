package com.valyakinaleksey.roleplayingsystem.core.persistence;

/**
 * Factory for providing an instance of object graph for fragment
 */
public interface ComponentCreator<C> {
    C create(String fragmentId);
}
