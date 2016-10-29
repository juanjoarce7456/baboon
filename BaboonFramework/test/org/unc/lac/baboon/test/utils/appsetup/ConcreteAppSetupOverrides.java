package org.unc.lac.baboon.test.utils.appsetup;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.main.BaboonApplication;

/**
 * ConcreteAppSetupOverrides is used by {@link BaboonMainApplicationSetupTest}
 * for testing purposes. It logs information to a {@link Logger} when its
 * {@link ConcreteAppSetupOverrides#declare()} and
 * {@link ConcreteAppSetupOverrides#subscribe()} methods are run.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @see {@link BaboonApplication}
 * @see {@link BaboonFramework}
 */
public class ConcreteAppSetupOverrides extends AbstractAppSetup {
    private final static Logger LOGGER = Logger.getLogger(ConcreteAppSetupOverrides.class.getName());

    @Override
    public void declare() {
        LOGGER.log(Level.INFO, "Declaring Concrete");
    }

    @Override
    public void subscribe() {
        LOGGER.log(Level.INFO, "Subscribing Concrete");
    }

}
