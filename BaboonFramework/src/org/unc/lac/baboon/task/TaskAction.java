package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;

/**
 * A TaskAction is an abstract class that inherits from {@link Action}, defined by
 * <li>An object instance.</li>
 * <li>A {@link Task} or {@link HappeningHandler} annotated method, member of
 * the class of the object instance.</li>
 * <li>An array of parameters to be used as {@link Action#actionMethod}
 * arguments.</li>
 * <li>A set of {@link GuardProvider} annotated methods, member of the class of
 * the object instance, that are organized in a Map indexed by the guard name
 * corresponding to the {@link GuardProvider#value()}.</li>
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class TaskAction extends Action {
    /**
     * The array of parameters to be used as {@link Action#actionMethod}
     * arguments.
     */
    private Object[] parameters;

    /**
     * Constructor. Sets the {@link #actionObject}, the {@link #actionMethod},
     * the {@link #parameters} and finally resolves the {@link GuardProvider}
     * annotated methods of the {@link #actionObject} and saves them into
     * {@link #guardProviderMethodsMap}
     * 
     * @param actionObject
     *            The object instance of the action
     * @param actionMethod
     *            A {@link Task} or {@link HappeningHandler} annotated method
     *            for this action, must be a member of {#actionObject} class
     * @param parameters
     *            Varargs used as list of arguments for {@link #actionMethod}
     * 
     * @throws MultipleGuardProvidersException
     *             <li>When more than one {@link GuardProvider} annotated
     *             methods are referred to the same guard name</li>
     * @throws InvalidGuardProviderMethod
     *             <li>When a {@link GuardProvider} annotated method has a
     *             return type other than boolean</li>
     *             <li>When a {@link GuardProvider} annotated method requires
     *             arguments</li>
     * @throws IllegalArgumentException
     *             <li>When the actionObject provided is null</li>
     *             <li>When the actionMethod provided is null</li>-
     *             <li>When the actionMethod provided is not annotated with
     *             {@link Task}</li>
     * 
     */
    public TaskAction(Object actionObject, Method actionMethod, Object... parameters)
            throws MultipleGuardProvidersException, InvalidGuardProviderMethod, IllegalArgumentException {
        super(actionObject, actionMethod);
        if (!actionMethod.isAnnotationPresent(Task.class)) {
            throw new IllegalArgumentException("Method must be annotated with Task");
        }
        this.parameters = parameters;
    }

    /**
     * Calls the invoke method on {@link #actionMethod} using
     * {@link #actionObject} and {@link #parameters} as the arguments.
     * 
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * 
     * @see Method#invoke(Object, Object...)
     * 
     */
    public void executeMethod() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        actionMethod.invoke(actionObject, parameters);
    }

    /**
     * Returns the name of the {@link #actionMethod} of this {@link TaskAction}
     * 
     * @return the name of the {@link #actionMethod} of this {@link TaskAction}
     * 
     */
    public String getMethodName() {
        return actionMethod.getName();
    }

}
