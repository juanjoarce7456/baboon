package org.unc.lac.baboon.task;

import java.lang.reflect.Method;

/**
 * This class is as a wrapper that defines a HappeningHandlerObject as a pair of an object instance
 * and a method. It is used internally by framework and should not be known
 * by user.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class HappeningHandlerObject extends AbstractTask {

    public HappeningHandlerObject(Object objInstance, Method objMethod) {
        super(objInstance, objMethod);
    }

}
