package org.unc.lac.baboon.exceptions;

public class BadTopicsJsonFormat extends Exception {

    private static final long serialVersionUID = 1L;

    public BadTopicsJsonFormat(String errorMessage) {
        super(errorMessage);
    }
}
