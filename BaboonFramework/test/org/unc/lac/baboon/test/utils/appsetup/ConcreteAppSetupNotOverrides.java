package org.unc.lac.baboon.test.utils.appsetup;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.main.BaboonApplication;

/**
 * ConcreteAppSetupNotOverrides is used by
 * {@link BaboonMainApplicationSetupTest} for testing purposes. It logs
 * information to a {@link Logger} when its
 * {@link ConcreteAppSetupNotOverrides#declare()} and
 * {@link ConcreteAppSetupNotOverrides#subscribe()} methods are run.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @see BaboonApplication
 * @see BaboonFramework
 */
public class ConcreteAppSetupNotOverrides extends AbstractAppSetup {
    private final static Logger LOGGER = Logger.getLogger(ConcreteAppSetupNotOverrides.class.getName());

    public ConcreteAppSetupNotOverrides() {
        LOGGER.log(Level.INFO, "Instantiating Concrete");
    }
}
