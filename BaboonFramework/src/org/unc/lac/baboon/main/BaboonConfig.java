package org.unc.lac.baboon.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.javatuples.Pair;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.task.AbstractActionSubscription;
import org.unc.lac.baboon.task.ComplexSecuentialTaskSubscription;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.task.SimpleTaskSubscription;
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
	 * Map of HappeningHandlerSubscriptions and TaskSubscriptions
	 */
	private HashMap<Pair<Object, Method>, HappeningHandlerSubscription> subscriptionsMap = new HashMap<Pair<Object, Method>, HappeningHandlerSubscription>();

	private ArrayList<ComplexSecuentialTaskSubscription> tasksList = new ArrayList<ComplexSecuentialTaskSubscription>();

	private HashMap<String, ComplexSecuentialTaskSubscription> complexTaskMap = new HashMap<String, ComplexSecuentialTaskSubscription>();

	/**
	 * Map of Topics registered on the system indexed by name
	 */
	private HashMap<String, Topic> topicsList = new HashMap<String, Topic>();

	/**
	 * Provides the {@link #subscriptionsMap}.
	 * 
	 * @return a Map of HappeningHandlers and Tasks subscribed to topics
	 */
	public HappeningHandlerSubscription getHappeningHandler(Pair<Object, Method> key) {
		return subscriptionsMap.get(key);
	}

	public ArrayList<ComplexSecuentialTaskSubscription> getTasksList() {
		return tasksList;
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
	public void subscribeToTopic(String topicName, Object object, String methodName, Class<?>[] parameterClasses,
			Object... parameters) throws NotSubscribableException {
		Topic topic = getTopicByName(topicName);
		if (topicName == null || topic == null) {
			throw new NotSubscribableException("Cannot subscribe to a null topic");
		}
		if (topic.getPermission() == null) {
			throw new NotSubscribableException("Cannot subscribe to a topic with null permission array.");
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
			method = MethodDictionary.getMethod(object, methodName, parameterClasses);
			if (method.isAnnotationPresent(HappeningHandler.class)) {
				Pair<Object, Method> key = new Pair<Object, Method>(object, method);
				HappeningHandlerSubscription happeningHandler = new HappeningHandlerSubscription(object, method,
						topic);
				subscribeGuardCallbacks(topic, happeningHandler);
				if (subscriptionsMap.putIfAbsent(key, happeningHandler) != null) {
					throw new NotSubscribableException("The happening handler is already subscribed to a topic.");
				}
			} else if (method.isAnnotationPresent(Task.class)) {
				if (topic.getPermission().isEmpty()) {
					throw new NotSubscribableException(
							"The topic permission array cannot be empty for a task subscription");
				} else if (topic.getPermission().get(0) == null) {
					throw new NotSubscribableException(
							"The topic's permission cannot be null for a task subscription.");
				}
				SimpleTaskSubscription task = new SimpleTaskSubscription(object, method, topic, parameters);
				subscribeGuardCallbacks(topic, task);
				tasksList.add(task);

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
	 * the task using
	 * {@link AbstractTaskSubscription#addGuardCallback(String, Method)}
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
	private void subscribeGuardCallbacks(Topic topic, AbstractActionSubscription task) throws NotSubscribableException {
		for (String guard : topic.getGuardCallback(task.getSize() - 1)) {
			try {
				task.addGuardCallback(guard,
						MethodDictionary.getGuardProviderMethod(task.getObject(task.getSize() - 1), guard));
			} catch (NoSuchMethodException | IllegalArgumentException e) {
				throw new NotSubscribableException(e);
			}
		}
	}

	public void createNewComplexTask(String complexTaskName, String topicName) throws NotSubscribableException {
		Topic topic = getTopicByName(topicName);
		if (complexTaskName == null || complexTaskName.isEmpty()) {
			throw new NotSubscribableException("Task name cannot be empty or null");
		}
		if (topicName == null || topic == null) {
			throw new NotSubscribableException("Cannot create a complex task with a null topic");
		}
		if (topic.getPermission() == null) {
			throw new NotSubscribableException(
					"Cannot create a complex task using a topic with null permission array.");
		}
		if (topic.getPermission().isEmpty()) {
			throw new NotSubscribableException("The topic permission array cannot be empty for a task subscription");
		}
		ComplexSecuentialTaskSubscription task = new ComplexSecuentialTaskSubscription(topic);
		tasksList.add(task);
		complexTaskMap.putIfAbsent(complexTaskName, task);
	}

	public void appendTaskToComplexTask(String complexTaskName, Object object, String methodName,
			Class<?>[] parameterClasses, Object... parameters) throws NotSubscribableException {
		try {
			Method method = MethodDictionary.getMethod(object, methodName, parameterClasses);
			if (method.isAnnotationPresent(Task.class)) {
				ComplexSecuentialTaskSubscription task = complexTaskMap.get(complexTaskName);
				if(task ==null){
					throw new NotSubscribableException(
							"Cannot subscribe to a null task");
				}
				else{
					task.addTask(object, method, parameters);
					subscribeGuardCallbacks(task.getTopic(), task);
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

}
