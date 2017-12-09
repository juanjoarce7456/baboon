package org.unc.lac.baboon.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
@Aspect
public class HappeningControllerJoinPointReporter {

    private static JoinPointObserver observer;

    public static void setObserver(JoinPointObserver ho) {
        observer = ho;
    }

    /**
     * The pointcut happening is defined as any execution of a method annotated
     * with {@link HappeningController}
     * 
     * @param joinPoint
     *      The {@link JoinPoint} of execution reached. Is only one point of all 
     *      the joinpoints that conform the pointcut.
     */
    @Pointcut(value = "execution(@org.unc.lac.baboon.annotations.HappeningController * *(..))",
            argNames = "joinPoint")
    public void happening(final JoinPoint joinPoint) {
    }

    /**
     * Before the execution of the {@link HappeningController} annotated method,
     * this advice updates the {@link JoinPointObserver} {@link #observer}. This
     * is done to allow the framework to manage the synchronization of the
     * {@link HappeningController} annotated method.
     * 
     *  @param joinPoint
     *      The {@link JoinPoint} of execution reached. Indicates which instance of object and 
     *      {@link HappeningController} method has triggered the advice.
     */
    @Before(value = "happening(joinPoint)", argNames = "joinPoint")
    public void beforeHappening(final JoinPoint joinPoint) {
        observer.update(joinPoint.getTarget(), joinPoint.getSignature().getName(), State.BEFORE_EXECUTION);
    }

    /**
     * After the execution of the {@link HappeningController} annotated method,
     * this advice updates the {@link JoinPointObserver} {@link #observer}. This
     * is done to allow the framework to manage the synchronization of the
     * {@link HappeningController} annotated method.
     * 
     * @param joinPoint
     *      The {@link JoinPoint} of execution reached. Indicates which instance of object and 
     *      {@link HappeningController} method has triggered the advice.
     *
     */
    @After(value = "happening(joinPoint)", argNames = "joinPoint")
    public void afterHappening(final JoinPoint joinPoint) {
        observer.update(joinPoint.getTarget(), joinPoint.getSignature().getName(), State.AFTER_EXECUTION);
    }
}