package org.unc.lac.baboon.test.utils.appsetup;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.main.BaboonApplication;

/**
 * AbstractAppSetup is used by {@link BaboonMainApplicationSetupTest} for
 * testing purposes. It logs information to a {@link Logger} when its
 * {@link AbstractAppSetup#declare()} and {@link AbstractAppSetup#subscribe()}
 * methods are run.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @see {@link BaboonApplication}
 * @see {@link BaboonFramework}
 */
public abstract class AbstractAppSetup implements BaboonApplication {
    private final static Logger LOGGER = Logger.getLogger(AbstractAppSetup.class.getName());

    public void declare() {
        LOGGER.log(Level.INFO, "Declaring Abstract from: " + this.getClass().getSimpleName());
    }

    public void subscribe() {
        LOGGER.log(Level.INFO, "Subscribing Abstract from: " + this.getClass().getSimpleName());
    }
}
