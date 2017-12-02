package org.unc.lac.baboon.exceptions;

import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;

public class BadPolicyException extends PetriNetException {
    
    private static final long serialVersionUID = -1010804350089329766L;
    
    public BadPolicyException() {
        super("Bad Policy");
    }

    public BadPolicyException(String message) {
        super(message);
    }

    public BadPolicyException(Throwable cause) {
        super(cause);
    }

    public BadPolicyException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPolicyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
