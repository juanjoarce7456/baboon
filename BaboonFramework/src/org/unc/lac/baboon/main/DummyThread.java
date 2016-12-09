package org.unc.lac.baboon.main;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import org.unc.lac.baboon.task.TaskObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.NotInitializedPetriNetException;

/**
 * A {@link Callable} object that executes a {@link TaskObject}. This is the
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
    /**
     * The task to be executed.
     */
    TaskObject task;
    /**
     * The topic defining the permission and the callback.
     */
    Topic topic;
    /**
     * The Petri core used to synchronize the execution of the task.
     */
    BaboonPetriCore petriCore;

    public DummyThread(TaskObject task, Topic topic, BaboonPetriCore petriCore) {
        if(task==null){
            throw new IllegalArgumentException("Task can not be null");
        }
        if(topic==null){
            throw new IllegalArgumentException("Topic can not be null");
        }
        if(petriCore==null){
            throw new IllegalArgumentException("Petri Core can not be null");
        }
        this.topic = topic;
        this.task = task;
        this.petriCore = petriCore;
    }

    /**
     * Executes a {@link TaskObject}. Asks the Petri monitor for permission to
     * execute a task, executes the task and finally fires the transition
     * callback and set the guard callback. After setting the callback,
     * starts the task execution process over again.
     * 
     * @see Topic
     * @see TaskObject
     * 
     */
    @Override
    public Void call() {
        while (true) {
            try {
                petriCore.fireTransition(topic.getPermission(), false);
                task.executeMethod();
                for (String transitionCallback : topic.getFireCallback()) {
                    petriCore.fireTransition(transitionCallback, true);
                }
            } catch (IllegalArgumentException | IllegalTransitionFiringError | NotInitializedPetriNetException
                    | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
