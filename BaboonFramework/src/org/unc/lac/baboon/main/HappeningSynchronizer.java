package org.unc.lac.baboon.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.javatuples.Pair;
import org.unc.lac.baboon.happeninghandleraspect.HappeningObserver;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;

public class HappeningSynchronizer implements HappeningObserver {
    
    private BaboonConfig baboonConfig;
    private BaboonPetriCore petriCore;
    public HappeningSynchronizer(BaboonConfig baboonConfig, BaboonPetriCore petriCore) {
        this.baboonConfig = baboonConfig;
        this.petriCore = petriCore;
    }

    @Override
    public void updateBefore(Object target, String methodName) {
        try {
            Method method = MethodDictionary.getMethod(target, methodName);
            HappeningHandlerSubscription happeningHandler = (HappeningHandlerSubscription) baboonConfig
                    .getSubscriptionsMap().get(new Pair<Object, Method>(target, method));
            if (happeningHandler == null) {
                throw new RuntimeException("This Happening Handler is not subscribed");
            } else {
                try {
                    petriCore.fireTransition(happeningHandler.getTopic().getPermission(), false);
                } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException e) {
                    throw new RuntimeException("Error while firing the permission transition", e);
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("The method cannot be resolved", e);
        }

    }

    @Override
    public void updateAfter(Object target, String methodName) {
        try {
            Method method = MethodDictionary.getMethod(target, methodName);
            HappeningHandlerSubscription happeningHandler = (HappeningHandlerSubscription) baboonConfig
                    .getSubscriptionsMap().get(new Pair<Object, Method>(target, method));
            if (happeningHandler == null) {
                throw new RuntimeException("This Happening Handler is not subscribed");
            } else {
                for (String guardCallback : happeningHandler.getTopic().getSetGuardCallback()) {
                    try {
                        Boolean result;
                        result = (Boolean) happeningHandler.getGuardCallback(guardCallback)
                                .invoke(happeningHandler.getObject());
                        petriCore.setGuard(guardCallback, result.booleanValue());
                    } catch (NullPointerException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | IndexOutOfBoundsException | PetriNetException e) {
                        throw new RuntimeException("Error while setting the callback guards", e);
                    }
                }
                for (String transitionCallback : happeningHandler.getTopic().getFireCallback()) {
                    try {
                        petriCore.fireTransition(transitionCallback, true);
                    } catch (IllegalArgumentException | IllegalTransitionFiringError | PetriNetException e) {
                        throw new RuntimeException("Error while firing the callback transitions", e);
                    }
                }
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("The method cannot be resolved", e);
        }

    }

}