package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;

/**
 * An Action is an abstract class defined by
 * <li>An object instance.</li>
 * <li>A {@link Task} or {@link HappeningHandler} annotated method, member of
 * the class of the object instance.</li>
 * <li>A set of {@link GuardProvider} annotated methods, member of the class of
 * the object instance, that are organized in a Map indexed by the guard name
 * corresponding to the {@link GuardProvider#value()}.</li>
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public abstract class Action {

    /**
     * The object instance of this action
     */
    protected Object actionObject;
    /**
     * The {@link Task} or {@link HappeningHandler} annotated method of this
     * action, must be a member of {#actionObject} class
     */
    protected Method actionMethod;

    /**
     * The {@link Map} of {@link GuardProvider} annotated methods of this
     * action. They must be members of {#actionObject} class. The map is indexed
     * by guard names, taken from {@link GuardProvider#value()}.
     */
    protected HashMap<String, Method> guardProviderMethodsMap;

    /**
     * Constructor. Sets the {@link #actionObject}, the {@link #actionMethod}
     * and finally resolves the {@link GuardProvider} annotated methods of the
     * {@link #actionObject} and saves them into
     * {@link #guardProviderMethodsMap}
     * 
     * @param actionObject
     *            The object instance of the action
     * @param actionMethod
     *            A {@link Task} or {@link HappeningHandler} annotated method
     *            for this action, must be a member of {#actionObject} class
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
     *             <li>When the actionMethod provided is null</li>
     * 
     */
    public Action(Object actionObject, Method actionMethod)
            throws MultipleGuardProvidersException, InvalidGuardProviderMethod, IllegalArgumentException {
        if (actionObject == null) {
            throw new IllegalArgumentException("The object cannot be null");
        }
        if (actionMethod == null) {
            throw new IllegalArgumentException("The method cannot be null");
        }
        this.actionMethod = actionMethod;
        this.actionObject = actionObject;
        this.guardProviderMethodsMap = new HashMap<String, Method>();
        resolveGuardProviderMethods();
    }

    public Object getActionObject() {
        return actionObject;
    }

    /**
     * This method returns the value of the guard by executing the
     * {@link GuardProvider} annotated method associated with the guard. It is
     * intended to be used by the framework only, the user should not call this
     * method.
     * 
     * @param guardName
     *            The name of the guard whose value is to be known
     * @return The value of the guard
     *
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public boolean getGuardValue(String guardName)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Boolean result = (Boolean) guardProviderMethodsMap.get(guardName).invoke(actionObject);
        return result.booleanValue();
    }

    /**
     * This method returns a boolean value indicating the presence of a
     * {@link GuardProvider} annotated method to handle the guardName provided
     * as argument
     * 
     * @param guardName
     *            the guard name associated to the {@link GuardProvider}
     *            annotated method, whose presence is to be tested.
     * @returns true if there is a {@link GuardProvider} annotated method
     *          associated to the guardName
     */
    public boolean hasGuardProvider(String guardName) {
        return guardProviderMethodsMap.containsKey(guardName) && guardProviderMethodsMap.get(guardName) != null;
    }

    /**
     * This method iterates through {@link #actionObject} methods, obtaining its
     * {@link GuardProvider} annotated methods and saving them into {@link
     * #guardProviderMethodsMap}, indexing the map by {@link
     * GuardProvider#value()}
     * 
     * @throws MultipleGuardProvidersException
     *             <li>When more than one {@link GuardProvider} annotated
     *             methods are referred to the same guard name</li>
     * @throws InvalidGuardProviderMethod
     *             <li>When a {@link GuardProvider} annotated method has a
     *             return type other than boolean</li>
     *             <li>When a {@link GuardProvider} annotated method requires
     *             arguments</li>
     */
    private void resolveGuardProviderMethods() throws MultipleGuardProvidersException, InvalidGuardProviderMethod {
        for (Method method : actionObject.getClass().getMethods()) {
            GuardProvider provider = method.getAnnotation(GuardProvider.class);
            if (provider != null) {
                if (method.getReturnType() != boolean.class) {
                    throw new InvalidGuardProviderMethod("The method " + method.getName()
                            + "annotated as GuardProvider has a return type other than boolean");
                } else if (method.getParameterCount() != 0) {
                    throw new InvalidGuardProviderMethod("The method " + method.getName()
                            + "annotated as GuardProvider cannot require parameters on its declaration");
                }
                if (provider.value() != null && !provider.value().isEmpty()) {
                    if (guardProviderMethodsMap.putIfAbsent(provider.value(), method) != null) {
                        throw new MultipleGuardProvidersException(
                                "There is another method declared as a GuardProvider for guard: " + provider.value());
                    }
                }
            }
        }
    }

}
