package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.aspect.HappeningControllerJoinPointReporter;
import org.unc.lac.baboon.config.BaboonConfig;
import org.unc.lac.baboon.exceptions.BadPolicyException;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.execution.HappeningControllerSynchronizer;
import org.unc.lac.baboon.petri.BaboonPetriCore;
import org.unc.lac.baboon.test.utils.TransitionEventObserver;
import org.unc.lac.baboon.test.utils.tasks.MockUserSystemObject;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.factory.PetriNetFactory.petriNetType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class HappeningControllerJoinPointTest {
    private BaboonConfig baboonConfig;
    final String petriNetFile = "/org/unc/lac/baboon/test/resources/pnml01.pnml";
    private HappeningControllerSynchronizer hcSyncronizer;
    private BaboonPetriCore petriCore;
    
    private final String topicsPath02 = "/org/unc/lac/baboon/test/resources/topics02.json";
    private final String topicName = "topic1";
    
    /**
     * <li>Given I have a petri net with a transition "t0" enabled, a place "p1" fed by "t0" and a transition "t1" fed by "p1" </li>
     * <li>And I have a topics file with a topic named "topic1" </li>
     * <li>And "topic1" permission is "t0" and its transition callback is "t1" </li>
     * <li>And I have a {@link TransitionEventObserver} listening for firing events on transitions "t0" and "t1".
     * <li>And I have an instance of {@link MockUserSystemObject}.
     * <li>And I subscribe {@link MockUserSystemObject#eventInserterHappeningController(TransitionEventObserver, Boolean, String)}} as Happening Controller </li>
     * <li>And the {@link HappeningController</li>
     * <li>And I add the topics configuration to the Framework </li>
     * <li>And I subscribe the instance of the object and the
     * {@link HappeningController} annotated method to a {@link Topic}</li>
     * And I created a {@link HappeningControllerSynchronizer}. </li>
     * And I subscribe the {@link HappeningControllerSynchronizer} to {@link HappeningControllerJoinPointReporter}. </li>
     * <li>When I execute {@link MockUserSystemObject#eventInserterHappeningController(TransitionEventObserver, Boolean, String)}} </li>
     * <li>Then result from  {@link MockUserSystemObject#eventInserterHappeningController(TransitionEventObserver, Boolean, String)}} is true.</li>
     * <li>And {@link TransitionEventObserver} contains an event from "t0", an event added on 
     * {@link MockUserSystemObject#eventInserterHappeningController(TransitionEventObserver, Boolean, String)}} and an event from "t1". In that order. </li>
     */
    @Test
    public void test() throws BadTopicsJsonFormat, NoTopicsJsonFileException, NotSubscribableException, BadPolicyException, JsonProcessingException, IOException {
        baboonConfig = new BaboonConfig();
        baboonConfig.addTopics(topicsPath02);
        petriCore = new BaboonPetriCore(petriNetFile, petriNetType.PLACE_TRANSITION, null);
        petriCore.initializePetriNet(); 
        
        final MockUserSystemObject mockUserSystemObj = new MockUserSystemObject();
        final String happeningControllerMethod = "eventInserterHappeningController";
        Boolean result = new Boolean(false);
        String expectedEventT0 = "t0";
        String expectedEventT1 = "t1";
        String eventToAdd = "TEST_EVENT";
        TransitionEventObserver tObserver = new TransitionEventObserver();
        petriCore.listenToTransitionInforms("t0", tObserver);
        petriCore.listenToTransitionInforms("t1", tObserver);
        
        baboonConfig.subscribeControllerToTopic(topicName, mockUserSystemObj, happeningControllerMethod, tObserver, expectedEventT0, result, eventToAdd);

       hcSyncronizer = new HappeningControllerSynchronizer(baboonConfig, petriCore);
       HappeningControllerJoinPointReporter.setObserver(hcSyncronizer);
       
       //HappeningControllerJoinPointReporter before is executed Here
       mockUserSystemObj.eventInserterHappeningController(tObserver, expectedEventT0, result, eventToAdd);
       //HappeningControllerJoinPointReporter after is executed Here
       
       assertEquals(3,tObserver.getEvents().size());
       
       ObjectMapper mapper = new ObjectMapper();
       String receivedEventT0 =  mapper.readTree(tObserver.getEvents().get(0)).findValue("name").asText();
       String receivedEventT1 =  mapper.readTree(tObserver.getEvents().get(2)).findValue("name").asText();
       
       assertEquals(expectedEventT0,receivedEventT0);
       assertEquals(eventToAdd,tObserver.getEvents().get(1));
       assertEquals(expectedEventT1,receivedEventT1);
       
       for(String event : tObserver.getEvents()){
           System.out.println(event);
       }
       
    }

}
