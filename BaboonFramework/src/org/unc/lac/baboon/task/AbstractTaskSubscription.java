package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.topic.Topic;

/**
 * This class is as a wrapper that defines an AbstractTaskSubscription as a pair
 * of an object instance and a method, which are subscribed to a topic. It is
 * used internally by framework and should not be known by user.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public abstract class AbstractTaskSubscription {
    Object object;
    Method method;
    Topic topic;
    private HashMap<String, Method> guardCallback = new HashMap<String, Method>();

    public AbstractTaskSubscription(Object objInstance, Method objMethod, Topic topic) {
        this.object = objInstance;
        this.method = objMethod;
        this.topic = topic;
    }

    /**
     * This method adds a new guard callback to the task. It is intended to be
     * used by the framework only, the user should not call this method.
     *
     * @param guardName
     *            The name of the guard to be set by the callback
     * @param callback
     *            The Method to call for obtaining the guard value. This method
     *            must be annotated with {@link GuardProvider} and must return a
     *            boolean value.
     * @throws IllegalArgumentException
     *             When guardName is empty or null, when callback is not
     *             annotated with {@link GuardProvider}, does not return a
     *             boolean or has parameters and last, when callback is not a
     *             method present in the object instance of this task.
     */
    public void addGuardCallback(String guardName, Method callback) {
        if (guardName == null || guardName.isEmpty()) {
            throw new IllegalArgumentException("guardName cannot be empty");
        }
        if (!callback.isAnnotationPresent(GuardProvider.class)) {
            throw new IllegalArgumentException("callback should be annotated with GuardProvider");
        } else if (!callback.getReturnType().getTypeName().equals("boolean")) {
            throw new IllegalArgumentException("callback should return a boolean value");
        } else if (callback.getParameterCount() != 0) {
            throw new IllegalArgumentException("callback should take no parameters");
        } else if (!isMethodPresent(callback)) {
            throw new IllegalArgumentException("callback is not a Method of the object instance present in this task");
        } else {
            guardCallback.put(guardName, callback);
        }

    }

    /**
     * This method returns a {@link Method} object to be called for obtaining
     * the value of the guard. It is intended to be used by the framework only,
     * the user should not call this method.
     *
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public boolean getGuardValue(String guardName)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Boolean result = (Boolean) guardCallback.get(guardName).invoke(getObject());

        return result.booleanValue();
    }

    private boolean isMethodPresent(Method method) {
        for (Method m : getObject().getClass().getMethods()) {
            if (method.equals(m)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns the topic to which the abstract task is subscribed.
     *
     * @return the topic to which the abstract task is subscribed
     */
    public Topic getTopic() {
        return this.topic;
    }

    /**
     * This method returns the instance of the object that defines the abstract
     * task.
     *
     * @return the instance of the object of this abstract task
     */
    public Object getObject() {
        return this.object;
    }

    /**
     * This returns the method that defines the abstract task.
     *
     * @return the method of this abstract task
     */
    public Method getMethod() {
        return this.method;
    }
}
