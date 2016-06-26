package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for view errors
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ErrorType {
    ErrorTypes type() default ErrorTypes.DEFAULT;
}
