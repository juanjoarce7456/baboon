package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.test.cases.TaskExecutionTest;

/**
 * MockController is used by {@link TaskExecutionTest} for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see TaskController
 */
public class TaskExecutionController {

    private int number = 0;

    @TaskController
    public void increaseNumber() {
        this.number++;
    }

    public int getNumber() {
        return number;
    }
}
