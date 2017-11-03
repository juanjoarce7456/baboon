package org.unc.lac.baboon.exceptions;

import org.unc.lac.baboon.topic.Topic;

/**
 * This exception is thrown when the given topics json file can not be found on
 * the filesystem.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see Exception
 * @see Topic
 */
public class NoTopicsJsonFileException extends Exception {

    private static final long serialVersionUID = -8169836890348633262L;

    public NoTopicsJsonFileException() {
        super("No Topics Json File");
    }

    public NoTopicsJsonFileException(String message) {
        super(message);
    }

    public NoTopicsJsonFileException(Throwable cause) {
        super(cause);
    }

    public NoTopicsJsonFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTopicsJsonFileException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
