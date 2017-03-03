package org.unc.lac.baboon.exceptions;

import org.unc.lac.baboon.annotations.GuardProvider;

/**
 * This exception is thrown when a method annotated with
 * {@link GuardProvider} has parameters on its declaration or if its return
 * type is not boolean
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see Exception
 * @see GuardProvider
 */
public class InvalidGuardProviderMethod extends Exception {
    private static final long serialVersionUID = 2312173516747133994L;

    public InvalidGuardProviderMethod() {
        super("The method annotated as GuardProvider is invalid.");
    }

    public InvalidGuardProviderMethod(String message) {
        super(message);
    }

    public InvalidGuardProviderMethod(Throwable cause) {
        super(cause);
    }

    public InvalidGuardProviderMethod(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGuardProviderMethod(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
