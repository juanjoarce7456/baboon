package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import org.junit.Test;
import org.unc.lac.baboon.subscription.ComplexSecuentialTaskControllerSubscription;
import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.config.BaboonConfig;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.topic.Topic;

public class ComplexTaskControllersSubscriptionTest {
    private final String topicsPath = "/org/unc/lac/baboon/test/resources/topics02.json";
    private final String[] topicNamesDefined = { "topic1", "topic2", "topic3", "topic4","complex_topic" };
    
    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex secuential task associated with a {@link Topic}</li>
     * <li>Then the {@link ComplexSecuentialTaskControllerSubscription} subscriptions Map should contain a
     * {@link ComplexSecuentialTaskControllerSubscription} with the complex task name as a map's key</li>
     * <li>And the {@link ComplexSecuentialTaskControllerSubscription} should contain the
     * {@link Topic}</li>
     */
    @Test
    public void subscribingAComplexTaskToAnExistingTopicShouldGetRegisteredInConfigTest() 
            throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final BaboonConfig baboonConfig = new BaboonConfig();
            baboonConfig.addTopics(topicsPath);
            String complexTaskName = "complexFooTask";
            baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
            ComplexSecuentialTaskControllerSubscription complexTask = baboonConfig.getComplexSecuentialTask(complexTaskName);
            assertEquals(0, complexTask.getSize());
            assertEquals(topicNamesDefined[4], complexTask.getTopic().getName());
    }
    
    /**
     * <li>Given I have a topics json file containing a topic with a permission
     *  list containing two permissions</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I create a new complex secuential task associated with the {@link Topic}</li>
     * <li>And I have an instance of an object of the user's system with two methods annotated
     * with {@link TaskController}</li>
     * <li> When I append the two {@link TaskController} annotated methods to the {@link ComplexSecuentialTaskControllerSubscription}</li>
     * <li>Then the {@link ComplexSecuentialTaskControllerSubscription} subscriptions Map should contain a
     * {@link ComplexSecuentialTaskControllerSubscription} with the complex task name as a map's key</li>
     * <li>And the {@link ComplexSecuentialTaskControllerSubscription} should contain the
     * {@link Topic}</li>
     * <li>And the {@link ComplexSecuentialTaskControllerSubscription} should contain the
     * two actions ordered by insertion time</li>
     */
    @Test
    public void appendingTaskstoAComplexTaskShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObject = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        final String taskMethod2 = "mockTask2";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        baboonConfig.appendControllerToComplexTaskController(complexTaskName,mockUserSystemObject,taskMethod);
        baboonConfig.appendControllerToComplexTaskController(complexTaskName,mockUserSystemObject,taskMethod2);
        ComplexSecuentialTaskControllerSubscription complexTask = baboonConfig.getComplexSecuentialTask(complexTaskName);
        assertEquals(2, complexTask.getSize());
        assertEquals(topicNamesDefined[4], complexTask.getTopic().getName());
        assertEquals(mockUserSystemObject, complexTask.getAction(0).getActionObject());
        assertEquals(taskMethod, complexTask.getAction(0).getMethodName());
        assertEquals(mockUserSystemObject, complexTask.getAction(1).getActionObject());
        assertEquals(taskMethod2, complexTask.getAction(1).getMethodName());
    }
    
    /**
     * <li>Given I have a topics json file containing a topic with a permission
     *  list containing TWO permissions</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I create a new complex secuential task associated with the {@link Topic}</li>
     * <li>And I have an instance of an object of the user's system with THREE methods annotated
     * with {@link TaskController}</li>
     * <li> When I append the third {@link TaskController} annotated method to the {@link ComplexSecuentialTaskControllerSubscription}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingMoreTasksThanPermissionsOnAComplexTaskShouldNotBeAllowed() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObject = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        final String taskMethod2 = "mockTask2";
        final String taskMethod3 = "mockTask3";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        baboonConfig.appendControllerToComplexTaskController(complexTaskName,mockUserSystemObject,taskMethod);
        baboonConfig.appendControllerToComplexTaskController(complexTaskName,mockUserSystemObject,taskMethod2);
        ComplexSecuentialTaskControllerSubscription complexTask = baboonConfig.getComplexSecuentialTask(complexTaskName);
        assertEquals(2, complexTask.getSize());
        baboonConfig.appendControllerToComplexTaskController(complexTaskName,mockUserSystemObject,taskMethod3);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex secuential task with a {@link Topic} name 
     * that does not exist on topics file</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void creatingAComplexTaskUsingANotExistingTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException{
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, "notExistingTopic");
        fail("Exception should have been thrown before this point");

    }
    
    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex secuential task with a null {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void creatingAComplexTaskUsingANullTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException{
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, null);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex secuential task with an empty string as {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void creatingAComplexTaskUsingAnEmptyTopicNameShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException{
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, "");
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex secuential task with null name </li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void creatingAComplexTaskUsingANullTaskNameShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException{
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        baboonConfig.createNewComplexTaskController(null, topicNamesDefined[4]);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex secuential task with empty string as name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li> 
     */
    @Test (expected=NotSubscribableException.class)
    public void creatingAComplexTaskUsingAnEmptyTaskNameShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException{
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        baboonConfig.createNewComplexTaskController("", topicNamesDefined[4]);
        fail("Exception should have been thrown before this point");
    }
    
    
    /**
     * <li>Given I have a topics json file containing a topic with a permission
     *  list containing two permissions</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I create a new complex secuential task associated with the {@link Topic}</li>
     * <li>And I have a class of an object of the user's system with a static method annotated
     * with {@link TaskController}</li>
     * <li> When I append the {@link TaskController} annotated static method to the {@link ComplexSecuentialTaskControllerSubscription}</li>
     * <li>Then the {@link ComplexSecuentialTaskControllerSubscription} subscriptions Map should contain a
     * {@link ComplexSecuentialTaskControllerSubscription} with the complex task name as a map's key</li>
     * <li>And the {@link ComplexSecuentialTaskControllerSubscription} should contain the
     * {@link Topic}</li>
     * <li>And the {@link ComplexSecuentialTaskControllerSubscription} should contain the
     * action with the static method</li>
     */
    @Test
    public void appendingAStaticTaskControllerToAComplexTaskShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final String taskMethod = "staticMockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        baboonConfig.appendStaticControllerToComplexTaskController(complexTaskName, MockUserSystemObject.class, taskMethod);
        ComplexSecuentialTaskControllerSubscription complexTask = baboonConfig.getComplexSecuentialTask(complexTaskName);
        assertEquals(1, complexTask.getSize());
        assertEquals(topicNamesDefined[4], complexTask.getTopic().getName());
        assertEquals(MockUserSystemObject.class, complexTask.getAction(0).getActionObject());
        assertEquals(taskMethod, complexTask.getAction(0).getMethodName());
    }

    
    /**
     * <li>Given I have a topics json file containing a topic with a permission
     *  list containing two permissions</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I create a new complex secuential task associated with the {@link Topic}</li>
     * <li>And I have a class of a user's system object</li>
     * <li> When I append a non existing method to the {@link ComplexSecuentialTaskControllerSubscription}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li> 
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingANonExistingMethodToAComplexTaskShouldNotBeAllowed() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        baboonConfig.appendStaticControllerToComplexTaskController(complexTaskName, MockUserSystemObject.class, "NonExistingMethod");
        fail("Exception should have been thrown before this point");
    }
    
    
    /**
     * <li>Given I have a topics json file containing a topic with a permission
     *  list containing two permissions</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I create a new complex secuential task associated with the {@link Topic}</li>
     * <li>And I have a class of a user's system object</li>
     * <li> When I append a non static {@link TaskController} method to the {@link ComplexSecuentialTaskControllerSubscription} using 
     * {@link BaboonConfig#appendStaticControllerToComplexTaskController(String, Class, String, Object...)}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li> 
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingANonStaticTaskControllerToAComplexTaskUsingStaticSubscriptionInterfaceShouldNotBeAllowed() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        baboonConfig.appendStaticControllerToComplexTaskController(complexTaskName, MockUserSystemObject.class, taskMethod);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I have a {@link Topic} </li>
     * <li> When I try to create a {@link ComplexSecuentialTaskControllerSubscription} using an empty permission on {@link Topic} </li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li> 
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingAComplexTaskToAnEmptyTopicShouldThrowAnException() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        baboonConfig.getTopicByName(topicNamesDefined[4]).getPermission().clear();
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I created a {@link ComplexSecuentialTaskControllerSubscription} with a name </li>
     * <li> When I try to create another {@link ComplexSecuentialTaskControllerSubscription} using the same name </li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li> 
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingMoreThanOneComplexTaskWithTheSameNameShouldThrowAnException() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath);
        String complexTaskName = "complexFooTask";
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[4]);
        baboonConfig.createNewComplexTaskController(complexTaskName, topicNamesDefined[0]);
        fail("Exception should have been thrown before this point");
    }
    
    /**
     * <li> Given I have a  {@link BaboonConfig} object </li>
     * <li> When I try to append an {@link ActionController} to a non existing complex task </li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li> 
     */
    @Test (expected=NotSubscribableException.class)
    public void appendingControllerToNonExistingComplexTaskSubscriptionShouldThrowAnException() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final BaboonConfig baboonConfig = new BaboonConfig();
        final MockUserSystemObject mockUserSystemObject = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        baboonConfig.appendControllerToComplexTaskController("NonExistingComplexTask", mockUserSystemObject, taskMethod);
        fail("Exception should have been thrown before this point");
        

    }
}
