package org.unc.lac.baboon.main;

import java.util.ArrayList;
import java.util.Set;
import org.reflections.Reflections;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.happeninghandleraspect.HappeningHandlerJoinPoint;
import org.unc.lac.baboon.task.AbstractTaskSubscription;
import org.unc.lac.baboon.task.Action;
import org.unc.lac.baboon.task.ComplexSecuentialTaskSubscription;
import org.unc.lac.baboon.task.HappeningHandlerAction;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.task.SimpleTaskSubscription;
import org.unc.lac.baboon.task.TaskAction;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.TopicsJsonParser;
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
        for (AbstractTaskSubscription simpleTask : baboonConfig.getSimpleTasksCollection()) {
            dummiesExecutor.executeDummy(new DummyThread(simpleTask, petriCore));
        }
        for (AbstractTaskSubscription complexTask : baboonConfig.getComplexSecuentialTasksCollection()) {
            dummiesExecutor.executeDummy(new DummyThread(complexTask, petriCore));
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
        HappeningHandlerJoinPoint.setObserver(new HappeningSynchronizer(baboonConfig, petriCore));
    }

    /**
     * Subscribes an object instance, a method and the arguments of this method
     * to one topic. The method to be subscribed must be annotated with
     * {@link HappeningHandler} or {@link Task}. A
     * {@link HappeningHandlerSubscription} or {@link SimpleTaskSubscription} is
     * created.
     * 
     * @param topicName
     *            The name of the topic to be used for the subscription
     * @param object
     *            The object instance to subscribe on a new {@link TaskAction}
     *            or {@link HappeningHandlerAction} as
     *            {@link Action#actionObject}
     * @param methodName
     *            The name of the method to subscribe on a new
     *            {@link TaskAction} or {@link HappeningHandlerAction} as
     *            {@link Action#actionMethod}
     * @param parameters
     *            <li>The parameters to be used as arguments of the method on
     *            the new {@link TaskAction} or {@link HappeningHandlerAction}.
     *            This parameters are used along with the methodName to resolve
     *            the right method to use.</li>
     *            <li>When registering a {@link HappeningHandlerAction}
     *            parameters will be used only to determine the method, since
     *            the execution of {@link HappeningHandler} annotated methods is
     *            triggered by user software but synchronized by Baboon
     *            framework. In this case, mock instances of the classes
     *            required by the method could be used, allowing the framework
     *            to resolve and subscribe the method.</li>
     * 
     * @throws NotSubscribableException
     *             <li>If the topicName provided as argument is null</li>
     *             <li>When a topic with name topicName does not exist</li>
     *             <li>If there is more than one permission on
     *             {@link Topic#permission}</li>
     *             <li>If the object provided as argument is null</li>
     *             <li>If the methodName provided as argument is null</li>
     *             <li>If the framework fails to resolve the method</li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method</li>
     *             <li>If there's an exception resolving the
     *             {@link Action#guardProviderMethodsMap}</li>
     *             <li>If the method is not annotated with
     *             {@link HappeningHandler} or {@link Task}</li>
     *             <li>When trying to subscribe a {@link HappeningHandlerAction}
     *             that is already subscribed</li>
     *             <li>When trying to subscribe a {@link TaskAction} to a
     *             {@link Topic} with empty {@link Topic#permission}</li>
     *             <li>If the permission transition name is an empty String for
     *             a {@link SimpleTaskSubscription}</li>
     *             <li>If the permission transition name is null for a
     *             {@link SimpleTaskSubscription}</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If the {@link Action#actionObject} does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *
     * 
     * @see Topic
     * @see Action
     * @see TaskAction
     * @see HappeningHandlerAction
     * @see SimpleTaskSubscription
     * @see ComplexSecuentialTaskSubscription
     */
    public static void subscribeToTopic(String topicName, Object object, String methodName, Object... parameters)
            throws NotSubscribableException {
        baboonConfig.subscribeToTopic(topicName, object, methodName, parameters);
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

    /**
     * Creates a new {@link ComplexSecuentialTaskSubscription}
     * 
     * @param complexTaskName
     *            A name to identify this complex task.
     * @param topicName
     *            The name of the topic to be used for the subscription
     * 
     * @see Topic
     * @see ComplexSecuentialTaskSubscription
     * @see TaskAction
     * 
     * @throws NotSubscribableException
     *             <li>If complexTaskName is empty String</li>
     *             <li>If complexTaskName is null</li>
     *             <li>If topicName is empty String</li>
     *             <li>When a topic with name topicName does not exist</li>
     *             <li>When the {@link Topic} has an empty
     *             {@link Topic#permission}</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     */
    public static void createNewComplexTask(String complexTaskName, String topicName) throws NotSubscribableException {
        baboonConfig.createNewComplexTask(complexTaskName, topicName);
    }

    /**
     * This method requires an object instance, a method and the arguments of
     * this method to append a new {@link TaskAction} to the
     * {@link ComplexSecuentialTaskSubscription} identified by complexTaskName.
     * The method to be subscribed must be annotated with {@link Task}.
     * 
     * @param complexTaskName
     *            The name that identifies the complex task, it is provided on
     *            {@link #createNewComplexTask(String, String)} when creating
     *            the task.
     * @param object
     *            The object instance to be subscribed on a new
     *            {@link TaskAction} as {@link Action#actionObject}.
     * @param methodName
     *            The name of the method to be subscribed on a new
     *            {@link TaskAction} as {@link Action#actionMethod}.
     * @param parameters
     *            The parameters to be used as arguments of the method on the
     *            new {@link TaskAction}. This parameters are used along with
     *            the methodName to resolve the right method to use.
     * 
     * @throws NotSubscribableException
     *             <li>If the object provided as argument is null</li>
     *             <li>If the methodName provided as argument is null</li>
     *             <li>If the framework fails to resolve the method</li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method</li>
     *             <li>If there's an exception resolving the
     *             {@link Action#guardProviderMethodsMap}</li>
     *             <li>If the method is not annotated with {@link Task}</li>
     *             <li>If the {@link TaskAction} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskAction} objects in
     *             {@link ComplexSecuentialTaskSubscription}</li>
     *             <li>If the topic permission corresponding to this task is
     *             empty</li>
     *             <li>If the topic permission corresponding to this task is
     *             null</li>
     *             <li>If fails to append the {@link TaskAction} to
     *             {@link ComplexSecuentialTaskSubscription}.</li>
     *
     */
    public static void appendTaskToComplexTask(String complexTaskName, Object object, String methodName,
            Object... parameters) throws NotSubscribableException {
        baboonConfig.appendTaskToComplexTask(complexTaskName, object, methodName, parameters);
    }

}
