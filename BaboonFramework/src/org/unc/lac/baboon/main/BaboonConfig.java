package org.unc.lac.baboon.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.javatuples.Pair;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.InvalidGuardProviderMethod;
import org.unc.lac.baboon.exceptions.MultipleGuardProvidersException;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.task.Action;
import org.unc.lac.baboon.task.ComplexSecuentialTaskSubscription;
import org.unc.lac.baboon.task.HappeningHandlerAction;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.task.SimpleTaskSubscription;
import org.unc.lac.baboon.task.TaskAction;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.baboon.utils.TopicsJsonParser;

/**
 * Configuration Class. This class allows to import a json file containing the
 * {@link Topic} objects description of the system.
 * <p>
 * Also, this class contains the method for subscribing {@link HappeningHandler}
 * and {@link Task} annotated methods (and the instance of the object supposed
 * to run this methods) to a {@link Topic}
 * </p>
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class BaboonConfig {
    /**
     * Map containing all the {@link HappeningHandlerSubscription} subscriptions
     * registered in this {@link BaboonConfig}, indexed by a {@link Pair} of
     * {@link HappeningHandlerAction#actionObject} and
     * {@link HappeningHandlerAction#actionMethod}
     */
    private HashMap<Pair<Object, Method>, HappeningHandlerSubscription> happeningHandlerSubscriptionsMap = new HashMap<Pair<Object, Method>, HappeningHandlerSubscription>();

    /**
     * List containing all the {@link SimpleTaskSubscription} subscriptions
     * registered in this {@link BaboonConfig}.
     */
    private ArrayList<SimpleTaskSubscription> simpleTaskSubscriptionsList = new ArrayList<SimpleTaskSubscription>();

    /**
     * Map containing all the {@link ComplexSecuentialTaskSubscription}
     * subscriptions registered in this {@link BaboonConfig}, indexed by name
     * given by user.
     */
    private HashMap<String, ComplexSecuentialTaskSubscription> complexTaskMap = new HashMap<String, ComplexSecuentialTaskSubscription>();

    /**
     * Map of Topics registered this {@link BaboonConfig}, indexed by the names
     * given by user on topics .json file
     */
    private HashMap<String, Topic> topicsList = new HashMap<String, Topic>();

    /**
     * Returns the {@link HappeningHandlerSubscription} mapped to key on
     * {@link #happeningHandlerSubscriptionsMap}
     * 
     * @param key
     *            a {@link Pair} of an {@link Object} and a {@link Method}
     *            annotated with {@link HappeningHandler} whose associated
     *            {@link HappeningHandlerSubscription} is to be returned.
     * @return a {@link HappeningHandlerSubscription} to which the specified key
     *         is mapped, or null if this map contains no mapping for the key.
     * 
     */
    public HappeningHandlerSubscription getHappeningHandler(Pair<Object, Method> key) {
        return happeningHandlerSubscriptionsMap.get(key);
    }

    /**
     * This method returns the number of {@link HappeningHandlerSubscription}
     * objects that are mapped to a key on
     * {@link #happeningHandlerSubscriptionsMap}
     * 
     * @return the number of {@link HappeningHandlerSubscription} objects that
     *         are mapped to a key on {@link #happeningHandlerSubscriptionsMap}
     * 
     */
    public int getHappeningHandlerCount() {
        return happeningHandlerSubscriptionsMap.size();
    }

    /**
     * This method returns a {@link Collection} containing all the
     * {@link SimpleTaskSubscription} subscriptions registered in this
     * {@link BaboonConfig}.
     * 
     * @return a {@link Collection} containing all the
     *         {@link SimpleTaskSubscription} subscriptions registered in this
     *         {@link BaboonConfig}.
     * 
     */
    public Collection<SimpleTaskSubscription> getSimpleTasksCollection() {
        return simpleTaskSubscriptionsList;
    }

    /**
     * This method returns a {@link Collection} containing all the
     * {@link ComplexSecuentialTaskSubscription} subscriptions registered in
     * this {@link BaboonConfig}.
     * 
     * @return a {@link Collection} containing all the
     *         {@link ComplexSecuentialTaskSubscription} subscriptions
     *         registered in this {@link BaboonConfig}.
     */
    public Collection<ComplexSecuentialTaskSubscription> getComplexSecuentialTasksCollection() {
        return complexTaskMap.values();
    }

    /**
     * Returns the {@link ComplexSecuentialTaskSubscription} mapped to name on
     * {@link #complexTaskMap}
     * 
     * @param name
     *            A name given by user on the creation of a new
     *            {@link ComplexSecuentialTaskSubscription}, by using
     *            {@link #createNewComplexTask(String, String)}
     * 
     * @return a {@link ComplexSecuentialTaskSubscription} to which the
     *         specified name is mapped, or null if this map contains no mapping
     *         for the name.
     * 
     */
    public ComplexSecuentialTaskSubscription getComplexSecuentialTask(String name) {
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
            if (method.isAnnotationPresent(HappeningHandler.class)) {
                HappeningHandlerAction happeningHandler;
                try {
                    happeningHandler = new HappeningHandlerAction(object, method);

                    Pair<Object, Method> key = new Pair<Object, Method>(object, method);
                    HappeningHandlerSubscription happeningHandlerSubscription = new HappeningHandlerSubscription(topic,
                            happeningHandler);
                    if (happeningHandlerSubscriptionsMap.putIfAbsent(key, happeningHandlerSubscription) != null) {
                        throw new NotSubscribableException(
                                "The happening handler is already subscribed to another topic.");
                    }
                } catch (MultipleGuardProvidersException | InvalidGuardProviderMethod e) {
                    throw new NotSubscribableException("Error resolving GuardProvider Methods", e);
                } catch (IllegalArgumentException e) {
                    throw new NotSubscribableException("Error creating HappeningHandlerAction", e);
                }
            } else if (method.isAnnotationPresent(Task.class)) {
                if (topic.getPermission().isEmpty()) {
                    throw new NotSubscribableException(
                            "Cannot subscribe a task to a topic with empty permission array.");
                } else if (topic.getPermission().get(0).isEmpty()) {
                    throw new NotSubscribableException(
                            "The permission cannot be an empty String for a task subscription");
                } else if (topic.getPermission().get(0) == null) {
                    throw new NotSubscribableException(
                            "The topic's permission cannot be null for a task subscription.");
                }
                TaskAction task;
                try {
                    task = new TaskAction(object, method, parameters);
                    SimpleTaskSubscription taskSubscription = new SimpleTaskSubscription(topic, task);
                    simpleTaskSubscriptionsList.add(taskSubscription);
                } catch (MultipleGuardProvidersException | InvalidGuardProviderMethod e) {
                    throw new NotSubscribableException("Error resolving GuardProvider Methods", e);
                } catch (IllegalArgumentException e) {
                    throw new NotSubscribableException("Error creating TaskAction", e);
                }

            } else {
                throw new NotSubscribableException(
                        "The method should be annotated with HappeningHandler or Task annotations");
            }
        } catch (NoSuchMethodException e) {
            throw new NotSubscribableException("This method does not exist on object provided", e);
        } catch (SecurityException e) {
            throw new NotSubscribableException("Security violation while trying to get method provided", e);
        }

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
    public void createNewComplexTask(String complexTaskName, String topicName) throws NotSubscribableException {
        Topic topic = getTopicByName(topicName);
        if (complexTaskName == null || complexTaskName.isEmpty()) {
            throw new NotSubscribableException("Task name cannot be empty or null");
        }
        if (topicName == null || topic == null) {
            throw new NotSubscribableException("Cannot create a complex task with a null topic");
        }
        if (topic.getPermission().isEmpty()) {
            throw new NotSubscribableException("The topic permission array cannot be empty for a task subscription");
        }
        ComplexSecuentialTaskSubscription task = new ComplexSecuentialTaskSubscription(topic);
        if (complexTaskMap.putIfAbsent(complexTaskName, task) != null) {
            throw new NotSubscribableException("Already registered a task with the name " + complexTaskName);
        }
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
    public void appendTaskToComplexTask(String complexTaskName, Object object, String methodName, Object... parameters)
            throws NotSubscribableException {
        try {
            Class<?>[] paramClasses = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                paramClasses[i] = parameters[i].getClass();
            }
            Method method = MethodDictionary.getMethod(object, methodName, paramClasses);
            if (method.isAnnotationPresent(Task.class)) {
                ComplexSecuentialTaskSubscription complexTask = complexTaskMap.get(complexTaskName);
                if (complexTask == null) {
                    throw new NotSubscribableException(
                            "The complex task with name " + complexTaskName + " does not exists");
                } else {
                    complexTask.addTask(new TaskAction(object, method, parameters));
                }
            } else {
                throw new NotSubscribableException("The method should be annotated with Task annotation");
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
