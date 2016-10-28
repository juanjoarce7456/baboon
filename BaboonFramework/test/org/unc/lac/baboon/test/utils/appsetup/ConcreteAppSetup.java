package org.unc.lac.baboon.test.utils.appsetup;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <b>ConcreteAppSetup</b>
 * 
 * @author Ariel Ivan Rabinovich & Juan Jose Arce Giacobbe
 *         <p>
 *         Class used by {@link BaboonMainApplicationSetupTest}
 */
public class ConcreteAppSetup extends AbstractAppSetup {
    private final static Logger LOGGER = Logger.getLogger(ConcreteAppSetup.class.getName());

    @Override
    public void declare() {
        LOGGER.log(Level.INFO, "Declaring Concrete");
    }

    @Override
    public void subscribe() {
        LOGGER.log(Level.INFO, "Subscribing Concrete");
    }

}
