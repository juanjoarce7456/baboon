package org.unc.lac.baboon.main;

import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;

/**
 * BaboonFramework.
 * 
 * @author Ariel Ivan Rabinovich & Juan Jose Arce Giacobbe This class contains
 *         the main method of the framework. See
 *         {@link BaboonFramework#main(String[])}
 */

public class BaboonFramework {

    /**
     * List of BaboonApplication objects automatically instantiated by the
     * framework
     */
    static private ArrayList<BaboonApplication> appSetupObjects = new ArrayList<BaboonApplication>();

    /**
     * Main method. Creates an instance for every class implementing
     * {@link BaboonApplication} interface. Calls
     * {@link BaboonApplication#declare()} for every BaboonApplication instance.
     * After, calls {@link BaboonApplication#subscribe()} for every
     * BaboonApplication instance.
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
