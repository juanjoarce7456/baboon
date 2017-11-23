package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.unc.lac.baboon.actioncontroller.HappeningActionController;
import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.subscription.ComplexSecuentialTaskControllerSubscription;
import org.unc.lac.baboon.subscription.HappeningControllerSubscription;
import org.unc.lac.baboon.subscription.SimpleTaskControllerSubscription;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;

public class SubscriptionsTest {

    /**
     * <li> Given I Have a {@link Topic} with empty permission </li>
     * <li> And I have a {@link TaskActionController} </li>
     * <li> When I create a {@link SimpleTaskControllerSubscription} </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void emptyPermissionOnTopicUsedToCreateSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockController, taskMethod); 
        TaskActionController taskController = new TaskActionController(mockController,methodObj);
        @SuppressWarnings("unused")
        SimpleTaskControllerSubscription subscription  = new SimpleTaskControllerSubscription(topic, taskController);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I have a {@link TaskActionController} </li>
     * <li> When I try to create a {@link SimpleTaskControllerSubscription} with a null a {@link Topic}</li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void nullTopicUsedToCreateSubscriptionShouldThrowException() throws Exception {
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockController, taskMethod); 
        TaskActionController taskController = new TaskActionController(mockController,methodObj);
        @SuppressWarnings("unused")
        SimpleTaskControllerSubscription subscription  = new SimpleTaskControllerSubscription(null, taskController);
        fail("Exception should have been thrown before this point");
    }
  
    /**
     * <li> Given I Have a {@link Topic} with more than one permission </li>
     * <li> And I have a {@link TaskActionController} </li>
     * <li> When I create a {@link SimpleTaskControllerSubscription} </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void moreThanOnePermissionOnSimpleTaskSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        topic.getPermission().add("t1");
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockController, taskMethod); 
        TaskActionController taskController = new TaskActionController(mockController,methodObj);
        @SuppressWarnings("unused")
        SimpleTaskControllerSubscription subscription  = new SimpleTaskControllerSubscription(topic, taskController);
        fail("Exception should have been thrown before this point");
    }

    /**
     * <li> Given I Have a {@link Topic} with more than one permission </li>
     * <li> And I have a {@link HappeningActionController} </li>
     * <li> When I create a {@link HappeningControllerSubscription} </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void moreThanOnePermissionOnHappeningControllerSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        topic.getPermission().add("t1");
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String happeningMethod = "mockHappeningController";
        Method methodObj = MethodDictionary.getMethod(mockController, happeningMethod); 
        HappeningActionController happeningController = new HappeningActionController(mockController,methodObj);
        @SuppressWarnings("unused")
        HappeningControllerSubscription subscription  = new HappeningControllerSubscription(topic, happeningController);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I Have a {@link Topic} with one permission and two guard callbacks</li>
     * <li> And I have a {@link HappeningActionController} </li>
     * <li> When I create a {@link HappeningControllerSubscription} </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void addingMoreGuardsThanPermissionsOnSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        String[] mockGuards ={"g1","g2"};
        String[] mockGuards2 ={"g3","g2"};
        topic.getSetGuardCallback().add(mockGuards);
        topic.getSetGuardCallback().add(mockGuards2);
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String happeningMethod = "mockHappeningController"; 
        Method methodObj = MethodDictionary.getMethod(mockController, happeningMethod); 
        HappeningActionController happeningController = new HappeningActionController(mockController,methodObj);
        @SuppressWarnings("unused")
        HappeningControllerSubscription subscription  = new HappeningControllerSubscription(topic, happeningController);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I Have a {@link Topic} with one permission</li>
     * <li> And I create a {@link ComplexSecuentialTaskControllerSubscription} using the {@link Topic}</li>
     * <li> When I use a null object as parameter on {@link ComplexSecuentialTaskControllerSubscription#addTask(TaskActionController)} </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingNullActionToComplexSecuentialTaskSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        ComplexSecuentialTaskControllerSubscription subscription  = new ComplexSecuentialTaskControllerSubscription(topic);
        subscription.addTask(null);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I Have a {@link Topic} with one permission</li>
     * <li> And I create a {@link ComplexSecuentialTaskControllerSubscription} using the {@link Topic}</li>
     * <li> When I append more than one {@link TaskActionController} using {@link ComplexSecuentialTaskControllerSubscription#addTask(TaskActionController)} </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingMoreActionsThanPermissionsToComplexSecuentialTaskSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        ComplexSecuentialTaskControllerSubscription subscription  = new ComplexSecuentialTaskControllerSubscription(topic);
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockController, taskMethod); 
        TaskActionController taskController1 = new TaskActionController(mockController,methodObj);
        subscription.addTask(taskController1);
        assertEquals(1,subscription.getSize());
        assertEquals(taskController1,subscription.getAction(0));
        TaskActionController taskController2 = new TaskActionController(mockController,methodObj);
        subscription.addTask(taskController2);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I Have a {@link Topic} with a correct permission and an empty permission</li>
     * <li> And I create a {@link ComplexSecuentialTaskControllerSubscription} using the {@link Topic}</li>
     * <li> And I append a {@link TaskActionController} using {@link ComplexSecuentialTaskControllerSubscription#addTask(TaskActionController)}
     * <li> When I append the second {@link TaskActionController} corresponding to the empty permission </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingTaskActionToEmptyPermissionInComplexSecuentialTaskSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        topic.getPermission().add("");
        ComplexSecuentialTaskControllerSubscription subscription  = new ComplexSecuentialTaskControllerSubscription(topic);
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockController, taskMethod); 
        TaskActionController taskController1 = new TaskActionController(mockController,methodObj);
        subscription.addTask(taskController1);
        assertEquals(1,subscription.getSize());
        assertEquals(taskController1,subscription.getAction(0));
        TaskActionController taskController2 = new TaskActionController(mockController,methodObj);
        subscription.addTask(taskController2);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I Have a {@link Topic} with a correct permission and a null permission</li>
     * <li> And I create a {@link ComplexSecuentialTaskControllerSubscription} using the {@link Topic}</li>
     * <li> And I append a {@link TaskActionController} using {@link ComplexSecuentialTaskControllerSubscription#addTask(TaskActionController)}
     * <li> When I append the second {@link TaskActionController} corresponding to the null permission </li>
     * <li> Then a {@link NotSubscribableException} should be thrown </li>
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingTaskActionToNullPermissionInComplexSecuentialTaskSubscriptionShouldThrowException() throws Exception {
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        topic.getPermission().add(null);
        ComplexSecuentialTaskControllerSubscription subscription  = new ComplexSecuentialTaskControllerSubscription(topic);
        final MockUserSystemObject mockController = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        Method methodObj = MethodDictionary.getMethod(mockController, taskMethod); 
        TaskActionController taskController1 = new TaskActionController(mockController,methodObj);
        subscription.addTask(taskController1);
        assertEquals(1,subscription.getSize());
        assertEquals(taskController1,subscription.getAction(0));
        TaskActionController taskController2 = new TaskActionController(mockController,methodObj);
        subscription.addTask(taskController2);
        fail("Exception should have been thrown before this point");
    }
}
