package org.unc.lac.baboon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used on methods. A method annotated with Task can
 * be subscribed to a {@link Topic}, allowing Baboon framework to handle its
 * execution. After subscribing a Task annotated method (and the object instance
 * to execute the method) to a topic, the framework automatically creates a
 * Thread for running the task based on topic's permission and the decisions
 * {@link PetriMonitor} takes. After a Task annotated method ends its execution,
 * the framework automatically fires the transitions and sets the guards
 * specified by the subscribed topic's fire callback and set guard callback.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Task {
}
