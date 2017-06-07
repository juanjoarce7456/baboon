package org.unc.lac.baboon.subscription;

import java.util.ArrayList;

import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

/* This class defines an AbstractActionControllerSubscription as a list of {@link ActionController}
 * objects, which are subscribed to a topic. It is used internally by framework
 * and should not be known by user.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */

public abstract class AbstractActionControllerSubscription {

    /**
     * List of {@link ActionController} objects subscribed to topic
     */
    protected ArrayList<ActionController> actionsList;

    /**
     * Topic to which the {@link ActionController} objects are subscribed
     */
    protected Topic topic;

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which {@link ActionController} objects will be subscribed
     * 
     * @throws NotSubscribableException
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     * 
     */
    public AbstractActionControllerSubscription(Topic topic) throws NotSubscribableException {
        if (topic == null) {
            throw new NotSubscribableException("The topic cannot be null");
        }
        if (!topic.getSetGuardCallback().isEmpty()
                && topic.getPermission().size() != topic.getSetGuardCallback().size()) {
            throw new NotSubscribableException(
                    "The permission array and the guardCallbackArray cannot be of different sizes");
        }

        this.actionsList = new ArrayList<ActionController>();
        this.topic = topic;
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
     *
     * @see {@link Topic#permission}
     * @see {@link Topic#setGuardCallback}
     * 
     */
    protected boolean addAction(ActionController actionController) throws NotSubscribableException {
        for (String guardName : topic.getGuardCallback(actionsList.size())) {
            if (!actionController.hasGuardProvider(guardName)) {
                throw new NotSubscribableException(
                        "The actionController does not have a GuardProvider to handle guard: " + guardName);
            }
        }
        return actionsList.add(actionController);
    }

    /**
     * This method returns the {@link Topic} element present in this
     * subscription.
     * 
     * @return the {@link Topic} object to which the {@link ActionController} objects are
     *         subscribed on this subscription
     * 
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * This method returns the number of {@link ActionController} elements on
     * {@link #actionsList}
     * 
     * @return the number of {@link ActionController} elements on {@link #actionsList}.
     * 
     */
    public int getSize() {
        return actionsList.size();
    }

    /**
     * This method returns the {@link ActionController} element at specified actionIndex
     * on {@link #actionsList}.
     * 
     * @param actionIndex
     *            index of the {@link ActionController} object to return.
     * @return the {@link ActionController} element at specified actionIndex on
     *         {@link #actionsList}.
     * 
     */
    protected ActionController getAction(int actionIndex) {
        return actionsList.get(actionIndex);
    }

}
