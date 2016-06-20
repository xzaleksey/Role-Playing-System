package com.valyakinaleksey.roleplayingsystem.di.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * All entities that are related to account should be provided
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerAccount {
}
