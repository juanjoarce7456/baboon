package org.unc.lac.baboon.exceptions;

import org.unc.lac.baboon.topic.Topic;

/**
 * This exception is thrown when the given topics json file format is invalid.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see Exception
 * @see Topic
 */
public class BadTopicsJsonFormat extends Exception {

    private static final long serialVersionUID = 2730170828613456371L;

    public BadTopicsJsonFormat() {
        super("Bad Topics Json Format");
    }

    public BadTopicsJsonFormat(String message) {
        super(message);
    }
    public BadTopicsJsonFormat(Throwable cause) {
        super(cause);
    }

    public BadTopicsJsonFormat(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BadTopicsJsonFormat(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
