package org.unc.lac.baboon.subscription;

import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.actioncontroller.HappeningActionController;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.annotations.GuardProvider;

/**
 * A HappeningControllerSubscription is one {@link HappeningActionController} object
 * subscribed to a topic.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractActionControllerSubscription
 */
public class HappeningControllerSubscription extends AbstractActionControllerSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which the {@link HappeningActionController} object
     *            will be subscribed
     * @param happeningController
     *            The {@link HappeningActionController} object that will be
     *            subscribed to topic
     * 
     * @throws NotSubscribableException
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If the {@link ActionController} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If there's more than one permission</li>
     * 
     */
    public HappeningControllerSubscription(Topic topic, HappeningActionController happeningController)
            throws NotSubscribableException {
        super(topic);
        if (topic.getPermission().size() > 1) {
            throw new NotSubscribableException("There must be only one permission for a HappeningControllerSubscription");
        }
        addAction(happeningController);
    }

    /**
     * This method returns the {@link HappeningActionController} of this
     * subscription
     * 
     * @return the {@link HappeningActionController} of this subscription
     * 
     */
    public HappeningActionController getAction() {
        return (HappeningActionController) super.getAction(0);
    }

    /**
     * This method appends the given actionController to the end of {@link #actionsList}
     * 
     * @param actionController
     *            {@link ActionController} object to be appended to {@link #actionsList}.
     *            The permission and guard callback corresponding to this
     *            {@link ActionController} are determined by the order (or index).
     * @return true if {@link #actionsList} changed as a result of the call
     * 
     * @throws NotSubscribableException
     *             <li>If the {@link ActionController} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the actionController's type is not an instance of
     *             {@link HappeningActionController}</li>
     *
     * @see {@link Topic#permission}
     * @see {@link Topic#setGuardCallback}
     * 
     */
    @Override
    protected boolean addAction(ActionController actionController) throws NotSubscribableException {
        if (!(actionController instanceof HappeningActionController)) {
            throw new NotSubscribableException("The actionController must be of type HappeningActionController");
        }
        return super.addAction(actionController);
    }

}
