package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.Task;
import org.unc.lac.baboon.test.cases.TaskExecutionTest;

/**
 * MockController is used by {@link TaskExecutionTest} for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see Task
 */
public class TaskExecutionController {

    private int number = 0;

    @Task
    public void increaseNumber() {
        this.number++;
    }

    public int getNumber() {
        return number;
    }
}