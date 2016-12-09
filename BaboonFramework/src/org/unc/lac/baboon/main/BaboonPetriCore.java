package org.unc.lac.baboon.main;

import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.NotInitializedPetriNetException;
import org.unc.lac.javapetriconcurrencymonitor.monitor.PetriMonitor;
import org.unc.lac.javapetriconcurrencymonitor.monitor.policies.FirstInLinePolicy;
import org.unc.lac.javapetriconcurrencymonitor.monitor.policies.TransitionsPolicy;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.PetriNet;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.factory.PetriNetFactory;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.factory.PetriNetFactory.petriNetType;

/**
 * BaboonPetriCore is a wrapper containing the objects that are necessary for
 * initialize and execute petri nets.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see PetriMonitor
 * @see PetriNet
 */

public class BaboonPetriCore {
    private PetriNetFactory factory;
    private PetriMonitor monitor;
    private PetriNet petri;

    /**
     * Creates the Petri Net core of the application by using the pnml file
     * provided as an argument, the petri net type and the transition firing
     * policy.
     * <p>
     * If the petri net type provided is null then
     * {@link petriNetType#PLACE_TRANSITION} is used by default.
     * </p>
     * <p>
     * If the transition firing policy provided is null then
     * {@link FirstInLinePolicy} is used by default.
     * </p>
     * <p>
     * If pnml file path is null then {@link IllegalArgumentException} is
     * thrown.
     * </p>
     * 
     * 
     * @param pnmlFilePath
     *            The path to the file containing the PNML (Tina dialect)
     *            representation of the petri net.
     * @param type
     *            Indicates if the petri net to be created is a timed petri net
     *            or a place-transition petri net.
     * @param firingPolicy
     *            A {@link TransitionsPolicy} object used by petri monitor to
     *            decide which transition to fire next.
     */
    public BaboonPetriCore(String pnmlFilePath, petriNetType type, TransitionsPolicy firingPolicy) {
        if (pnmlFilePath == null) {
            throw new IllegalArgumentException("The pnml file path can not be null");
        }
        TransitionsPolicy firingPolicyChecked = firingPolicy == null ? new FirstInLinePolicy() : firingPolicy;
        petriNetType typeChecked = type == null ? petriNetType.PLACE_TRANSITION : type;
        factory = new PetriNetFactory(pnmlFilePath);
        petri = factory.makePetriNet(typeChecked);
        monitor = new PetriMonitor(petri, firingPolicyChecked);
    }

    /**
     * Initializes the petri net. This method is called by Baboon framework on its
     * setup.
     */
    public void initializePetriNet() {
        petri.initializePetriNet();
    }

    /**
     * Changes the petri monitor trasition firing prolicy. If the policy
     * provided is null then no actions are taken.
     * 
     * @param firingPolicy
     *            A {@link TransitionsPolicy} object used by petri monitor to
     *            decide which transition to fire next.
     * @see PetriMonitor
     * @see PetriMonitor#setTransitionsPolicy(TransitionsPolicy)
     */
    public void changeFiringPolicy(TransitionsPolicy firingPolicy) {
        monitor.setTransitionsPolicy(firingPolicy);
    }

    /**
     * Fires a transition by using petri monitor. This method is called
     * automatically by Baboon framework and is not intended to be used by user,
     * 
     * @param transitionName
     *            The name of the transition to be fired.
     * @param perennialFiring
     *            Indicates if the firing is perennial or not.
     * 
     * @see PetriMonitor
     * @see PetriMonitor#fireTransition(String, boolean)
     */
    public void fireTransition(String transitionName, boolean perennialFiring)
            throws IllegalArgumentException, IllegalTransitionFiringError, NotInitializedPetriNetException {
        monitor.fireTransition(transitionName, perennialFiring);
    }

}
