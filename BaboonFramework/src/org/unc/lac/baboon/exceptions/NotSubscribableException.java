package org.unc.lac.baboon.exceptions;

/**
 * This exception is thrown when the system can not subscribe an object and
 * method to a topic. The causes can be: the method name provided is null, the
 * object provided is null, the topic name provided is null, the topic or method
 * does not exists on the system or there was a security exception while trying
 * to resolve the method from its name.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see Exception
 */
public class NotSubscribableException extends Exception {

    private static final long serialVersionUID = -6680335799064723743L;

    public NotSubscribableException() {
        super("The object instance and method provided is not a Task or HappeningHandler");
    }

    public NotSubscribableException(String message) {
        super(message);
    }

    public NotSubscribableException(Throwable cause) {
        super(cause);
    }

    public NotSubscribableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSubscribableException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
