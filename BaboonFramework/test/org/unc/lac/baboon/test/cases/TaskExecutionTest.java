package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.test.utils.appsetup.TaskExecutionAppSetup;
import org.unc.lac.baboon.test.utils.tasks.TaskExecutionMockSingletonObject;

public class TaskExecutionTest {
    
    private TaskExecutionAppSetup appSetup;
    
    @Before
    public void setUp() {
        appSetup = new TaskExecutionAppSetup();
    }
    /**
     * <li>Given I have a class {@link TaskExecutionAppSetup} which implements
     * {@link BaboonApplication} interface</li>
     * <li> And The setup interface has a pnml file describing a petri net </li>
     * <li> And the setup has a {@link TaskExecutionMockSingletonObject} </li>
     * <li> And the {@link TaskExecutionMockSingletonObject} has a TaskController ({@link TaskExecutionMockSingletonObject#increaseNumber()}) that increases a number by one </li>
     * <li> And the setup interface subscribed this taskController to the petri net </li>
     * <li> And the petri net limits the execution of the taskController to five times </li>
     * <li> When I run the BaboonFramework main application </li>
     * <li> And I wait for the taskController execution </li>
     * <li> Then the number of the {@link TaskExecutionMockSingletonObject} is 5 </li> 
     */
    @Test
    public void taskExecutionTest() throws Exception {
        assertEquals(0,appSetup.getUserSystemObject().getNumber());
        BaboonFramework.main(null);
        Thread.sleep(500);
        assertEquals(5,appSetup.getUserSystemObject().getNumber());
    }

}
