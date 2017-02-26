package org.unc.lac.baboon.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Pair;
import org.unc.lac.baboon.happeninghandleraspect.HappeningObserver;
import org.unc.lac.baboon.happeninghandleraspect.HappeningObserver.State;
import org.unc.lac.baboon.happeninghandleraspect.HappeningHandlerJoinPoint;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;

/**
 * This class uses the {@link BaboonPetriCore} and the
 * {@link HappeningHandlerSubscription} objects from {@BaboonConfig} to
 * Synchronize the execution of {@link HappeningHandler} annotated methods.
 * Also, to achieve the synchronization, this class implements
 * {@HappeningObserver} to observe the aspect advices in
 * {@link HappeningHandlerJoinPoint}.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class HappeningSynchronizer implements HappeningObserver {

    private BaboonConfig baboonConfig;
    private BaboonPetriCore petriCore;

    private static Logger LOGGER = Logger.getLogger(HappeningSynchronizer.class.getName());

    public HappeningSynchronizer(BaboonConfig baboonConfig, BaboonPetriCore petriCore) {
        this.baboonConfig = baboonConfig;
        this.petriCore = petriCore;
    }

    /**
     * This method is called when a HappeningHandler method is about to be
     * executed or when a HappeningHandler method execution just finished. It
     * manages the synchronization of the {@link HappeningHandler} annotated
     * method by firing the permission transition before the method execution,
     * and by firing the transition callbacks and setting the guard callbacks
     * after the method execution.
     * 
     * @param target
     *            The instance of the object invoking the
     *            {@link HappeningHandler} annotated method.
     * @param methodName
     *            The name of the {@link HappeningHandler} annotated method.
     * @param state
     *            The {@link State} of the execution of the
     *            {@link HappeningHandler} annotated method.
     */
    @Override
    public void update(Object target, String methodName, HappeningObserver.State state) {
        try {
            Method method = MethodDictionary.getMethod(target, methodName);
            HappeningHandlerSubscription happeningHandler = (HappeningHandlerSubscription) baboonConfig
                    .getHappeningHandler(new Pair<Object, Method>(target, method));
            if (happeningHandler == null) {
                throw new RuntimeException("This Happening Handler is not subscribed");
            } else {
                switch (state) {
                case BEFORE_EXECUTION:
                    before(happeningHandler);
                    break;
                case AFTER_EXECUTION:
                    after(happeningHandler);
                    break;
                default:
                    break;
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("The method cannot be resolved", e);
        }

    }

    /**
     * This method is called after the execution of a {@link HappeningHandler}
     * annotated method. Inside this method the transition callbacks are fired
     * and the guard callbacks are setted.
     * 
     * @param happeningHandlerSubscription
     *            The {@link HappeningHandlerSubscription} object containing the
     *            {@link HappeningHandler} annotated method, the invoking
     *            object, and the topic with the permission and callbacks.
     */
    private void after(HappeningHandlerSubscription happeningHandlerSubscription) {
        for (String guardCallback : happeningHandlerSubscription.getTopic().getGuardCallback(0)) {
            try {
                boolean result = happeningHandlerSubscription.getAction().getGuardValue(guardCallback);
                petriCore.setGuard(guardCallback, result);
            } catch (NullPointerException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | IndexOutOfBoundsException | PetriNetException e) {
                LOGGER.log(Level.SEVERE, "Failed to set the guard callback " + guardCallback, e);
                throw new RuntimeException("Error while setting the guard callback " + guardCallback, e);
            }
        }
        for (String transitionCallback : happeningHandlerSubscription.getTopic().getFireCallback()) {
            try {
                petriCore.fireTransition(transitionCallback, true);
            }  catch (IllegalTransitionFiringError | PetriNetException e) {
                LOGGER.log(Level.SEVERE, "Error while firing the callback transition " + transitionCallback, e);
                throw new RuntimeException("Error while firing the permission transition ", e);
            } catch (IllegalArgumentException e) {
                if (transitionCallback == null || transitionCallback.isEmpty()) {
                    LOGGER.log(Level.WARNING, "Tried to fire a transitionCallback without permission transition on topic " + happeningHandlerSubscription.getTopic().getName());
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to fire the callback transition " + transitionCallback
                            + " because it does not exists on petri net", e);
                    throw new RuntimeException("The callback transition does not exists on petri net", e);
                }
            }
        }
    }

    /**
     * This method is called before the execution of a {@link HappeningHandler}
     * annotated method. Inside this method the permission transition is fired.
     * 
     * @param happeningHandlerSubscription
     *            The {@link HappeningHandlerSubscription} object containing the
     *            {@link HappeningHandler} annotated method, the invoking
     *            object, and the topic with the permission and callbacks.
     */
    private void before(HappeningHandlerSubscription happeningHandlerSubscription) {
        String permission = happeningHandlerSubscription.getTopic().getPermission().get(0);
        try {
            petriCore.fireTransition(permission, false);
        } catch (IllegalTransitionFiringError | PetriNetException e) {
            LOGGER.log(Level.SEVERE, "Error while firing the permission transition", e);
            throw new RuntimeException("Error while firing the permission transition", e);
        } catch (IllegalArgumentException e) {
            if (permission == null || permission.isEmpty()) {
                LOGGER.log(Level.WARNING, "Executing a HappenningHandler without permission transition");
            } else {
                LOGGER.log(Level.SEVERE, "Failed to fire the permission transition " + permission
                        + " because it does not exists on petri net", e);
                throw new RuntimeException("The permission transition does not exists on petri net", e);
            }
        }

    }

}