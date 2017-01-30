package org.unc.lac.baboon.main;

import java.util.ArrayList;
import java.util.Set;
import org.reflections.Reflections;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.task.AbstractTask;
import org.unc.lac.baboon.task.HappeningHandlerObject;
import org.unc.lac.baboon.task.TaskObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.TopicsJsonParser;
import org.unc.lac.javapetriconcurrencymonitor.errors.IllegalTransitionFiringError;
import org.unc.lac.javapetriconcurrencymonitor.exceptions.PetriNetException;
import org.unc.lac.javapetriconcurrencymonitor.monitor.PetriMonitor;
import org.unc.lac.javapetriconcurrencymonitor.monitor.policies.FirstInLinePolicy;
import org.unc.lac.javapetriconcurrencymonitor.monitor.policies.TransitionsPolicy;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.factory.PetriNetFactory.petriNetType;

import rx.Observer;
import rx.Subscription;

/**
 * BaboonFramework is the main class of the framework. This class should be
 * selected as Main class on Run Configurations in order to use Baboon.
 * <p>
 * At least one class implementing {@link BaboonApplication} must be written for
 * BaboonFramework to initialize the system.
 * </p>
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see BaboonApplication
 * @see BaboonFramework#main(String[])
 */

public class BaboonFramework {
    /**
     * List of BaboonApplication objects automatically instantiated by the
     * framework
     */
    private static ArrayList<BaboonApplication> appSetupObjects = new ArrayList<BaboonApplication>();

    private static BaboonConfig baboonConfig = new BaboonConfig();

    private static DummiesExecutor dummiesExecutor = new DummiesExecutor();

    private static BaboonPetriCore petriCore;

    /**
     * Main method.
     * <p>
     * Creates an instance for every class implementing
     * {@link BaboonApplication} interface.
     * <p>
     * Calls {@link BaboonApplication#declare()} for every
     * {@link BaboonApplication} instance. After, calls
     * {@link BaboonApplication#subscribe()} for every {@link BaboonApplication}
     * instance.
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
        if (petriCore == null) {
            throw new NullPointerException("The petri core is null");
        } else {
            petriCore.initializePetriNet();
        }
        for (AbstractTask task : baboonConfig.getSubscriptionsUnmodifiableMap().keySet()) {
            if (task.getClass().equals(TaskObject.class)) {
                Topic topic = baboonConfig.getSubscriptionsUnmodifiableMap().get(task);
                dummiesExecutor.executeDummy(new DummyThread((TaskObject) task, topic, petriCore));
            }
        }
    }

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
     * @see BaboonPetriCore
     */
    public static void createPetriCore(String pnmlFilePath, petriNetType type, TransitionsPolicy firingPolicy) {
        petriCore = new BaboonPetriCore(pnmlFilePath, type, firingPolicy);
    }

    /**
     * Subscribes an object instance and one method to the topic with the name
     * provided. The method to be subscribed must be annotated with
     * {@link HappeningHandler} or {@link Task}
     * 
     * @param topicsJsonFilePath
     *            the path of the json file containing the topics configuration.
     * 
     * @see Topic
     */
    public static void subscribeToTopic(String topicName, Object object, String methodName)
            throws NotSubscribableException {
        baboonConfig.subscribeToTopic(topicName, object, methodName);
    }

    /**
     * Imports the configuration of {@link Topic} objects described on the file
     * provided.
     * 
     * @param topicsJsonFilePath
     *            the path of the json file containing the topics configuration.
     * 
     * @see TopicsJsonParser
     */
    public static void addTopicsFile(String topicsJsonFilePath) throws BadTopicsJsonFormat, NoTopicsJsonFileException {
        baboonConfig.addTopics(topicsJsonFilePath);
    }

    /**
     * Returns the {@link Topic} object to which the task provided as a
     * parameter is subscribed or null if there's no topic subscribed to the
     * task.
     * 
     * @param task
     *            The task subscribed to the topic to be returned
     * 
     * @return The {@link Topic} to which the task is subscribed or null if
     *         there's no topic subscribed to the task provided
     */
    public static Topic getTopic(AbstractTask task) {
        return baboonConfig.getSubscriptionsUnmodifiableMap().get(task);
    }

    /**
     * Fires a transition by using petri core. This method is called
     * automatically by Baboon framework and is not intended to be used by user,
     * 
     * @param transitionName
     *            The name of the transition to be fired.
     * @param perennialFiring
     *            Indicates if the firing is perennial or not.
     * @throws PetriNetException
     * 
     * @see PetriMonitor
     * @see PetriMonitor#fireTransition(String, boolean)
     */
    public static void fireTransition(String transitionName, boolean perennialFiring)
            throws IllegalArgumentException, IllegalTransitionFiringError, PetriNetException {
        petriCore.fireTransition(transitionName, perennialFiring);
    }

    /**
     * Sets a guard by using petri core. This method is called automatically by
     * Baboon framework and is not intended to be used by user,
     * 
     * @param guardName
     *            The name of the guard to be modified.
     * @param newValue
     *            the new boolean value to be set on the guard.
     * @throws PetriNetException
     * 
     * @see PetriMonitor
     * @see PetriMonitor#setGuard(String, boolean)
     */
    public static void setGuard(String guardName, boolean newValue)
            throws IndexOutOfBoundsException, NullPointerException, PetriNetException {
        petriCore.setGuard(guardName, newValue);
    }

    /**
     * @deprecated
     * Returns the a copy of the {@link HappeningHandlerObject} originally
     * subscribed in the framework starting from a happening handler with the
     * same method and object. This method is not intended to be used by
     * user. (This is a provisory method and will be deleted in the future).
     */
    public static HappeningHandlerObject getSubscribedHappeningHandler(HappeningHandlerObject happeningHandler) {
        for (AbstractTask task : baboonConfig.getSubscriptionsUnmodifiableMap().keySet()) {
            if (task.equals(happeningHandler)) {
                return (HappeningHandlerObject) task;
            }
        }
        return null;
    }

    /**
     * Subscribe the given observer to the given transition events if it's
     * informed
     * 
     * @param _transitionName
     *            the name of the transition to subscribe to
     * @param _observer
     *            the observer to subscribe
     * @throws IllegalArgumentException
     *             if the given transition is not informed
     * @return a Subscription object used to unsubscribe
     * @see PetriMonitor#subscribeToTransition(Transition, Observer)
     */
    public static Subscription listenToTransitionInforms(final String _transitionName,
            final Observer<String> _observer) {
        return petriCore.listenToTransitionInforms(_transitionName, _observer);
    }

    /**
     * This method returns the current marking on the Petri Net core of the
     * application
     * 
     * @return the tokens in each place of the Petri Net
     */
    public static Integer[] getMarking() {
        return petriCore.getMarking();
    }
}
