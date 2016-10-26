package org.unc.lac.baboon.main;
import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;


public class BaboonFramework {
	static private ArrayList<BaboonApplicationSetup> appSetupObjects = new ArrayList<BaboonApplicationSetup>();
	
	/**
	 * Main function.
	 * Creates an instance for every class implementing {@link BaboonApplicationSetup} interface.
	 * Calls {@link BaboonApplicationSetup#declare()} for every BaboonApplicationSetup instance.
	 * After, calls {@link BaboonApplicationSetup#subscribe()} for every BaboonApplicationSetup instance.
	 */
	public static void main(String[] args) {
		Reflections reflections = new Reflections("");
		Set<Class<? extends BaboonApplicationSetup>> apps = reflections.getSubTypesOf(BaboonApplicationSetup.class);
		for (Class<? extends BaboonApplicationSetup> app : apps) {
			try {
				appSetupObjects.add(app.newInstance());
			} catch (IllegalAccessException | InstantiationException e) {
				
			}
		}
		for (BaboonApplicationSetup appSetup : appSetupObjects) {
			appSetup.declare();
		}
		for (BaboonApplicationSetup appSetup : appSetupObjects) {
			appSetup.subscribe();
		}
	}
}
