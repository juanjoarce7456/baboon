package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;
import org.junit.Test;
import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.main.BaboonConfig;
import org.unc.lac.baboon.task.HappeningHandlerSubscription;
import org.unc.lac.baboon.task.SimpleTaskSubscription;
import org.unc.lac.baboon.test.utils.tasks.MockController;
import org.unc.lac.baboon.test.utils.tasks.MockController2;
import org.unc.lac.baboon.test.utils.tasks.MockController3;
import org.unc.lac.baboon.test.utils.tasks.MockController4;
import org.unc.lac.baboon.test.utils.tasks.MockController5;
import org.unc.lac.baboon.test.utils.tasks.parameters.AbstractParameter;
import org.unc.lac.baboon.test.utils.tasks.parameters.IncorrectParameter;
import org.unc.lac.baboon.test.utils.tasks.parameters.Parameter;
import org.unc.lac.baboon.test.utils.tasks.parameters.Parameter2;
import org.unc.lac.baboon.test.utils.tasks.parameters.Parameter3;
import org.unc.lac.baboon.test.utils.tasks.parameters.Testable;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;

public class BaboonConfigSubscriptionsTest {
    private final String topicsPath02 = "test/org/unc/lac/baboon/test/resources/topics02.json";
    private final String topicsPath03 = "test/org/unc/lac/baboon/test/resources/topics03.json";
    private final String topicsPath04 = "test/org/unc/lac/baboon/test/resources/topics04.json";
    private final String[] topicNamesDefined = { "topic1", "topic2", "topic3", "topic4", "topic5", "topic6", "topic7",
            "topic8", "topic9" };

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link HappeningHandler}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the
     * {@link HappeningHandler} annotated method to a {@link Topic}</li>
     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
     * should contain a {@link HappeningHandlerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingAnExistingHappeningHandlerToAnExistingTopicShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, happeningHandlerMethod);
            Method testMethod = MethodDictionary.getMethod(mockController, happeningHandlerMethod);
            Pair<Object, Method> testKey = new Pair<Object, Method>(mockController, testMethod);
            assertEquals(1, baboonConfig.getHappeningHandler(testKey).getSize());
            assertEquals(mockController, baboonConfig.getHappeningHandler(testKey).getAction().getActionObject());
            assertEquals(topicNamesDefined[0], baboonConfig.getHappeningHandler(testKey).getTopic().getName());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (SecurityException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the {@link Task}
     * annotated method to a {@link Topic}</li>
     * <li>Then the {@link SimpleTaskSubscription} list should contain a
     * {@link SimpleTaskSubscription} with the object instance and the method
     * subscribed</li>
     * <li>And the {@link SimpleTaskSubscription} should contain the
     * {@link Topic}</li>
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
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
            List<SimpleTaskSubscription> tasksList = (List<SimpleTaskSubscription>) baboonConfig
                    .getSimpleTasksCollection();
            assertEquals(1, tasksList.size());
            assertEquals(1, tasksList.get(0).getSize());
            assertEquals(mockController, tasksList.get(0).getAction(0).getActionObject());
            assertEquals(topicNamesDefined[0], tasksList.get(0).getTopic().getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method that is not
     * annotated</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
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
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a null method to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
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
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, null);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and a method name that
     * does not exists on the object to a {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
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
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, "methodNotExistingOnClass");
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object and the method to a not
     * existing {@link Topic} name (or null topic name)</li>
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
            baboonConfig.subscribeToTopic("notExistingTopicName", mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
        try {
            baboonConfig.subscribeToTopic(null, mockController, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
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
            baboonConfig.subscribeToTopic(topicNamesDefined[0], null, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link HappeningHandler}</li>
     * <li>And the instance of controller object also has a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe more than one {@link TaskSubscription} or
     * {@link HappeningHandlerSubscription} to the same {@link Topic} in any
     * combinations</li>
     * <li>Then all the {@link HappeningHandlerSubscription} and
     * {@link TaskSubscription} objects should be subscribed
     */
    @Test
    public void subscribingMoreThanOneHappeningHandlerOrTaskToTheSameTopicShouldBePossibleTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final String taskMethod = "mockTask";
        final String happeningHandlerMethod2 = "mockHappeningHandler2";
        final String taskMethod2 = "mockTask2";

        // Subscribing HappeningHandler and then Task should be possible
        BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing Task and then HappeningHandler should be possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod2);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod2);
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing Task and then Task should should be possible
        baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod2);
            assertEquals(2, baboonConfig.getSimpleTasksCollection().size());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }

        // Subscribing HappeningHandler and then HappeningHandler should be
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
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod2);
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            assertEquals(2, baboonConfig.getHappeningHandlerCount());
        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link HappeningHandlerSubscription} to more than
     * one topic</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void subscribingSameHappeningHandlerToMoreThanOneTopicShouldNotBePossibleTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, happeningHandlerMethod);
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe a {@link TaskSubscription} to more than one
     * topic</li>
     * <li>Then the {@link TaskSubscription} should be subscribed</li>
     */
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
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
            assertEquals(1, baboonConfig.getSimpleTasksCollection().size());
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, taskMethod);
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
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
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
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, taskMethod);
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
     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
     * topic1</li>
     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
     * should contain a {@link HappeningHandlerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingHappeningHandlerToTopicWithEmptyPermissionStringShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
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
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, happeningHandlerMethod);
            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            HappeningHandlerSubscription happeningHandler = baboonConfig.getHappeningHandler(testHHOKey);
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
            assertEquals(1, happeningHandler.getSize());
            assertEquals(mockController, happeningHandler.getAction().getActionObject());
            assertEquals(topicNamesDefined[0], happeningHandler.getTopic().getName());
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
     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
     * topic2</li>
     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
     * should contain a {@link HappeningHandlerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     */
    @Test
    public void subscribingHappeningHandlerToTopicWithEmptyPermissionArrayShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
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
            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, happeningHandlerMethod);
            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            HappeningHandlerSubscription happeningHandler = baboonConfig.getHappeningHandler(testHHOKey);
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
            assertEquals(1, happeningHandler.getSize());
            assertEquals(mockController, happeningHandler.getAction().getActionObject());
            assertEquals(topicNamesDefined[1], happeningHandler.getTopic().getName());
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
     * <li>And topic3 has a setGuardCallback [["g1","g2"]]</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the same controller has a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g2"</li>
     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
     * topic3</li>
     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
     * should contain a {@link HappeningHandlerSubscription} with the object
     * instance and the method subscribed as a map's key</li>
     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
     * contain the {@link Topic} as value for the key</li>
     * <li>And {@link HappeningHandlerSubscription#getGuardCallback(String)}
     * should return a guard callback method with for "g1"</li> *
     * <li>And {@link HappeningHandlerSubscription#getGuardCallback(String)}
     * should return a guard callback method with for "g2"</li>
     */
    @Test
    public void subscribingAbstractTaskWithGuardProvidersToTopicWithGuardCallbackShouldGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final String happeningHandlerMethod = "mockHappeningHandler";
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            List<String> guardCallBack = Arrays
                    .asList(baboonConfig.getTopicByName(topicNamesDefined[2]).getSetGuardCallback().get(0));

            assertTrue(guardCallBack.contains("g1"));
            assertTrue(guardCallBack.contains("g2"));
            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
            assertEquals(1, baboonConfig.getHappeningHandlerCount());
            assertEquals(mockController, baboonConfig.getHappeningHandler(testHHOKey).getAction().getActionObject());
            try {
                assertFalse(baboonConfig.getHappeningHandler(testHHOKey).getAction().getGuardValue("g1"));
                mockController.setGuard1Value(true);
                assertTrue(baboonConfig.getHappeningHandler(testHHOKey).getAction().getGuardValue("g1"));
                assertFalse(baboonConfig.getHappeningHandler(testHHOKey).getAction().getGuardValue("g2"));
                mockController.setGuard2Value(true);
                assertTrue(baboonConfig.getHappeningHandler(testHHOKey).getAction().getGuardValue("g2"));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                fail(e.getMessage());
            }
            assertEquals(topicNamesDefined[2], baboonConfig.getHappeningHandler(testHHOKey).getTopic().getName());
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
     * <li>And topic4 has a setGuardCallback [["g1","g3"]]</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the controller has not a method annotated with
     * {@link GuardProvider#value()} "g3"</li>
     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
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
            List<String> guardCallBack = Arrays
                    .asList(baboonConfig.getTopicByName(topicNamesDefined[3]).getSetGuardCallback().get(0));
            assertTrue(guardCallBack.contains("g1"));
            assertTrue(guardCallBack.contains("g3"));
            baboonConfig.subscribeToTopic(topicNamesDefined[3], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic5"</li>
     * <li>And topic5 has a set_guard_callback [["g1","g3"],[],["g2"]]</li>
     * <li>And topic5 has a permission ["t1,"t2"]</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I have a controller with a method that returns a boolean and
     * requires no parameters annotated with {@link GuardProvider#value()}
     * "g1"</li>
     * <li>And the controller has a method that returns a boolean and requires
     * no parameters annotated with {@link GuardProvider#value()} "g2"</li> *
     * <li>And the controller has a method that returns a boolean and requires
     * no parameters annotated with {@link GuardProvider#value()} "g3"</li>
     * <li>When I subscribe a {@link HappeningHandlerSubscription} or
     * {@link SimpleTaskSubscription} to topic5</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that permission and setGuardCallback are of different
     * sizes</li>
     */
    @Test
    public void subscribingToATopicWithDifferentPermissionAndSetGuardCallbackSizesShouldNotBePossibleTest() {
        final MockController2 mockController = new MockController2();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod = "mockTask";
        final String happeningHandlerMethod = "mockHappeningHandler";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        List<String> guardCallBack = Arrays
                .asList(baboonConfig.getTopicByName(topicNamesDefined[4]).getSetGuardCallback().get(0));
        assertEquals(2, guardCallBack.size());
        assertTrue(guardCallBack.contains("g1"));
        assertTrue(guardCallBack.contains("g3"));
        guardCallBack = Arrays.asList(baboonConfig.getTopicByName(topicNamesDefined[4]).getSetGuardCallback().get(1));
        assertTrue(guardCallBack.isEmpty());
        guardCallBack = Arrays.asList(baboonConfig.getTopicByName(topicNamesDefined[4]).getSetGuardCallback().get(2));
        assertEquals(1, guardCallBack.size());
        assertTrue(guardCallBack.contains("g2"));

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[4], mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }

        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[4], mockController, happeningHandlerMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task} that takes a parameter of type {@link Parameter}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object, the {@link Task}
     * annotated method and an instance of {@link Parameter} to a
     * {@link Topic}</li>
     * <li>Then the {@link SimpleTaskSubscription} list should contain a
     * {@link SimpleTaskSubscription} with the object instance and the method
     * subscribed</li>
     * <li>And the {@link SimpleTaskSubscription} should contain the
     * {@link Topic}</li>
     */
    @Test
    public void subscribingTaskMethodWithParameterOfTheClassRequiredShouldGetRegisteredInConfigTest() {
        final MockController2 mockController = new MockController2();
        final String taskMethod = "mockTask2";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final Parameter parameter = new Parameter();
        try {
            baboonConfig.addTopics(topicsPath04);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod, parameter);
            List<SimpleTaskSubscription> tasksList = (List<SimpleTaskSubscription>) baboonConfig
                    .getSimpleTasksCollection();
            assertEquals(1, tasksList.size());
            assertEquals(1, tasksList.get(0).getSize());
            assertEquals(mockController, tasksList.get(0).getAction(0).getActionObject());
            assertEquals(topicNamesDefined[0], tasksList.get(0).getTopic().getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task} that takes a parameter of type {@link Parameter}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object, the {@link Task}
     * annotated method and an instance of {@link IncorrectParameter} to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that a method with the given characteristics cannot be
     * found</li>
     */
    @Test
    public void subscribingTaskMethodWithIncorrectParameterClassShouldNotBePossibleTest() {
        final MockController2 mockController = new MockController2();
        final String taskMethod = "mockTask2";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final IncorrectParameter parameter = new IncorrectParameter();
        try {
            baboonConfig.addTopics(topicsPath04);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod, parameter);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task} that takes a parameter of type {@link Testable}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object, the {@link Task}
     * annotated method and an instance of {@link Parameter} to a
     * {@link Topic}</li>
     * <li>Then the {@link SimpleTaskSubscription} list should contain a
     * {@link SimpleTaskSubscription} with the object instance and the method
     * subscribed</li>
     * <li>And the {@link SimpleTaskSubscription} should contain the
     * {@link Topic}</li>
     */
    @Test
    public void subscribingTaskMethodWithParameterOfAClassImplementingTheRequiredInterfaceShouldGetRegisteredInConfigTest() {
        final MockController2 mockController = new MockController2();
        final String taskMethod = "mockTask3";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final Testable parameter = new Parameter();
        try {
            baboonConfig.addTopics(topicsPath04);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod, parameter);
            List<SimpleTaskSubscription> tasksList = (List<SimpleTaskSubscription>) baboonConfig
                    .getSimpleTasksCollection();
            assertEquals(1, tasksList.size());
            assertEquals(1, tasksList.get(0).getSize());
            assertEquals(mockController, tasksList.get(0).getAction(0).getActionObject());
            assertEquals(topicNamesDefined[0], tasksList.get(0).getTopic().getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task} that takes a parameter of type
     * {@link AbstractParameter}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object, the {@link Task}
     * annotated method and an instance of {@link Parameter2} to a
     * {@link Topic}</li>
     * <li>Then the {@link SimpleTaskSubscription} list should contain a
     * {@link SimpleTaskSubscription} with the object instance and the method
     * subscribed</li>
     * <li>And the {@link SimpleTaskSubscription} should contain the
     * {@link Topic}</li>
     */
    @Test
    public void subscribingTaskMethodWithParameterOfAClassImplementingTheRequiredAbstractClassShouldGetRegisteredInConfigTest() {
        final MockController2 mockController = new MockController2();
        final String taskMethod = "mockTask4";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final AbstractParameter parameter = new Parameter2();
        try {
            baboonConfig.addTopics(topicsPath04);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod, parameter);
            List<SimpleTaskSubscription> tasksList = (List<SimpleTaskSubscription>) baboonConfig
                    .getSimpleTasksCollection();
            assertEquals(1, tasksList.size());
            assertEquals(1, tasksList.get(0).getSize());
            assertEquals(mockController, tasksList.get(0).getAction(0).getActionObject());
            assertEquals(topicNamesDefined[0], tasksList.get(0).getTopic().getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task} that takes one parameter of type {@link Parameter}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object, the {@link Task}
     * annotated method and two instances of {@link Parameter} to a
     * {@link Topic}</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that a method with the given characteristics cannot be
     * found</li>
     */
    @Test
    public void subscribingTaskMethodWithIncorrectNumberOfParametersShouldNotBePossibleTest() {
        final MockController2 mockController = new MockController2();
        final String taskMethod = "mockTask2";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final Parameter parameter1 = new Parameter();
        final Parameter parameter2 = new Parameter();
        try {
            baboonConfig.addTopics(topicsPath04);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod, parameter1, parameter2);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task} that takes three parameters of type {@link Parameter},
     * {@link Parameter2} and {@link Parameter3}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I subscribe the instance of the object, the {@link Task}
     * annotated method and three objects of class {@link Parameter},
     * {@link Parameter2} and {@link Parameter3} (in that order) to a
     * {@link Topic}</li>
     * <li>Then the {@link SimpleTaskSubscription} list should contain a
     * {@link SimpleTaskSubscription} with the object instance and the method
     * subscribed</li>
     * <li>And the {@link SimpleTaskSubscription} should contain the
     * {@link Topic}</li>
     */
    @Test
    public void subscribingTaskMethodWithMultipleParametersShouldGetRegisteredInConfigTest() {
        final MockController2 mockController = new MockController2();
        final String taskMethod = "mockTask5";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final Parameter parameter = new Parameter();
        final Parameter2 parameter2 = new Parameter2();
        final Parameter3 parameter3 = new Parameter3();
        try {
            baboonConfig.addTopics(topicsPath04);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod, parameter, parameter2,
                    parameter3);
            List<SimpleTaskSubscription> tasksList = (List<SimpleTaskSubscription>) baboonConfig
                    .getSimpleTasksCollection();
            assertEquals(1, tasksList.size());
            assertEquals(1, tasksList.get(0).getSize());
            assertEquals(mockController, tasksList.get(0).getAction(0).getActionObject());
            assertEquals(topicNamesDefined[0], tasksList.get(0).getTopic().getName());

        } catch (NotSubscribableException e) {
            fail(e.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex task using a {@link Topic} name that does
     * not exist (or null topic name)</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void creatingComplexTaskUsingANotExistingTopicShouldNotGetRegisteredInConfigTest() {
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.createNewComplexTask("complexTask1", "notExistingTopicName");
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
        try {
            baboonConfig.createNewComplexTask("complexTask1", null);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex task using an empty (or null) complex
     * task name and a valid topic name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void creatingComplexTaskUsingAnEmptyTaskNameShouldNotGetRegisteredInConfigTest() {
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.createNewComplexTask("", topicNamesDefined[0]);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
        try {
            baboonConfig.createNewComplexTask(null, topicNamesDefined[0]);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I have an instance of controller object with a method annotated
     * with {@link Task}</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I create a new complex task using a complex task name and
     * "topic1"</li>
     * <li>When I try to create a new complex task using the same complex task
     * name and a valid topic name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void creatingComplexTaskUsingAnAlreadyRegisteredTaskNameShouldNotGetRegisteredInConfigTest() {
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.createNewComplexTask("task1", topicNamesDefined[0]);
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask("task1", topicNamesDefined[0]);
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
     * <li>When I try to create a new complex task using topic2</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void creatingComplexTaskUsingATopicWithEmptyPermissionArrayShouldNotBePossibleTest() {
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
            baboonConfig.createNewComplexTask("task1", topicNamesDefined[1]);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic5"</li>
     * <li>And topic5 has a set_guard_callback [["g1","g3"],[],["g2"]]</li>
     * <li>And topic5 has a permission ["t1,"t2"]</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I try to create a new complex task using topic5</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that permission and setGuardCallback are of different
     * sizes</li>
     */
    @Test
    public void creatingComplexTaskUsingATopicWithDifferentPermissionAndSetGuardCallbackSizesShouldNotBePossibleTest() {
        final BaboonConfig baboonConfig = new BaboonConfig();
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        List<String> guardCallBack = Arrays
                .asList(baboonConfig.getTopicByName(topicNamesDefined[4]).getSetGuardCallback().get(0));
        assertEquals(2, guardCallBack.size());
        assertTrue(guardCallBack.contains("g1"));
        assertTrue(guardCallBack.contains("g3"));
        guardCallBack = Arrays.asList(baboonConfig.getTopicByName(topicNamesDefined[4]).getSetGuardCallback().get(1));
        assertTrue(guardCallBack.isEmpty());
        guardCallBack = Arrays.asList(baboonConfig.getTopicByName(topicNamesDefined[4]).getSetGuardCallback().get(2));
        assertEquals(1, guardCallBack.size());
        assertTrue(guardCallBack.contains("g2"));

        try {
            baboonConfig.createNewComplexTask("task1", topicNamesDefined[4]);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic1"</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I create a new complex task using a complex task name and
     * "topic1"</li>
     * <li>Then the number of complexTasks in ComplexSecuentialTasksCollection
     * should be one</li>
     * <li>And the name of the topic of the ComplexSecuentialTask should be
     * "topic1"</li>
     */
    @Test
    public void creatingComplexTaskUsingAValidTopicAndNameShouldGetRegisteredInConfig() {
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[0]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[0], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a null method</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void appendingATaskToAComplexTaskWithANullMethodNameShouldNotGetRegisteredInConfigTest() {
        final BaboonConfig baboonConfig = new BaboonConfig();
        final MockController mockController = new MockController();
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[1]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[1], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, null);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with a null object and a
     * string as name of method</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void appendingATaskToAComplexTaskWithNullObjectShouldNotGetRegisteredInConfigTest() {
        final String method = "mockTask";
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[0]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[0], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, null, method);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a method name that does not exists on the object</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void appendingATaskToAComplexTaskWithANotExistingMethodNameShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[1]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[1], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, "methodNotExistingOnClass");
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic6"</li>
     * <li>And "topic6" has a permission list of 3 transitions</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append 4 tasks to a complex task with the instance of the
     * object and a method name</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void appendingMoreTasksThanAvailablePermissionsInTopicToAComplexTaskShouldNotBePossibleTest() {
        final MockController2 mockController = new MockController2();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod = "mockTask";
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[5]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[5], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            assertEquals(1, baboonConfig.getComplexSecuentialTask(taskName).getSize());
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            assertEquals(2, baboonConfig.getComplexSecuentialTask(taskName).getSize());
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            assertEquals(3, baboonConfig.getComplexSecuentialTask(taskName).getSize());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic6"</li>
     * <li>And "topic6" has a permission list of 3 transitions</li>
     * <li>And I have two instances of controller objects</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append 3 tasks to a complex task using the instances of the
     * objects, method names and parameters</li>
     * <li>Then the size of the complex task should be 3</li>
     */
    @Test
    public void appendingMultipleTasksToAComplexTaskShouldGetRegisteredInConfigTest() {
        final MockController mockController1 = new MockController();
        final MockController2 mockController2 = new MockController2();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod1 = "mockTask";
        final String taskMethod2 = "mockTask2";
        final String taskMethod3 = "mockTask5";
        final Parameter parameter = new Parameter();
        final Parameter2 parameter2 = new Parameter2();
        final Parameter3 parameter3 = new Parameter3();
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[5]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[5], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController1, taskMethod1);
            assertEquals(1, baboonConfig.getComplexSecuentialTask(taskName).getSize());
            baboonConfig.appendTaskToComplexTask(taskName, mockController2, taskMethod2, parameter);
            assertEquals(2, baboonConfig.getComplexSecuentialTask(taskName).getSize());
            baboonConfig.appendTaskToComplexTask(taskName, mockController2, taskMethod3, parameter, parameter2,
                    parameter3);
            assertEquals(3, baboonConfig.getComplexSecuentialTask(taskName).getSize());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a method that is annotated with {@link HappeningHandler}</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void appendingATaskToAComplexTaskWithAHappeningHandlerAnnotatedMethodShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        final String happeningHandlerMethod = "mockHappeningHandler";
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[1]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[1], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, happeningHandlerMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file</li>
     * <li>And I have an instance of controller object</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a method that is not annotated</li>
     * <li>Then a {@link NotSubscribableException} exception should be
     * thrown</li>
     */
    @Test
    public void appendingATaskToAComplexTaskWithANotAnnotatedMethodShouldNotGetRegisteredInConfigTest() {
        final MockController mockController = new MockController();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        final String notAnnotatedMethod = "setGuard1Value";
        try {
            baboonConfig.addTopics(topicsPath02);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[1]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[1], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, notAnnotatedMethod, true);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic7"</li>
     * <li>And "topic7" has a permission list ["t1","","t3"]</li>
     * <li>And I have two instances of controller objects</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I try to append 2 tasks to a complex task using the instances of
     * the objects, method names and parameters</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that permission for the second task is empty</li>
     */
    @Test
    public void appendingTaskToAComplexTaskWithEmptyPermissionForThisTaskShouldNotBePossibleTest() {
        final MockController mockController1 = new MockController();
        final MockController2 mockController2 = new MockController2();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod1 = "mockTask";
        final String taskMethod2 = "mockTask2";
        final Parameter parameter = new Parameter();
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[6]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[6], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController1, taskMethod1);
            assertEquals(1, baboonConfig.getComplexSecuentialTask(taskName).getSize());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }
        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController2, taskMethod2, parameter);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic8"</li>
     * <li>And "topic8" has a permission list ["t1","t2","t3"]</li>
     * <li>And "topic8" has a setGuardCallback list
     * [["g1"],["g4"],["g2","g3"]]</li>
     * <li>And I have an instance of a controller object</li>
     * <li>And this object has GuardProvider methods for guards "g1","g2" and
     * "g3"</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>And I append a task to a complex task using the instance of the
     * object, method names and parameters</li>
     * <li>When I try to append a second task to a complex task using the
     * instance of the object, method names and parameters</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that the object does not have a GuardProvider for guard
     * "g4"</li>
     */
    @Test
    public void appendingTaskToAComplexTaskUsingAnObjectThatMissesAGuardProviderShouldNotBePossibleTest() {
        final MockController2 mockController2 = new MockController2();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskMethod1 = "mockTask";
        final String taskName = "task1";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[7]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[7], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController2, taskMethod1);
            assertEquals(1, baboonConfig.getComplexSecuentialTask(taskName).getSize());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }
        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController2, taskMethod1);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic9"</li>
     * <li>And "topic9" has a permission list ["t1","t2","t3"]</li>
     * <li>And "topic9" has a setGuardCallback list [["g1"],[],[]]</li>
     * <li>And I have an instance of controller object</li>
     * <li>And the object contains a {@link GuardProvider} method for guard "g1"
     * that returns void</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a method</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that declaring a guard provider with returning type other than
     * boolean is invalid</li>
     */
    @Test
    public void appendingATaskToAComplexTaskUsingAnObjectWithAnInvalidGuardProviderMethodReturningTypeShouldNotGetRegisteredInConfigTest() {
        final MockController3 mockController = new MockController3();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        final String taskMethod = "mockTask";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[8]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[8], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic9"</li>
     * <li>And "topic9" has a permission list ["t1","t2","t3"]</li>
     * <li>And "topic9" has a setGuardCallback list [["g1"],[],[]]</li>
     * <li>And I have an instance of controller object</li>
     * <li>And the object contains a {@link GuardProvider} method for guard "g1"
     * that requires parameters on its declaration</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a method</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that a guard provider that requires parameters on its
     * declaration is invalid</li>
     */
    @Test
    public void appendingATaskToAComplexTaskUsingAnObjectWithAGuardProviderMethodThatTakesParametersInItsDeclarationShouldNotGetRegisteredInConfigTest() {
        final MockController4 mockController = new MockController4();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        final String taskMethod = "mockTask";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[8]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[8], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }

    /**
     * <li>Given I have a topics json file containing a topic with name
     * "topic9"</li>
     * <li>And "topic9" has a permission list ["t1","t2","t3"]</li>
     * <li>And "topic9" has a setGuardCallback list [["g1"],[],[]]</li>
     * <li>And I have an instance of controller object</li>
     * <li>And the object contains multiple {@link GuardProvider} method for
     * guard "g1"</li>
     * <li>And I add the topics configuration to the Framework</li>
     * <li>When I append a task to a complex task with the instance of the
     * object and a method</li>
     * <li>Then a {@link NotSubscribableException} exception should be thrown
     * indicating that declaring more than one guard provider for the same
     * guard on the same object is invalid</li>
     */
    @Test
    public void appendingATaskToAComplexTaskUsingAnObjectWithMultipleGuardProviderMethodsForTheSameGuardShouldNotGetRegisteredInConfigTest() {
        final MockController5 mockController = new MockController5();
        final BaboonConfig baboonConfig = new BaboonConfig();
        final String taskName = "task1";
        final String taskMethod = "mockTask";
        try {
            baboonConfig.addTopics(topicsPath03);
        } catch (BadTopicsJsonFormat e) {
            fail(e.getMessage());
        } catch (NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }

        try {
            baboonConfig.createNewComplexTask(taskName, topicNamesDefined[8]);
            assertEquals(1, baboonConfig.getComplexSecuentialTasksCollection().size());
            assertEquals(topicNamesDefined[8], baboonConfig.getComplexSecuentialTask(taskName).getTopic().getName());
        } catch (NotSubscribableException e1) {
            fail(e1.getMessage());
        }

        try {
            baboonConfig.appendTaskToComplexTask(taskName, mockController, taskMethod);
            fail("Exception should have been thrown before this point");
        } catch (Exception e) {
            assertEquals(NotSubscribableException.class, e.getClass());
        }
    }
}
