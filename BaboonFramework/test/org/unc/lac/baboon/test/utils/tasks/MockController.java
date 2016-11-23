package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.test.cases.TasksAndHappeningHandlersSubscriptionTest;

/**
 * MockController is used by
 * {@link TasksAndHappeningHandlersSubscriptionTest} for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see Task
 * @see HappeningHandler
 */
public class MockController {
    @HappeningHandler
    public void mockHappeningHandler() {

    }

    @Task
    public void mockTask() {

    }

    public void mockNotSubscribableMethod() {

    }

    @HappeningHandler
    public void mockHappeningHandler2() {

    }

    @Task
    public void mockTask2() {

    }
}
