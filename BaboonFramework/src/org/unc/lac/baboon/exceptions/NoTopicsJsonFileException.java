package org.unc.lac.baboon.exceptions;

public class NoTopicsJsonFileException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoTopicsJsonFileException(String errorMessage) {
        super(errorMessage);
    }
}
