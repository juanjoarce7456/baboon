package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is as a wrapper that defines a TaskObject as a pair of an object
 * instance and a method. It is used internally by framework and should not be
 * known by user.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class TaskObject extends AbstractTask {

    public TaskObject(Object objInstance, Method objMethod) {
        super(objInstance, objMethod);
    }

    /**
     * This method makes the instance of the object defined in this Task to
     * execute the method defined in this Task.
     */
    public void executeMethod() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        getMethod().invoke(getObject());
    }
}
