package org.unc.lac.baboon.main;

/**
 * Setup Interface. The classes implementing this interface will be instantiated
 * automatically. The declare and subscribe method will be called automatically
 * by the framework in that order.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public interface BaboonApplication {
    /**
     * This method gets called automatically on application initialization. This
     * method should be used for creating and instatiating objects and
     * controllers.
     */
    void declare();

    /**
     * This method gets called automatically on application initialization. This
     * method should be used for subscribing taskControllers and happeningControllers to specific
     * events.
     */
    void subscribe();
}
