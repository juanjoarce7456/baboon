package org.unc.lac.baboon.task;

import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

/**
 * A ComplexSecuentialTaskSubscription is a list of {@link TaskAction} objects
 * subscribed to a topic.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractTaskSubscription
 */
public class ComplexSecuentialTaskSubscription extends AbstractTaskSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which {@link TaskAction} objects will be
     *            subscribed
     * 
     * @throws NotSubscribableException
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If {@link Topic#permission} is null.</li>
     *             <li>If {@link Topic#permission} is empty.</li>
     * 
     */
    public ComplexSecuentialTaskSubscription(Topic topic) throws NotSubscribableException {
        super(topic);
    }

    /**
     * This method appends the given {@link TaskAction} to the end of
     * {@link #actionsList}
     * 
     * @param action
     *            {@link TaskAction} object to be appended to
     *            {@link #actionsList}. The permission and guard callback
     *            corresponding to this {@link TaskAction} are determined by the
     *            order (or index).
     * 
     * @throws NotSubscribableException
     *             <li>If the {@link TaskAction} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskAction} objects in
     *             {@link #actionsList}</li>
     *             <li>If the topic permission corresponding to this task is
     *             empty</li>
     *             <li>If the topic permission corresponding to this task is
     *             null</li>
     *             <li>If fails to append the {@link TaskAction} to
     *             {@link #actionsList}.</li>
     *
     * @see {@link Topic#permission}
     * @see {@link Topic#setGuardCallback}
     * 
     */
    public void addTask(TaskAction task) throws NotSubscribableException {
        if (!(super.addAction(task))) {
            throw new NotSubscribableException("Failed to add the task");
        }
    }

}
