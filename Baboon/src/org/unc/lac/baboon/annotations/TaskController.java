package org.unc.lac.baboon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.javapetriconcurrencymonitor.monitor.PetriMonitor;

/**
 * This annotation should be used on methods. A method annotated with TaskController can
 * be subscribed to a {@link Topic}, allowing Baboon framework to handle its
 * execution. After subscribing a TaskController annotated method (and the object instance
 * to execute the method) to a topic, the framework automatically creates a
 * Thread for running the taskController based on topic's permission and the decisions
 * {@link PetriMonitor} takes. After a TaskController annotated method ends its execution,
 * the framework automatically fires the transitions and sets the guards
 * specified by the subscribed topic's fire callback and set guard callback.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TaskController {
}
