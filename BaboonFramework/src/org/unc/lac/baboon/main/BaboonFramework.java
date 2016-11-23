package org.unc.lac.baboon.main;

import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;

/**
 * BaboonFramework is the main class of the framework. This class should be
 * selected as Main class on Run Configurations in order to use Baboon.
 * <p>
 * At least one class implementing {@link BaboonApplication} must be written for
 * BaboonFramework to initialize the system.
 * </p>
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see BaboonApplication
 * @see BaboonFramework#main(String[])
 */

public class BaboonFramework {
    /**
     * List of BaboonApplication objects automatically instantiated by the
     * framework
     */
    private static ArrayList<BaboonApplication> appSetupObjects = new ArrayList<BaboonApplication>();

    /**
     * Main method.
     * <p>
     * Creates an instance for every class implementing
     * {@link BaboonApplication} interface.
     * <p>
     * Calls {@link BaboonApplication#declare()} for every
     * {@link BaboonApplication} instance. After, calls
     * {@link BaboonApplication#subscribe()} for every {@link BaboonApplication}
     * instance.
     */
    public static void main(String[] args) {
        Reflections reflections = new Reflections("");
        Set<Class<? extends BaboonApplication>> apps = reflections.getSubTypesOf(BaboonApplication.class);
        for (Class<? extends BaboonApplication> app : apps) {
            try {
                appSetupObjects.add(app.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
            }
        }
        for (BaboonApplication appSetup : appSetupObjects) {
            appSetup.declare();
        }
        for (BaboonApplication appSetup : appSetupObjects) {
            appSetup.subscribe();
        }
    }
}
