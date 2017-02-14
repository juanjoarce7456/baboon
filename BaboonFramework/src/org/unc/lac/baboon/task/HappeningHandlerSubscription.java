package org.unc.lac.baboon.task;

import java.lang.reflect.Method;

import org.unc.lac.baboon.topic.Topic;

/**
 * This class is as a wrapper that defines a HappeningHandlerSubscription as a
 * pair of an object instance and a method, which are subscribed to a topic.
 * 
 * A HappeningHandler is a method annotated with
 * {@link org.unc.lac.baboon.annotations.HappeningHandler}. The HappeningHandler
 * method can be executed on any moment (asynchronously), for example as an
 * event listener, and the framework will synchronize its action using its Petri
 * Net Core.
 * 
 * HappeningHandlerSubscription class is used internally by framework and should
 * not be known by user.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class HappeningHandlerSubscription extends AbstractTaskSubscription {

    public HappeningHandlerSubscription(Object objInstance, Method objMethod, Topic topic) {
        super(objInstance, objMethod, topic);
    }

}
