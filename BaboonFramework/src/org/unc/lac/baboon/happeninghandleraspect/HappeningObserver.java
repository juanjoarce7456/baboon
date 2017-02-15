package org.unc.lac.baboon.happeninghandleraspect;

import org.unc.lac.baboon.annotations.HappeningHandler;

/**
 * Observer Interface. The classes implementing this interface can be subscribed
 * to {@link HappeningHandlerJoinPoint}. This way, the observer gets an update
 * if a {@link HappeningHandler} annotated method is about to be executed or if
 * its execution just finished.
 * 
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public interface HappeningObserver {
    /**
     * This enum represent the state of the execution of the
     * {@link HappeningHandler} annotated method. {@link State#BEFORE_EXECUTION}
     * indicates the HappeningHandler method is about to be executed.
     * {@link State#AFTER_EXECUTION} indicates the HappeningHandler method
     * execution just finished.
     */
    public enum State {
        AFTER_EXECUTION, BEFORE_EXECUTION
    };

    /**
     * This method is called when a HappeningHandler method is about to be
     * executed or when a HappeningHandler method execution just finished.
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
    public void update(Object target, String methodName, HappeningObserver.State state);
}
