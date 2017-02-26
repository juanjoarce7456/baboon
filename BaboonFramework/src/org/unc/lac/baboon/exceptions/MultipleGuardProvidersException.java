package org.unc.lac.baboon.exceptions;

import org.unc.lac.baboon.annotations.GuardProvider;
public class MultipleGuardProvidersException extends Exception {

    /**
     * This exception is thrown when more than one method of the same class are
     * declared as GuardProviders for the same guard name
     * 
     * @author Ariel Ivan Rabinovich
     * @author Juan Jose Arce Giacobbe
     * @version 1.0
     * @see Exception
     * @see GuardProvider
     */
    private static final long serialVersionUID = -5450191540499496764L;

    public MultipleGuardProvidersException() {
        super();
    }

    public MultipleGuardProvidersException(String message) {
        super(message);
    }

    public MultipleGuardProvidersException(Throwable cause) {
        super(cause);
    }

    public MultipleGuardProvidersException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleGuardProvidersException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
