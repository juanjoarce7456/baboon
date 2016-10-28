package org.unc.lac.baboon.main;

/**
 * Setup Interface. The classes implementing this interface will be instantiated
 * automatically. The declare and subscribe method will be called automatically
 * by the framework in that order.
 */
public interface BaboonApplication {
    /**
     * This method gets called automatically on application initialization. This
     * method should be used for creating and instatiating objects and
     * controllers.
     */
    public void declare();

    /**
     * This method gets called automatically on application initialization. This
     * method should be used for subscribing tasks and happenings to specific
     * events.
     */
    public void subscribe();
}
