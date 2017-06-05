package org.unc.lac.baboon.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.javatuples.Pair;
import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.actioncontroller.HappeningActionController;
import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.subscription.ComplexSecuentialTaskControllerSubscription;
import org.unc.lac.baboon.subscription.HappeningControllerSubscription;
import org.unc.lac.baboon.subscription.SimpleTaskControllerSubscription;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.baboon.utils.TopicsJsonParser;

/**
 * Configuration Class. This class allows to import a json file containing the
 * {@link Topic} objects description of the system.
 * <p>
 * Also, this class contains the method for subscribing {@link HappeningController}
 * and {@link TaskController} annotated methods (and the instance of the object supposed
 * to run this methods) to a {@link Topic}
 * </p>
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class BaboonConfig {
    /**
     * Map containing all the {@link HappeningControllerSubscription} subscriptions
     * registered in this {@link BaboonConfig}, indexed by a {@link Pair} of
     * {@link HappeningActionController#actionObject} and
     * {@link HappeningActionController#actionMethod}
     */
    private HashMap<Pair<Object, Method>, HappeningControllerSubscription> happeningControllerSubscriptionsMap = new HashMap<Pair<Object, Method>, HappeningControllerSubscription>();

    /**
     * List containing all the {@link SimpleTaskControllerSubscription} subscriptions
     * registered in this {@link BaboonConfig}.
     */
    private ArrayList<SimpleTaskControllerSubscription> simpleTaskSubscriptionsList = new ArrayList<SimpleTaskControllerSubscription>();

    /**
     * Map containing all the {@link ComplexSecuentialTaskControllerSubscription}
     * subscriptions registered in this {@link BaboonConfig}, indexed by name
     * given by user.
     */
    private HashMap<String, ComplexSecuentialTaskControllerSubscription> complexTaskMap = new HashMap<String, ComplexSecuentialTaskControllerSubscription>();

    /**
     * Map of Topics registered this {@link BaboonConfig}, indexed by the names
     * given by user on topics .json file
     */
    private HashMap<String, Topic> topicsList = new HashMap<String, Topic>();

    /**
     * Returns the {@link HappeningControllerSubscription} mapped to key on
     * {@link #happeningControllerSubscriptionsMap}
     * 
     * @param key
     *            a {@link Pair} of an {@link Object} and a {@link Method}
     *            annotated with {@link HappeningController} whose associated
     *            {@link HappeningControllerSubscription} is to be returned.
     * @return a {@link HappeningControllerSubscription} to which the specified key
     *         is mapped, or null if this map contains no mapping for the key.
     * 
     */
    public HappeningControllerSubscription getHappeningController(Pair<Object, Method> key) {
        return happeningControllerSubscriptionsMap.get(key);
    }

    /**
     * This method returns the number of {@link HappeningControllerSubscription}
     * objects that are mapped to a key on
     * {@link #happeningControllerSubscriptionsMap}
     * 
     * @return the number of {@link HappeningControllerSubscription} objects that
     *         are mapped to a key on {@link #happeningControllerSubscriptionsMap}
     * 
     */
    public int getHappeningControllerCount() {
        return happeningControllerSubscriptionsMap.size();
    }

    /**
     * This method returns a {@link Collection} containing all the
     * {@link SimpleTaskControllerSubscription} subscriptions registered in this
     * {@link BaboonConfig}.
     * 
     * @return a {@link Collection} containing all the
     *         {@link SimpleTaskControllerSubscription} subscriptions registered in this
     *         {@link BaboonConfig}.
     * 
     */
    public Collection<SimpleTaskControllerSubscription> getSimpleTasksCollection() {
        return simpleTaskSubscriptionsList;
    }

    /**
     * This method returns a {@link Collection} containing all the
     * {@link ComplexSecuentialTaskControllerSubscription} subscriptions registered in
     * this {@link BaboonConfig}.
     * 
     * @return a {@link Collection} containing all the
     *         {@link ComplexSecuentialTaskControllerSubscription} subscriptions
     *         registered in this {@link BaboonConfig}.
     */
    public Collection<ComplexSecuentialTaskControllerSubscription> getComplexSecuentialTasksCollection() {
        return complexTaskMap.values();
    }

    /**
     * Returns the {@link ComplexSecuentialTaskControllerSubscription} mapped to name on
     * {@link #complexTaskMap}
     * 
     * @param name
     *            A name given by user on the creation of a new
     *            {@link ComplexSecuentialTaskControllerSubscription}, by using
     *            {@link #createNewComplexTask(String, String)}
     * 
     * @return a {@link ComplexSecuentialTaskControllerSubscription} to which the
     *         specified name is mapped, or null if this map contains no mapping
     *         for the name.
     * 
     */
    public ComplexSecuentialTaskControllerSubscription getComplexSecuentialTask(String name) {
        return complexTaskMap.get(name);
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
    public void addTopics(String topicsJsonFilePath) throws BadTopicsJsonFormat, NoTopicsJsonFileException {
        topicsList.putAll((new TopicsJsonParser()).getTopicsFromJson(topicsJsonFilePath));
    }

    /**
     * Returns the {@link Topic} with the name provided or null if there's no
     * topic with such name.
     * 
     * @param topicName
     *            The name of the topic to be returned
     * @return The {@link Topic} with the name provided as parameter
     */
    public Topic getTopicByName(String topicName) {
        return topicsList.get(topicName);
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
     *             {@link ActionController#guardProviderMethodsMap}</li>
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
     *
     * 
     * @see Topic
     * @see ActionController
     * @see TaskActionController
     * @see HappeningActionController
     * @see SimpleTaskControllerSubscription
     * @see ComplexSecuentialTaskControllerSubscription
     */
    public void subscribeToTopic(String topicName, Object object, String methodName, Object... parameters)
            throws NotSubscribableException {
        Class<?>[] paramClasses = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            paramClasses[i] = parameters[i].getClass();
        }
        Topic topic = getTopicByName(topicName);
        if (topicName == null || topic == null) {
            throw new NotSubscribableException("Cannot subscribe to a null topic");
        }
        if (topic.getPermission().size() > 1) {
            throw new NotSubscribableException(
                    "Cannot subscribe to a topic with more than one permission. This can only be done through a ComplexSecuentialTask");
        }
        if (object == null) {
            throw new NotSubscribableException("Cannot subscribe a null object");
        }
        if (methodName == null) {
            throw new NotSubscribableException("Cannot subscribe a null method name");
        }
        Method method;
        try {
            method = MethodDictionary.getMethod(object, methodName, paramClasses);
            if (method.isAnnotationPresent(HappeningController.class)) {
                HappeningActionController happeningController;
                try {
                    happeningController = new HappeningActionController(object, method);

                    Pair<Object, Method> key = new Pair<Object, Method>(object, method);
                    HappeningControllerSubscription happeningControllerSubscription = new HappeningControllerSubscription(topic,
                            happeningController);
                    if (happeningControllerSubscriptionsMap.putIfAbsent(key, happeningControllerSubscription) != null) {
                        throw new NotSubscribableException(
                                "The happeningController is already subscribed to another topic.");
                    }
                } catch (MultipleGuardProvidersException | InvalidGuardProviderMethod e) {
                    throw new NotSubscribableException("Error resolving GuardProvider Methods", e);
                } catch (IllegalArgumentException e) {
                    throw new NotSubscribableException("Error creating HappeningActionController", e);
                }
            } else if (method.isAnnotationPresent(TaskController.class)) {
                if (topic.getPermission().isEmpty()) {
                    throw new NotSubscribableException(
                            "Cannot subscribe a taskController to a topic with empty permission array.");
                } else if (topic.getPermission().get(0).isEmpty()) {
                    throw new NotSubscribableException(
                            "The permission cannot be an empty String for a taskController subscription");
                } else if (topic.getPermission().get(0) == null) {
                    throw new NotSubscribableException(
                            "The topic's permission cannot be null for a taskController subscription.");
                }
                TaskActionController taskController;
                try {
                    taskController = new TaskActionController(object, method, parameters);
                    SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
                    simpleTaskSubscriptionsList.add(taskSubscription);
                } catch (MultipleGuardProvidersException | InvalidGuardProviderMethod e) {
                    throw new NotSubscribableException("Error resolving GuardProvider Methods", e);
                } catch (IllegalArgumentException e) {
                    throw new NotSubscribableException("Error creating TaskActionController", e);
                }

            } else {
                throw new NotSubscribableException(
                        "The method should be annotated with HappeningController or TaskController annotations");
            }
        } catch (NoSuchMethodException e) {
            throw new NotSubscribableException("This method does not exist on object provided", e);
        } catch (SecurityException e) {
            throw new NotSubscribableException("Security violation while trying to get method provided", e);
        }

    }

    /**
     * Creates a new {@link ComplexSecuentialTaskControllerSubscription}
     * 
     * @param complexTaskName
     *            A name to identify this complex taskController. This name must be unique.
     * @param topicName
     *            The name of the topic to be used for the subscription
     * 
     * @see Topic
     * @see ComplexSecuentialTaskControllerSubscription
     * @see TaskActionController
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
    public void createNewComplexTask(String complexTaskName, String topicName) throws NotSubscribableException {
        Topic topic = getTopicByName(topicName);
        if (complexTaskName == null || complexTaskName.isEmpty()) {
            throw new NotSubscribableException("TaskController name cannot be empty or null");
        }
        if (topicName == null || topic == null) {
            throw new NotSubscribableException("Cannot create a complex taskController with a null topic");
        }
        if (topic.getPermission().isEmpty()) {
            throw new NotSubscribableException("The topic permission array cannot be empty for a taskController subscription");
        }
        ComplexSecuentialTaskControllerSubscription taskController = new ComplexSecuentialTaskControllerSubscription(topic);
        if (complexTaskMap.putIfAbsent(complexTaskName, taskController) != null) {
            throw new NotSubscribableException("Already registered a taskController with the name " + complexTaskName);
        }
    }

    /**
     * This method requires an object instance, a method and the arguments of
     * this method to append a new {@link TaskActionController} to the
     * {@link ComplexSecuentialTaskControllerSubscription} identified by complexTaskName.
     * The method to be subscribed must be annotated with {@link TaskController}.
     * 
     * @param complexTaskName
     *            The name that identifies the complex taskController, it is provided on
     *            {@link #createNewComplexTask(String, String)} when creating
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
     *             <li>If the object provided as argument is null</li>
     *             <li>If the methodName provided as argument is null</li>
     *             <li>If the framework fails to resolve the method</li>
     *             <li>If there is a SecurityException when trying to resolve
     *             the method</li>
     *             <li>If there's an exception resolving the
     *             {@link ActionController#guardProviderMethodsMap}</li>
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
     *
     */
    public void appendTaskToComplexTask(String complexTaskName, Object object, String methodName, Object... parameters)
            throws NotSubscribableException {
        try {
            Class<?>[] paramClasses = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                paramClasses[i] = parameters[i].getClass();
            }
            Method method = MethodDictionary.getMethod(object, methodName, paramClasses);
            if (method.isAnnotationPresent(TaskController.class)) {
                ComplexSecuentialTaskControllerSubscription complexTask = complexTaskMap.get(complexTaskName);
                if (complexTask == null) {
                    throw new NotSubscribableException(
                            "The complex taskController with name " + complexTaskName + " does not exists");
                } else {
                    complexTask.addTask(new TaskActionController(object, method, parameters));
                }
            } else {
                throw new NotSubscribableException("The method should be annotated with TaskController annotation");
            }
        } catch (NoSuchMethodException e) {
            throw new NotSubscribableException("This method does not exist on object provided", e);
        } catch (SecurityException e) {
            throw new NotSubscribableException("Security violation while trying to get method provided", e);
        } catch (MultipleGuardProvidersException | InvalidGuardProviderMethod e) {
            throw new NotSubscribableException("Error resolving GuardProvider Methods", e);
        }

    }

}
