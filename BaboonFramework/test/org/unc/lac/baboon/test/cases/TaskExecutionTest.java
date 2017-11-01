package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.test.utils.appsetup.TaskExecutionAppSetup;
import org.unc.lac.baboon.test.utils.tasks.TaskExecutionController;

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
     * <li> And the setup has a {@link TaskExecutionController} </li>
     * <li> And the {@link TaskExecutionController} has a TaskController ({@link TaskExecutionController#increaseNumber()}) that increases a number by one </li>
     * <li> And the setup interface subscribed this taskController to the petri net </li>
     * <li> And the petri net limits the execution of the taskController to five times </li>
     * <li> When I run the BaboonFramework main application </li>
     * <li> And I wait for the taskController execution </li>
     * <li> Then the number of the {@link TaskExecutionController} is 5 </li>
     */
    @Test
    public void taskExecutionTest() {
        assertEquals(0, appSetup.getController().getNumber());
        BaboonFramework.main(null);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            fail("Interrupted Thread");
        }
        assertEquals(5, appSetup.getController().getNumber());
    }

}
