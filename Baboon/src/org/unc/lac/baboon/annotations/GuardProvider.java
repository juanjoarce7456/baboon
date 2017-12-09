package org.unc.lac.baboon.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.unc.lac.baboon.actioncontroller.ActionController;

/**
 * This annotation should be used on methods. A method annotated with
 * {@link GuardProvider} is automatically resolved and registered after an
 * {@link ActionController} object is created. The methods annotated with
 * {@link GuardProvider} are used by Baboon Framework to get the boolean flag of
 * the guard declared on its {@link #value()} attribute, so a method annotated
 * with {@link GuardProvider} must have a boolean return type. Also a method
 * annotated with {@link GuardProvider} must not accept parameters on its
 * signature.
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
     * Method to access the name of the guard configured on the annotation.
     * @return The name of the guard whose boolean value is to be provided by the
     * method.
     */
    String value();
}
