package org.unc.lac.baboon.main;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.task.AbstractTaskSubscription;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.task.TaskSubscription;
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
     * Map of HappeningHandlers and Tasks subscribed to topics
     */
    private HashMap<Pair<Object,Method>, AbstractTaskSubscription> subscriptionsMap = new HashMap<Pair<Object,Method>, AbstractTaskSubscription>();
    /**
     * Map of Topics registered on the system indexed by name
     */
    private HashMap<String, Topic> topicsList = new HashMap<String, Topic>();

    /**
     * Provides an Unmodifiable instance of {@link #subscriptionsMap}
     * 
     * @return a Map of HappeningHandlers and Tasks subscribed to topics with
     *         "read-only" access.
     * @see Collections#unmodifiableMap(Map)
     */
    public Map<Pair<Object,Method>, AbstractTaskSubscription> getSubscriptionsMap() {
        return subscriptionsMap;
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
     * @param The
     *            name of the topic to be returned
     * @return The {@link Topic} with the name provided as parameter
     */
    public Topic getTopicByName(String topicName) {
        return Collections.unmodifiableMap(topicsList).get(topicName);
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
    public void subscribeToTopic(String topicName, Object object, String methodName) throws NotSubscribableException {
        Topic topic = getTopicByName(topicName);
        if (topicName == null || topic == null) {
            throw new NotSubscribableException("Cannot subscribe to a null topic");
        }
        if (object == null) {
            throw new NotSubscribableException("Cannot subscribe a null object");
        }
        if (methodName == null) {
            throw new NotSubscribableException("Cannot subscribe a null method name");
        }
        Method method;
        try {
            method = MethodDictionary.getMethod(object, methodName);
            Pair<Object,Method> key = new Pair<Object,Method>(object,method);
            if (method.isAnnotationPresent(HappeningHandler.class)) {
                AbstractTaskSubscription happeningHandler = new HappeningHandlerSubscription(object, method,topic);
                subscribeGuardCallbacks(topic, happeningHandler);
                if (subscriptionsMap.putIfAbsent(key, happeningHandler) != null) {
                    throw new NotSubscribableException("The happening handler is already subscribed to a topic.");
                }
            } else if (method.isAnnotationPresent(Task.class)) {
                AbstractTaskSubscription task = new TaskSubscription(object, method,topic);
                subscribeGuardCallbacks(topic, task);
                if (topic.getPermission() == null) {
                    throw new NotSubscribableException("The topic's permission cannot be null.");
                } else if (topic.getPermission().isEmpty()) {
                    throw new NotSubscribableException("The topic's permission cannot be empty.");
                } else if (subscriptionsMap.putIfAbsent(key,task) != null) {
                    throw new NotSubscribableException("The task is already subscribed to a topic.");
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
     * Determine the {@link Method} objects that will be invoked to resolve the
     * guard values after the execution of the task (this methods should be
     * annotated with {@link GuardProvider}). Also saves this Method objects on
     * the task using {@link AbstractTaskSubscription#addGuardCallback(String, Method)}
     * 
     * @param topic
     *            The topic containing the names of the guards.
     * @param task
     *            The task subscribing to the topic, on which the callback
     *            methods will be saved for future invocation.
     * 
     * @see Topic
     * @see AbstractTaskSubscription
     */
    private void subscribeGuardCallbacks(Topic topic, AbstractTaskSubscription task) throws NotSubscribableException {
        for (String guard : topic.getSetGuardCallback()) {
            try {
                task.addGuardCallback(guard, MethodDictionary.getGuardProviderMethod(task.getObject(), guard));
            } catch (NoSuchMethodException | IllegalArgumentException e) {
                throw new NotSubscribableException(e);
            }
        }
    }

}
