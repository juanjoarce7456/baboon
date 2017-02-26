package org.unc.lac.baboon.task;

import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

/**
 * A SimpleTaskSubscription is one {@link HappeningHandlerAction} object
 * subscribed to a topic.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractTaskSubscription
 */
public class HappeningHandlerSubscription extends AbstractActionSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which the {@link HappeningHandlerAction} object
     *            will be subscribed
     * @param task
     *            The {@link HappeningHandlerAction} object that will be
     *            subscribed to topic
     * 
     * @throws NotSubscribableException
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If the {@link Action} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If there's more than one permission</li>
     * 
     */
    public HappeningHandlerSubscription(Topic topic, HappeningHandlerAction happeningHandler)
            throws NotSubscribableException {
        super(topic);
        if (topic.getPermission().size() > 1) {
            throw new NotSubscribableException("There must be only one permission for a HappeningHandlerSubscription");
        }
        addAction(happeningHandler);
    }

    /**
     * This method returns the {@link HappeningHandlerAction} of this
     * subscription
     * 
     * @return the {@link HappeningHandlerAction} of this subscription
     * 
     */
    public HappeningHandlerAction getAction() {
        return (HappeningHandlerAction) super.getAction(0);
    }

    /**
     * This method appends the given action to the end of {@link #actionsList}
     * 
     * @param action
     *            {@link Action} object to be appended to {@link #actionsList}.
     *            The permission and guard callback corresponding to this
     *            {@link Action} are determined by the order (or index).
     * @return true if {@link #actionsList} changed as a result of the call
     * 
     * @throws NotSubscribableException
     *             <li>If the {@link Action} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the action's type is not an instance of
     *             {@link HappeningHandlerAction}</li>
     *
     * @see {@link Topic#permission}
     * @see {@link Topic#setGuardCallback}
     * 
     */
    @Override
    protected boolean addAction(Action action) throws NotSubscribableException {
        if (!(action instanceof HappeningHandlerAction)) {
            throw new NotSubscribableException("The action must be of type HappeningHandlerAction");
        }
        return super.addAction(action);
    }

}
