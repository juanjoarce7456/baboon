package org.unc.lac.baboon.happeninghandleraspect;

import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.happeninghandleraspect.HappeningObserver.State;

/**
 * In this Aspect are declared the pointcut and advices to be applied when a
 * {@link HappeningHandler} annotated method execution JoinPoints is reached.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public aspect HappeningHandlerJoinPoint {

    private static HappeningObserver observer;

    public static void setObserver(HappeningObserver ho) {
        observer = ho;
    }

    /**
     * The pointcut happening is defined as any execution of a method annotated
     * with {@link HappeningHandler}
     */
    pointcut happening():
           execution(@org.unc.lac.baboon.annotations.HappeningHandler * *(..));

    /**
     * Before the execution of the {@link HappeningHandler} annotated method,
     * this advice updates the {@link HappeningObserver} {@link #observer}. This
     * is done to allow the framework to manage the synchronization of the
     * {@link HappeningHandler} annotated method.
     */
    before(): happening(){
        observer.update(thisJoinPoint.getTarget(), thisJoinPoint.getSignature().getName(), State.BEFORE_EXECUTION);
    }

    /**
     * After the execution of the {@link HappeningHandler} annotated method,
     * this advice updates the {@link HappeningObserver} {@link #observer}. This
     * is done to allow the framework to manage the synchronization of the
     * {@link HappeningHandler} annotated method.
     */
    after() : happening(){
        observer.update(thisJoinPoint.getTarget(), thisJoinPoint.getSignature().getName(), State.AFTER_EXECUTION);

    }
}