package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.annotations.TaskController;
import org.unc.lac.baboon.test.cases.TaskExecutionTest;

/**
 * This Mock Controller is used by {@link TaskExecutionTest} for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see TaskController
 */
public enum TaskExecutionController {
	INSTANCE(0);

	private int counter;

	TaskExecutionController(int counter) {
		this.counter = counter;
	}

	@TaskController
	public void increaseNumber() {
		this.counter++;
	}

	public int getNumber() {
		return counter;
	}

	public void reset() {
		this.counter = 0;
	}
}
