package org.unc.lac.baboon.main;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.task.AbstractTask;
import org.unc.lac.baboon.task.HappeningHandlerObject;
import org.unc.lac.baboon.task.TaskObject;
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
    private HashMap<AbstractTask, Topic> subscriptionsMap = new HashMap<AbstractTask, Topic>();
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
    public Map<AbstractTask, Topic> getSubscriptionsUnmodifiableMap() {
        return Collections.unmodifiableMap(subscriptionsMap);
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
            throw new NotSubscribableException("Can Not subscribe to a null topic");
        }
        if (object == null) {
            throw new NotSubscribableException("Can Not subscribe a null object");
        }
        if (methodName == null) {
            throw new NotSubscribableException("Can Not subscribe a null method name");
        }
        Method method;
        try {
            method = MethodDictionary.getMethod(object, methodName);
            boolean isHappeningHandler = false;
            if ((isHappeningHandler = method.isAnnotationPresent(HappeningHandler.class))
                    || method.isAnnotationPresent(Task.class)) {
                AbstractTask task = (isHappeningHandler) ? new HappeningHandlerObject(object, method)
                        : new TaskObject(object, method);
                if (subscriptionsMap.putIfAbsent(task, topic) != null) {
                    throw new NotSubscribableException(
                            "This HappeningHandler or Task is already subscribed to a topic.");
                }
            } else {
                throw new NotSubscribableException();
            }
        } catch (NoSuchMethodException e) {
            throw new NotSubscribableException("This method does not exist on object provided", e);
        } catch (SecurityException e) {
            throw new NotSubscribableException("Security violation while trying to get method provided", e);
        }
    }

}
