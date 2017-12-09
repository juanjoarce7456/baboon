package org.unc.lac.baboon.actioncontroller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.google.common.base.Strings;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;

/**
 * An ActionController is an abstract class defined by
 * <ul>
 * <li>An object instance.</li>
 * <li>A {@link TaskController} or {@link HappeningController} annotated method, member of
 * the class of the object instance.</li>
 * <li>A set of {@link GuardProvider} annotated methods, member of the class of
 * the object instance, that are organized in a Map indexed by the guard name
 * corresponding to the {@link GuardProvider#value()}.</li>
 * </ul>
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public abstract class ActionController {

    /**
     * The object instance of this actionController
     */
    protected Object actionObject;
    /**
     * The {@link TaskController} or {@link HappeningController} annotated method of this
     * actionController, must be a member of {#actionObject} class
     */
    protected Method actionMethod;

    /**
     * The Map of {@link GuardProvider} annotated methods of this
     * actionController. They must be members of {#actionObject} class. The map is indexed
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
     *            The object instance of the actionController
     * @param actionMethod
     *            A {@link TaskController} or {@link HappeningController} annotated method
     *            for this actionController, must be a member of {#actionObject} class
     * 
     * @throws MultipleGuardProvidersException
     *             <ul>
     *             <li>When more than one {@link GuardProvider} annotated
     *             methods are referred to the same guard name</li>
     *             </ul>
     * @throws InvalidGuardProviderMethod
     *             <ul>
     *             <li>When a {@link GuardProvider} annotated method has a
     *             return type other than boolean</li>
     *             <li>When a {@link GuardProvider} annotated method requires
     *             arguments</li>
     *             </ul>
     * @throws IllegalArgumentException
     *             <ul>
     *             <li>When the actionObject provided is null</li>
     *             <li>When the actionMethod provided is null</li>
     *             </ul>
     * 
     */
    public ActionController(Object actionObject, Method actionMethod)
            throws MultipleGuardProvidersException, InvalidGuardProviderMethod, IllegalArgumentException {
        if (actionObject == null) {
            throw new IllegalArgumentException("The object cannot be null");
        }
        if (actionMethod == null) {
            throw new IllegalArgumentException("The method cannot be null");
        }
        this.actionMethod = actionMethod;
        this.actionObject = actionObject;
        this.guardProviderMethodsMap = new HashMap<>();
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
     *         Error when invoking a {@link GuardProvider} method.
     * @throws IllegalAccessException
     *         Error when accessing a {@link GuardProvider} method.
     */
    public boolean getGuardValue(String guardName)
            throws IllegalAccessException, InvocationTargetException {
        return (Boolean) guardProviderMethodsMap.get(guardName).invoke(actionObject);
    }

    /**
     * This method returns a boolean value indicating the presence of a
     * {@link GuardProvider} annotated method to handle the guardName provided
     * as argument
     * 
     * @param guardName
     *            the guard name associated to the {@link GuardProvider}
     *            annotated method, whose presence is to be tested.
     * @return true if there is a {@link GuardProvider} annotated method
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
     *             <ul>
     *             <li>When more than one {@link GuardProvider} annotated
     *             methods are referred to the same guard name</li>
     *             </ul>
     * @throws InvalidGuardProviderMethod
     *             <ul>
     *             <li>When a {@link GuardProvider} annotated method has a
     *             return type other than boolean</li>
     *             <li>When a {@link GuardProvider} annotated method requires
     *             arguments</li>
     *             <li>When {@link #actionMethod} is static and {@link GuardProvider} 
     *             annotated method is not static</li>
     *             </ul>
     */
    private void resolveGuardProviderMethods() throws MultipleGuardProvidersException, InvalidGuardProviderMethod {
        boolean isStaticController = Modifier.isStatic(actionMethod.getModifiers());
        for (Method method : actionObject.getClass().getMethods()) {
            GuardProvider provider = method.getAnnotation(GuardProvider.class);
            if (provider != null) {
                if (method.getReturnType() != boolean.class) {
                    throw new InvalidGuardProviderMethod("The method " + method.getName()
                            + " annotated as GuardProvider has a return type other than boolean");
                } else if (method.getParameterCount() != 0) {
                    throw new InvalidGuardProviderMethod("The method " + method.getName()
                            + " annotated as GuardProvider cannot require parameters on its declaration");
                } else if(isStaticController && !Modifier.isStatic(method.getModifiers())){
                    throw new InvalidGuardProviderMethod("The method " + method.getName()
                    + " annotated as GuardProvider must be static since it is a static controller");
                }
                if (!Strings.isNullOrEmpty(provider.value())) {

                    if (guardProviderMethodsMap.putIfAbsent(provider.value(), method) != null) {
                        throw new MultipleGuardProvidersException(
                                "There is another method declared as a GuardProvider for guard: " + provider.value());
                    }
                }
            }
        }
    }
    
    /**
     * Returns the name of the {@link #actionMethod}
     * 
     * @return the name of the {@link #actionMethod}
     * 
     */
    public String getMethodName() {
        return actionMethod.getName();
    }

}
