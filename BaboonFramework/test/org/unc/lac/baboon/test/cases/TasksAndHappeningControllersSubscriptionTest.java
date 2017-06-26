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
import org.unc.lac.baboon.test.utils.tasks.MockController;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;

public class TasksAndHappeningControllersSubscriptionTest {
    private final String topicsPath02 = "/org/unc/lac/baboon/test/resources/topics02.json";
    private final String topicsPath03 = "/org/unc/lac/baboon/test/resources/topics03.json";
    private final String[] topicNamesDefined = { "topic1", "topic2", "topic3", "topic4" };

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
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
    public void subscribingAnExistingHappeningControllerToAnExistingTopicShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[0], mockController, happeningControllerMethod);
            Method testMethod = MethodDictionary.getMethod(mockController, happeningControllerMethod);
            Pair<Object, Method> testKey = new Pair<Object, Method>(mockController, testMethod);
            assertEquals(1, baboonConfig.getHappeningController(testKey).getSize());
            assertEquals(mockController, baboonConfig.getHappeningController(testKey).getAction().getActionObject());
            assertEquals(topicNamesDefined[0], baboonConfig.getHappeningController(testKey).getTopic().getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the {@link TaskController}
     * annotated method to a {@link Topic}</li>
     * <li>Then the {@link TaskSubscription} subscriptions Map should contain a
     * {@link TaskSubscription} with the object instance and the method
     * subscribed as a map's key</li>
     * <li>And the {@link TaskSubscription} subscriptions Map should contain the
     * {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingAnExistingTaskToAnExistingTopicShouldShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[0], mockController, taskMethod);
            List<SimpleTaskControllerSubscription> tasksList = (List<SimpleTaskControllerSubscription>) baboonConfig.getSimpleTasksCollection();
            assertEquals(1, tasksList.size());
            assertEquals(1,tasksList.get(0).getSize());
            assertEquals(mockController,tasksList.get(0).getAction(0).getActionObject());
            assertEquals(topicNamesDefined[0], tasksList.get(0).getTopic().getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test
    public void subscribingAMethodThatIsNotAnnotatedToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String method = "mockNotSubscribableMethod";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a null method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test
    public void subscribingANullMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController,null);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a method name that
     * does not exists on the object to a {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * for each subscription</li>
     */
    @Test
    public void subscribingANotExistingMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController, "methodNotExistingOnClass");
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a not
     * existing {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingToANotExistingTopicShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic("notExistingTopicName", mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
        try {
            baboonConfig.subscribeControllerToTopic(null, mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a null object and a string as name of method to an
     * existing {@link Topic} name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingNullObjectToExistingTopicShouldNotGetRegisteredInConfigTest() {
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[0], null, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link HappeningController}</li>
     * <li>And the instance of controller object also has a method annotated
     * with {@link TaskController}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe more than one {@link TaskSubscription} or
     * {@link HappeningControllerSubscription} to the same {@link Topic} in any
     * combinations</li>
     * <li>Then all the {@link HappeningControllerSubscription} and
     * {@link TaskSubscription} objects should be subscribed
     */
    @Test
    public void subscribingMoreThanOneHappeningControllerOrTaskToTheSameTopicShouldBePossibleTest() {
        final MockController mockController = new MockController();
        final String happeningControllerMethod = "mockHappeningController";
        final String taskMethod = "mockTask";
        final String happeningControllerMethod2 = "mockHappeningController2";
        final String taskMethod2 = "mockTask2";

        // Subscribing HappeningController and then TaskController should be possible
        BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, happeningControllerMethod);
            assertEquals(1, baboonConfig.getHappeningControllerCount());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing TaskController and then HappeningController should be possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, taskMethod2);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, happeningControllerMethod2);
            assertEquals(1, baboonConfig.getHappeningControllerCount());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing TaskController and then TaskController should should be possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, taskMethod2);
            assertEquals(2, baboonConfig.getSimpleTasksCollection().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing HappeningController and then HappeningController should be
        // possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, happeningControllerMethod2);
            assertEquals(1, baboonConfig.getHappeningControllerCount());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, happeningControllerMethod);
            assertEquals(2, baboonConfig.getHappeningControllerCount());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskSubscription} or
     * {@link HappeningControllerSubscription} to more than one topic</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingSameHappeningControllerToMoreThanOneTopicShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController, happeningControllerMethod);
            assertEquals(1, baboonConfig.getHappeningControllerCount());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, happeningControllerMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }
    
    
    @Test
    public void subscribingSameTaskToMoreThanOneTopicShouldGetRegisteredInConfig() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
            try {
                baboonConfig.subscribeControllerToTopic(topicNamesDefined[0], mockController, taskMethod);
                assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
                baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController, taskMethod);
                assertEquals(2, baboonConfig.getSimpleTasksCollection().size());
            } catch (NotSubscribableException e) {
                fail(e.getMessage());
            }
        
    }
    
    

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And topic1 has an empty permission string</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskSubscription} to topic1</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingTaskToTopicWithEmptyStringPermissionShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertEquals("", baboonConfig.getTopicByName(topicNamesDefined[0]).getPermission().get(0));
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[0], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic2"</li>
     * <li>And topic2 has not the permission field</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskSubscription} to topic2</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingTaskToTopicWithEmptyPermissionArrayShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String taskMethod = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[1]).getPermission().isEmpty());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
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
    public void subscribingHappeningControllerToTopicWithEmptyPermissionStringShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertEquals("", baboonConfig.getTopicByName(topicNamesDefined[0]).getPermission().get(0));
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[0], mockController, happeningControllerMethod);
            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
                    MethodDictionary.getMethod(mockController, happeningControllerMethod));
            HappeningControllerSubscription happeningController = baboonConfig.getHappeningController(testHHOKey);
            assertEquals(1, baboonConfig.getHappeningControllerCount());
            assertEquals(1,happeningController.getSize());
            assertEquals(mockController, happeningController.getAction().getActionObject());
            assertEquals(topicNamesDefined[0], happeningController.getTopic().getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
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
    public void subscribingHappeningControllerToTopicWithEmptyPermissionArrayShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[1]).getPermission().isEmpty());
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[1], mockController, happeningControllerMethod);
            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
                    MethodDictionary.getMethod(mockController, happeningControllerMethod));
            HappeningControllerSubscription happeningController = baboonConfig.getHappeningController(testHHOKey);
            assertEquals(1, baboonConfig.getHappeningControllerCount());
            assertEquals(1,happeningController.getSize());
            assertEquals(mockController, happeningController.getAction().getActionObject());
            assertEquals(topicNamesDefined[1], happeningController.getTopic().getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic3"</li>
     * <li>And topic3 has a set_guard_callback {{["g1","g2"]}}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the same controller has a method that returns a boolean and
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
    public void subscribingAbstractTaskWithGuardProvidersToTopicWithGuardCallbackShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningControllerMethod = "mockHappeningController";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            List<String>guardCallBack = Arrays.asList(baboonConfig.getTopicByName(topicNamesDefined[2]).getSetGuardCallback().get(0)); 
                    
            assertTrue(guardCallBack.contains("g1"));
            assertTrue(guardCallBack.contains("g2"));
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[2], mockController, happeningControllerMethod);
            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
                    MethodDictionary.getMethod(mockController, happeningControllerMethod));
            assertEquals(1, baboonConfig.getHappeningControllerCount());
            assertEquals(mockController,baboonConfig.getHappeningController(testHHOKey).getAction().getActionObject());
            try {
                assertFalse(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g1"));
                mockController.setGuard1Value(true);
                assertTrue(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g1"));
                assertFalse(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g2"));
                mockController.setGuard2Value(true);
                assertTrue(baboonConfig.getHappeningController(testHHOKey).getAction().getGuardValue("g2"));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                fail(e.getMessage());
            }
            assertEquals(topicNamesDefined[2], baboonConfig.getHappeningController(testHHOKey).getTopic().getName());
        } catch (NotSubscribableException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic4"</li>
     * <li>And topic4 has a set_guard_callback {{["g1","g3"]}}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the controller has not a method annotated with
     * {@link GuardProvider#value()} "g3"</li>
     * <li>When I subscribe a {@link HappeningControllerSubscription} to
     * topic4</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingAnAbstractTaskWithMissingGuardProviderToTopicWithGuardCallbackShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod = "mockTask";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            List<String>guardCallBack = Arrays.asList(baboonConfig.getTopicByName(topicNamesDefined[3]).getSetGuardCallback().get(0));
            assertTrue(guardCallBack.contains("g1"));
            assertTrue(guardCallBack.contains("g3"));
            baboonConfig.subscribeControllerToTopic(topicNamesDefined[3], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

}
