package org.unc.lac.baboon.task;

import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

/**
 * A SimpleTaskSubscription is one {@link TaskAction} object subscribed to a
 * topic.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractTaskSubscription
 */
public class SimpleTaskSubscription extends AbstractTaskSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which the {@link TaskAction} object will be
     *            subscribed
     * @param task
     *            The {@link TaskAction} object that will be subscribed to topic
     * 
     * @throws NotSubscribableException
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If {@link Topic#permission} is null.</li>
     *             <li>If {@link Topic#permission} is empty.</li>
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
     *             <li>If there's more than one permission</li>
     * 
     */
    public SimpleTaskSubscription(Topic topic, TaskAction task) throws NotSubscribableException {
        super(topic);
        if (topic.getPermission().size() > 1) {
            throw new NotSubscribableException("There must be only one permission for a SimpleTaskSubscription");
        }
        super.addAction(task);

    }

}
