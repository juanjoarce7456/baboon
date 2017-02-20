//package org.unc.lac.baboon.test.cases;
//import static org.junit.Assert.*;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Map;
//
//import org.javatuples.Pair;
//import org.junit.Test;
//import org.unc.lac.baboon.annotations.GuardProvider;
//import org.unc.lac.baboon.annotations.HappeningHandler;
//import org.unc.lac.baboon.annotations.Task;
//import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
//import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
//import org.unc.lac.baboon.exceptions.NotSubscribableException;
//import org.unc.lac.baboon.main.BaboonConfig;
//import org.unc.lac.baboon.task.AbstractTaskSubscription;
//import org.unc.lac.baboon.task.HappeningHandlerSubscription;
//import org.unc.lac.baboon.task.TaskSubscription;
//import org.unc.lac.baboon.test.utils.tasks.MockController;
//import org.unc.lac.baboon.topic.Topic;
//import org.unc.lac.baboon.utils.MethodDictionary;
//
//public class TasksAndHappeningHandlersSubscriptionTest {
//    private final String topicsPath02 = "test/org/unc/lac/baboon/test/resources/topics02.json";
//    private final String topicsPath03 = "test/org/unc/lac/baboon/test/resources/topics03.json";
//    private final String[] topicNamesDefined = { "topic1", "topic2", "topic3", "topic4" };
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method annotated
//     * with {@link HappeningHandler}</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe the instance of the object and the
//     * {@link HappeningHandler} annotated method to a {@link Topic}</li>
//     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
//     * should contain a {@link HappeningHandlerSubscription} with the object
//     * instance and the method subscribed as a map's key</li>
//     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
//     * contain the {@link Topic} as value for the key</li>
//     */
//    @Test
//    public void subscribingAnExistingHappeningHandlerToAnExistingTopicShouldGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String happeningHandlerMethod = "mockHappeningHandler";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, happeningHandlerMethod);
//            Map<Pair<Object, Method>, AbstractTaskSubscription> subscriptionsMap = baboonConfig.getSubscriptionsMap();
//            Pair<Object, Method> testKey = new Pair<Object, Method>(mockController,
//                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
//            assertEquals(1, subscriptionsMap.size());
//            assertTrue(subscriptionsMap.keySet().contains(testKey));
//            assertEquals(topicNamesDefined[0], subscriptionsMap.get(testKey).getTopic().getName());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        } catch (NoSuchMethodException e) {
//            fail(e.getMessage());
//        } catch (SecurityException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method annotated
//     * with {@link Task}</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe the instance of the object and the {@link Task}
//     * annotated method to a {@link Topic}</li>
//     * <li>Then the {@link TaskSubscription} subscriptions Map should contain a
//     * {@link TaskSubscription} with the object instance and the method
//     * subscribed as a map's key</li>
//     * <li>And the {@link TaskSubscription} subscriptions Map should contain the
//     * {@link Topic} as value for the key</li>
//     */
//    @Test
//    public void subscribingAnExistingTaskToAnExistingTopicShouldShouldGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String taskMethod = "mockTask";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
//            Map<Pair<Object, Method>, AbstractTaskSubscription> subscriptionsMap = baboonConfig.getSubscriptionsMap();
//            Pair<Object, Method> testTOKey = new Pair<Object, Method>(mockController,
//                    MethodDictionary.getMethod(mockController, taskMethod));
//            assertEquals(1, subscriptionsMap.size());
//            assertTrue(subscriptionsMap.keySet().contains(testTOKey));
//            assertEquals(topicNamesDefined[0], subscriptionsMap.get(testTOKey).getTopic().getName());
//
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        } catch (NoSuchMethodException e) {
//            fail(e.getMessage());
//        } catch (SecurityException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method that is not
//     * annotated</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe the instance of the object and the method to a
//     * {@link Topic}</li>
//     * <li>Then a {@link NotSubscribableException} exception should be thrown
//     * for each subscription</li>
//     */
//    @Test
//    public void subscribingAMethodThatIsNotAnnotatedToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String method = "mockNotSubscribableMethod";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, method);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method that is not
//     * annotated</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe the instance of the object and a null method to a
//     * {@link Topic}</li>
//     * <li>Then a {@link NotSubscribableException} exception should be thrown
//     * for each subscription</li>
//     */
//    @Test
//    public void subscribingANullMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, null);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method that is not
//     * annotated</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe the instance of the object and a method name that
//     * does not exists on the object to a {@link Topic}</li>
//     * <li>Then a {@link NotSubscribableException} exception should be thrown
//     * for each subscription</li>
//     */
//    @Test
//    public void subscribingANotExistingMethodNameToAnExistingTopicShouldNotGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, "methodNotExistingOnClass");
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method annotated
//     * with {@link Task}</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe the instance of the object and the method to a not
//     * existing {@link Topic} name</li>
//     * <li>Then a {@link NotSubscribableException} exception should be
//     * thrown</li>
//     */
//    @Test
//    public void subscribingToANotExistingTopicShouldNotGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String method = "mockTask";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic("notExistingTopicName", mockController, method);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//        try {
//            baboonConfig.subscribeToTopic(null, mockController, method);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe a null object and a string as name of method to an
//     * existing {@link Topic} name</li>
//     * <li>Then a {@link NotSubscribableException} exception should be
//     * thrown</li>
//     */
//    @Test
//    public void subscribingNullObjectToExistingTopicShouldNotGetRegisteredInConfigTest() {
//        final String method = "mockTask";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[0], null, method);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I have an instance of controller object with a method annotated
//     * with {@link HappeningHandler}</li>
//     * <li>And the instance of controller object also has a method annotated
//     * with {@link Task}</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe more than one {@link TaskSubscription} or
//     * {@link HappeningHandlerSubscription} to the same {@link Topic} in any
//     * combinations</li>
//     * <li>Then all the {@link HappeningHandlerSubscription} and
//     * {@link TaskSubscription} objects should be subscribed
//     */
//    @Test
//    public void subscribingMoreThanOneHappeningHandlerOrTaskToTheSameTopicShouldBePossibleTest() {
//        final MockController mockController = new MockController();
//        final String happeningHandlerMethod = "mockHappeningHandler";
//        final String taskMethod = "mockTask";
//        final String happeningHandlerMethod2 = "mockHappeningHandler2";
//        final String taskMethod2 = "mockTask2";
//
//        // Subscribing HappeningHandler and then Task should be possible
//        BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
//            assertEquals(1, baboonConfig.getSubscriptionsMap().size());
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod);
//            assertEquals(2, baboonConfig.getSubscriptionsMap().size());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        }
//
//        // Subscribing Task and then HappeningHandler should be possible
//        baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod2);
//            assertEquals(1, baboonConfig.getSubscriptionsMap().size());
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod2);
//            assertEquals(2, baboonConfig.getSubscriptionsMap().size());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        }
//
//        // Subscribing Task and then Task should should be possible
//        baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod);
//            assertEquals(1, baboonConfig.getSubscriptionsMap().size());
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, taskMethod2);
//            assertEquals(2, baboonConfig.getSubscriptionsMap().size());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        }
//
//        // Subscribing HappeningHandler and then HappeningHandler should be
//        // possible
//        baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod2);
//            assertEquals(1, baboonConfig.getSubscriptionsMap().size());
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
//            assertEquals(2, baboonConfig.getSubscriptionsMap().size());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing three topics</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe a {@link TaskSubscription} or
//     * {@link HappeningHandlerSubscription} to more than one topic</li>
//     * <li>Then a {@link NotSubscribableException} exception should be
//     * thrown</li>
//     */
//    @Test
//    public void subscribingSameTaskOrHappeningHandlerToMoreThanOneTopicShouldNotBePossibleTest() {
//        final MockController mockController = new MockController();
//        final String happeningHandlerMethod = "mockHappeningHandler";
//        final String taskMethod = "mockTask";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath02);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
//            assertEquals(1, baboonConfig.getSubscriptionsMap().size());
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, taskMethod);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//        try {
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, happeningHandlerMethod);
//            assertEquals(2, baboonConfig.getSubscriptionsMap().size());
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing a topic with name
//     * "topic1"</li>
//     * <li>And topic1 has an empty permission string</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe a {@link TaskSubscription} to topic1</li>
//     * <li>Then a {@link NotSubscribableException} exception should be
//     * thrown</li>
//     */
//    @Test
//    public void subscribingTaskToTopicWithEmptyPermissionShouldNotBePossibleTest() {
//        final MockController mockController = new MockController();
//        final String taskMethod = "mockTask";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath03);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            assertEquals("", baboonConfig.getTopicByName(topicNamesDefined[0]).getPermission());
//            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, taskMethod);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing a topic with name
//     * "topic2"</li>
//     * <li>And topic2 has not the permission field</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe a {@link TaskSubscription} to topic2</li>
//     * <li>Then a {@link NotSubscribableException} exception should be
//     * thrown</li>
//     */
//    @Test
//    public void subscribingTaskToTopicWithNullPermissionShouldNotBePossibleTest() {
//        final MockController mockController = new MockController();
//        final String taskMethod = "mockTask";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath03);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            assertEquals(null, baboonConfig.getTopicByName(topicNamesDefined[1]).getPermission());
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, taskMethod);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing a topic with name
//     * "topic1"</li>
//     * <li>And topic1 has an empty permission string</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
//     * topic1</li>
//     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
//     * should contain a {@link HappeningHandlerSubscription} with the object
//     * instance and the method subscribed as a map's key</li>
//     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
//     * contain the {@link Topic} as value for the key</li>
//     */
//    @Test
//    public void subscribingHappeningHandlerToTopicWithEmptyPermissionShouldGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String happeningHandlerMethod = "mockHappeningHandler";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath03);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            assertEquals("", baboonConfig.getTopicByName(topicNamesDefined[0]).getPermission());
//            baboonConfig.subscribeToTopic(topicNamesDefined[0], mockController, happeningHandlerMethod);
//            Map<Pair<Object, Method>, AbstractTaskSubscription> subscriptionsMap = baboonConfig.getSubscriptionsMap();
//            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
//                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
//            assertEquals(1, subscriptionsMap.size());
//            assertTrue(subscriptionsMap.keySet().contains(testHHOKey));
//            assertEquals(topicNamesDefined[0], subscriptionsMap.get(testHHOKey).getTopic().getName());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        } catch (NoSuchMethodException e) {
//            fail(e.getMessage());
//        } catch (SecurityException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing a topic with name
//     * "topic2"</li>
//     * <li>And topic2 has not the permission field</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
//     * topic2</li>
//     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
//     * should contain a {@link HappeningHandlerSubscription} with the object
//     * instance and the method subscribed as a map's key</li>
//     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
//     * contain the {@link Topic} as value for the key</li>
//     */
//    @Test
//    public void subscribingHappeningHandlerToTopicWithNullPermissionShouldGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String happeningHandlerMethod = "mockHappeningHandler";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath03);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            assertEquals(null, baboonConfig.getTopicByName(topicNamesDefined[1]).getPermission());
//            baboonConfig.subscribeToTopic(topicNamesDefined[1], mockController, happeningHandlerMethod);
//            Map<Pair<Object, Method>, AbstractTaskSubscription> subscriptionsMap = baboonConfig.getSubscriptionsMap();
//            Pair<Object, Method> testHHO = new Pair<Object, Method>(mockController,
//                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
//            assertEquals(1, subscriptionsMap.size());
//            assertTrue(subscriptionsMap.keySet().contains(testHHO));
//            assertEquals(topicNamesDefined[1], subscriptionsMap.get(testHHO).getTopic().getName());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        } catch (NoSuchMethodException e) {
//            fail(e.getMessage());
//        } catch (SecurityException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing a topic with name
//     * "topic3"</li>
//     * <li>And topic3 has a set_guard_callback {{["g1","g2"]}}</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>And I have a controller with a method that returns a boolean and
//     * requires no parameters annotated with {@link GuardProvider#value()}
//     * "g1"</li>
//     * <li>And the same controller has a method that returns a boolean and
//     * requires no parameters annotated with {@link GuardProvider#value()}
//     * "g2"</li>
//     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
//     * topic3</li>
//     * <li>Then the {@link HappeningHandlerSubscription} subscriptions Map
//     * should contain a {@link HappeningHandlerSubscription} with the object
//     * instance and the method subscribed as a map's key</li>
//     * <li>And the {@link HappeningHandlerSubscription} subscriptions Map should
//     * contain the {@link Topic} as value for the key</li>
//     * <li>And {@link HappeningHandlerSubscription#getGuardCallback(String)}
//     * should return a guard callback method with for "g1"</li> *
//     * <li>And {@link HappeningHandlerSubscription#getGuardCallback(String)}
//     * should return a guard callback method with for "g2"</li>
//     */
//    @Test
//    public void subscribingAbstractTaskWithGuardProvidersToTopicWithGuardCallbackShouldGetRegisteredInConfigTest() {
//        final MockController mockController = new MockController();
//        final String happeningHandlerMethod = "mockHappeningHandler";
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        try {
//            baboonConfig.addTopics(topicsPath03);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[2]).getSetGuardCallback().contains("g1"));
//            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[2]).getSetGuardCallback().contains("g2"));
//            baboonConfig.subscribeToTopic(topicNamesDefined[2], mockController, happeningHandlerMethod);
//            Map<Pair<Object, Method>, AbstractTaskSubscription> subscriptionsMap = baboonConfig.getSubscriptionsMap();
//            Pair<Object, Method> testHHOKey = new Pair<Object, Method>(mockController,
//                    MethodDictionary.getMethod(mockController, happeningHandlerMethod));
//            assertEquals(1, subscriptionsMap.size());
//            assertTrue(subscriptionsMap.keySet().contains(testHHOKey));
//            try {
//                assertFalse(subscriptionsMap.get(testHHOKey).getGuardValue("g1"));
//                mockController.setGuard1Value(true);
//                assertTrue(subscriptionsMap.get(testHHOKey).getGuardValue("g1"));
//                assertFalse(subscriptionsMap.get(testHHOKey).getGuardValue("g2"));
//                mockController.setGuard2Value(true);
//                assertTrue(subscriptionsMap.get(testHHOKey).getGuardValue("g2"));
//            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//                fail(e.getMessage());
//            }
//            assertEquals(topicNamesDefined[2], subscriptionsMap.get(testHHOKey).getTopic().getName());
//        } catch (NotSubscribableException e) {
//            fail(e.getMessage());
//        } catch (NoSuchMethodException e) {
//            fail(e.getMessage());
//        } catch (SecurityException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * <li>Given I have a topics json file containing a topic with name
//     * "topic4"</li>
//     * <li>And topic4 has a set_guard_callback {{["g1","g3"]}}</li>
//     * <li>And I add the topics configuration to the Framework</li>
//     * <li>And I have a controller with a method that returns a boolean and
//     * requires no parameters annotated with {@link GuardProvider#value()}
//     * "g1"</li>
//     * <li>And the controller has not a method annotated with
//     * {@link GuardProvider#value()} "g3"</li>
//     * <li>When I subscribe a {@link HappeningHandlerSubscription} to
//     * topic4</li>
//     * <li>Then a {@link NotSubscribableException} exception should be
//     * thrown</li>
//     */
//    @Test
//    public void subscribingAnAbstractTaskWithMissingGuardProviderToTopicWithGuardCallbackShouldNotBePossibleTest() {
//        final MockController mockController = new MockController();
//        final BaboonConfig baboonConfig = new BaboonConfig();
//        final String taskMethod = "mockTask";
//        try {
//            baboonConfig.addTopics(topicsPath03);
//        } catch (BadTopicsJsonFormat e) {
//            fail(e.getMessage());
//        } catch (NoTopicsJsonFileException e) {
//            fail(e.getMessage());
//        }
//        try {
//            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[3]).getSetGuardCallback().contains("g1"));
//            assertTrue(baboonConfig.getTopicByName(topicNamesDefined[3]).getSetGuardCallback().contains("g3"));
//            baboonConfig.subscribeToTopic(topicNamesDefined[3], mockController, taskMethod);
//            fail("Exception should have been thrown before this point");
//        } catch (Exception e) {
//            assertEquals(NotSubscribableException.class, e.getClass());
//        }
//    }
//
//}
