package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.unc.lac.baboon.topic.Topic;

/**
 * This class is as a wrapper that defines a TaskObject as a pair of an object
 * instance and a method, which are subscribed to a topic. It is used internally
 * by framework and should not be known by user.
 * 
 * A task is a method annotated with
 * {@link org.unc.lac.baboon.annotations.Task}. When the Task method, along with
 * an instance of an object are subscribed to a topic, the framework tries to
 * run this task on loop, and it is synchronized by the Petri Net Core.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class TaskSubscription extends AbstractTaskSubscription {

    public TaskSubscription(Object objInstance, Method objMethod, Topic topic) {
        super(objInstance, objMethod, topic);
    }

    /**
     * This method makes the instance of the object defined in this Task to
     * execute the method defined in this Task.
     */
    public void executeMethod() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        getMethod().invoke(getObject());
    }
}
