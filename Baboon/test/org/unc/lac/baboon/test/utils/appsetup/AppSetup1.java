package org.unc.lac.baboon.test.utils.appsetup;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.main.BaboonApplication;

/**
 * AppSetup1 is used by {@link BaboonMainApplicationSetupTest} for testing
 * purposes. It logs information to a {@link Logger} when its
 * {@link AppSetup1#declare()} and {@link AppSetup1#subscribe()} methods are
 * run.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @see BaboonApplication
 * @see BaboonFramework
 */
public class AppSetup1 implements BaboonApplication {
    private final static Logger LOGGER = Logger.getLogger(AppSetup1.class.getName());

    @Override
    public void declare() {
        LOGGER.log(Level.INFO, "Declaring 1");
    }

    @Override
    public void subscribe() {
        LOGGER.log(Level.INFO, "Subscribing 1");
    }

}
