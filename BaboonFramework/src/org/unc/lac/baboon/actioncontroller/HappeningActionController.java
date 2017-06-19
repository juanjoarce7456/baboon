package org.unc.lac.baboon.actioncontroller;

import java.lang.reflect.Method;

import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;

/**
 * A HappeningControllerAction is a class defined by
 * <li>An object instance.</li>
 * <li>A {@link HappeningController} annotated method, member of
 * the class of the object instance.</li>
 * <li>A set of {@link GuardProvider} annotated methods, member of the class of
 * the object instance, that are organized in a Map indexed by the guard name
 * corresponding to the {@link GuardProvider#value()}.</li>
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class HappeningActionController extends ActionController {

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
     *             <li>When more than one {@link GuardProvider} annotated
     *             methods are referred to the same guard name</li>
     * @throws InvalidGuardProviderMethod
     *             <li>When a {@link GuardProvider} annotated method has a
     *             return type other than boolean</li>
     *             <li>When a {@link GuardProvider} annotated method requires
     *             arguments</li>
     *             <li>When {@link #actionMethod} is static and {@link GuardProvider} 
     *             annotated method is not static</li>
     * @throws IllegalArgumentException
     *             <li>When the actionObject provided is null</li>
     *             <li>When the actionMethod provided is null</li>
     *             <li>When the actionMethod provided is not annotated with {@link HappeningController}</li>
     * 
     */
    public HappeningActionController(Object actionObject, Method actionMethod)
            throws MultipleGuardProvidersException, InvalidGuardProviderMethod, IllegalArgumentException {
        super(actionObject, actionMethod);
        if(!actionMethod.isAnnotationPresent(HappeningController.class)){
            throw new IllegalArgumentException("Method must be annotated with HappeningController");
        }

    }

}
