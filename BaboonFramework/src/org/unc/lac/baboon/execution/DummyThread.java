package org.unc.lac.baboon.execution;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.action_controller.TaskActionController;
import org.unc.lac.baboon.petri.BaboonPetriCore;
import org.unc.lac.baboon.subscription.AbstractTaskControllerSubscription;
import org.unc.lac.baboon.subscription.ComplexSecuentialTaskControllerSubscription;
import org.unc.lac.baboon.subscription.SimpleTaskControllerSubscription;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;

/**
 * A {@link Callable} object that executes a {@link AbstractTaskControllerSubscription}.
 * This is the wrapper of a thread that asks the Petri monitor for permission to
 * execute a simple taskController, executes it and after sets the guard callback
 * associated to this taskController.
 * 
 * It repeats this process for all the simple taskControllers in the
 * {@link AbstractTaskControllerSubscription} and finally, after the execution of the last
 * taskController, fires the transition callback. After setting the callback, the thread
 * starts the taskController execution process over again.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 */
public class DummyThread implements Callable<Void> {
    private final static Logger LOGGER = Logger.getLogger(DummyThread.class.getName());
    /**
     * The taskController to be executed.
     */
    AbstractTaskControllerSubscription taskSubscription;
    /**
     * The Petri core used to synchronize the execution of the taskController.
     */
    BaboonPetriCore petriCore;

    public DummyThread(AbstractTaskControllerSubscription taskSubscription, BaboonPetriCore petriCore) {
        if (taskSubscription == null) {
            throw new IllegalArgumentException("Task can not be null");
        }
        if (petriCore == null) {
            throw new IllegalArgumentException("Petri Core can not be null");
        }
        this.taskSubscription = taskSubscription;
        this.petriCore = petriCore;
    }

    /**
     * This method asks the Petri monitor for permission to execute a simple
     * taskController, executes it and after sets the guard callback associated to this
     * taskController.
     * 
     * Repeats this process for all the simple taskControllers in the
     * {@link AbstractTaskControllerSubscription} and finally, after the execution of the
     * last taskController, fires the transition callback. After setting the callback, the
     * thread starts the taskController execution process over again.
     * 
     * @see Topic
     * @see AbstractTaskControllerSubscription
     * @see ComplexSecuentialTaskControllerSubscription
     * @see SimpleTaskControllerSubscription
     * 
     */
    @Override
    public Void call() {
        int secuenceStatus = 0;
        int maxStatus = taskSubscription.getSize();
        while (true) {
            TaskActionController taskController = taskSubscription.getAction(secuenceStatus);
            String permission = taskSubscription.getTopic().getPermission().get(secuenceStatus);
            try {
                petriCore.fireTransition(permission, false);
            } catch (IllegalTransitionFiringError | PetriNetException e) {
                LOGGER.log(Level.SEVERE, "Error while firing the callback transition " + permission, e);
                throw new RuntimeException("Error while firing the permission transition ", e);
            } catch (IllegalArgumentException e) {
                if (permission == null || permission.isEmpty()) {
                    LOGGER.log(Level.SEVERE,
                            "Tried to fire a permission transition without permission transition on topic "
                                    + taskSubscription.getTopic().getName());
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to fire the permission transition " + permission
                            + " because it does not exists on petri net", e);
                }
                throw new RuntimeException("The permission transition does not exists on petri net", e);
            }

            try {
                taskController.executeMethod();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                LOGGER.log(Level.SEVERE, "Error when trying to execute the method " + taskController.getMethodName(), e1);
                throw new RuntimeException("Error when trying to execute the method " + taskController.getMethodName(), e1);
            }
            for (String guardCallback : taskSubscription.getTopic().getGuardCallback(secuenceStatus)) {
                boolean result;
                try {
                    result = taskController.getGuardValue(guardCallback);
                    petriCore.setGuard(guardCallback, result);
                } catch (NullPointerException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | IndexOutOfBoundsException | PetriNetException e) {
                    LOGGER.log(Level.SEVERE, "Failed to set the guard callback " + guardCallback, e);
                    throw new RuntimeException("Error while setting the guard callback " + guardCallback, e);
                }
            }
            secuenceStatus = (secuenceStatus + 1) % maxStatus;
            if (secuenceStatus == 0) {
                for (String transitionCallback : taskSubscription.getTopic().getFireCallback()) {
                    try {
                        petriCore.fireTransition(transitionCallback, true);
                    } catch (IllegalTransitionFiringError | PetriNetException e) {
                        LOGGER.log(Level.SEVERE, "Error while firing the callback transition " + transitionCallback, e);
                        throw new RuntimeException("Error while firing the callback transition " + transitionCallback,
                                e);
                    } catch (IllegalArgumentException e) {
                        if (transitionCallback == null || transitionCallback.isEmpty()) {
                            LOGGER.log(Level.WARNING,
                                    "Tried to fire a transitionCallback with empty callback transition on topic "
                                            + taskSubscription.getTopic().getName());
                        } else {
                            LOGGER.log(Level.SEVERE, "Failed to fire the callback transition " + transitionCallback
                                    + " because it does not exists on petri net", e);
                            throw new RuntimeException("The callback transition does not exists on petri net", e);
                        }
                    }
                }
            }
        }
    }

}
