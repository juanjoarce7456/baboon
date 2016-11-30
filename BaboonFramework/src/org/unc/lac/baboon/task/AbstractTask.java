package org.unc.lac.baboon.task;

import java.lang.reflect.Method;

import org.javatuples.Pair;

/**
 * This class is as a wrapper that defines an AbstractTask as a pair of an object instance and a
 * method. It is used internally by framework and should not be known
 * by user.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public abstract class AbstractTask {
    Pair<Object, Method> task;

    public AbstractTask(Object objInstance, Method objMethod) {
        task = new Pair<Object, Method>(objInstance, objMethod);
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
