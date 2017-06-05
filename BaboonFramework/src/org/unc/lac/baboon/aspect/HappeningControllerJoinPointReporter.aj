package org.unc.lac.baboon.aspect;

import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.aspect.JoinPointObserver.State;

/**
 * In this Aspect are declared the pointcut and advices to be applied when a
 * {@link HappeningController} annotated method execution JoinPoints is reached.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public aspect HappeningControllerJoinPointReporter {

    private static JoinPointObserver observer;

    public static void setObserver(JoinPointObserver ho) {
        observer = ho;
    }

    /**
     * The pointcut happening is defined as any execution of a method annotated
     * with {@link HappeningController}
     */
    pointcut happening():
           execution(@org.unc.lac.baboon.annotations.HappeningController * *(..));

    /**
     * Before the execution of the {@link HappeningController} annotated method,
     * this advice updates the {@link JoinPointObserver} {@link #observer}. This
     * is done to allow the framework to manage the synchronization of the
     * {@link HappeningController} annotated method.
     */
    before(): happening(){
        observer.update(thisJoinPoint.getTarget(), thisJoinPoint.getSignature().getName(), State.BEFORE_EXECUTION);
    }

    /**
     * After the execution of the {@link HappeningController} annotated method,
     * this advice updates the {@link JoinPointObserver} {@link #observer}. This
     * is done to allow the framework to manage the synchronization of the
     * {@link HappeningController} annotated method.
     */
    after() : happening(){
        observer.update(thisJoinPoint.getTarget(), thisJoinPoint.getSignature().getName(), State.AFTER_EXECUTION);

    }
}