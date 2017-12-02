package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.GuardProvider;
import org.unc.lac.baboon.annotations.HappeningController;
import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.test.cases.TasksAndHappeningControllersSubscriptionTest;

/**
 * MockController is used by {@link TasksAndHappeningControllersSubscriptionTest}
 * for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see TaskController
 * @see HappeningController
 */
public class MockController {

    boolean guard1Value = false;
    boolean guard2Value = false;

    @HappeningController
    public void mockHappeningController() {
    }

    @TaskController
    public void mockTask() {
    }

    public void mockNotSubscribableMethod() {
    }

    @HappeningController
    public void mockHappeningController2() {
    }

    @TaskController
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
