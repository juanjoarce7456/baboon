package org.unc.lac.baboon.task;

import java.lang.reflect.Method;

/**
 * This class defines a HappeningHandlerObject as a pair of an object instance
 * and a method
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
