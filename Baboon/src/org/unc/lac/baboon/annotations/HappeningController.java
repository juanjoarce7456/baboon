package org.unc.lac.baboon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.unc.lac.baboon.topic.Topic;

/**
 * This annotation should be used on methods.
 * A method annotated with HappeningController can be subscribed
 * to a  {@link Topic}, allowing Baboon framework to handle its execution.
 * A HappeningController method is an event listener defined by the user.
 * Before a HappeningController annotated method begins its execution, the framework automatically
 * fires the transition specified by the subscribed topic's permission.
 * After a HappeningController annotated method ends its execution, the framework automatically
 * fires the transitions and sets the guards specified by the subscribed topic's fire callback
 * and set guard callback.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HappeningController {
}
