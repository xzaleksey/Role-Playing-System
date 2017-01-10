package com.valyakinaleksey.roleplayingsystem.core.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Dagger annotation
 * Custom scope for Activity
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {}
