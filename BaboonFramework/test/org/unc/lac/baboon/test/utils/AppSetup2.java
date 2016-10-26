package org.unc.lac.baboon.test.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unc.lac.baboon.main.BaboonApplicationSetup;

public class AppSetup2 implements BaboonApplicationSetup {
	private final static Logger LOGGER = Logger.getLogger(AppSetup2.class.getName());
	boolean declared, subscribed=false;
	long timeDeclared, timeSubscribed = 0;
	@Override
	public void declare() {
		LOGGER.log(Level.INFO, "Declaring 2");
		declared=true;
	}

	@Override
	public void subscribe() {
		timeSubscribed = System.currentTimeMillis();
		LOGGER.log(Level.INFO, "Subscribing 2");
		subscribed=true;
	}

}