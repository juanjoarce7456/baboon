package org.unc.lac.baboon.task;

import java.lang.reflect.Method;

import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

public class SimpleTaskSubscription extends ComplexSecuentialTaskSubscription {

	public SimpleTaskSubscription(Object objInstance, Method objMethod, Topic topic, Object... parameters) throws NotSubscribableException {
		super(topic);
		addTask(objInstance, objMethod, parameters);
	}
	
	@Override
	public void addTask(Object object, Method method, Object... parameters) throws NotSubscribableException{
		if(!(getSize() == 0)){
			 throw new NotSubscribableException("Cannot add more than one Task to a Simple Task");
		}
		if(getSize() + 1 > topic.getPermission().size()){
            throw new NotSubscribableException("Cannot add more tasks than permissions available in topic");
        }
        
        if(!( super.addAction(object, method) && parametersList.add(parameters))){
            throw new NotSubscribableException("Failed to add the task");
        }
	}

}
