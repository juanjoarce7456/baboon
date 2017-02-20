package org.unc.lac.baboon.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

public class ComplexSecuentialTaskSubscription extends AbstractActionSubscription{

    protected ArrayList<Object[]> parametersList;
    
    public ComplexSecuentialTaskSubscription(Topic topic) {
    	super(topic);
        this.parametersList= new ArrayList<Object[]>();
    }
    
    public void addTask(Object object, Method method, Object... parameters) throws NotSubscribableException {   
        if(getSize() + 1 > topic.getPermission().size()){
            throw new NotSubscribableException("Trying to add more tasks than permissions available in topic");
        }
        
        if(!( super.addAction(object, method) && parametersList.add(parameters))){
        	throw new NotSubscribableException("Failed to add the task");
        }
    }
    
    public void executeMethod(int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        methodsList.get(index).invoke(objectsList.get(index), parametersList.get(index));
    }
    
    @Override
    public int getSize(){
    	if(super.getSize() != parametersList.size()){
    		throw new RuntimeException("The task size is undefined");
    	}
    	return objectsList.size();
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
    @Override
    public boolean getGuardValue(String guardName, int taskIndex)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Boolean result = (Boolean) guardCallbackList.get(taskIndex).get(guardName).invoke(objectsList.get(taskIndex), parametersList.get(taskIndex));

        return result.booleanValue();
    }

}
