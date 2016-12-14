package org.unc.lac.baboon.task;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.javatuples.Pair;
import org.unc.lac.baboon.annotations.GuardProvider;

/**
 * This class is as a wrapper that defines an AbstractTask as a pair of an
 * object instance and a method. It is used internally by framework and should
 * not be known by user.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public abstract class AbstractTask {
    Pair<Object, Method> task;
    private HashMap<String, Method> guardCallback = new HashMap<String, Method>();

    public AbstractTask(Object objInstance, Method objMethod) {
        task = new Pair<Object, Method>(objInstance, objMethod);
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
     */
    public Method getGuardCallback(String guardName) {
        return guardCallback.get(guardName);
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
     * This method returns the instance of the object that defines the abstract
     * task.
     * 
     * @return the instance of the object of this abstract task
     */
    public Object getObject() {
        return task.getValue0();
    }

    /**
     * This returns the method that defines the abstract task.
     * 
     * @return the method of this abstract task
     */
    public Method getMethod() {
        return task.getValue1();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((task == null) ? 0 : task.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractTask other = (AbstractTask) obj;
        if (task == null) {
            if (other.task != null)
                return false;
        } else if (!task.equals(other.task))
            return false;
        return true;
    }
}
