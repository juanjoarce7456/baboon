package org.unc.lac.baboon.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
 * {@HappeningObserver} to observe the aspect
 * advices in {@link HappeningHandlerJoinPoint}.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class HappeningSynchronizer implements HappeningObserver {

    private BaboonConfig baboonConfig;
    private BaboonPetriCore petriCore;

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
                    .getSubscriptionsMap().get(new Pair<Object, Method>(target, method));
            if (happeningHandler == null) {
                throw new RuntimeException("This Happening Handler is not subscribed");
            } else {
                switch(state){
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
     * @param happeningHandlerSub
     *            The {@link HappeningHandlerSubscription} object containing the
     *            {@link HappeningHandler} annotated method, the invoking
     *            object, and the topic with the permission and callbacks.
     */
    private void after(HappeningHandlerSubscription happeningHandlerSub) {
        for (String guardCallback : happeningHandlerSub.getTopic().getSetGuardCallback()) {
            try {
                boolean result = happeningHandlerSub.getGuardValue(guardCallback);
                petriCore.setGuard(guardCallback, result);
            } catch (NullPointerException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | IndexOutOfBoundsException | PetriNetException e) {
                throw new RuntimeException("Error while setting the callback guards", e);
            }
        }
        for (String transitionCallback : happeningHandlerSub.getTopic().getFireCallback()) {
            try {
                petriCore.fireTransition(transitionCallback, true);
            } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException e) {
                throw new RuntimeException("Error while firing the callback transitions", e);
            }
        }
    }

    /**
     * This method is called before the execution of a {@link HappeningHandler}
     * annotated method. Inside this method the permission transition is fired.
     * 
     * @param happeningHandlerSub
     *            The {@link HappeningHandlerSubscription} object containing the
     *            {@link HappeningHandler} annotated method, the invoking
     *            object, and the topic with the permission and callbacks.
     */
    private void before(HappeningHandlerSubscription happeningHandlerSub) {
        try {
            petriCore.fireTransition(happeningHandlerSub.getTopic().getPermission(), false);
        } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException e) {
            throw new RuntimeException("Error while firing the permission transition", e);
        }
    }

}