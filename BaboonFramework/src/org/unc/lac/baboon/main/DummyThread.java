package org.unc.lac.baboon.main;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.task.TaskSubscription;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;
/**
 * A {@link Callable} object that executes a {@link TaskSubscription}. This is the
 * wrapper of a thread that asks the Petri monitor for permission to execute a
 * task, executes the task and finally fires the transition callback and set
 * the guard callback. After setting the callback, the thread starts the task
 * execution process over again.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 */
public class DummyThread implements Callable<Void> {
    private final static Logger LOGGER = Logger.getLogger(DummyThread.class.getName());
    /**
     * The task to be executed.
     */
    TaskSubscription task;
    /**
     * The Petri core used to synchronize the execution of the task.
     */
    BaboonPetriCore petriCore;

    public DummyThread(TaskSubscription task, BaboonPetriCore petriCore) {
        if(task==null){
            throw new IllegalArgumentException("Task can not be null");
        }
        if(petriCore==null){
            throw new IllegalArgumentException("Petri Core can not be null");
        }
        this.task = task;
        this.petriCore = petriCore;
    }

    /**
     * Executes a {@link TaskSubscription}. Asks the Petri monitor for permission to
     * execute a task, executes the task and finally fires the transition
     * callback and set the guard callback. After setting the callback,
     * starts the task execution process over again.
     * 
     * @see Topic
     * @see TaskSubscription
     * 
     */
    @Override
    public Void call() {
        while (true) {
            try {
                petriCore.fireTransition(task.getTopic().getPermission(), false);
                task.executeMethod();
                for (String guardCallback : task.getTopic().getSetGuardCallback()) {
                    try{
                        boolean result =  task.getGuardValue(guardCallback);
                        petriCore.setGuard(guardCallback,result);
                    }
                    catch(NullPointerException e){
                        LOGGER.log(Level.SEVERE, "Cannot set a guard on callback", e);
                    }
                }
                for (String transitionCallback : task.getTopic().getFireCallback()) {
                    petriCore.fireTransition(transitionCallback, true);
                }
            } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException
                    | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
