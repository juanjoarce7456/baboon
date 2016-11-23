package org.unc.lac.baboon.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation should be used on methods.
 * A method annotated with HappeningHandler can be subscribed
 * to a  {@link Topic}, allowing Baboon framework to handle its execution.
 * A HappeningHandler method is an event listener defined by the user.
 * Before a HappeningHandler annotated method begins its execution, the framework automatically
 * fires the transition specified by the subscribed topic's permission.
 * After a HappeningHandler annotated method ends its execution, the framework automatically
 * fires the transitions and sets the guards specified by the subscribed topic's fire callback
 * and set guard callback.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HappeningHandler {
}
