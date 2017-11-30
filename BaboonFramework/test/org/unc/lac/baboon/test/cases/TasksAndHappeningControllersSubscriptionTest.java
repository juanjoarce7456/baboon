package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;
import org.junit.Test;
import org.unc.lac.baboon.subscription.HappeningControllerSubscription;
import org.unc.lac.baboon.subscription.SimpleTaskControllerSubscription;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.config.BaboonConfig;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;

public class TasksAndHappeningControllersSubscriptionTest {
    private final String topicsPath02 = "/org/unc/lac/baboon/test/resources/topics02.json";
    private final String topicsPath03 = "/org/unc/lac/baboon/test/resources/topics03.json";
    
    private final String TOPIC_1 = "topic1";
    private final String TOPIC_2 = "topic2";
    private final String TOPIC_3 = "topic3";
    private final String TOPIC_4 = "topic4";

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method annotated
     * with {@link HappeningController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the
     * {@link HappeningController} annotated method to a {@link Topic}</li>
     * <li>Then the {@link HappeningControllerSubscription} subscriptions Map
     * should contain a {@link HappeningControllerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningControllerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingAnExistingHappeningControllerToAnExistingTopicShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException, NoSuchMethodException, SecurityException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_1, mockUserSystemObj, happeningControllerMethod);
        Method testMethod = MethodDictionary.getMethod(mockUserSystemObj, happeningControllerMethod);
        Pair<Object, Method> testKey = new Pair<Object, Method>(mockUserSystemObj, testMethod);
        assertEquals(1, baboonConfig.getHappeningController(testKey).getSize());
        assertEquals(mockUserSystemObj, baboonConfig.getHappeningController(testKey).getAction().getActionObject());
        assertEquals(TOPIC_1, baboonConfig.getHappeningController(testKey).getTopic().getName());
        assertEquals(happeningControllerMethod, baboonConfig.getHappeningController(testKey).getAction().getMethodName());
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the {@link TaskController}
     * annotated method to a {@link Topic}</li>
     * <li>Then the {@link SimpleTaskControllerSubscription} subscriptions list should contain a
     * {@link SimpleTaskControllerSubscription} with the object instance, the method
     * subscribed and the {@link Topic}</li>
     */
    @Test
    public void subscribingAnExistingTaskToAnExistingTopicShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_1, mockUserSystemObj, taskMethod);
        List<SimpleTaskControllerSubscription> tasksList = (List<SimpleTaskControllerSubscription>) baboonConfig.getSimpleTasksCollection();
        assertEquals(1, tasksList.size());
        assertEquals(1,tasksList.get(0).getSize());
        assertEquals(mockUserSystemObj,tasksList.get(0).getAction(0).getActionObject());
        assertEquals(TOPIC_1, tasksList.get(0).getTopic().getName());
        assertEquals(taskMethod, tasksList.get(0).getAction(0).getMethodName());
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingAMethodThatIsNotAnnotatedToAnExistingTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String method = "mockNotSubscribableMethod";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj, method);
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a null method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingANullMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj,null);
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a method name that
     * does not exists on the object to a {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>

     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingANotExistingMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj, "methodNotExistingOnClass");
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a not
     * existing {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingToANotExistingTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic("notExistingTopicName", mockUserSystemObj, method);
    }
    
    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a null {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingToANullTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(null, mockUserSystemObj, method);
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a null object and a string as name of method to an
     * existing {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingNullObjectToExistingTopicShouldNotGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_1, null, method);
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of an object of the user's system with a method annotated
     * with {@link HappeningController}</li>
     * <li>And the instance of controller object also has a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe more than one {@link SimpleTaskControllerSubscription} or
     * {@link HappeningControllerSubscription} to the same {@link Topic} in any
     * combinations</li>
     * <li>Then all the {@link HappeningControllerSubscription} and
     * {@link SimpleTaskControllerSubscription} objects should be subscribed
     */
    @Test
    public void subscribingMoreThanOneHappeningControllerOrTaskToTheSameTopicShouldBePossibleTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "mockHappeningController";
        final String taskMethod = "mockTask";
        final String happeningControllerMethod2 = "mockHappeningController2";
        final String taskMethod2 = "mockTask2";

        // Subscribing HappeningController and then TaskController should be possible
        BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, happeningControllerMethod);
        assertEquals(1, baboonConfig.getHappeningControllerCount());
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, taskMethod);
        assertEquals(1, baboonConfig.getSimpleTasksCollection().size());

        // Subscribing TaskController and then HappeningController should be possible
        baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, taskMethod2);
        assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, happeningControllerMethod2);
        assertEquals(1, baboonConfig.getHappeningControllerCount());

        // Subscribing TaskController and then TaskController should should be possible
        baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, taskMethod);
        assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, taskMethod2);
        assertEquals(2, baboonConfig.getSimpleTasksCollection().size());

        // Subscribing HappeningController and then HappeningController should be
        // possible
        baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, happeningControllerMethod2);
        assertEquals(1, baboonConfig.getHappeningControllerCount());
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, happeningControllerMethod);
        assertEquals(2, baboonConfig.getHappeningControllerCount());
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link HappeningControllerSubscription} to more than one topic</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingSameHappeningControllerToMoreThanOneTopicShouldNotBePossibleTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj, happeningControllerMethod);
        assertEquals(1, baboonConfig.getHappeningControllerCount());
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, happeningControllerMethod);
    }
    

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link SimpleTaskControllerSubscription} to more than one topic</li>
     * <li>Then the  {@link SimpleTaskControllerSubscription} should be subscribed.</li>
     */
    @Test
    public void subscribingSameTaskToMoreThanOneTopicShouldGetRegisteredInConfig() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeControllerToTopic(TOPIC_1, mockUserSystemObj, taskMethod);
        assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj, taskMethod);
        assertEquals(2, baboonConfig.getSimpleTasksCollection().size());
        
    }
    
    

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And topic1 has an empty permission string</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link SimpleTaskControllerSubscription} to topic1</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingTaskToTopicWithEmptyStringPermissionShouldNotBePossibleTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath03);
        assertEquals("", baboonConfig.getTopicByName(TOPIC_1).getPermission().get(0));
        baboonConfig.subscribeControllerToTopic(TOPIC_1, mockUserSystemObj, taskMethod);
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic2"</li>
     * <li>And topic2 has not the permission field</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link SimpleTaskControllerSubscription} to topic2</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingTaskToTopicWithEmptyPermissionArrayShouldNotBePossibleTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath03);
        assertTrue(baboonConfig.getTopicByName(TOPIC_2).getPermission().isEmpty());
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj, taskMethod);
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And topic1 has an empty permission string</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link HappeningControllerSubscription} to
     * topic1</li>
     * <li>Then the {@link HappeningControllerSubscription} subscriptions Map
     * should contain a {@link HappeningControllerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningControllerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingHappeningControllerToTopicWithEmptyPermissionStringShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException, NoSuchMethodException, SecurityException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath03);
        assertEquals("", baboonConfig.getTopicByName(TOPIC_1).getPermission().get(0));
        baboonConfig.subscribeControllerToTopic(TOPIC_1, mockUserSystemObj, happeningControllerMethod);
        Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockUserSystemObj,
                MethodDictionary.getMethod(mockUserSystemObj, happeningControllerMethod));
        HappeningControllerSubscription happeningController = baboonConfig.getHappeningController(testHHOKey);
        assertEquals(1, baboonConfig.getHappeningControllerCount());
        assertEquals(1,happeningController.getSize());
        assertEquals(mockUserSystemObj, happeningController.getAction().getActionObject());
        assertEquals(TOPIC_1, happeningController.getTopic().getName());
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic2"</li>
     * <li>And topic2 has not the permission field</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link HappeningControllerSubscription} to
     * topic2</li>
     * <li>Then the {@link HappeningControllerSubscription} subscriptions Map
     * should contain a {@link HappeningControllerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningControllerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingHappeningControllerToTopicWithEmptyPermissionArrayShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException, NoSuchMethodException, SecurityException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath03);
        assertTrue(baboonConfig.getTopicByName(TOPIC_2).getPermission().isEmpty());
        baboonConfig.subscribeControllerToTopic(TOPIC_2, mockUserSystemObj, happeningControllerMethod);
        Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockUserSystemObj,
                MethodDictionary.getMethod(mockUserSystemObj, happeningControllerMethod));
        HappeningControllerSubscription happeningController = baboonConfig.getHappeningController(testHHOKey);
        assertEquals(1, baboonConfig.getHappeningControllerCount());
        assertEquals(1,happeningController.getSize());
        assertEquals(mockUserSystemObj, happeningController.getAction().getActionObject());
        assertEquals(TOPIC_2, happeningController.getTopic().getName());
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic3"</li>
     * <li>And topic3 has a set_guard_callback {{["g1","g2"]}}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have an object of the user's system with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the same user's object has a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g2"</li>
     * <li>When I subscribe a {@link HappeningControllerSubscription} to
     * topic3</li>
     * <li>Then the {@link HappeningControllerSubscription} subscriptions Map
     * should contain a {@link HappeningControllerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningControllerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     * <li>And {@link HappeningControllerSubscription#getGuardCallback(String)}
     * should return a guard callback method with for "g1"</li> *
     * <li>And {@link HappeningControllerSubscription#getGuardCallback(String)}
     * should return a guard callback method with for "g2"</li>
     */
    @Test
    public void subscribingAbstractTaskWithGuardProvidersToTopicWithGuardCallbackShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath03);
        List<String>guardCallBack = Arrays.asList(baboonConfig.getTopicByName(TOPIC_3).getSetGuardCallback().get(0)); 
               
        assertTrue(guardCallBack.contains("g1"));
        assertTrue(guardCallBack.contains("g2"));
        baboonConfig.subscribeControllerToTopic(TOPIC_3, mockUserSystemObj, happeningControllerMethod);
        Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockUserSystemObj,
                MethodDictionary.getMethod(mockUserSystemObj, happeningControllerMethod));
        assertEquals(1, baboonConfig.getHappeningControllerCount());
        assertEquals(mockUserSystemObj,baboonConfig.getHappeningController(testHHOKey).getAction().getActionObject());
        assertFalse(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g1"));
        mockUserSystemObj.setGuard1Value(true);
        assertTrue(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g1"));
        assertFalse(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g2"));
        mockUserSystemObj.setGuard2Value(true);
        assertTrue(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g2"));
        assertEquals(TOPIC_3, baboonConfig.getHappeningController(testHHOKey).getTopic().getName());
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic4"</li>
     * <li>And topic4 has a set_guard_callback {{["g1","g3"]}}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have an object of the user's system with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the controller has not a method annotated with
     * {@link GuardProvider#value()} "g3"</li>
     * <li>When I subscribe a {@link HappeningControllerSubscription} to
     * topic4</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingAnAbstractTaskWithMissingGuardProviderToTopicWithGuardCallbackShouldNotBePossibleTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod = "mockTask";
        baboonConfig.addTopics(topicsPath03);
        List<String>guardCallBack = Arrays.asList(baboonConfig.getTopicByName(TOPIC_4).getSetGuardCallback().get(0));
        assertTrue(guardCallBack.contains("g1"));
        assertTrue(guardCallBack.contains("g3"));
        baboonConfig.subscribeControllerToTopic(TOPIC_4, mockUserSystemObj, taskMethod);
    }
    
    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have a class of an object of the user's system with a static method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the class of the object and the {@link TaskController}
     * annotated method to a {@link Topic} using {@link BaboonConfig#subscribeStaticControllerToTopic(String, Class, String, Object...)} </li>
     * <li>Then the {@link SimpleTaskControllerSubscription} subscriptions list should contain a
     * {@link SimpleTaskControllerSubscription} with the object class, the method
     * subscribed and the {@link Topic}</li>
     */
    @Test
    public void subscribingAStaticTaskControllerToAnExistingTopicShouldGetRegisteredInConfigTest() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final String taskMethod = "staticMockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeStaticControllerToTopic(TOPIC_1, MockUserSystemObject.class, taskMethod);
        List<SimpleTaskControllerSubscription> tasksList = (List<SimpleTaskControllerSubscription>) baboonConfig.getSimpleTasksCollection();
        assertEquals(1, tasksList.size());
        assertEquals(1,tasksList.get(0).getSize());
        assertEquals(MockUserSystemObject.class,tasksList.get(0).getAction(0).getActionObject());
        assertEquals(TOPIC_1, tasksList.get(0).getTopic().getName());
        assertEquals(taskMethod, tasksList.get(0).getAction(0).getMethodName());
    }
    
    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have a class of an object of the user's system with a non-static method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the class of the object and the non-static {@link TaskController} to a {@link Topic} using {@link BaboonConfig#subscribeStaticControllerToTopic(String, Class, String, Object...)} </li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingANonStaticTaskControllerUsingStaticSubscriptionInterfaceShouldNotBeAllowed() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeStaticControllerToTopic(TOPIC_1, MockUserSystemObject.class, taskMethod);
    }
    
    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have a class of an object of the user's system</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the class of the object and a non-existing method name to a {@link Topic} using {@link BaboonConfig#subscribeStaticControllerToTopic(String, Class, String, Object...)} </li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test (expected=NotSubscribableException.class)
    public void subscribingANonExistingMethodUsingStaticSubscriptionInterfaceShouldNotBeAllowed() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException {
        final BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        baboonConfig.subscribeStaticControllerToTopic(TOPIC_1, MockUserSystemObject.class, "NonExistingMethod");
    }

}
