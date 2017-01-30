package org.unc.lac.baboon.happeninghandleraspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.task.HappeningHandlerObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;

/**
 * In this Aspect are declared the pointcut and advices to be applied when a
 * {@link HappeningHandler} annotated method execution JoinPoints is reached.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public aspect HappeningHandlerJoinPoint {

    /**
     * The pointcut happening is defined as any execution of a method annotated
     * with {@link HappeningHandler}
     */
    pointcut happening():
           execution(@org.unc.lac.baboon.annotations.HappeningHandler * *(..));

    /**
     * Before the execution of the {@link HappeningHandler} annotated method,
     * this advice fires the permission required to execute the method. This
     * permission is obtained from the {@link Topic} to which the
     * HappeningHandler was subscribed.
     */
    before(): happening(){
        Method method;
        Object target = thisJoinPoint.getTarget();
        try {
            method = MethodDictionary.getMethod(target, thisJoinPoint.getSignature().getName());
            HappeningHandlerObject happeningHandler = new HappeningHandlerObject(target, method);
            Topic topic = BaboonFramework.getTopic(happeningHandler);
            if (topic == null) {
                throw new RuntimeException("This Happening Handler is not subscribed to a topic");
            } else {
                try {
                    BaboonFramework.fireTransition(topic.getPermission(), false);
                } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException e) {
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
        }
    }

    /**
     * After the execution of the {@link HappeningHandler} annotated method,
     * this advice fires the transition callback and sets the guard callback. This
     * callback are obtained from the {@link Topic} to which the
     * HappeningHandler was subscribed.
     */
    after() : happening(){
        Method method;
        Object target = thisJoinPoint.getTarget();
        try {
            method = MethodDictionary.getMethod(target, thisJoinPoint.getSignature().getName());
            HappeningHandlerObject happeningHandler = new HappeningHandlerObject(target, method);
            Topic topic = BaboonFramework.getTopic(happeningHandler);
            HappeningHandlerObject subscribedHH = BaboonFramework.getSubscribedHappeningHandler(happeningHandler);
            if (topic == null) {
                throw new RuntimeException("This Happening Handler is not subscribed to a topic");
            } else {
                for (String guardCallback : topic.getSetGuardCallback()) {
                    try {
                        Boolean result;
                        result = (Boolean) subscribedHH.getGuardCallback(guardCallback)
                                .invoke(subscribedHH.getObject());
                        BaboonFramework.setGuard(guardCallback, result.booleanValue());
                    } catch (NullPointerException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | IndexOutOfBoundsException | PetriNetException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (String transitionCallback : topic.getFireCallback()) {
                    try {
                        BaboonFramework.fireTransition(transitionCallback, true);
                    } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }
}