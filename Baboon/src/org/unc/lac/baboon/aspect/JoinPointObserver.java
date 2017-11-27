package org.unc.lac.baboon.aspect;

import org.unc.lac.baboon.annotations.HappeningController;

/**
 * Observer Interface. The classes implementing this interface can be subscribed
 * to {@link HappeningControllerJoinPointReporter}. This way, the observer gets an update
 * if a {@link HappeningController} annotated method is about to be executed or if
 * its execution just finished.
 * 
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public interface JoinPointObserver {
    /**
     * This enum represent the state of the execution of the
     * {@link HappeningController} annotated method. {@link State#BEFORE_EXECUTION}
     * indicates the HappeningController method is about to be executed.
     * {@link State#AFTER_EXECUTION} indicates the HappeningController method
     * execution just finished.
     */
    enum State {
        AFTER_EXECUTION,
        BEFORE_EXECUTION
    }

    /**
     * This method is called when a HappeningController method is about to be
     * executed or when a HappeningController method execution just finished.
     * 
     * @param target
     *            The instance of the object invoking the
     *            {@link HappeningController} annotated method.
     * @param methodName
     *            The name of the {@link HappeningController} annotated method.
     * @param state
     *            The {@link State} of the execution of the
     *            {@link HappeningController} annotated method.
     */
    void update(Object target, String methodName, JoinPointObserver.State state);
}
