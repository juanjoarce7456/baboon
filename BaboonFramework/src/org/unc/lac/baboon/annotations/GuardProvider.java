package org.unc.lac.baboon.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation should be used on methods. A method annotated with
 * {@link GuardProvider} is automatically resolved and registered after an
 * {@link Action} object is created. The methods annotated with
 * {@link GuardProvider} are used by Baboon Framework to get the boolean flag of
 * the guard declared on its {@link #value()} attribute.
 * 
 * The {@link #value()} attribute must contain the name of the guard whose
 * boolean value is to be provided by the method.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 */

@Retention(RUNTIME)
@Target({ ElementType.METHOD })
public @interface GuardProvider {
    /**
     * The name of the guard whose boolean value is to be provided by the
     * method.
     */
    String value();
}
