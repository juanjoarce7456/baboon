package org.unc.lac.baboon.main;

import java.util.ArrayList;
import java.util.Set;
import org.reflections.Reflections;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.aspect.HappeningControllerJoinPointReporter;
import org.unc.lac.baboon.actioncontroller.HappeningActionController;
import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.exceptions.BadPolicyException;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.execution.DummiesExecutor;
import org.unc.lac.baboon.execution.DummyThread;
import org.unc.lac.baboon.execution.HappeningControllerSynchronizer;
import org.unc.lac.baboon.petri.BaboonPetriCore;
import org.unc.lac.baboon.config.BaboonConfig;
import org.unc.lac.baboon.subscription.AbstractTaskControllerSubscription;
import org.unc.lac.baboon.subscription.ComplexSecuentialTaskControllerSubscription;
import org.unc.lac.baboon.subscription.HappeningControllerSubscription;
import org.unc.lac.baboon.subscription.SimpleTaskControllerSubscription;
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
    private static ArrayList<BaboonApplication> appSetupObjects = new ArrayList<>();

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
     * 
     * @param args 
     *      No arguments are received yet.
     */
    public static void main(String[] args) {
        Reflections reflections = new Reflections("org.unc.lac.baboon");
        Set<Class<? extends BaboonApplication>> apps = reflections.getSubTypesOf(BaboonApplication.class);
        for (Class<? extends BaboonApplication> app : apps) {
            try {
                appSetupObjects.add(app.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {}
        }
        for (BaboonApplication appSetup : appSetupObjects) {
            appSetup.declare();
        }
        if (petriCore == null) {
            throw new NullPointerException("The petri core is null. Must be created on declare() method");
        } else {
            HappeningControllerJoinPointReporter.setObserver(new HappeningControllerSynchronizer(baboonConfig, petriCore));
            petriCore.initializePetriNet();
        }
        for (BaboonApplication appSetup : appSetupObjects) {
            appSetup.subscribe();
        }
        for (AbstractTaskControllerSubscription simpleTask : baboonConfig.getSimpleTasksCollection()) {
            dummiesExecutor.executeDummy(new DummyThread(simpleTask, petriCore));
        }
        for (AbstractTaskControllerSubscription complexTask : baboonConfig.getComplexSecuentialTasksCollection()) {
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
     *            A {@link Class} object that extends {@link TransitionsPolicy} used by 
     *            petri monitor to decide which transition to fire next. It might be null, 
     *            in which case {@link FirstInLinePolicy} will be used.
     * @param <A> 
     *            Class type that extends {@link TransitionsPolicy}.
     * @throws BadPolicyException
     *      If the transitions policy provided is badly formed.
     * @see BaboonPetriCore
     */
    public static <A extends TransitionsPolicy> void createPetriCore(String pnmlFilePath, petriNetType type, Class<A> firingPolicy) throws BadPolicyException {
        petriCore = new BaboonPetriCore(pnmlFilePath, type, firingPolicy);
    }

    /**
     * Subscribes an object instance, a method and the arguments of this method
     * to one topic. The method to be subscribed must be annotated with
     * {@link HappeningController} or {@link TaskController}. A
     * {@link HappeningControllerSubscription} or {@link SimpleTaskControllerSubscription} is
     * created.
     * 
     * @param topicName
     *            The name of the topic to be used for the subscription
     * @param object
     *            The object instance to subscribe on a new {@link TaskActionController}
     *            or {@link HappeningActionController} as
     *            {@link ActionController#actionObject}
     * @param methodName
     *            The name of the method to subscribe on a new
     *            {@link TaskActionController} or {@link HappeningActionController} as
     *            {@link ActionController#actionMethod}
     * @param parameters
     *            <ul>
     *            <li>The parameters to be used as arguments of the method on
     *            the new {@link TaskActionController} or {@link HappeningActionController}.
     *            This parameters are used along with the methodName to resolve
     *            the right method to use.</li>
     *            <li>When registering a {@link HappeningActionController}
     *            parameters will be used only to determine the method, since
     *            the execution of {@link HappeningController} annotated methods is
     *            triggered by user software but synchronized by Baboon
     *            framework. In this case, mock instances of the classes
     *            required by the method could be used, allowing the framework
     *            to resolve and subscribe the method.</li>
     *            </ul>
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>If the topicName provided as argument is null</li>
     *             <li>When a topic with name topicName does not exist</li>
     *             <li>If there is more than one permission on
     *             {@link Topic#permission}</li>
     *             <li>If the object provided as argument is null </li>
     *             <li>If the methodName provided as argument is null </li>
     *             <li>If the framework fails to resolve the method </li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method </li>
     *             <li>If there's an exception on
     *             {@link ActionController#resolveGuardProviderMethods()} </li>
     *             <li>If the method is not annotated with
     *             {@link HappeningController} or {@link TaskController} </li>
     *             <li>When trying to subscribe a {@link HappeningActionController}
     *             that is already subscribed </li>
     *             <li>When trying to subscribe a {@link TaskActionController} to a
     *             {@link Topic} with empty {@link Topic#permission} </li>
     *             <li>If the permission transition name is an empty String for
     *             a {@link SimpleTaskControllerSubscription} </li>
     *             <li>If the permission transition name is null for a
     *             {@link SimpleTaskControllerSubscription} </li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If the {@link ActionController#actionObject} does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic </li>
     *             </ul>
     *
     * 
     * @see Topic
     * @see ActionController
     * @see TaskActionController
     * @see HappeningActionController
     * @see SimpleTaskControllerSubscription
     * @see ComplexSecuentialTaskControllerSubscription
     */
    public static void subscribeControllerToTopic(String topicName, Object object, String methodName, Object... parameters)
            throws NotSubscribableException {
        baboonConfig.subscribeControllerToTopic(topicName, object, methodName, parameters);
    }
    
    
    /**
     * Subscribes a static method and the arguments of this method
     * to one topic. The method to be subscribed must be annotated with
     * {@link HappeningController} or {@link TaskController}. A
     * {@link HappeningControllerSubscription} or {@link SimpleTaskControllerSubscription} is
     * created.
     * 
     * @param topicName
     *            The name of the topic to be used for the subscription
     * @param methodsClass
     *            The class on which the method is defined
     * @param methodName
     *            The name of the static method to subscribe on a new
     *            {@link TaskActionController} or {@link HappeningActionController} as
     *            {@link ActionController#actionMethod}
     * @param parameters
     *            <ul>
     *            <li>The parameters to be used as arguments of the method on
     *            the new {@link TaskActionController} or {@link HappeningActionController}.
     *            This parameters are used along with the methodName to resolve
     *            the right method to use.</li>
     *            <li>When registering a {@link HappeningActionController}
     *            parameters will be used only to determine the method, since
     *            the execution of {@link HappeningController} annotated methods is
     *            triggered by user software but synchronized by Baboon
     *            framework. In this case, mock instances of the classes
     *            required by the method could be used, allowing the framework
     *            to resolve and subscribe the method.</li>
     *            </ul>
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>If the topicName provided as argument is null</li>
     *             <li>When a topic with name topicName does not exist</li>
     *             <li>If there is more than one permission on
     *             {@link Topic#permission}</li>
     *             <li>If the methodsClass provided as argument is null</li>
     *             <li>If the methodName provided as argument is null</li>
     *             <li>If the framework fails to resolve the method</li>
     *             <li>If the method resolved is not static</li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method</li>
     *             <li>If there's an exception on
     *             {@link ActionController#resolveGuardProviderMethods()}</li>
     *             <li>If the method is not annotated with
     *             {@link HappeningController} or {@link TaskController}</li>
     *             <li>When trying to subscribe a {@link HappeningActionController}
     *             that is already subscribed</li>
     *             <li>When trying to subscribe a {@link TaskActionController} to a
     *             {@link Topic} with empty {@link Topic#permission}</li>
     *             <li>If the permission transition name is an empty String for
     *             a {@link SimpleTaskControllerSubscription}</li>
     *             <li>If the permission transition name is null for a
     *             {@link SimpleTaskControllerSubscription}</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If the {@link ActionController#actionObject} does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             </ul>
     * 
     * @see Topic
     * @see ActionController
     * @see TaskActionController
     * @see HappeningActionController
     * @see SimpleTaskControllerSubscription
     * @see ComplexSecuentialTaskControllerSubscription
     */
    public static void subscribeStaticControllerToTopic(String topicName, Class<?> methodsClass, String methodName, Object... parameters) throws NotSubscribableException{
        baboonConfig.subscribeStaticControllerToTopic(topicName, methodsClass, methodName, parameters);
    }

    /**
     * Imports the configuration of {@link Topic} objects described on the file
     * provided.
     * 
     * @param topicsJsonFilePath
     *            the path of the json file containing the topics configuration.
     * 
     * @throws BadTopicsJsonFormat
     *      If the json file is badly formed
     *      
     * @throws NoTopicsJsonFileException
     *      If the json file does not exist.
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
     * @see PetriMonitor#subscribeToTransition(String, Observer)
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
     * Creates a new {@link ComplexSecuentialTaskControllerSubscription}
     * 
     * @param complexTaskName
     *            A name to identify this complex taskController.
     * @param topicName
     *            The name of the topic to be used for the subscription
     * 
     * @see Topic
     * @see ComplexSecuentialTaskControllerSubscription
     * @see TaskActionController
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>If complexTaskName is empty String</li>
     *             <li>If complexTaskName is null</li>
     *             <li>If topicName is empty String</li>
     *             <li>When a topic with name topicName does not exist</li>
     *             <li>When the {@link Topic} has an empty
     *             {@link Topic#permission}</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             </ul>
     */
    public static void createNewComplexTaskController(String complexTaskName, String topicName) throws NotSubscribableException {
        baboonConfig.createNewComplexTaskController(complexTaskName, topicName);
    }

    /**
     * This method requires an object instance, a method and the arguments of
     * this method to append a new {@link TaskActionController} to the
     * {@link ComplexSecuentialTaskControllerSubscription} identified by complexTaskName.
     * The method to be subscribed must be annotated with {@link TaskController}.
     * 
     * @param complexTaskName
     *            The name that identifies the complex taskController, it is provided on
     *            {@link #createNewComplexTaskController(String, String)} when creating
     *            the taskController.
     * @param object
     *            The object instance to be subscribed on a new
     *            {@link TaskActionController} as {@link ActionController#actionObject}.
     * @param methodName
     *            The name of the method to be subscribed on a new
     *            {@link TaskActionController} as {@link ActionController#actionMethod}.
     * @param parameters
     *            The parameters to be used as arguments of the method on the
     *            new {@link TaskActionController}. This parameters are used along with
     *            the methodName to resolve the right method to use.
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>If the object provided as argument is null</li>
     *             <li>If the methodName provided as argument is null</li>
     *             <li>If the framework fails to resolve the method</li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method</li>
     *             <li>If there's an exception on
     *             {@link ActionController#resolveGuardProviderMethods()}</li>
     *             <li>If the method is not annotated with {@link TaskController}</li>
     *             <li>If the {@link TaskActionController} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskActionController} objects in
     *             {@link ComplexSecuentialTaskControllerSubscription}</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             empty</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             null</li>
     *             <li>If fails to append the {@link TaskActionController} to
     *             {@link ComplexSecuentialTaskControllerSubscription}.</li>
     *             </ul>
     *
     */
    public static void appendControllerToComplexTaskController(String complexTaskName, Object object, String methodName,
            Object... parameters) throws NotSubscribableException {
        baboonConfig.appendControllerToComplexTaskController(complexTaskName, object, methodName, parameters);
    }
    
    
    /**
     * This method requires a static method and the arguments of
     * this method to append a new {@link TaskActionController} to the
     * {@link ComplexSecuentialTaskControllerSubscription} identified by complexTaskName.
     * The method to be subscribed must be annotated with {@link TaskController}.
     * 
     * @param complexTaskName
     *            The name that identifies the complex taskController, it is provided on
     *            {@link #createNewComplexTaskController(String, String)} when creating
     *            the taskController.
     * @param methodsClass
     *            The class on which the method is defined
     * @param methodName
     *            The name of the static method to be subscribed on a new
     *            {@link TaskActionController} as {@link ActionController#actionMethod}.
     * @param parameters
     *            The parameters to be used as arguments of the method on the
     *            new {@link TaskActionController}. This parameters are used along with
     *            the methodName to resolve the right method to use.
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>If the methodsClass provided as argument is null</li>
     *             <li>If the methodName provided as argument is null</li>
     *             <li>If the framework fails to resolve the method</li>
     *             <li>If the the method is not static</li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method</li>
     *             <li>If there's an exception on
     *             {@link ActionController#resolveGuardProviderMethods()}</li>
     *             <li>If the method is not annotated with {@link TaskController}</li>
     *             <li>If the {@link TaskActionController} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskActionController} objects in
     *             {@link ComplexSecuentialTaskControllerSubscription}</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             empty</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             null</li>
     *             <li>If fails to append the {@link TaskActionController} to
     *             {@link ComplexSecuentialTaskControllerSubscription}.</li>
     *             </ul>
     */
    public static void appendStaticControllerToComplexTaskController(String complexTaskName, Class<?> methodsClass, String methodName, Object... parameters)
            throws NotSubscribableException {
        baboonConfig.appendStaticControllerToComplexTaskController(complexTaskName, methodsClass, methodName, parameters);
    }

}
