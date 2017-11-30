package org.unc.lac.baboon.test.cases;


import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.config.BaboonConfig;
import org.unc.lac.baboon.subscription.AbstractTaskControllerSubscription;
import org.unc.lac.baboon.execution.DummyThread;
import org.unc.lac.baboon.petri.BaboonPetriCore;
import org.unc.lac.baboon.subscription.SimpleTaskControllerSubscription;
import org.unc.lac.baboon.test.utils.TransitionEventObserver;
import org.unc.lac.baboon.test.utils.tasks.CustomCounter;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.MethodDictionary;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.factory.PetriNetFactory.petriNetType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DummyThreadTest {


    final String petriNetFile = "/org/unc/lac/baboon/test/resources/pnml01.pnml";
    final String petriNetFile_02 = "/org/unc/lac/baboon/test/resources/pnml02.pnml";
    private final String topicsPath02 = "/org/unc/lac/baboon/test/resources/topics02.json";
    
    
    @After
    public void turnOnLogger(){
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.ALL);
    }
    
    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains fired transitions "t0" and "t1" </li>
     * <li>And "t0" is enabled to be fired repeatedly </li>
     * <li>And I configure a {@link Topic} with permission "t0" and a fireCallback "t1" </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I have a {@link TransitionEventObserver} object listening for firing events on "t0" and "t1" </li>
     * <li>And the {@link TaskController} method in the {@link TaskActionController} increases the count on a {@link CustomCounter} object. </li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I create a Thread to execute {@link DummyThread#call()} method</li>
     * <li>And I let the Thread execute for a second before interrupting it</li>
     * <li>Then {@link CustomCounter#getVal()} should be greater than zero </li>
     * <li>And transition firing event count should be two times the counter value (with an error of +/- 1 because execution thread is interrupted) </li>
     * <li>And transition firing events on {@link TransitionEventObserver} should be interleaved in the following way "t0, t1, t0, t1, ..." </li>
     * 
     */
    @Test
    public void simpleTaskControllerShouldRunCorrectlyOnDummyThread() throws Exception {
        final String topicName = "topic1";
        BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(baboonConfig.getTopicByName(topicName), taskController);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        baboonConfig.getTopicByName(topicName).getFireCallback().add(""); //should not make test fail
        baboonConfig.getTopicByName(topicName).getFireCallback().add(null);
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);

        TransitionEventObserver tObserver = new TransitionEventObserver();
        petriCore.listenToTransitionInforms("t0", tObserver);
        petriCore.listenToTransitionInforms("t1", tObserver);
        Thread t = new Thread(new Runnable(){
            public void run(){
                dummyThreadInstance.call();
            };});
        
        t.start();
        Thread.sleep(1000);
        t.interrupt();
        assertTrue(counter.getVal() > 0);
        final int receivedEventCount = tObserver.getEvents().size();
        final int expectedEvents = 2*counter.getVal();
        final int maxEventCountDeviation = 1;
        boolean eventCountRangeOk = receivedEventCount <= (expectedEvents+maxEventCountDeviation) && receivedEventCount > (expectedEvents-maxEventCountDeviation);
        assertTrue(eventCountRangeOk);
        ObjectMapper mapper = new ObjectMapper();
        for(int i = 0; i< tObserver.getEvents().size() ; i++){
            String receivedEvent =  mapper.readTree(tObserver.getEvents().get(i)).findValue("name").asText();
            if(i%2 == 0){
                assertEquals("t0", receivedEvent);
            }
            else{
                assertEquals("t1", receivedEvent);
            }
        }
    }
    
    
    
    
    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains fired transitions "t0" and "t1" </li>
     * <li>And "t0" is enabled to be fired repeatedly </li>
     * <li>And I configure a {@link Topic} with a permission "t2", that does not exists on Petri Net model </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I execute {@link DummyThread#call()} method</li>
     * <li>And The framework tries to fire "t2" </li>
     * <li>Then a {@link RuntimeException} should be thrown </li>
     */
    @Test (expected=RuntimeException.class)
    public void notExistingPermissionTransitionShouldMakeDummyThreadInterruptItsExecution() throws Exception{
        final String topicName = "topic_dummy_th_wrong_permission";
        BaboonConfig baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(baboonConfig.getTopicByName(topicName), taskController);
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        dummyThreadInstance.call();
    }
    
    
    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains fired transitions "t0" and "t1" </li>
     * <li>And "t0" is enabled to be fired repeatedly </li>
     * <li>And I configure a {@link Topic} with permission "t0" </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I after change the {@link Topic} permission to an empty string </li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I execute {@link DummyThread#call()} method</li>
     * <li>Then the empty permission is detected </li>
     * <li>And a {@link RuntimeException} should be thrown </li>
     */
    @Test (expected=RuntimeException.class)
    public void emptyPermissionTransitionShouldMakeDummyThreadInterruptItsExecution() throws Exception{
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile_02, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
        topic.getPermission().clear();
        topic.getPermission().add("");
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        dummyThreadInstance.call();
    }
    
    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains transition "t0" of "automatic" type</li>
     * <li>And "t0" is enabled</li>
     * <li>And I configure a {@link Topic} with permission "t0" </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I execute {@link DummyThread#call()} method</li>
     * <li>And the framework tries to fire the automatic transition </li>
     * <li>Then a {@link RuntimeException} should be thrown </li>
     */
    @Test (expected=RuntimeException.class)
    public void permissionTransitionOfTypeAutomaticShouldMakeDummyThreadInterruptItsExecution() throws Exception{
        Topic topic = new Topic();
        topic.getPermission().add("t0");
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile_02, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        dummyThreadInstance.call();
    }
    

    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains transition "t0" of "automatic" type and transition "t1" of "fired" type </li>
     * <li>And "t0" is enabled</li>
     * <li>And "t1" is enabled</li>
     * <li>And I configure a {@link Topic} with permission "t1" </li>
     * <li>And I configure the {@link Topic} with fire callback "t0" </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I execute {@link DummyThread#call()} method</li>
     * <li>And the framework tries to fire the automatic transition "t0"</li>
     * <li>Then a {@link RuntimeException} should be thrown </li>
     */
    @Test (expected=RuntimeException.class)
    public void fireCallbackTransitionOfTypeAutomaticShouldMakeDummyThreadInterruptItsExecution() throws Exception{
        Topic topic = new Topic();
        topic.getPermission().add("t1");
        topic.getFireCallback().add("t0");
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile_02, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        dummyThreadInstance.call();
    }
    
    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains transition "t0" of "automatic" type and transition "t1" of "fired" type </li>
     * <li>And "t0" is enabled</li>
     * <li>And "t1" is enabled</li>
     * <li>And I configure a {@link Topic} with permission "t1" </li>
     * <li>And I configure the {@link Topic} with fire callback "t3", that does not exists on Petri Net model</li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I execute {@link DummyThread#call()} method</li>
     * <li>And the framework tries to fire the non existent transition "t3"</li>
     * <li>Then a {@link RuntimeException} should be thrown </li>
     */
    @Test (expected=RuntimeException.class)
    public void nonExistentFireCallbackTransitionShouldMakeDummyThreadInterruptItsExecution() throws Exception{
        Topic topic = new Topic();
        topic.getPermission().add("t1");
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile_02, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
        topic.getFireCallback().add("t3");
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        dummyThreadInstance.call();
    }
    
    
    /**
     * <li>Given I have a Petri Net model inside a {@link BaboonPetriCore} object, which contains transition "t0" of "automatic" type and transition "t1" of "fired" type </li>
     * <li>And "t0" is enabled</li>
     * <li>And "t1" is enabled</li>
     * <li>And I configure a {@link Topic} with permission "t1" </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>And I configure the {@link Topic} with a guard callback callback "g2", that does not exists on Petri Net model </li>
     * <li>And I configure a {@link DummyThread} object to execute the {@link SimpleTaskControllerSubscription} using the {@link BaboonPetriCore} </li>
     * <li>When I execute {@link DummyThread#call()} method</li>
     * <li>And the framework tries to set the non existent guard "g2"</li>
     * <li>Then a {@link RuntimeException} should be thrown </li>
     */
    @Test (expected=RuntimeException.class)
    public void nonExistentGuardCallbackShouldMakeDummyThreadInterruptItsExecution() throws Exception{
        Topic topic = new Topic();
        topic.getPermission().add("t1");
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile_02, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
        String[] fakeGuardCallback = {"g2"};
        topic.getSetGuardCallback().add(fakeGuardCallback);
        DummyThread dummyThreadInstance = new DummyThread(taskSubscription, petriCore);
        Logger.getLogger(DummyThread.class.getName()).setLevel(Level.OFF);
        dummyThreadInstance.call();
    }
    
    /**
     * <li>Given I configure a {@link Topic} with permission "t1" </li>
     * <li>And I subscribe a {@link TaskActionController} to the {@link Topic}, creating the {@link SimpleTaskControllerSubscription}</li>
     * <li>When I use a null object as petriCore on {@link DummyThread#DummyThread(AbstractTaskControllerSubscription, BaboonPetriCore)} </li>
     * <li>Then an {@link IllegalArgumentException} should be thrown </li>
     */
    @Test (expected=IllegalArgumentException.class)
    public void dummyThreadInitializationWithNullPetriCoreShouldNotBePossible() throws Exception{
        Topic topic = new Topic();
        topic.getPermission().add("t1");
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String taskMethod = "mockTaskCounter";
        Method methodObj =  MethodDictionary.getMethod(mockUserSystemObj, taskMethod, CustomCounter.class);
        CustomCounter counter = new CustomCounter();
        TaskActionController taskController = new TaskActionController(mockUserSystemObj,methodObj, counter);
        SimpleTaskControllerSubscription taskSubscription = new SimpleTaskControllerSubscription(topic, taskController);
        BaboonPetriCore petriCore = null;
        new DummyThread(taskSubscription, petriCore);
    }
    
    /**
     * <li>Given I have a {@link BaboonPetriCore} containing the Petri Net model </li>
     * <li>When I use a null object as taskSubscription on {@link DummyThread#DummyThread(AbstractTaskControllerSubscription, BaboonPetriCore)} </li>
     * <li>Then an {@link IllegalArgumentException} should be thrown </li>
     */
    @Test (expected=IllegalArgumentException.class)
    public void dummyThreadInitializationWithNullTaskShouldNotBePossible() throws Exception{
        BaboonPetriCore petriCore = new BaboonPetriCore(petriNetFile_02, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet();
        new DummyThread(null, petriCore);
    }
}
