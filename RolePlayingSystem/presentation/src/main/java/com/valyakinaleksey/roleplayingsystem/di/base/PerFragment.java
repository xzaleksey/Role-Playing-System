package com.valyakinaleksey.roleplayingsystem.di.base;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Annotation for Dagger component that is scoped to an Fragment.
 */
@Scope
@Retention(CLASS)
public @interface PerFragment {
}
