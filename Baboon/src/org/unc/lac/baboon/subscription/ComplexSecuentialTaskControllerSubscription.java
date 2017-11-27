package org.unc.lac.baboon.subscription;

import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.annotations.GuardProvider;

/**
 * A ComplexSecuentialTaskSubscription is a list of {@link TaskActionController} objects
 * subscribed to a topic.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractTaskControllerSubscription
 */
public class ComplexSecuentialTaskControllerSubscription extends AbstractTaskControllerSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which {@link TaskActionController} objects will be
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
    public ComplexSecuentialTaskControllerSubscription(Topic topic) throws NotSubscribableException {
        super(topic);
    }

    /**
     * This method appends the given {@link TaskActionController} to the end of
     * {@link #actionsList}
     * 
     * @param taskController
     *            {@link TaskActionController} object to be appended to
     *            {@link #actionsList}. The permission and guard callback
     *            corresponding to this {@link TaskActionController} are determined by the
     *            order (or index).
     * 
     * @throws NotSubscribableException
     *             <li>If the {@link TaskActionController} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskActionController} objects in
     *             {@link #actionsList}</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             empty</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             null</li>
     *             <li>If fails to append the {@link TaskActionController} to
     *             {@link #actionsList}.</li>
     *
     * @see {@link Topic#permission}
     * @see {@link Topic#setGuardCallback}
     * 
     */
    public void addTask(TaskActionController taskController) throws NotSubscribableException {
        if (!(super.addAction(taskController))) {
            throw new NotSubscribableException("Failed to add the taskController");
        }
    }

}
