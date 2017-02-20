package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.topic.Topic;

public abstract class AbstractActionSubscription {
	protected ArrayList<Object> objectsList;
	protected ArrayList<Method> methodsList;
	protected Topic topic;
	protected ArrayList<HashMap<String, Method>> guardCallbackList;

	public AbstractActionSubscription(Topic topic) {
		this.objectsList = new ArrayList<Object>();
		this.methodsList = new ArrayList<Method>();
		this.guardCallbackList = new ArrayList<HashMap<String, Method>>();
		this.topic = topic;
	}

	public boolean addAction(Object object, Method method) {
		return (objectsList.add(object) && methodsList.add(method));
	}

	public Topic getTopic() {
		return topic;
	}

	public int getSize() {
		if (objectsList.size() != methodsList.size()) {
			throw new RuntimeException("The task size is undefined");
		}
		return objectsList.size();
	}
	
	public Object getObject(int actionIndex){
		return objectsList.get(actionIndex);
	}

	/**
	 * This method adds a new guard callback to the task. It is intended to be
	 * used by the framework only, the user should not call this method.
	 *
	 * @param guardName
	 *            The name of the guard to be set by the callback
	 * @param callback
	 *            The Method to call for obtaining the guard value. This method
	 *            must be annotated with {@link GuardProvider} and must return a
	 *            boolean value.
	 * @throws IllegalArgumentException
	 *             When guardName is empty or null, when callback is not
	 *             annotated with {@link GuardProvider}, does not return a
	 *             boolean or has parameters and last, when callback is not a
	 *             method present in the object instance of this task.
	 */
	public void addGuardCallback(String guardName, Method callback) {
		if (guardName == null || guardName.isEmpty()) {
			throw new IllegalArgumentException("guardName cannot be empty");
		}
		if (!callback.isAnnotationPresent(GuardProvider.class)) {
			throw new IllegalArgumentException("callback should be annotated with GuardProvider");
		} else if (!callback.getReturnType().getTypeName().equals("boolean")) {
			throw new IllegalArgumentException("callback should return a boolean value");
		} else if (callback.getParameterCount() != 0) {
			throw new IllegalArgumentException("callback should take no parameters");
		} else if (!isMethodPresent(callback, objectsList.size() - 1)) {
			throw new IllegalArgumentException("callback is not a Method of the object instance present in this task");
		} else {
			if(guardCallbackList.size() == (getSize() - 1)){
				guardCallbackList.add(new HashMap<String,Method>());
			}
			guardCallbackList.get(guardCallbackList.size()-1).put(guardName, callback);
		}

	}
	
    /**
     * This method returns the value of the guard by executing the
     * {@link GuardProvider} annotated method associated with the guard. It is
     * intended to be used by the framework only, the user should not call this
     * method.
     * 
     * @param guardName
     *            The name of the guard whose value is to be known
     * @return The value of the guard
     *
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public boolean getGuardValue(String guardName, int taskIndex)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Boolean result = (Boolean) guardCallbackList.get(taskIndex).get(guardName).invoke(objectsList.get(taskIndex));

        return result.booleanValue();
    }

	private boolean isMethodPresent(Method method, int taskIndex) {
		for (Method m : objectsList.get(taskIndex).getClass().getMethods()) {
			if (method.equals(m)) {
				return true;
			}
		}
		return false;
	}

}
