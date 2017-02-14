package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningHandler;
import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.test.cases.TasksAndHappeningHandlersSubscriptionTest;

/**
 * MockController is used by {@link TasksAndHappeningHandlersSubscriptionTest}
 * for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see Task
 * @see HappeningHandler
 */
public class MockController {

    boolean guard1Value = false;
    boolean guard2Value = false;

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

    public void setGuard1Value(boolean newValue) {
        guard1Value = newValue;
    }

    public void setGuard2Value(boolean newValue) {
        guard2Value = newValue;
    }

    @GuardProvider("g1")
    public boolean mockGuard1Provider() {
        return guard1Value;
    }

    @GuardProvider("g2")
    public boolean mockGuard2Provider() {
        return guard2Value;
    }

}
