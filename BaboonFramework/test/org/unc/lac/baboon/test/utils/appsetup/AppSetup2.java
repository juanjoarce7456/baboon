package org.unc.lac.baboon.test.utils.appsetup;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.main.BaboonApplication;

/**
 * <b>AbstractAppSetup2</b>
 * 
 * @author Ariel Ivan Rabinovich & Juan Jose Arce Giacobbe
 *         <p>
 *         Class used by {@link BaboonMainApplicationSetupTest}
 */
public class AppSetup2 implements BaboonApplication {
    private final static Logger LOGGER = Logger.getLogger(AppSetup2.class.getName());

    @Override
    public void declare() {
        LOGGER.log(Level.INFO, "Declaring 2");
    }

    @Override
    public void subscribe() {
        LOGGER.log(Level.INFO, "Subscribing 2");
    }

}